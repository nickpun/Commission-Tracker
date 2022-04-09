package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Double.parseDouble;

public class Loader {

    private static ArrayList<String> splitOnString(String line, String str) {
        String[] splits = line.split(str);
        return new ArrayList<>(Arrays.asList(splits));
    }

    public ArrayList<Employee> loadEmployees(String file) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Employee> employees = new ArrayList<>();
        for (String line : lines) {
            if (!line.contains("    ")) {
                Employee employee;
                if (file.equals("employees_on")) {
                    employee = new EmployeeOn();
                } else {
                    employee = new EmployeeOff();
                }
                employee.setName(line);
                employees.add(employee);
                employee.addIncome(0);
            } else {
                String newline = line.replace("    ", "");
                List<String> partsOfLine = splitOnString(newline, ": ");
                List<String> numsString = splitOnString(partsOfLine.get(1), ", ");
                Commission commission = new CommissionDaily();
                for (String string : numsString) {
                    commission.addIncome(parseDouble(string));
                }
                employees.get(employees.size()-1).addCommission(partsOfLine.get(0), commission);
            }
        }
        for (Employee employee : employees) {
            if (file.equals("employees_on")) {
                System.out.println("Loaded present employee: " + employee.getName() + " -- Income: " + employee.getIncome());
            } else {
                System.out.println("Loaded absent employee: " + employee.getName() + " -- Income: " + employee.getIncome());
            }
        }
        return employees;
    }

    public ArrayList<Client> loadClients(String file) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Client> clients = new ArrayList<>();
        for (String line : lines) {
            ArrayList<String> partsOfLine = splitOnString(line, ": ");
            Client client = new Client(partsOfLine.get(0), parseDouble(partsOfLine.get(1)));
            System.out.println("Loaded client: " + client.getName() + " -- Budget: " + client.getBudget());
        }
        return clients;
    }

    public HashMap<String, Double> loadMenu(String file) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String, Double> menu = new HashMap<>();
        for (String line : lines) {
            ArrayList<String> partsOfLine = splitOnString(line, ": ");
            menu.put(partsOfLine.get(0), parseDouble(partsOfLine.get(1)));
            System.out.println("Loaded menu item: " + partsOfLine.get(0) + " -- Price: " + partsOfLine.get(1));
        }
        return menu;
    }

    public void save(List<Employee> employees, String file) {
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            for (Employee employee : employees) {
                String dailyLine = (employee.getName() + ": " + employee.getCommissionDaily());
                System.out.println(dailyLine);

                List<String> lines = new ArrayList<>();
                lines.add(employee.getName());
                Set<Map.Entry<String, Commission>> entries = employee.getCommissions().entrySet();
                for (Map.Entry<String, Commission> entry : entries) {
                    List<String> numsList = new ArrayList<>();
                    for (Double d : entry.getValue().getCommission()) {
                        numsList.add(Double.toString(d));
                    }
                    numsList.removeIf(x -> x.equals("0.0"));
                    if (numsList.size() == 0) {
                        numsList.add("0.0");
                    }
                    String nums = String.join(", ", numsList);
                    String line = ("    ").concat(entry.getKey().concat(": " +nums));
                    lines.add(line);
                }
                for (String line : lines) {
                    writer.println(line);
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(HashMap<String, Double> menu, String file) {
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            for (Map.Entry<String, Double> entry : menu.entrySet()) {
                String line = (entry.getKey() +": " +entry.getValue());
                writer.println(line);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
