package Model.SAT;

import Model.*;
import Model.SAT.logic.StatementCNF;
import Model.SAT.logic.structures.SymbolTracker;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.*;

public class SATCompiler {
    private HashMap<Integer, Predicate> ID_Predicate_Map;
    private HashMap<Predicate, Integer> Predicate_ID_Map;
    private int counter;
    private static String or = ") OR (";
    private static String and = "AND";
    private List<Apartment> apartments;
    private List<Student> students;
    private SymbolTracker tracker;
    private List<StatementCNF> statements;

    public SATCompiler(List<Apartment> apartments, List<Student> students) {
        this.ID_Predicate_Map = new HashMap<Integer, Predicate>();
        this.Predicate_ID_Map = new HashMap<Predicate, Integer>();
        this.counter = 1;
        this.apartments = apartments;
        this.students = students;
        this.tracker = new SymbolTracker();
        statements = new ArrayList<StatementCNF>();
        tracker.addFunctions("LiveAt", "Apartment", "Student");
    }

    private void add_Student_At_Apartment_Predicates() {
        for (Student student : students) {
            String str = "";
            for (int i = 0; i < apartments.size(); i++) {
                boolean unsuitable_apartment = false;
                if (!(apartments.get(i) instanceof Couple_Apartment) && student.getStudentRequest().getCouples_dormitory().equals(Couples_Dormitory.YES)) {
                    unsuitable_apartment = true;
                } else if ((apartments.get(i).getDormitory_type().equals(Dormitory.Dormitory_Type.DALED_TROMIM_FEMALES) && student.getSex().equals(Student.Sex.MALE)) || (apartments.get(i).getDormitory_type().equals(Dormitory.Dormitory_Type.DALED_TROMIM_MALES) && student.getSex().equals(Student.Sex.FEMALE))) {
                    unsuitable_apartment = true;
                }
                else  if(apartments.get(i).getDormitory_type().equals(student.getStudentRequest().getDormitory_type())==false){
                    unsuitable_apartment = true;
                }
                if (unsuitable_apartment) {
                    statements.add(StatementCNF.fromInfixString("!LiveAt(" + student.getID() + "," + apartments.get(i).getApartment_ID() + ")", tracker));
                    continue;
                }
                if (str.isEmpty() == false) {
                    str +=" OR ";
                }
                str += "( LiveAt(" + student.getID() + "," + apartments.get(i).getApartment_ID() + ")";
                for (int j = 0; j < apartments.size(); j++) {
                    if (i != j) {
                        str += " AND !LiveAt(" + student.getID() + "," + apartments.get(j).getApartment_ID() + ")";
                    }
                }
                str += " )";
            }
            if(str.isEmpty()==false){
                statements.add(StatementCNF.fromInfixString(str, tracker));
            }
        }
    }

    private void Define_Students() {
        for (Student student : students) {
            tracker.addConstants(student.getID());
            //StatementCNF statementCNF = StatementCNF.fromInfixString("Student(" + student.getID() + ")", tracker);
            //statements.add(statementCNF);
        }
    }

    private void Define_Apartments() {
        for (Apartment apartment : apartments) {
            tracker.addConstants(apartment.getApartment_ID());
            //StatementCNF statementCNF = StatementCNF.fromInfixString("Apartment(" + apartment.getApartment_ID() + ")", tracker);
            //statements.add(statementCNF);
            List<ArrayList<Student>> groups = new ArrayList<ArrayList<Student>>();
            combinations2(students, apartment.getApartment_Quantity() + 1, 0, 0, new ArrayList<Student>(), groups);
            String str = null;
            for (ArrayList<Student> result : groups) {
                if (str != null) {
                    str += ") AND !(";
                } else
                    str = "!( ";
                boolean first = true;
                for (Student student : result) {
                    if (!first)
                        str += " AND ";
                    else
                        first = !first;
                    str += "LiveAt(" + student.getID() + "," + apartment.getApartment_ID() + ")";

                }
            }
            str += ")";
            statements.add(StatementCNF.fromInfixString(str, tracker));
        }
    }

    static void combinations2(List<Student> arr, int len, int layer, int startPosition, ArrayList<Student> result, List<ArrayList<Student>> groups) {
        if (len == 0) {
            groups.add(result);
            return;
        }
        for (int i = startPosition; i <= arr.size() - len; i++) {
            if (layer == result.size())
                result.add(arr.get(i));
            else {
                ArrayList<Student> temp = new ArrayList<Student>();
                for (Student s : result)
                    temp.add(s);
                temp.set(layer, arr.get(i));
                result = temp;
            }

            combinations2(arr, len - 1, layer + 1, i + 1, result, groups);
        }
    }

