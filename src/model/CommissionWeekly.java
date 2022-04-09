package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Double.parseDouble;

public class CommissionWeekly extends Commission {

    @Override
    public double getIncome() {
        double income = 0;
        for (double num: this.commission) {
            income = income + num;
        }
        return income;
    }

    public void addIncome(double commission) {
        this.commission.add(commission);
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("weeklyIncome"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("weeklyIncome","UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> partsOfLine = splitOnSpace(lines.get(0));
        double amount = parseDouble(partsOfLine.get(1)) + getIncome();
        writer.println("Income: " +amount);
        writer.close();
    }

    public static ArrayList<String> splitOnSpace(String line){
        String[] splits = line.split(" ");
        return new ArrayList<>(Arrays.asList(splits));
    }
}
