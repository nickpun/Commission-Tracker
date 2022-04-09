package test;

import interfaces.Income;
import model.CommissionDaily;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncomeTest {
    private Income onEmployee;

    @BeforeEach
    public void runBefore() {
        onEmployee = new CommissionDaily();
    }

    @Test
    public void testEmployeeTIncome() throws IOException {
        onEmployee.addIncome(10);
        onEmployee.addIncome(15);
        assertEquals(onEmployee.getIncome(),25);
    }

    @Test
    public void testAddIncome() throws IOException {
        onEmployee.addIncome(10);
        assertEquals(onEmployee.getIncome(),10);
    }
}