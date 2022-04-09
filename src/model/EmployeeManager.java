package model;

import exceptions.DuplicateEmployeeException;
import exceptions.EmployeeNotRegisteredException;
import exceptions.NoEmployeesException;

import java.util.ArrayList;
import java.util.List;

public class EmployeeManager extends Loader{
    private List<Employee> employeesOn;
    private List<Employee> employeesOff;
    private Employee selectedEmp;

    public EmployeeManager() {
        employeesOn = loadEmployees("employees_on");
        employeesOff = loadEmployees("employees_off");
    }

    public List<Employee> getEmployeesOn() {
        return employeesOn;
    }

    public List<Employee> getEmployeesOff() {
        return employeesOff;
    }

    public void addEmployeeOn(String name) throws DuplicateEmployeeException{
        if (!listEmployees(employeesOn).contains(name)) {
            Employee emp = new EmployeeOn();
            emp.setName(name);
            emp.addIncome(0);
            employeesOn.add(emp);
        } else {
            throw new DuplicateEmployeeException();
        }
    }

    public void removeEmployeeOn(Employee emp) {
        employeesOn.remove(emp);
    }

    public void addEmployeeOff(String name) {
        Employee emp = new EmployeeOff();
        emp.setName(name);
        employeesOff.add(emp);
    }

    public void removeEmployeeOff(Employee emp) {
        employeesOff.remove(emp);
    }

    public void switchEmployee(String name) throws EmployeeNotRegisteredException, NoEmployeesException{
        if (employeesOn.size()==0 && employeesOff.size()==0) {
            throw new NoEmployeesException();
        } else {
            try {
                findEmployee(employeesOn, name);
                Employee emp = new EmployeeOff();
                emp.setName(selectedEmp.getName());
                emp.setCommissions(selectedEmp.getCommissions());
                emp.setClients(selectedEmp.getClients());
                employeesOff.add(emp);
                employeesOn.remove(selectedEmp);
                System.out.println("Made " + name + " absent with " + emp.getIncome() + " dollars.");
            } catch (EmployeeNotRegisteredException e1) {
                findEmployee(employeesOff, name);
                Employee emp = new EmployeeOn();
                emp.setName(selectedEmp.getName());
                emp.setCommissions(selectedEmp.getCommissions());
                emp.setClients(selectedEmp.getClients());
                employeesOn.add(emp);
                employeesOff.remove(selectedEmp);
                System.out.println("Made " + name + " present with " + emp.getIncome() + " dollars.");
            }
        }
    }

    // REQUIRES: employeesOn is not null
    // MODIFIES: nothing
    // EFFECTS: creates and returns a list of the names of all employeesOn
    public ArrayList<String> listEmployees(List<Employee> employees) {
        ArrayList<String> names = new ArrayList<>();
        for (Employee employee : employees) {
            names.add(employee.getName());
        }
        return names;
    }

    public void addIncome(String name, Double amount){
        selectedEmp.addIncome(amount);
        System.out.println("Added " + amount + " to " + name + "'s total income which is now "
                + selectedEmp.getCommissionDaily());
    }

    // REQUIRES: list of onEmployees be non-empty and not null
    // MODIFIES: this
    // EFFECTS: finds the user's selected employee if that employee is registered, else asks user to select another employee
    public void findEmployee(List<Employee> employees, String name) throws EmployeeNotRegisteredException, NoEmployeesException {
        if (employees.size() == 0) {
            throw new NoEmployeesException();
        } else if (!listEmployees(employees).contains(name)) {
            throw new EmployeeNotRegisteredException();
        }
        for (Employee employee : employees) {
            if (name.equals(employee.getName())) {
                selectedEmp = employee;
            }
        }
    }

    // REQUIRES: list of onEmployees is not null
    // MODIFIES: nothing
    // EFFECTS: returns the sum of all the incomes of all onEmployees currently registered
    public double shopTotalIncome() {
        double sum = 0;
        for (Employee onEmployee : employeesOn) {
            sum = sum + onEmployee.getCommissionDaily();
        }
        return sum;
    }

    public void save() {
        save(employeesOn, "employees_on");
        save(employeesOff, "employees_off");
    }
}
