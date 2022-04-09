package test;


import model.Commission;
import model.CommissionDaily;
import model.CommissionWeekly;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static model.CommissionWeekly.splitOnSpace;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommissionTest {
    private Commission commission;
    private Commission commissionW;

    @BeforeEach
    public void runBefore() throws IOException {
        commission = new CommissionDaily();
        commissionW = new CommissionWeekly();
        commission.addIncome(10);
        commission.addIncome(20);

    }

    @Test
    public void testGetIncome() {
        assertTrue(commission.getIncome() == 30);
    }

    @Test
    public void testAddIncome() throws IOException {
        List<Double> listnum = new ArrayList<>();
        listnum.add(10.00);
        listnum.add(20.00);
        listnum.add(30.00);
        commission.addIncome(30);
        assertEquals(commission.getCommission(), listnum);
    }

    @Test
    public void testGetCommission() {
        List<Double> listnum = new ArrayList<>();
        listnum.add(10.00);
        listnum.add(20.00);
        assertEquals(commission.getCommission(), listnum);
    }

    @Test
    public void testAddIncomeW() throws IOException {
        commissionW.addIncome(10);
        List<String> lines = Files.readAllLines(Paths.get("weeklyIncome"));
        ArrayList<String> partsOfLine = splitOnSpace(lines.get(0));
        double amount = parseDouble(partsOfLine.get(1));
        assertTrue(lines.get(0).equals("Income: " +amount));
    }
}