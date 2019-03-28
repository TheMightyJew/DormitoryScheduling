package Model;

import java.util.Set;

public class StudentRequest {

    private Dormitory.Dormitory_Type dormitory_type;
    private Room_Quantity room_quantity;
    private int max_price;
    private Religious religious;
    private Smoking smoking;
    private Set<Student> wanted;
    private Set<Student> unwanted;

}
