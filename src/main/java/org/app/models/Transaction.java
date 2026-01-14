package org.app.models;

import java.util.Objects;

public class Transaction {
    private String category;
    private double value;
    private boolean isIncome;

    public Transaction(String category, double value, boolean isIncome) {
        this.category = category;
        this.value = value;
        this.isIncome = isIncome;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.value, value) == 0 &&
                isIncome == that.isIncome &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, value, isIncome);
    }
}