    private void students_requests() {
        for (int i = 0; i < students.size(); i++) {
            Student student1 = students.get(i);
            Student_Request student_request1 = student1.getStudentRequest();
            String requesedID = "";
            boolean couple = false;
            if (student_request1.getCouples_dormitory().equals(Couples_Dormitory.YES)) {
                couple = true;
                if (student_request1.getWanted().iterator().hasNext())
                    requesedID = student_request1.getWanted().iterator().next().getID();
                else
                    requesedID = null;
            }
            for (Student student2 : student_request1.getWanted()) {
                String live_together = "";
                boolean first = true;
                for (Apartment apartment : apartments) {
                    if (first == false) {
                        live_together += " OR ";
                    } else {
                        first = false;
                    }
                    live_together += "( LiveAt(" + student1.getID() + "," + apartment.getApartment_ID() + ") AND LiveAt(" + student2.getID() + "," + apartment.getApartment_ID() + " ) )";
                }
                statements.add(StatementCNF.fromInfixString(live_together, tracker));
            }
            for (Student student2 : student_request1.getUnwanted()) {
                String cant_live_together = "";
                boolean first = true;
                for (Apartment apartment : apartments) {
                    if (first == false) {
                        cant_live_together += " AND ";
                    } else {
                        first = false;
                    }
                    cant_live_together += "!( LiveAt(" + student1.getID() + "," + apartment.getApartment_ID() + ") AND LiveAt(" + student2.getID() + "," + apartment.getApartment_ID() + ")";
                }
                statements.add(StatementCNF.fromInfixString(cant_live_together, tracker));
            }
            for (int j = i + 1; j < students.size(); j++) {
                String fol = "";
                StatementCNF cnf;
                Student student2 = students.get(j);
                boolean unwanted = false;
                Student_Request student_request2 = student2.getStudentRequest();
                if (students.get(i).getSex().equals(students.get(j).getSex()) == false) {
                    if (!(couple && requesedID.equals(student2.getID()))) {
                        unwanted = true;
                    }
                }
                if (student_request1.getSmoking() != student_request2.getSmoking()) {
                    unwanted = true;
                } else if (student_request1.getReligious() != student_request2.getReligious()) {
                    unwanted = true;
                }
                if (unwanted) {
                    for (Apartment apartment : apartments) {
                        fol = "!( LiveAt(" + student1.getID() + "," + apartment.getApartment_ID() + ") AND LiveAt(" + student2.getID() + "," + apartment.getApartment_ID() + ") )";
                        statements.add(StatementCNF.fromInfixString(fol, tracker));
                    }
                }

            }
        }
    }

