package ui;

import exceptions.DuplicateEmployeeException;
import exceptions.EmployeeNotRegisteredException;
import exceptions.NoEmployeesException;
import exceptions.NotOnMenuException;
import model.Client;
import model.EmployeeManager;
import model.Menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

public class CommissionTracker extends JFrame implements ActionListener {
    private JPanel inputPanel;
    private JPanel outputPanel;
    private JTextArea textPanel;
    private JPanel operationArea;
    private JPanel userArea;
    private JPanel userExtraArea;
    private JLabel label;
    private JTextField field;
    private JScrollPane outputScroll;
    private JPanel outputSelect;

    private ArrayList<JButton> listOps;
    private JButton btnSV;
    private JButton btnAI;
    private JButton btnAE;
    private JButton btnSW;
    private JButton btnAM;
    private JButton btnVW;
    private JButton btnGO;
    private JButton btnYS;
    private JButton btnNO;
    private JButton btnEP;
    private JButton btnEA;
    private JButton btnMU;
    private JButton btnTM;

    private EmployeeManager employeeManager;
    private Menu menu;
    private GridLayout gridLayoutCollapsed;
    private GridLayout gridLayoutExpanded;
    private Dimension dimensionCollapsed;
    private Dimension dimensionExpanded;

    private String operation;
    private String name;
    private Double amount;
    private String outputSelected;

