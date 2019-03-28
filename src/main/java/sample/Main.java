package sample;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Main {

//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
//    }


    public static void main(String[] args) {
        String [] names1 = { "Boris", "Fred", "Albert",
                "Tom", "James", "Matthew", "Mark", "Luke", "John", "David", "Harold", "Bob", "Jack", "Mike", "Raymond", "Cuthbert", "Casper", "Harry", "Cameron", "Warwick", "Steve", "Steven", "Simon", "Jeff", "Zach", "Chris", "Christian", "Matt", "Mathias", "Alex", "Will", "William", "Forest", "Clarke", "Gregory", "Joshua", "Josh", "Andy", "Andrew", "Dick", "Rick", "Richard", "Rob", "Robert", "Mohammad", "Hector", "Reginald", "Phillip", "Phil", "Pete", "Roger", "Brad", "Chad", "Shane", "Daniel", "Dan", "Tristan", "Roy", "Gary", "Tony", "Toby", "Barry", "Graham", "Kevin","Tommy","Sandie","Darth","Garth"};

//        String [] names2 = { "Annie", "Mary", "Sarah", "Laura", "Lauren", "Katy", "Kate", "Catherine", "Naomi", "Helen", "Nadine", "Alice", "Alison", "Susan", "Suzanne", "Sharon", "Georgina", "Sonya", "Marion", "Beth", "Una", "Sophia", "Rachel", "Christiana", "Maud", "Mildred", "Zoe", "Chantal", "Charlotte", "Chloe", "Flora", "Annabelle", "Elizabeth", "Morwenna", "Jenna", "Jenny", "Gemma", "Wenna", "Fairydust", "Charity", "Ocean", "Virginia", "Hannah", "Mavis", "Harriet", "Kathy", "Heather", "Kimberly", "May", "Carla", "Suki", "Michelle", "Rhiannon", "Ruth", "Polly", "Sally", "Molly", "Dolly", "Maureen", "Maud", "Doris", "Felicity","Jessica","Stanley" };

        String [] lastName = { "Gump", "Doop", "Gloop", "Snozcumber", "Giantbulb", "Slaughterhouse", "Godfrey", "Smith", "Jones", "Bogtrotter", "Ramsbottom", "Cockle", "Hemingway", "Pigeon", "Parker", "Nolan", "Parkes", "Butterscotch", "Barker", "Trescothik", "Superhalk", "Barlow", "MacDonald", "Ferguson", "Donaldson", "Platt", "Bishop", "Blunder", "Thunder", "Sparkle", "Walker", "Raymond", "Thornhill", "Sweet", "Parker", "Johnson", "Randall", "Zeus", "England", "Smart", "Gobble", "Clifford", "Thornton", "Cox", "Blast", "Plumb", "Wishmonger", "Fish", "Blacksmith", "Thomas", "Grey", "Russell", "Lakeman", "Ball", "Chan", "Chen", "Wu", "Khan", "Meadows", "Connor", "Williams", "Wilson", "Blackman", "Jones", "Humble", "Noris","Bond","Rabbit","McCallister","DeVito","Malkovich","Olsson","Sparrow","Kowalski",
                "Vader","Torrance", "Greenway","Rockatansky","Pitt","Willis","Jolie"};

        Random rnd = new Random();


//        private String ID;
//        private String firstName;
//        private String lastName;
//        private Sex sex;
//        private int Study_Year;
//        private Student_Request studentRequest;
//
        Set <Single_Rooms_Apartment> SA = new HashSet<Single_Rooms_Apartment>();

        //(String apartment_ID, Dormitory.Dormitory_Type dormitory_type, Room_Quantity room_quantity, int apartment_Quantity, int floor, int price)


        Set<Couple_Apartment> CA = new HashSet<Couple_Apartment>();


        //    public Couple_Apartment(String apartment_ID, Dormitory.Dormitory_Type dormitory_type, Room_Quantity room_quantity, int apartment_Quantity, int floor, int price) {
        for (int i = 1 ; i <500 ; i++)
        {
            if(rnd.nextInt(10)>4) {
                Single_Rooms_Apartment SRA=new Single_Rooms_Apartment(String.valueOf(i),
                        Dormitory.Dormitory_Type.values()[i % Dormitory.Dormitory_Type.values().length],
                        Room_Quantity.SINGLE_BED,
                        i % 4,
                        i % 4,
                        1000);
                SA.add(SRA);
                System.out.println(SRA);
            }
            else{
                Couple_Apartment CA1=new Couple_Apartment(String.valueOf(i),
                        Dormitory.Dormitory_Type.values()[i % Dormitory.Dormitory_Type.values().length],
                        Room_Quantity.TWIN_BED,
                        i % 4,
                        i % 4,
                        1000);
            CA.add(CA1);
                System.out.println(CA1);

            }
        }
//            int id = rnd.nextInt(99999999) + 300000000 ;
//            while (ids.add( id))
//            {
//                id=rnd.nextInt(99999999) + 300000000;
//            }
//            String first = names1[rnd.nextInt(names1.length)];
//            String last = lastName[rnd.nextInt(lastName.length)];
//            Student.Sex sex = Student.Sex.values()[rnd.nextInt(Student.Sex.values().length)];
//            int study_year=rnd.nextInt(4);
//            Dormitory.Dormitory_Type dormitory_type= Dormitory.Dormitory_Type.values()[rnd.nextInt(Dormitory.Dormitory_Type.values().length)];
//            Room_Quantity room_quantity = Room_Quantity.values()[rnd.nextInt(Room_Quantity.values().length)];
//            Couples_Dormitory couples_dormitory = Couples_Dormitory.values()[rnd.nextInt(Couples_Dormitory.values().length)];
//            Religious religious = Religious.values()[rnd.nextInt(Religious.values().length)];
//            Smoking smoking = Smoking.values()[rnd.nextInt(Smoking.values().length)];
////            if (i%2==0) {
////                 Set<Student> wanted =new HashSet<Student>();
////                Set<Student> wanted =new HashSet<Student>();
////            }
//            Student_Request studentRequest =
//                    new Student_Request(dormitory_type,room_quantity,couples_dormitory,1000,religious,smoking,new HashSet<Student>(),new HashSet<Student>() );
//            s = new Student(String.valueOf(id),first,last,sex,study_year,studentRequest);
//            created.add(s);
//            System.out.println(s.toString());
//        }


    }
}
