package org.app.services;

import org.app.models.Transaction;
import org.app.models.User;

import java.util.Map;
import java.util.stream.Collectors;

public class WalletService {
    public void addIncome(User user, String category, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Transaction transaction = new Transaction(category, amount, true);
        user.getWallet().addTransaction(transaction);
    }

    public void addOutcome(User user, String category, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Transaction transaction = new Transaction(category, amount, false);
        user.getWallet().addTransaction(transaction);
    }

    public void setBudget(User user, String category, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Budget amount cannot be negative");
        }
        user.getWallet().setBudget(category, amount);
    }

    public double getAllIncome(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(Transaction::isIncome)
                .mapToDouble(Transaction::getValue)
                .sum();
    }

    public double getAllOutcome(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(transaction -> !transaction.isIncome())
                .mapToDouble(Transaction::getValue)
                .sum();
    }

    public Map<String, Double> getIncomeByCategory(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(Transaction::isIncome)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getValue)
                ));
    }

    public Map<String, Double> getOutcomeByCategory(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(transaction -> !transaction.isIncome())
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getValue)
                ));
    }

    public double getBudgetRemaining(User user, String category) {
        double budget = user.getWallet().getBudget(category) != null ?
                user.getWallet().getBudget(category) : 0.0;
        double spent = getOutcomeByCategory(user).getOrDefault(category, 0.0);
        return budget - spent;
    }

    public boolean isBudgetEnded(User user, String category) {
        return getBudgetRemaining(user, category) < 0;
    }

    public boolean isOutcomeMoreIncome(User user) {
        return getAllOutcome(user) > getAllIncome(user);
    }
}
