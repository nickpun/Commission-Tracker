package exceptions;

public class NoEmployeesException extends Exception {
    public NoEmployeesException(){
        System.out.println("Sorry, there are currently no employees in the system.");
    }
}
