package Model;

import java.util.Set;

public class Single_Rooms_Apartment extends Apartment {
    private Set<Single_Bed_Room> rooms;

    public Single_Rooms_Apartment(String apartment_ID, Dormitory.Dormitory_Type dormitory_type, Room_Quantity room_quantity, int apartment_Quantity, int floor, int price) {
        super(apartment_ID, dormitory_type, room_quantity, apartment_Quantity, floor, price);
    }
}
