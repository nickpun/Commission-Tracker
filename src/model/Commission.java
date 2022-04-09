package model;

import interfaces.Income;

import java.util.ArrayList;
import java.util.List;

public abstract class Commission implements Income{
    protected List<Double> commission = new ArrayList<>();

    public void setCommission(List<Double> commission) {
        this.commission = commission;
    }

    public List<Double> getCommission() {
        return commission;
    }

    public double getIncome() {
        double income = 0;
        for (Double num: commission) {
            income = income + num;
        }
        return income;
    }

    public abstract void addIncome(double commission) ;
}
