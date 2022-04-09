package test;

import exceptions.EmployeeNotRegisteredException;
import exceptions.NoEmployeesException;
import interfaces.Worker;
import model.Employee;
import model.EmployeeOn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ExceptionTest {
    private Employee selectedOnEmp;
    private List<Employee> employees;

    @BeforeEach
    public void runBefore() {
        selectedOnEmp = new EmployeeOn();
        employees = new ArrayList<>();
    }

    @Test
    public void testThrowsENR() {
        Employee emp1 = new EmployeeOn();
        emp1.setName("emp1");
        try {
            findEmployee(employees, "emp2");
            fail();
        } catch (EmployeeNotRegisteredException e) {
        } catch (NoEmployeesException e) {fail();}
    }

    @Test
    public void testThrowsNE() {
        try {
            findEmployee(employees, "emp1");
            fail();
        } catch (EmployeeNotRegisteredException e) {
            fail();
        } catch (NoEmployeesException e) {}
    }

    @Test
    public void testThrowsNothing() {
        Employee emp1 = new EmployeeOn();
        emp1.setName("emp1");
        try {
            findEmployee(employees, "emp1");
        } catch (EmployeeNotRegisteredException | NoEmployeesException e) {
            fail();
        }
    }

    private static void fail(){
        System.out.println("This task failed.");
    }

    private void findEmployee(List<Employee> employees, String name) throws EmployeeNotRegisteredException, NoEmployeesException {
        if (employees.size() == 0) {
            throw new NoEmployeesException();
        } else if (!listEmployees(employees).contains(name)) {
            throw new EmployeeNotRegisteredException();
        }
        for (Employee employee : employees) {
            if (name.equals(employee.getName())) {
                selectedOnEmp = employee;
            }
        }
    }

    private ArrayList<String> listEmployees(List<Employee> employees) {
        ArrayList<String> names = new ArrayList<>();
        for (Worker employee : employees) {
            names.add(employee.getName());
        }
        return names;
    }
}
