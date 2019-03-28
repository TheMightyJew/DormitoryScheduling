package Model;

public class Student {

    public enum Sex{
        MALE,FEMALE
    }

    private String ID;
    private Sex sex;
    private int Study_Year;
    private Student_Request studentRequest;

    public Student(String ID, Sex sex, int study_Year, Student_Request studentRequest) {
        this.ID = ID;
        this.sex = sex;
        Study_Year = study_Year;
        this.studentRequest = studentRequest;
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
}
