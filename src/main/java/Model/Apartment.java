package Model;

import java.util.Set;

public abstract class Apartment {

    private String Apartment_ID;
    private Dormitory.Dormitory_Type dormitory_type;
    private Room_Quantity room_quantity;
    private int Apartment_Quantity;
    private int floor;
    private int price;

    public Apartment(String apartment_ID, Dormitory.Dormitory_Type dormitory_type, Room_Quantity room_quantity, int apartment_Quantity, int floor, int price) {
        Apartment_ID = apartment_ID;
        this.dormitory_type = dormitory_type;
        this.room_quantity = room_quantity;
        Apartment_Quantity = apartment_Quantity;
        this.floor = floor;
        this.price = price;
    }

    public String getApartment_ID() {
        return Apartment_ID;
    }

    public Dormitory.Dormitory_Type getDormitory_type() {
        return dormitory_type;
    }

    public Room_Quantity getRoom_quantity() {
        return room_quantity;
    }

    public int getApartment_Quantity() {
        return Apartment_Quantity;
    }

    public int getFloor() {
        return floor;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "Apartment_ID='" + Apartment_ID + '\'' +
                ", dormitory_type=" + dormitory_type +
                ", room_quantity=" + room_quantity +
                ", Apartment_Quantity=" + Apartment_Quantity +
                ", floor=" + floor +
                ", price=" + price +
                '}';
    }

    public int getPrice() {
        return price;
    }
}
