package Model.SAT;

import Model.Apartment;
import Model.Student;
import javafx.util.Pair;

public class Predicate_Student_At_Apartment extends Predicate {
    private Student student;
    private Apartment apartment;

    public Predicate_Student_At_Apartment(Student student, Apartment apartment) {
        this.student = student;
        this.apartment = apartment;
    }
}
