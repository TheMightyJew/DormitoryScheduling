package Model;

public class Couple_Apartment extends Apartment {
    private Twin_Bed_Room room;

    public Couple_Apartment(String apartment_ID, Dormitory.Dormitory_Type dormitory_type, Room_Quantity room_quantity, int apartment_Quantity, int floor, int price) {
        super(apartment_ID, dormitory_type, room_quantity, apartment_Quantity, floor, price);
    }
}
