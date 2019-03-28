package Model.SAT;

import Model.*;
import Model.SAT.logic.StatementCNF;
import Model.SAT.logic.structures.SymbolTracker;

import java.util.*;

public class SATCompiler {
    private HashMap<Integer,Predicate> ID_Predicate_Map;
    private HashMap<Predicate,Integer> Predicate_ID_Map;
    private Integer counter=0;
    private static String or=") OR (";
    private static String and="AND";
    private List<Apartment> apartments;
    private List<Student> students;
    private SymbolTracker tracker;
    private List<StatementCNF> statements;

    public SATCompiler(List<Apartment> apartments, List<Student> students) {
        this.ID_Predicate_Map = new HashMap<Integer, Predicate>();
        this.Predicate_ID_Map=new HashMap<Predicate, Integer>();
        this.counter = counter;
        this.apartments = apartments;
        this.students = students;
        this.tracker = new SymbolTracker();
        statements=new ArrayList<StatementCNF>();
        tracker.addFunctions( "LiveAt","Apartment","Student");
    }

    private void add_Student_At_Apartment_Predicates(){
        for (Student student:students) {
            String str="";
            for(int i=0;i<apartments.size();i++){
                boolean unsuitable_apartment=false;
                if (!(apartments.get(i) instanceof Couple_Apartment) && student.getStudentRequest().getCouples_dormitory().equals(Couples_Dormitory.YES)) {
                    unsuitable_apartment=true;
                }
                else if ((apartments.get(i).getDormitory_type().equals(Dormitory.Dormitory_Type.DALED_TROMIM_FEMALES) && student.getSex().equals(Student.Sex.MALE)) || (apartments.get(i).getDormitory_type().equals(Dormitory.Dormitory_Type.DALED_TROMIM_MALES) && student.getSex().equals(Student.Sex.FEMALE))) {
                    unsuitable_apartment=true;
                }
                if(unsuitable_apartment){
                    statements.add(StatementCNF.fromInfixString("!LiveAt("+student.getID()+","+apartments.get(i).getApartment_ID()+")",tracker));
                    continue;
                }
                if(str.isEmpty()==false){
                    str+=str+" OR ";
                }
                str+="( LiveAt("+student.getID()+","+apartments.get(i).getApartment_ID()+")";
                for(int j=0;j<apartments.size();j++){
                    if(i!=j){
                        str+=" AND !LiveAt("+student.getID()+","+apartments.get(j).getApartment_ID()+")";
                    }
                }
                str+=" )";
            }
            statements.add(StatementCNF.fromInfixString(str,tracker));
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
            List<ArrayList<Student>> groups=new ArrayList<ArrayList<Student>>();
            combinations2(students, apartment.getApartment_Quantity()+1, 0,0, new ArrayList<Student>(),groups);
            String str= null;
            for (ArrayList<Student> result:groups) {
                if(str != null){
                    str+=") AND !(";
                }
                else
                    str = "!( ";
                boolean first = true;
                for (Student student:result) {
                    if(!first)
                        str += " AND ";
                    else
                        first = !first;
                    str+="LiveAt("+student.getID()+","+apartment.getApartment_Quantity()+")";

                }
            }
            str+=")";
            statements.add(StatementCNF.fromInfixString(str, tracker));
        }
    }

    static void combinations2(List<Student> arr, int len,int layer, int startPosition, ArrayList<Student> result, List<ArrayList<Student>> groups){
        if (len == 0){
            groups.add(result);
            return;
        }
        for (int i = startPosition; i <= arr.size()-len; i++){
            if(layer == result.size())
                result.add(arr.get(i));
            else{
                ArrayList<Student> temp = new ArrayList<Student>();
                for(Student s : result)
                    temp.add(s);
                temp.set(layer, arr.get(i));
                result = temp;
            }

            combinations2(arr, len-1, layer + 1,i+1, result, groups);
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
            for(Student student2:student_request1.getWanted()){
                String live_together="";
                boolean first=true;
                for (Apartment apartment:apartments) {
                    if(first==false){
                        live_together+=" OR ";
                    }
                    else{
                        first=false;
                    }
                    live_together="( LiveAt("+student1.getID()+","+apartment.getApartment_ID()+") AND LiveAt("+student2.getID()+","+apartment.getApartment_ID()+" )";
                }
                statements.add(StatementCNF.fromInfixString(live_together,tracker));
            }
            for(Student student2:student_request1.getUnwanted()){
                String cant_live_together="";
                boolean first=true;
                for (Apartment apartment:apartments) {
                    if(first==false){
                        cant_live_together+=" AND ";
                    }
                    else{
                        first=false;
                    }
                    cant_live_together="!( LiveAt("+student1.getID()+","+apartment.getApartment_ID()+") AND LiveAt("+student2.getID()+","+apartment.getApartment_ID()+")";
                }
                statements.add(StatementCNF.fromInfixString(cant_live_together,tracker));
            }
            for(int j=i+1;j<students.size();j++){
                String fol="";
                StatementCNF cnf;
                Student student2=students.get(j);
                boolean unwanted=false;
                Student_Request student_request2=student1.getStudentRequest();
                if(students.get(i).getSex().equals(students.get(j).getSex())==false){
                    if(!(couple && requesedID.equals(student2.getID()))){
                        unwanted=true;
                    }
                }
                else if(student_request1.getSmoking()!=student_request2.getSmoking()){
                    unwanted=true;
                }
                else if(student_request1.getReligious()!=student_request2.getReligious()){
                    unwanted=true;
                }
                if(unwanted){
                    for (Apartment apartment:apartments) {
                        fol="!( LiveAt("+student1.getID()+","+apartment.getApartment_ID()+") AND LiveAt("+student2.getID()+","+apartment.getApartment_ID()+")";
                        statements.add(StatementCNF.fromInfixString(fol,tracker));
                    }
                }

            }
        }
    }

    public static void main(String[] args) {

        ArrayList<Student> students=new ArrayList<Student>();
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("3"));
        students.add(new Student("4"));
        List<ArrayList<Student>> groups=new ArrayList<ArrayList<Student>>();
        combinations2(students, 2, 0,0, new ArrayList<Student>(),groups);
        int a=0;

//        SymbolTracker tracker=new SymbolTracker();
//        tracker.addFunctions( "LiveAt","Apartment","Student");
//        tracker.addConstants("222");
//        StatementCNF hagdara=StatementCNF.fromInfixString("Student(222)",tracker);
//        StatementCNF statementCNF=StatementCNF.fromInfixString("EXISTS(x1) LiveAt(222,x1) AND !EXISTS(x2) LiveAt(222,x2)",tracker);
//        System.out.println(statementCNF);

//        try{
//            ISolver solver = SolverFactory.newDefault();
//            Reader reader = new DimacsReader(solver);
//            // CNF filename is given on the command line
//                IProblem problem = reader.parseInstance("C:\\Users\\stiven\\IdeaProjects\\DormitorySchedulling\\src\\main\\java\\Model\\test.txt");
//                if (problem.isSatisfiable()) {
//                    System.out.println("Satisfiable !");
//                    for(int num:problem.model()){
//                        System.out.print(num+",");
//                    }
//                } else {
//                    System.out.println("Unsatisfiable !");
//                }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }


        //rotemCNF();
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

    private void stevenCNF(){
        String test="(aANDb)OR(cANDd)OR(eANDf)";
        System.out.println(test);
        System.out.println(FOL2CNF(test));
    }

    public String FOL2CNF(String str){
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
