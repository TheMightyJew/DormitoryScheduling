package Model.SAT;

import Model.Apartment;
import Model.Couples_Dormitory;
import Model.SAT.logic.StatementCNF;
import Model.SAT.logic.structures.SymbolTracker;
import Model.Student;
import Model.Student_Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SATCompiler {
    private HashMap<Integer,Predicate> predicates_map;
    private Integer counter=0;
    private static String or=")OR(";
    private static String and="AND";
    private List<Apartment> apartments;
    private List<Student> students;
    private SymbolTracker tracker;
    private List<StatementCNF> statements;

    public SATCompiler(List<Apartment> apartments, List<Student> students) {
        this.predicates_map = new HashMap<Integer, Predicate>();
        this.counter = counter;
        this.apartments = apartments;
        this.students = students;
        this.tracker = new SymbolTracker();
        statements=new ArrayList<StatementCNF>();
        tracker.addFunctions( "LiveAt","Apartment","Student");
    }

    private void add_Student_At_Apartment_Predicates(){
        for (Student student:students) {
            for (Apartment apartment:apartments) {
                Predicate_Student_At_Apartment predicate=new Predicate_Student_At_Apartment(student,apartment);
                predicates_map.put(counter,predicate);
            }
        }
    }

    private void Define_Students(){
        for (Student student:students) {
            tracker.addConstants(student.getID());
            StatementCNF statementCNF=StatementCNF.fromInfixString("Student("+student.getID()+")",tracker);
            statements.add(statementCNF);
        }
    }

    private void Define_Apartments(){
        for (Apartment apartment:apartments) {
            tracker.addConstants(apartment.getApartment_ID());
            StatementCNF statementCNF=StatementCNF.fromInfixString("Apartment("+apartment.getApartment_ID()+")",tracker);
            statements.add(statementCNF);
        }
    }

    private void students_requests(){
        for (int i=0;i<students.size();i++) {
            Student student1=students.get(i);
            Student_Request student_request1=student1.getStudentRequest();
            String requesedID="";
            boolean couple=false;
            if(student_request1.getCouples_dormitory().equals(Couples_Dormitory.YES)){
                couple=true;
                requesedID=student_request1.getWanted().iterator().next().getID();
            }
            for(int j=i+1;j<students.size();j++){
                String cant_live_together="!EXISTS(x) Apartment(x) AND LiveAt("+students.get(i).getID()+",x) AND LiveAt("+students.get(j).getID()+",x)";
                String fol="";
                StatementCNF cnf;
                Student student2=students.get(j);
                Student_Request student_request2=student1.getStudentRequest();
                if(students.get(i).getSex().equals(students.get(j).getSex())==false){
                    if(!(couple && requesedID.equals(student2.getID()))){
                        fol = cant_live_together;
                    }
                }
                else if(student_request1.getSmoking()!=student_request2.getSmoking()){
                    fol =cant_live_together;

                }
                else if(student_request1.getReligious()!=student_request2.getReligious()){
                    fol =cant_live_together;

                }
                if(fol.isEmpty()==false)
                    cnf = StatementCNF.fromInfixString(fol,tracker);
            }
        }
    }


    public void addApartment(Apartment apartment){

    }

    public void addStudent(Student student){

    }

    public static void main(String[] args) {

        //
        //stevenCNF();

    }

    private static void rotemCNF(){
        SymbolTracker tracker = new SymbolTracker();
        tracker.addFunctions( "LiveAt","Student","Apartment");
        tracker.addConstants("209202126","111111111","A1", "a", "b", "c", "d", "e", "f");
        StatementCNF[] stat=new StatementCNF[]{StatementCNF.fromInfixString("Student(209202126) AND Student(111111111) AND Apartment(A1)",tracker),StatementCNF.fromInfixString("!EXISTS(x) Apartment(x) AND LiveAt(209202126,x) AND LiveAt(111111111,x)",tracker)};
//        StatementCNF hagdara=StatementCNF.fromInfixString("Student(a) AND Student(b) AND Student(c) ",tracker);
//        StatementCNF steven=StatementCNF.fromInfixString("EXISTS(x) Apartment(x) AND LiveAt(a,x) AND LiveAt(b,x) AND LiveAt(c,x)",tracker);
//        System.out.println(steven.toString());
        System.out.println(stat[00].toString());
        System.out.println(stat[1].toString());
    }

    private static void stevenCNF(){
        String test="(aANDb)OR(cANDd)OR(eANDf)";
        System.out.println(test);
        System.out.println(FOL2CNF(test));
    }

    public static String FOL2CNF(String str){
        ArrayList<ArrayList<String>> arr=new ArrayList<ArrayList<String>>();
        while (str.contains(or)){
            ArrayList<String> smallArr=new ArrayList<String>();
            int orIndex=str.indexOf(or);
            String part=str.substring(1,orIndex);
            arr.add(getPredicates(part));
            str=str.substring(orIndex+or.length()-1,str.length());
        }
        arr.add(getPredicates(str.substring(1,str.length()-1)));
        String ans="";
        for (int i=0;i<arr.get(0).size();i++) {
            if(ans.length()>0){
                ans+="AND";
            }
            ans+=recursive(1,arr.get(0).get(i),arr);
        }
        return ans;
    }

    private static String recursive(int arrIndex, String str, ArrayList<ArrayList<String>> arr) {
        String ans="";
        if(arrIndex==arr.size()){
            return "("+str+")";
        }
        for (int i=0;i<arr.get(0).size();i++) {
            if(ans.length()>0){
                ans+="AND";
            }
            ans+=recursive(arrIndex+1,str+"OR"+arr.get(arrIndex).get(i),arr);
        }
        return ans;
    }


    private static ArrayList<String> getPredicates(String part){
        ArrayList<String> arr=new ArrayList<String>();
        while (part.contains(and)){
            int andIndex=part.indexOf(and);
            String predicate=part.substring(0,andIndex);
            arr.add(predicate);
            part=part.substring(andIndex+and.length(),part.length());
        }
        arr.add(part);
        return arr;
    }

}
