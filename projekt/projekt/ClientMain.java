package projekt;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class ClientMain {
    public static void main(String[] args) {
        int myId = new Random().nextInt(1000);
        
        try (Socket socket = new Socket("localhost", 8080);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeInt(myId);
            out.flush();

            String status = (String) in.readObject();
            if ("REFUSED".equals(status)) {
                System.out.println("[Klient " + myId + "] Otrzymano REFUSED. Kończę.");
                return;
            }
            System.out.println("[Klient " + myId + "] Otrzymano OK.");

            String[] requests = {"device", "person", "telewizor"};

            for (String req : requests) {
                System.out.println("\n[Klient " + myId + "] Pytam o: " + req);
                out.writeObject(req);
                out.flush();

                Object receivedData = in.readObject();
                
                try {
                    @SuppressWarnings("unchecked")
                    List<Device> receivedList = (List<Device>) receivedData; 
                    
                    receivedList.stream()
                            .forEach(item -> System.out.println("   [ID: " + myId + "] -> " + item.toString()));

                } catch (ClassCastException e) {
                    System.err.println("   [ID: " + myId + "] BŁĄD RZUTOWANIA! Otrzymano zły typ: " + receivedData.getClass().getSimpleName());
                    System.err.println("   Szczegóły: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}