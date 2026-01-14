package org.app;

import org.app.services.MenuService;

public class Main {
    public static void main(String[] args) {
        MenuService app = new MenuService();
        app.run();
    }
}