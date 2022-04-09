package model;

import java.util.*;

public class Client implements Observer{
    private String name;
    private double budget;
    private List<Employee> employees;

    public Client(String name, double budget) {
        this.name = name;
        this.budget = budget;
        employees = new ArrayList<>();
    }

    public void addEmployee(Employee emp) {
        if (!employees.contains(emp)) {
            employees.add(emp);
            emp.addClient(this);
        }
    }

    public String getName() {
        return name;
    }

    public double getBudget() {
        return budget;
    }

    @Override
    public void update(Observable o, Object arg) {
        String item = ((Item) arg).getName();
        Double price = ((Item) arg).getPrice();
        System.out.println("Hey " +name + "! There's a new menu item: " + item);
        if (price <= budget) {
            System.out.println("And " +name +", it's price " +price +" is within your budget of " +budget +"!");
        } else {
            System.out.println("Unfortunately " +name +", it's price " +price +" is not within your budget of " +budget +" :(");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(name, client.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
