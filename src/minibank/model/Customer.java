package minibank.model;

import java.io.Serializable;
import minibank.annotation.MaxLength;

/**
 * Customer details model class.
 * Author: Deep Kanjiya (25IT041)
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    @MaxLength(40)
    private String name;
    private String phone;
    private String address;

    public Customer(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer [Name: " + name + ", Phone: " + phone + ", Address: " + address + "]";
    }
}
