package interfaces;

import java.io.IOException;

public interface Income {
    double getIncome();
    void addIncome(double num) throws IOException;
}
