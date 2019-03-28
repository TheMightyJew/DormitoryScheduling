package Model;

import java.util.Set;

public class Student_Request {

    private Dormitory.Dormitory_Type dormitory_type;
    private Room_Quantity room_quantity;
    private Couples_Dormitory couples_dormitory;
    private int max_price;
    private Religious religious;
    private Smoking smoking;
    private Set<Student> wanted;
    private Set<Student> unwanted;

    public Student_Request(Dormitory.Dormitory_Type dormitory_type, Room_Quantity room_quantity, Couples_Dormitory couples_dormitory, int max_price, Religious religious, Smoking smoking, Set<Student> wanted, Set<Student> unwanted) {
        this.dormitory_type = dormitory_type;
        this.room_quantity = room_quantity;
        this.couples_dormitory = couples_dormitory;
        this.max_price = max_price;
        this.religious = religious;
        this.smoking = smoking;
        this.wanted = wanted;
        this.unwanted = unwanted;
    }

    public Couples_Dormitory getCouples_dormitory() {
        return couples_dormitory;
    }

    public Dormitory.Dormitory_Type getDormitory_type() {
        return dormitory_type;
    }

    public Room_Quantity getRoom_quantity() {
        return room_quantity;
    }

    public int getMax_price() {
        return max_price;
    }

    public Religious getReligious() {
        return religious;
    }

    public Smoking getSmoking() {
        return smoking;
    }

    public Set<Student> getWanted() {
        return wanted;
    }

    public Set<Student> getUnwanted() {
        return unwanted;
    }

    @Override
    public String toString() {
        return "Student_Request{" +
                "dormitory_type=" + dormitory_type +
                ", room_quantity=" + room_quantity +
                ", couples_dormitory=" + couples_dormitory +
                ", max_price=" + max_price +
                ", religious=" + religious +
                ", smoking=" + smoking +
                ", wanted=" + wanted +
                ", unwanted=" + unwanted +
                '}';
    }
}
