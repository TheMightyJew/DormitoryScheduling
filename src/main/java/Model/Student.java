package Model;

import java.util.HashMap;
import java.util.Objects;

public class Student {

    public static HashMap<String, Student> students = new HashMap<String, Student>();

    public enum Sex{
        MALE,FEMALE
    }

    private String ID;
    private String firstName;
    private String lastName;
    private Sex sex;
    private int Study_Year;
    private Student_Request studentRequest;


    private void init(){
        students.put(ID,this);
    }

    public Student(String ID) {
        this.ID = ID;

        init();
    }

    public Student(String ID, String firstName, String lastName, Sex sex, int study_Year) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        Study_Year = study_Year;

        init();
    }

    public void setStudentRequest(Student_Request studentRequest) {
        this.studentRequest = studentRequest;
    }

    public Student(String ID, String firstName, String lastName, Sex sex, int study_Year, Student_Request studentRequest) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        Study_Year = study_Year;
        this.studentRequest = studentRequest;

        init();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getID() {
        return ID;
    }

    public Sex getSex() {
        return sex;
    }

    public int getStudy_Year() {
        return Study_Year;
    }

    public Student_Request getStudentRequest() {
        return studentRequest;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ID='" + ID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sex=" + sex +
                ", Study_Year=" + Study_Year +
                ", studentRequest=" + studentRequest +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return ID.equals(student.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
