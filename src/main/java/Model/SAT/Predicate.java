package Model.SAT;

import Model.Apartment;
import Model.Student;

import java.util.Objects;

public class Predicate {
    private Student student;
    private Apartment apartment;

    public Predicate(Student student, Apartment apartment) {
        this.student = student;
        this.apartment = apartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Predicate predicate = (Predicate) o;
        return student.equals(predicate.student) &&
                apartment.equals(predicate.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, apartment);
    }

    public Student getStudent() {
        return student;
    }

    public Apartment getApartment() {
        return apartment;
    }

    @Override
    public String toString() {
        return student.getFirstName() + " " + student.getLastName() + " schedule for "+apartment.getApartment_ID();
    }
}
