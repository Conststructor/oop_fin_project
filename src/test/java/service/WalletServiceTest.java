package service;

import org.app.models.Transaction;
import org.app.models.User;
import org.app.services.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

public class WalletServiceTest {
    User user;
    WalletService WS;


    @BeforeEach
    void setUp() {
        user = new User("test", "123");
        WS = new WalletService();
    }

    @Test
    void getAllIncomeTest() {
        WS.addIncome(user, "test_category", 1000);
        WS.addIncome(user, "test_category", 1700);
        WS.addIncome(user, "test_category", 300);
        double result = user.getWallet().getTransactions().stream().filter(Transaction::isIncome)
                .mapToDouble(Transaction::getValue).sum();

        Assertions.assertEquals(3000, result);
    }

    @Test
    void getAllOutcomeTest() {
        WS.addOutcome(user, "test_category", 1000);
        WS.addOutcome(user, "test_category", 1700);
        WS.addOutcome(user, "test_category", 300);
        double result = user.getWallet().getTransactions().stream().
                filter(transaction -> !transaction.isIncome())
                .mapToDouble(Transaction::getValue).sum();

        Assertions.assertEquals(3000, result);
    }

    @Test
    void getIncomeByCategoryTest() {
        WS.addIncome(user, "test_cat_1", 1000);
        WS.addIncome(user, "test_cat_1", 700);
        WS.addIncome(user, "test_cat_2", 1300);

        user.getWallet().getTransactions().stream().filter(Transaction::isIncome)
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.
                        summingDouble(Transaction::getValue)));
        Map<String, Double> category = WS.getIncomeByCategory(user);

        Assertions.assertEquals(1700, category.get("test_cat_1"));
        Assertions.assertEquals(1300, category.get("test_cat_2"));
    }

    @Test
    void getOucomeByCategoryTest() {
        WS.addIncome(user, "test_cat_1", 1000);
        WS.addIncome(user, "test_cat_1", 700);
        WS.addIncome(user, "test_cat_2", 1300);

        user.getWallet().getTransactions().stream().filter(transaction -> !transaction.isIncome())
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.
                        summingDouble(Transaction::getValue)));
        Map<String, Double> category = WS.getIncomeByCategory(user);

        Assertions.assertEquals(1700, category.get("test_cat_1"));
        Assertions.assertEquals(1300, category.get("test_cat_2"));
    }

    @Test
    void isBudgetEndedTestFalse() {
        String cat = "test_cat";
        user.getWallet().setBudget(cat, 1500d);
        WS.addOutcome(user, cat, 1000);
        Assertions.assertFalse(WS.getBudgetRemaining(user, cat) < 0);
    }

    @Test
    void isBudgetEndedTestTrue() {
        String cat = "test_cat";
        user.getWallet().setBudget(cat, 1500d);
        WS.addOutcome(user, cat, 2000);
        Assertions.assertTrue(WS.getBudgetRemaining(user, cat) < 0);
    }
}
