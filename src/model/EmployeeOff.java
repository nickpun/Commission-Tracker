package model;

public class EmployeeOff extends Employee {
    public void doPedi(String pedi) {
        if (pedi.equals("Classic Pedi")) {
            System.out.println("Classic Pedi will be booked for tomorrow.");
        }
        else if (pedi.equals("Jelly Pedi")) {
            System.out.println("Jelly Pedi will be booked for tomorrow.");
        }
    }
}