    public void compile() {
        Define_Students();
        Define_Apartments();
        add_Student_At_Apartment_Predicates();
        students_requests();
        ArrayList<String> ands = new ArrayList<String>();
        ArrayList<String> finalisedString=new ArrayList<String>();
        for (StatementCNF stat : statements) {
            ands.addAll(Arrays.asList(stat.toString().split(" AND ")));
        }
        for (String str : ands) {
//            if(str.length()>0 && str.charAt(0)=='(' && str.charAt(str.length()-1)==')'){
//                str=str.substring(1,str.length()-1);
//            }
            str = str.replaceAll("!", "-").replaceAll(", ", ",").replaceAll(" ,", ",");
            while (str.contains("LiveAt")) {
                int index = str.indexOf("LiveAt");
                int indexFirst = 0;
                int indexComma = 0;
                int i = 0;
                for (i = index; str.charAt(i) != ')' && i < str.length(); i++) {
                    if (str.charAt(i) == '(')
                        indexFirst = i;
                    else if (str.charAt(i) == ',')
                        indexComma = i;
                }
                Student student = Student.students.get(str.substring(indexFirst + 1, indexComma));
                Apartment apartment = Apartment.apartments.get(str.substring(indexComma + 1, i));
                Predicate predicate = new Predicate(student, apartment);
                if (Predicate_ID_Map.containsKey(predicate) == false) {
                    Predicate_ID_Map.put(predicate, counter);
                    ID_Predicate_Map.put(counter, predicate);
                    str = str.substring(0, index) + counter + str.substring(i + 1);
                    counter++;
                } else {
                    str = str.substring(0, index) + Predicate_ID_Map.get(predicate) + str.substring(i + 1);
                }
            }
            str = str.replaceAll("[()]", "");
            str = str.replaceAll("OR ", "");
            str += " 0";
            str = str.replaceAll("  ", " ");
            finalisedString.add(str);
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\stiven\\IdeaProjects\\DormitorySchedulling\\src\\main\\java\\Model\\test.txt"));
            writer.write("p cnf " + Predicate_ID_Map.size() + " " + ands.size());
            writer.newLine();
            for (int i = 0; i < finalisedString.size(); i++) {
                writer.write(finalisedString.get(i));
                if(i != finalisedString.size() - 1)
                    writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void solve() {
        try {
            ISolver solver = SolverFactory.newDefault();
            solver.setTimeout(3600); // 1 hour timeout
            Reader reader = new DimacsReader(solver);
            // CNF filename is given on the command line
            IProblem problem = reader.parseInstance("C:\\Users\\stiven\\IdeaProjects\\DormitorySchedulling\\src\\main\\java\\Model\\test.txt");
            if (problem.isSatisfiable()) {
                System.out.println("Satisfiable !");
                for (int num : problem.model()) {
                    if (num > 0) {
                        Predicate predicate = ID_Predicate_Map.get(num);
                        System.out.println(predicate.toString());
                    }
                }
            } else {
                System.out.println("Unsatisfiable !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //test();
        ArrayList<Apartment> apartments = new ArrayList<Apartment>();
        ArrayList<Student> students = new ArrayList<Student>();
        Couple_Apartment apartment1 = new Couple_Apartment("D1", Dormitory.Dormitory_Type.DALED_EAST, Room_Quantity.SINGLE_BED, 2, 0, 0);
        Single_Rooms_Apartment apartment2 = new Single_Rooms_Apartment("D2", Dormitory.Dormitory_Type.DALED_EAST, Room_Quantity.SINGLE_BED, 2, 0, 0);
        Single_Rooms_Apartment apartment3 = new Single_Rooms_Apartment("D3", Dormitory.Dormitory_Type.DALED_EAST, Room_Quantity.SINGLE_BED, 5, 0, 0);
        //first apartment group
        Student student1 = new Student("1", "rotem", "Sleepy", Student.Sex.MALE, 3);
        Student student2 = new Student("2", "gal", "sleeps", Student.Sex.FEMALE, 3);
        Set<Student> rotemList = new HashSet<Student>();
        rotemList.add(student2);
        Set<Student> galList = new HashSet<Student>();
        galList.add(student1);
        Student_Request student_request1 = new Student_Request(Dormitory.Dormitory_Type.DALED_EAST, Couples_Dormitory.YES, 1000, Religious.JEWISH, Smoking.NO, rotemList, new HashSet<Student>());
        Student_Request student_request2 = new Student_Request(Dormitory.Dormitory_Type.DALED_EAST, Couples_Dormitory.YES, 900, Religious.JEWISH, Smoking.NO, galList, new HashSet<Student>());
        student1.setStudentRequest(student_request1);
        student2.setStudentRequest(student_request2);
        //second apartment group
        Student student3 = new Student("3", "3", "3", Student.Sex.MALE, 3);
        Student student4 = new Student("4", "4", "4", Student.Sex.MALE, 3);
        Set<Student> list3 = new HashSet<Student>();
        list3.add(student4);
        Set<Student> list4 = new HashSet<Student>();
        list4.add(student3);
        Student_Request student_request3 = new Student_Request(Dormitory.Dormitory_Type.DALED_EAST, Couples_Dormitory.NO, 1000, Religious.MUSLIM, Smoking.NO, list3, new HashSet<Student>());
        Student_Request student_request4 = new Student_Request(Dormitory.Dormitory_Type.DALED_EAST, Couples_Dormitory.NO, 1000, Religious.MUSLIM, Smoking.NO, list4, new HashSet<Student>());
        student3.setStudentRequest(student_request3);
        student4.setStudentRequest(student_request4);
        //third apartment group
        Student student5 = new Student("5", "5", "5", Student.Sex.MALE, 3);
        Student student6 = new Student("6", "6", "6", Student.Sex.MALE, 3);
        Set<Student> list5 = new HashSet<Student>();
        list5.add(student6);
        Set<Student> list6 = new HashSet<Student>();
        list6.add(student5);
        Student_Request student_request5 = new Student_Request(Dormitory.Dormitory_Type.DALED_EAST, Couples_Dormitory.NO, 1000, Religious.JEWISH, Smoking.NO, list5, new HashSet<Student>());
        Student_Request student_request6 = new Student_Request(Dormitory.Dormitory_Type.DALED_EAST, Couples_Dormitory.NO, 1000, Religious.JEWISH, Smoking.NO, list6, new HashSet<Student>());
        student5.setStudentRequest(student_request5);
        student6.setStudentRequest(student_request6);
        //CONTINUE
        Student student7 = new Student("7", "7", "7", Student.Sex.MALE, 3);
        Student student8 = new Student("8", "8", "8", Student.Sex.MALE, 3);
        Student student9 = new Student("9", "9", "9", Student.Sex.MALE, 3);
        Set<Student> list7 = new HashSet<Student>();
        list7.add(student8);
        list7.add(student9);
        Set<Student> list8 = new HashSet<Student>();
        list8.add(student7);
        list8.add(student9);
        Set<Student> list9 = new HashSet<Student>();
        list9.add(student7);
        list9.add(student8);
        Student_Request student_request7 = new Student_Request(Dormitory.Dormitory_Type.DALED_EAST, Couples_Dormitory.NO, 1000, Religious.JEWISH, Smoking.NO, list7, new HashSet<Student>());
        Student_Request student_request8 = new Student_Request(Dormitory.Dormitory_Type.DALED_EAST, Couples_Dormitory.NO, 1000, Religious.JEWISH, Smoking.NO, list8, new HashSet<Student>());
        Student_Request student_request9 = new Student_Request(Dormitory.Dormitory_Type.DALED_EAST, Couples_Dormitory.NO, 1000, Religious.JEWISH, Smoking.NO, list9, new HashSet<Student>());
        student7.setStudentRequest(student_request7);
        student8.setStudentRequest(student_request8);
        student9.setStudentRequest(student_request9);
        apartments.add(apartment1);
        apartments.add(apartment2);
        apartments.add(apartment3);
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        students.add(student6);
        students.add(student7);
        students.add(student8);
        students.add(student9);

        SATCompiler satCompiler = new SATCompiler(apartments, students);
        satCompiler.compile();
        satCompiler.solve();


        //rotemCNF();
        //stevenCNF();

    }

    private static void test() {
        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        // CNF filename is given on the command line
        try {
            IProblem problem = reader.parseInstance("C:\\Users\\stiven\\IdeaProjects\\DormitorySchedulling\\src\\main\\java\\Model\\test.txt");
            if (problem.isSatisfiable()) {
                System.out.println("Satisfiable !");
            } else {
                System.out.println("Unsatisfiable !");
            }
        } catch (Exception e){
            e.printStackTrace();
    }
    }


    private static void rotemCNF() {
        SymbolTracker tracker = new SymbolTracker();
        tracker.addFunctions("LiveAt", "Student", "Apartment");
        tracker.addConstants("209202126", "111111111", "A1", "a", "b", "c", "d", "e", "f");
        StatementCNF[] stat = new StatementCNF[]{StatementCNF.fromInfixString("Student(209202126) AND Student(111111111) AND Apartment(A1)", tracker), StatementCNF.fromInfixString("!EXISTS(x) Apartment(x) AND LiveAt(209202126,x) AND LiveAt(111111111,x)", tracker)};
//        StatementCNF hagdara=StatementCNF.fromInfixString("Student(a) AND Student(b) AND Student(c) ",tracker);
//        StatementCNF steven=StatementCNF.fromInfixString("EXISTS(x) Apartment(x) AND LiveAt(a,x) AND LiveAt(b,x) AND LiveAt(c,x)",tracker);
//        System.out.println(steven.toString());
        System.out.println(stat[00].toString());
        System.out.println(stat[1].toString());
    }

    private void stevenCNF() {
        String test = "(aANDb)OR(cANDd)OR(eANDf)";
        System.out.println(test);
        System.out.println(FOL2CNF(test));
    }

    public String FOL2CNF(String str) {
        ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();
        while (str.contains(or)) {
            ArrayList<String> smallArr = new ArrayList<String>();
            int orIndex = str.indexOf(or);
            String part = str.substring(1, orIndex);
            arr.add(getPredicates(part));
            str = str.substring(orIndex + or.length() - 1, str.length());
        }
        arr.add(getPredicates(str.substring(1, str.length() - 1)));
        String ans = "";
        for (int i = 0; i < arr.get(0).size(); i++) {
            if (ans.length() > 0) {
                ans += "AND";
            }
            ans += recursive(1, arr.get(0).get(i), arr);
        }
        return ans;
    }

    private static String recursive(int arrIndex, String str, ArrayList<ArrayList<String>> arr) {
        String ans = "";
        if (arrIndex == arr.size()) {
            return "(" + str + ")";
        }
        for (int i = 0; i < arr.get(0).size(); i++) {
            if (ans.length() > 0) {
                ans += "AND";
            }
            ans += recursive(arrIndex + 1, str + "OR" + arr.get(arrIndex).get(i), arr);
        }
        return ans;
    }


    private static ArrayList<String> getPredicates(String part) {
        ArrayList<String> arr = new ArrayList<String>();
        while (part.contains(and)) {
            int andIndex = part.indexOf(and);
            String predicate = part.substring(0, andIndex);
            arr.add(predicate);
            part = part.substring(andIndex + and.length(), part.length());
        }
        arr.add(part);
        return arr;
    }

}