    public CommissionTracker()
    {
        super("Commission Tracker");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10) );
        setPreferredSize(new Dimension(800, 500));

        initializeFields();
        initializeOperations();
        initializeOperationsPanel();
        initializeUserPanel();
        initializeIOPanels();
        drawGUICollapsed();

        pack();
        setVisible(true);
        setVisible(true);
        setResizable(false);
    }

    //this is the method that runs when Swing registers an action on an element
    //for which this class is an ActionListener
    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if (ac.equals("AE")) {
            operation = "AE";
            label.setText("Name the employee you want to add.");
        } else if (ac.equals("AI")) {
            operation = "AI1";
            if (employeeManager.getEmployeesOn().size()==0) {
                label.setText("Sorry, there are no employees currently present to add income to!");
            } else {
                label.setText("Who would you like to add income to? "
                        + employeeManager.listEmployees(employeeManager.getEmployeesOn()));
            }
        } else if (ac.equals("SW")) {
            operation = "SW";
            label.setText("Name the employee you want to switch to absent/present.");
        } else if (ac.equals("SV")) {
            operationSave();
        } else if (ac.equals("AM")) {
            operation = "AM1";
            label.setText("What item would you like to add to the menu?");
        } else if (ac.equals("Enter")) {
            userGO();
        } else if (ac.equals("YS")) {
            userYES();
        } else if (ac.equals("NO")) {
            userNO();
        } else if (ac.equals("VW")) {
            operationView();
        } else if (ac.equals("EP")) {
            outputSelected = "employees_on";
            userExtraArea.add(btnTM);
            repaint();
            populateOutput();
        } else if (ac.equals("EA")) {
            outputSelected = "employees_off";
            userExtraArea.add(btnTM);
            repaint();
            populateOutput();
        } else if (ac.equals("MU")) {
            outputSelected = "menu";
            userExtraArea.remove(btnTM);
            repaint();
            populateOutput();
        } else if (ac.equals("TM")) {
            setTimeInterval();
        }
    }

    private void userGO() {
        if (!field.getText().equals("")) {
            if (operation.equals("AE")) {
                operationAddEmployee();
            } else if (operation.equals("SW")) {
                operationSwitchEmployee();
            } else if (operation.equals("AM1")) {
                operationAddToMenu1();
            } else if (operation.equals("AM2")) {
                operationAddToMenu2();
            } else if (operation.equals("AI1")) {
                operationAddIncome1();
            } else if (operation.equals("AI2")) {
                operationAddIncome2();
            }
        }
    }

    private void userYES() {
        if (operation.equals("YN1")) {
            operationAddIncome1YN("YS");
        } else if (operation.equals("YN2")) {
            operationAddIncome2YN("YS");
        }
        userArea.remove(btnYS);
        userArea.remove(btnNO);
        repaint();
        userArea.add(field);
        userArea.add(btnGO);
    }

    private void userNO() {
        if (operation.equals("YN1")) {
            operationAddIncome1YN("NO");
        } else if (operation.equals("YN2")) {
            operationAddIncome2YN("NO");
        }
        userArea.remove(btnYS);
        userArea.remove(btnNO);
        repaint();
        userArea.add(field);
        userArea.add(btnGO);
    }

    //==================================================================================================================
    // User Options

    // Adds a new present employee to the system
    private void operationAddEmployee() {
        name = field.getText();
        try {
            employeeManager.addEmployeeOn(name);
            label.setText("Added " + name + " to registered employees.");
            btnSV.setText("Save (Not saved)");
        } catch (DuplicateEmployeeException e) {
            label.setText(name +" is already registered in the system.");
        }
        operation = "";
        field.setText("");
    }

    // Switches employee to absent is present or present if absent
    private void operationSwitchEmployee() {
        name = field.getText();
        try {
            employeeManager.switchEmployee(name);
            if (employeeManager.listEmployees(employeeManager.getEmployeesOn()).contains(name)) {
                label.setText("Made " + name + " present.");
            } else if (employeeManager.listEmployees(employeeManager.getEmployeesOff()).contains(name)) {
                label.setText("Made " + name + " absent.");
            }
            btnSV.setText("Save (Not saved)");
        } catch (EmployeeNotRegisteredException e) {
            label.setText(name +" is not registered in the system.");
        } catch (NoEmployeesException e) {
            label.setText("Sorry, there are currently no employees registered in the system.");
        }
        operation = "";
        field.setText("");
    }

    // Adds income to an employee
    private void operationAddIncome1() {
        name = field.getText();
        try {
            employeeManager.findEmployee(employeeManager.getEmployeesOn(), name);
            label.setText("Select the service that " + name + " did or type in amount to add: " + menu.getMenuItems());
            operation = "AI2";
            field.setText("");
        } catch (EmployeeNotRegisteredException | NoEmployeesException e) {
            label.setText(name +" is not registered in the system, would you like to register " + name + "?");
            operation = "YN1";
            field.setText("");
            userArea.remove(field);
            userArea.remove(btnGO);
            repaint();
            userArea.add(btnYS);
            userArea.add(btnNO);
        }
    }
    private void operationAddIncome1YN(String add) {
        if (add.equals("YS")) {
            try {
                employeeManager.addEmployeeOn(name);
                employeeManager.findEmployee(employeeManager.getEmployeesOn(), name);
                label.setText("Select the service that " + name
                        + " did or type in amount to add: " + menu.getMenuItems());
                operation = "AI2";
                field.setText("");
                btnSV.setText("Save (Not saved)");
            } catch (EmployeeNotRegisteredException | NoEmployeesException | DuplicateEmployeeException e) {
                // Caught exception
            }
        } else {
            operation = "";
            field.setText("");
            label.setText("Please select an option.");
        }
    }
    private void operationAddIncome2() {
        String string = field.getText();
        Double price;
        try {
            try {
                price = parseDouble(string);
            } catch (NumberFormatException e) {
                price = menu.getPrice(string);
            }
            price = Math.round(price * 100d) / 100d;
            employeeManager.addIncome(name, price);
            amount += price;
            label.setText("Would you like to continue adding income to " +name +"?");
        } catch (NotOnMenuException e) {
            label.setText("That was either not on the menu or not a valid number. "
                    +"Would you like to continue adding income to " +name +"?");
        }
        operation = "YN2";
        field.setText("");
        userArea.remove(field);
        userArea.remove(btnGO);
        repaint();
        userArea.add(btnYS);
        userArea.add(btnNO);

    }
    private void operationAddIncome2YN(String cont) {
        if (cont.equals("YS")) {
            label.setText("Select the service that " + name + " did or type in amount to add: " + menu.getMenuItems());
            operation = "AI2";
            field.setText("");
        } else {
            operation = "";
            field.setText("");
            amount = Math.round(amount * 100d) / 100d;
            label.setText("Added a total of " +amount +" to " +name +".");
            amount = 0.0;
            btnSV.setText("Save (Not saved)");
        }
    }

    // Adds item to menu
    private void operationAddToMenu1() {
        name = field.getText();
        label.setText("What should the price be for " +name);
        operation = "AM2";
        field.setText("");
    }
    private void operationAddToMenu2() {
        try {
            amount = parseDouble(field.getText());
            amount = Math.round(amount * 100d) / 100d;
            menu.addItem(name, amount);
            label.setText("Added " +name +" to the menu at the price of " +amount +" dollars.");
            operation = "";
            amount = 0.0;
            btnSV.setText("Save (Not saved)");
        } catch (NumberFormatException e) {
            label.setText("Please enter a valid number.");
            operation = "AM2";
        }
        field.setText("");
    }

    // Saves list of employees and menu
    private void operationSave() {
        label.setText("Today's shop income was: " + employeeManager.shopTotalIncome());
        employeeManager.save();
        menu.save();
        operation = "";
        field.setText("");
        label.setText("Saved.");
        btnSV.setText("Save (Saved)");
        populateOutput();
    }

    // Expands or collapses view
    private void operationView() {
        if (btnVW.getText().equals("View Saved Info")) {
            drawGUIExpanded();
            btnVW.setText("Collapse Saved Info");
        } else {
            drawGUICollapsed();
            btnVW.setText("View Saved Info");
        }
    }

    //==================================================================================================================
    // Startup

    private void initializeFields() {
        inputPanel = new JPanel();
        outputPanel = new JPanel();
        userExtraArea = new JPanel();
        textPanel = new JTextArea(25,1);
        outputScroll = new JScrollPane(textPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outputSelect = new JPanel();
        gridLayoutCollapsed = new GridLayout(1,1,10,0);
        gridLayoutExpanded = new GridLayout(1,2,25,0);
        dimensionCollapsed = new Dimension(800,500);
        dimensionExpanded = new Dimension(1600,500);
        listOps = new ArrayList<>();
        operation = "";
        name = "";
        amount = 0.0;
        outputSelected = "employees_on";
        employeeManager = new EmployeeManager();
        menu = new Menu();
        menu.addObserver(new Client("Kevin", 50));
        menu.addObserver(new Client("Evelyn", 5));
    }

    private void initializeOperations() {
        btnSV = new JButton("Save (Saved)");
        btnAI = new JButton("Add Income");
        btnAE = new JButton("Add Employee");
        btnSW = new JButton("Switch Employee");
        btnAM = new JButton("Add to Menu");
        btnVW = new JButton("View Saved Info");
        btnEP = new JButton("Present Employees");
        btnEA = new JButton("Absent Employees");
        btnMU = new JButton("Menu");
        btnTM = new JButton("Today");
        btnSV.setActionCommand("SV");
        btnSV.addActionListener(this);
        btnAI.setActionCommand("AI");
        btnAI.addActionListener(this);
        btnAE.setActionCommand("AE");
        btnAE.addActionListener(this);
        btnSW.setActionCommand("SW");
        btnSW.addActionListener(this);
        btnAM.setActionCommand("AM");
        btnAM.addActionListener(this);
        btnVW.setActionCommand("VW");
        btnVW.addActionListener(this);
        btnEP.setActionCommand("EP");
        btnEP.addActionListener(this);
        btnEA.setActionCommand("EA");
        btnEA.addActionListener(this);
        btnMU.setActionCommand("MU");
        btnMU.addActionListener(this);
        btnTM.setActionCommand("TM");
        btnTM.addActionListener(this);
        listOps.add(btnSV);
        listOps.add(btnAI);
        listOps.add(btnAE);
        listOps.add(btnSW);
        listOps.add(btnAM);
        listOps.add(btnVW);
    }

    private void initializeOperationsPanel() {
        operationArea = new JPanel();
        operationArea.setLayout(new FlowLayout());

        for (JButton btn : listOps) {
            operationArea.add(btn);
        }
    }

    private void initializeUserPanel() {
        label = new JLabel("Please select an option above.", SwingConstants.CENTER);
        userArea = new JPanel();
        userArea.setLayout(new FlowLayout());
        userExtraArea.setLayout(new FlowLayout());
        field = new JTextField(30);
        btnGO = new JButton("Enter");
        btnYS = new JButton("Yes");
        btnNO = new JButton("No");
        btnGO.setActionCommand("Enter");
        btnYS.setActionCommand("YS");
        btnNO.setActionCommand("NO");
        btnGO.addActionListener(this);
        btnYS.addActionListener(this);
        btnNO.addActionListener(this);
        userArea.add(field);
        userArea.add(btnGO);
    }

    private void initializeIOPanels() {
        inputPanel.setLayout(new GridLayout(4,1));
        inputPanel.add(operationArea, BorderLayout.NORTH);
        inputPanel.add(label);
        inputPanel.add(userArea, BorderLayout.CENTER);
        inputPanel.add(userExtraArea, BorderLayout.CENTER);
        textPanel.setLayout(new BorderLayout());
        textPanel.setEditable(false);
        outputSelect.setLayout(new FlowLayout());
        outputSelect.add(btnEP);
        outputSelect.add(btnEA);
        outputSelect.add(btnMU);
        outputPanel.setLayout(new BoxLayout(outputPanel,BoxLayout.Y_AXIS));
        outputPanel.add(outputSelect);
        outputPanel.add(outputScroll);
    }

    private void drawGUICollapsed() {
        remove(outputPanel);
        userExtraArea.remove(btnTM);
        setLayout(gridLayoutCollapsed);
        setSize(dimensionCollapsed);
        add(inputPanel);
        revalidate();
    }

    private void drawGUIExpanded() {
        setLayout(gridLayoutExpanded);
        setSize(dimensionExpanded);
        add(outputPanel);
        userExtraArea.add(btnTM);
        populateOutput();
        revalidate();
    }

    private void populateOutput() {
        textPanel.setText("");
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(outputSelected));
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (outputSelected) {
            case "employees_on":
                textPanel.append("Present Employees\n=====================================================\n"); break;
            case "employees_off":
                textPanel.append("Absent Employees\n=====================================================\n"); break;
            case "menu":
                textPanel.append("Menu\n=====================================================\n"); break;
        }
        if (!outputSelected.equals("menu") && !(lines.size()==0)) {
            if (btnTM.getText().equals("Today")) {
                lines = getLastNElementsPerEmp(lines, 1);
            } else if (btnTM.getText().equals("Past Week")) {
                lines = getLastNElementsPerEmp(lines, 7);
            }
        }
        for (String line : lines) {
            textPanel.append(line +"\n");
        }
    }
    private ArrayList<String> getLastNElementsPerEmp(List<String> lines, int numDays) {
        ArrayList<String> newlines = new ArrayList<>();
        int ind = 0;
        int i = 0;
        int j = 1;
        while (i < lines.size()) {
            if (!lines.get(i).contains("    ")) {
                newlines.add(lines.get(i));
                while ((i+j) < lines.size() && lines.get(i + j).contains("    ")) {
                    if (ind < numDays) {
                        newlines.add(lines.get(i+j));
                        ind++;
                    } else {
                        newlines.remove(newlines.size() - ind);
                        newlines.add(lines.get(i + j));
                    }
                    j++;
                }
                ind = 0;
                j = 1;
            }
            i++;
        }
        return newlines;
    }

    private void setTimeInterval() {
        if (btnTM.getText().equals("Today")) {
            btnTM.setText("Past Week");
        } else if (btnTM.getText().equals("Past Week")) {
            btnTM.setText("ALLLL TIME!!");
        } else if (btnTM.getText().equals("ALLLL TIME!!")) {
            btnTM.setText("Today");
        }
        populateOutput();
    }
}