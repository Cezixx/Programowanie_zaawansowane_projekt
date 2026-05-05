package projekt;

import java.io.Serializable;
import java.util.Objects;

public class Zgloszenie implements Serializable {
    private String dataPrzyjecia;
    private String statusNaprawy;

    public Zgloszenie(String dataPrzyjecia, String statusNaprawy) {
        this.dataPrzyjecia = dataPrzyjecia;
        this.statusNaprawy = statusNaprawy;
    }

    @Override
    public String toString() {
        return "Zgloszenie{data='" + dataPrzyjecia + "', status='" + statusNaprawy + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zgloszenie that = (Zgloszenie) o;
        return Objects.equals(dataPrzyjecia, that.dataPrzyjecia) && 
               Objects.equals(statusNaprawy, that.statusNaprawy);
    }
}