package projekt;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerMain {
    private static final int MAX_CLIENTS = 2; 
    private static final AtomicInteger activeClients = new AtomicInteger(0);
    private static final Map<String, Object> map = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        initData();
        
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Serwer napraw uruchomiony. Oczekiwanie na klientów...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initData() {
        for (int i = 1; i <= 4; i++) {
            map.put("person_" + i, new Person("Jan " + i, "Kowalski", "10020030" + i));
            map.put("device_" + i, new Device("Samsung", "S" + (20 + i), "Zbita szybka"));
            map.put("zgloszenie_" + i, new Zgloszenie("2026-05-0" + i, "W trakcie"));
        }
        System.out.println("Zainicjalizowano obiekty w mapie. Rozmiar: " + map.size());
    }

    private static void handleClient(Socket clientSocket) {
        try (
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            int clientId = in.readInt();

            if (activeClients.get() >= MAX_CLIENTS) {
                out.writeObject("REFUSED"); 
                System.out.println("Odrzucono klienta ID: " + clientId + " (Przekroczono limit)");
                return; 
            }

            activeClients.incrementAndGet();
            out.writeObject("OK");
            System.out.println("Rozpoczęto obsługę klienta ID: " + clientId);

            while (true) {
                String requestedClass;
                try {
                    requestedClass = (String) in.readObject();
                } catch (EOFException e) {
                    break; 
                }

                Thread.sleep(new Random().nextInt(1000) + 500);

                List<Object> collectionToSend = new ArrayList<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getKey().startsWith(requestedClass.toLowerCase())) {
                        collectionToSend.add(entry.getValue());
                    }
                }

                if (collectionToSend.isEmpty()) {
                    System.out.println("Pułapka! Brak klasy: " + requestedClass + ". Wysyłanie fałszywego obiektu do ID: " + clientId);
                    out.writeObject(new Person("Błąd", "Rzutowania", "000"));
                } else {
                    System.out.println("Wysłano kolekcję " + requestedClass + " do ID: " + clientId);
                    out.writeObject(collectionToSend);
                }
            }

        } catch (Exception e) {
            System.err.println("Rozłączono klienta.");
        } finally {
            activeClients.decrementAndGet();
            try { clientSocket.close(); } catch (IOException ignored) {}
        }
    }
}