package ui;

import exceptions.DuplicateEmployeeException;
import exceptions.EmployeeNotRegisteredException;
import exceptions.NoEmployeesException;
import exceptions.NotOnMenuException;
import model.*;

import java.io.IOException;
import java.util.*;

public class DeveloperUser {
    private EmployeeManager employeeManager;

    private Menu menu;
    private Scanner scanner;
    private Client kevin;
    private Client evelyn;



    public DeveloperUser() {
        employeeManager = new EmployeeManager();
        menu = new Menu();

        kevin = new Client("Kevin", 50);
        evelyn = new Client("Evelyn", 5);
        menu.addObserver(kevin);
        menu.addObserver(evelyn);

        scanner = new Scanner(System.in);

        ArrayList<String> listOps = new ArrayList<>();
        listOps.add("ae -- add employee");
        listOps.add("ai -- add income");
        listOps.add("cs -- close shop");
        listOps.add("sw -- switch employee");
        listOps.add("wb -- get web URL");
        listOps.add("am -- add to menu");
        String operation;

        while (true) {
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Please select an option:");
            for (String op : listOps) {
                System.out.println(op);
            }
            operation = scanner.nextLine();
            System.out.println("You selected: " + operation);
            if (operation.equals("ae")) {
                operationAddEmployee();
            } else if (operation.equals("ai")) {
                operationAddIncome();
            } else if (operation.equals("sw")) {
                operationSwitchEmployee();
            } else if (operation.equals("cs")) {
                operationCloseShop();
                break;
            } else if (operation.equals("wb")) {
                operationWeb();
            } else if (operation.equals("am")) {
                operationAddToMenu();
            } else
                System.out.println("That was not a valid operation.");
        }
    }

    //==================================================================================================================
    // DeveloperUser Options

    private void operationAddEmployee() {
        System.out.println("Name the employee you want to add:");
        String name = scanner.nextLine();
        try {
            employeeManager.addEmployeeOn(name);
        } catch (DuplicateEmployeeException e) {
            System.out.println(name +" is already registered in the system.");
        }
    }

    private void operationSwitchEmployee() {
        System.out.println("Name the employee you want to switch to absent/present:");
        String name = scanner.nextLine();
        try {
            employeeManager.switchEmployee(name);
        } catch (EmployeeNotRegisteredException | NoEmployeesException e) {}
    }


    public void operationAddIncome() {
        if (employeeManager.getEmployeesOn().size()==0) {
            System.out.println("Sorry, there are no employees currently present to add income to!");
        } else {
            System.out.println("Who would you like to add income to? " + employeeManager.listEmployees(employeeManager.getEmployeesOn()));
            String name = scanner.nextLine();
            try {
                employeeManager.findEmployee(employeeManager.getEmployeesOn(), name);
                System.out.println("Select the service that " + name + " did or type in amount to add: " + menu.getMenuItems());
                String string = scanner.nextLine();
                Double price;
                try {
                    try {
                        price = Double.parseDouble(string);

                    } catch (NumberFormatException e) {
                        price = menu.getPrice(string);
                    }
                    employeeManager.addIncome(name, price);
                } catch (NotOnMenuException e) {
                    System.out.println("That was either not on the menu or not a valid number.");
                }
            } catch (EmployeeNotRegisteredException | NoEmployeesException e) {
                System.out.println("If you would like to register " +name +" into the system, type 'Yes', otherwise type 'No'");
                String add = scanner.nextLine();
                if (add.equals("Yes")) {
                    try {
                        employeeManager.addEmployeeOn(name);
                    } catch (DuplicateEmployeeException e1) {
                        // caught exception
                    }
                    try {
                        employeeManager.findEmployee(employeeManager.getEmployeesOn(), name);
                        System.out.println("Select the service that " + name + " did or type in amount to add: " + menu.getMenuItems());
                        String string = scanner.nextLine();
                        Double price;
                        try {
                            try {
                                price = Double.parseDouble(string);

                            } catch (NumberFormatException e1) {
                                price = menu.getPrice(string);
                            }
                            employeeManager.addIncome(name, price);
                        } catch (NotOnMenuException e1) {
                            System.out.println("That was either not on the menu or not a valid number.");
                        }
                    } catch (EmployeeNotRegisteredException | NoEmployeesException e2) {
                        // Caught exception
                    }
                }
            } finally {
                System.out.println("If you would like to continue adding income, type 'Yes', otherwise type 'No'.");
                String cont = scanner.nextLine();
                if (cont.equals("Yes")) {
                    operationAddIncome();
                }
            }
        }
    }

    public void operationCloseShop() {
        System.out.println("Today's shop income was: " + employeeManager.shopTotalIncome());
        employeeManager.save();
        menu.save();
    }

    public void operationWeb() {
        ReadWebPageEx web = new ReadWebPageEx();
        try {
            web.GetWeb();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void operationAddToMenu() {
        System.out.println("What item would you like to add to the menu?");
        String name = scanner.nextLine();
        System.out.println("What should the price be for that item?");
        Double price = scanner.nextDouble(); scanner.nextLine();
        menu.addItem(name, price);
    }
}
