package model;

import interfaces.Income;
import interfaces.Worker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class Employee implements Worker, Income {
    private DateFormat dateFormat;
    private String date;

    private String name;
    private HashMap<String, Commission> commissions;
    private List<Client> clients;

    public Employee() {
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = dateFormat.format(new Date());

        name = "";
        commissions = new HashMap<>();
        clients = new ArrayList<>();
    }


    // MODIFIES: this
    // EFFECTS: sets name of employee to input name
    @Override
    public void setName (String name) {this.name = name;}
    @Override
    public String getName() {return name;}

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public void setCommissions(HashMap<String, Commission> commissions) {
        this.commissions = commissions;
    }

    public HashMap<String, Commission> getCommissions() {
        return commissions;
    }

    public void addCommission(String date, Commission commission) {
        commissions.put(date, commission);
    }

    public abstract void doPedi(String pedi);

    public double getCommissionDaily(String date) {
        return commissions.get(date).getIncome();
    }

    public double getCommissionDaily() {
        return getIncome();
    }

    public double getIncome() {
        if (!commissions.containsKey(date)) {
            return 0;
        } else {
            return commissions.get(date).getIncome();
        }
    }

    @Override
    public void addIncome(double num) {
        if (!commissions.containsKey(date)) {
            Commission com = new CommissionDaily();
            commissions.put(date,com);
        }
        commissions.get(date).addIncome(num);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(name, employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void removeCommissionDaily(String date) {
        commissions.remove(date);
    }

    public void addClient(Client c) {
        if (!clients.contains(c)) {
            clients.add(c);
            c.addEmployee(this);
        }
    }
}