package org.app.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet {
    private List<Transaction> transactions;
    private Map<String, Double> budgetsList;

    public Wallet() {
        this.transactions = new ArrayList<>();
        this.budgetsList = new HashMap<>();
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void setBudgetsList(Map<String, Double> budgetsList) {
        this.budgetsList = budgetsList;
    }

    public Map<String, Double> getBudgetsList() {
        return budgetsList;
    }

    public void setBudget(String category, Double amount) {
        budgetsList.put(category, amount);
    }

    public Double getBudget(String category) {
        return budgetsList.get(category);
    }
}
