package org.app.services;

import org.app.exceptions.UserExceptions;
import org.app.models.User;

import java.util.Map;
import java.util.Scanner;

public class MenuService {

    private final UserService userService;
    private final WalletService financeService;
    private final Scanner scanner;

    public MenuService() {
        userService = new UserService();
        financeService = new WalletService();
        scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Запуск приложения");

        while (true) {
            if (!userService.isUserLoggedIn()) {
                showMainMenu();
                mainMenuHandler();
            } else {
                showWalletMenu();
                walletMenuHandler();
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n------------------------------");
        System.out.println("0 - выход");
        System.out.println("1 - зарегистртроватся");
        System.out.println("2 - войти в аккауунт");
    }

    private void mainMenuHandler() {
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "0":
                System.out.println("закрытие приложения");
                System.exit(0);
                break;
            case "1":
                registrHandler();
                break;
            case "2":
                loginHandler();
                break;
            default:
                System.out.println("некорректный ввод, повторите попытку");
        }
    }

    private void registrHandler() {
        try {
            System.out.print("придумайте и введите логин : ");
            String username = scanner.nextLine().trim();

            System.out.print("пруидумайте и введите пароль: ");
            String password = scanner.nextLine().trim();

            userService.registerUser(username, password);
            System.out.println("регистрация прошла успешно");
        } catch (IllegalArgumentException e) {
            System.out.println("ошибка при регистрации: " + e.getMessage());
        }
    }

    private void loginHandler() {
        try {
            System.out.print("введите ваш логин: ");
            String username = scanner.nextLine().trim();

            System.out.print("введите ваш пароль: ");
            String password = scanner.nextLine().trim();

            userService.authenticateUser(username, password);
            System.out.println("выполнен вход как : " + username);
        } catch (UserExceptions e) {
            System.out.println("ошибка при входе: " + e.getMessage());
        }
    }

    private void showWalletMenu() {
        System.out.println("\n------------------------------");
        System.out.println("0 - выход");
        System.out.println("1 - добавить доход");
        System.out.println("2 - добавить расход");
        System.out.println("3 - установить бюджет");
        System.out.println("4 - просмотр статистики");
    }

    private void walletMenuHandler() {
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "0":
                userService.logout();
                break;
            case "1":
                addIncomeHandler();
                break;
            case "2":
                addOutcomeHandler();
                break;
            case "3":
                setBudgetHandler();
                break;
            case "4":
                showStatistics();
                break;
            default:
                System.out.println("некорректный ввод, повторите попытку");
        }
    }

    private void addIncomeHandler() {
        try {
            System.out.print("введите категорию доходов : ");
            String category = scanner.nextLine().trim();

            System.out.print("введите сумму доходов : ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            financeService.addIncome(userService.getCurrentUser(), category, amount);
            System.out.println("доход добавлен");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка : введите корректное число");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка : " + e.getMessage());
        }
    }

    private void addOutcomeHandler() {
        try {
            System.out.print("введите категорию расходов : ");
            String category = scanner.nextLine().trim();

            System.out.print("введите сумму расходов : ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            financeService.addOutcome(userService.getCurrentUser(), category, amount);

            if (financeService.isBudgetEnded(userService.getCurrentUser(), category)) {
                System.out.println("бюджет в категории " + category + " превышен");
            }

            System.out.println("расход добавлен");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка : введите корректное число");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка : " + e.getMessage());
        }
    }

    private void setBudgetHandler() {
        try {
            System.out.print("введите категорию : ");
            String category = scanner.nextLine().trim();

            System.out.print("введите бюджет : ");
            double budget = Double.parseDouble(scanner.nextLine().trim());

            financeService.setBudget(userService.getCurrentUser(), category, budget);
            System.out.println("бюджет установлен");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка : введите корректное число");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка : " + e.getMessage());
        }
    }

    private void showStatistics() {
        User user = userService.getCurrentUser();

        System.out.println("\n===== Статистика ======");

        double allIncome = financeService.getAllIncome(user);
        double allOutcome = financeService.getAllOutcome(user);

        System.out.println("общий доход : " + allIncome);
        System.out.println("общие расходы : " + allOutcome);

        // Доходы по категориям
        Map<String, Double> incomeByCategory = financeService.getIncomeByCategory(user);
        if (!incomeByCategory.isEmpty()) {
            System.out.println("доходы по категориям :");
            incomeByCategory.forEach((category, amount) ->
                    System.out.println(category + " : " + amount));
        }

        // Расходы по категориям с бюджетами
        Map<String, Double> outcomeByCategory = financeService.getOutcomeByCategory(user);
        if (!outcomeByCategory.isEmpty()) {
            System.out.println("расходы по категориям :");
            outcomeByCategory.forEach((category, amount) -> {
                System.out.println(category + ": " + amount);
                Double budget = user.getWallet().getBudget(category);
                if (budget != null) {
                    double remaining = financeService.getBudgetRemaining(user, category);
                    System.out.println("бюджет: " + budget + ", оставшийся бюджет: " + remaining);
                }
            });
        }

        // Проверка превышения расходов над доходами
        if (financeService.isOutcomeMoreIncome(user)) {
            System.out.println("внимание : расходы превышают доходы!");
        }
    }
}
