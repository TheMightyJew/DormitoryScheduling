package Model;

public class Student {

    public enum Sex{
        MALE,FEMALE
    }
    private String ID;
    private Sex sex;

    public Student(String ID, Sex sex) {
        this.ID = ID;
        this.sex = sex;
    }

    public String getID() {
        return ID;
    }

    public Sex getSex() {
        return sex;
    }

    public void send_request(StudentRequest studentRequest){

    }
}
