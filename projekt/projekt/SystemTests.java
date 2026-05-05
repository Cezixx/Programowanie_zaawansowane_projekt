package projekt;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SystemTests {

    // Główna metoda, która służy jako nasz "uruchamiacz testów"
    public static void main(String[] args) throws Exception {
        System.out.println("=== ROZPOCZYNAMY TESTY AUTOMATYCZNE ===");

        // 1. Uruchamiamy serwer w tle (zanim zaczną się testy)
        Thread serverThread = new Thread(() -> ServerMain.main(new String[]{}));
        serverThread.setDaemon(true);
        serverThread.start();
        Thread.sleep(1500); // Dajemy serwerowi 1.5 sekundy na start

        // 2. Wykonujemy testy
        try {
            testDeviceEqualsMethod();
            System.out.println("✅ testDeviceEqualsMethod - ZALICZONY");

            testSerialization();
            System.out.println("✅ testSerialization - ZALICZONY");

            testE2EMaxClientsAndTrap();
            System.out.println("✅ testE2EMaxClientsAndTrap - ZALICZONY");

            System.out.println("=== WSZYSTKIE TESTY ZAKOŃCZONE SUKCESEM! ===");
        } catch (Exception e) {
            System.err.println("❌ BŁĄD TESTU: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- LOGIKA TESTÓW ---

    private static void testDeviceEqualsMethod() {
        Device d1 = new Device("Dell", "XPS", "Bateria");
        Device d2 = new Device("Dell", "XPS", "Bateria");
        Device d3 = new Device("Apple", "MacBook", "Ekran");

        if (!d1.equals(d2)) {
            throw new RuntimeException("Błąd: Obiekty o tych samych danych powinny być równe!");
        }
        if (d1.equals(d3)) {
            throw new RuntimeException("Błąd: Obiekty różniące się NIE mogą być równe!");
        }
    }

    private static void testSerialization() throws Exception {
        List<Device> list = new ArrayList<>();
        list.add(new Device("Test", "Test", "Test"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        @SuppressWarnings("unchecked")
        List<Device> deserialized = (List<Device>) ois.readObject();

        if (deserialized.size() != 1) {
            throw new RuntimeException("Błąd: Rozmiar listy po deserializacji się nie zgadza.");
        }
        if (!list.get(0).equals(deserialized.get(0))) {
            throw new RuntimeException("Błąd: Obiekt po deserializacji nie jest identyczny z oryginałem.");
        }
    }

    private static void testE2EMaxClientsAndTrap() throws Exception {
        // Klient 1
        Socket s1 = new Socket("localhost", 8080);
        ObjectOutputStream out1 = new ObjectOutputStream(s1.getOutputStream());
        ObjectInputStream in1 = new ObjectInputStream(s1.getInputStream());
        out1.writeInt(1); out1.flush();
        if (!"OK".equals(in1.readObject())) throw new RuntimeException("Błąd: Klient 1 powinien dostać OK");

        // Klient 2
        Socket s2 = new Socket("localhost", 8080);
        ObjectOutputStream out2 = new ObjectOutputStream(s2.getOutputStream());
        ObjectInputStream in2 = new ObjectInputStream(s2.getInputStream());
        out2.writeInt(2); out2.flush();
        if (!"OK".equals(in2.readObject())) throw new RuntimeException("Błąd: Klient 2 powinien dostać OK");

        // Klient 3 (ponad limit MAX_CLIENTS = 2)
        Socket s3 = new Socket("localhost", 8080);
        ObjectOutputStream out3 = new ObjectOutputStream(s3.getOutputStream());
        ObjectInputStream in3 = new ObjectInputStream(s3.getInputStream());
        out3.writeInt(3); out3.flush();
        if (!"REFUSED".equals(in3.readObject())) throw new RuntimeException("Błąd: Klient 3 MUSI dostać REFUSED!");

        // Pułapka rzutowania na kliencie 1
        out1.writeObject("zla_klasa");
        out1.flush();
        Object trapResponse = in1.readObject();

        boolean caughtException = false;
        try {
            @SuppressWarnings("unchecked")
            List<Device> trap = (List<Device>) trapResponse; // To wywoła wyjątek
        } catch (ClassCastException e) {
            caughtException = true; // Złapaliśmy błąd, czyli wszystko działa jak należy
        }

        if (!caughtException) {
            throw new RuntimeException("Błąd: Oczekiwano błędu ClassCastException przy rzutowaniu fałszywego obiektu, ale test przeszedł dalej!");
        }

        // Sprzątanie po teście
        s1.close(); s2.close(); s3.close();
    }
}