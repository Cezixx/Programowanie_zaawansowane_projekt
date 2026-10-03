import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
public class zadanie {
    public static void wczytajOceny() {
        try(BufferedReader reader = new BufferedReader(new FileReader("oceny.txt"))){
            String line;

            while((line = reader.readLine()) != null){
                String[] parts = line.split(";");
                System.out.println("Imię: " + parts[1] + ", Nazwisko: " + parts[0] +
                    ", Wynik: " + parts[2] + " i " + parts[3]);
    
            }
            
        } catch (IOException e) {
            System.out.println("Błąd");
        }        
    }  
}