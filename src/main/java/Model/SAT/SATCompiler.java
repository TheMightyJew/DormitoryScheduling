package Model.SAT;

import Model.Apartment;
import Model.SAT.logic.StatementCNF;
import Model.SAT.logic.structures.SymbolTracker;
import Model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SATCompiler {
    private HashMap<Integer,Predicate> predicates_map;
    private Integer counter=0;
    private static String or=")OR(";
    private static String and="AND";


    private void add_Student_At_Apartment_Predicates(Set<Student> students, Set<Apartment> apartments){
        for (Student student:students) {
            for (Apartment apartment:apartments) {
                Predicate_Student_At_Apartment predicate=new Predicate_Student_At_Apartment(student,apartment);
                predicates_map.put(counter,predicate);
            }
        }
    }


    public void addApartment(Apartment apartment){

    }

    public void addStudent(Student student){

    }

    public static void main(String[] args) {
        SymbolTracker tracker = new SymbolTracker();
        tracker.addFunctions( "LiveAt");
        String fol = "EXISTS(x) LiveAt(2,x) AND LiveAt(3,x)";
        StatementCNF cnf = StatementCNF.fromInfixString(fol,tracker);
        System.out.println(cnf.toString());
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
