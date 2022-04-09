package test;

import model.Employee;
import model.EmployeeOn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeTest {
    private Employee onEmployee;

    @BeforeEach
    public void runBefore() {
        onEmployee = new EmployeeOn();
    }

    @Test
    public void testSetName() {
        onEmployee.setName("Nick");
        assertEquals(onEmployee.getName(),"Nick");
    }

    @Test
    public void testDoPedi() throws IOException {
        onEmployee.doPedi("Classic Pedi");
        assertEquals(onEmployee.getCommissionDaily(), 20);
    }
}