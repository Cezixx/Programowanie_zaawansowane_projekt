package projekt;

import java.io.Serializable;
import java.util.Objects;

public class Device implements Serializable {
    private String brand;
    private String model;
    private String faultType;

    public Device(String brand, String model, String faultType) {
        this.brand = brand;
        this.model = model;
        this.faultType = faultType;
    }

    @Override
    public String toString() {
        return "Person{name='" + brand +"'" + model + "', phone='" + faultType + "'}";
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(brand, device.brand) && Objects.equals(model, device.model) && Objects.equals(faultType, device.faultType);
    }
}
