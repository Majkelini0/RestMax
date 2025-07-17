package model;

import gui.StartScreen;
import util.ObjectPlus;

import javax.swing.*;
import java.io.File;
import java.time.LocalDate;
import java.util.EnumSet;

public class Main {
    public static void main(String[] args) {
        File file = new File("extent");
        if (!file.exists()) {
//            initializeData(); // it resets the program data
        }

        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            new StartScreen();
        });

    }

    private static void initializeData() {
        System.out.println("Initializing data...");
        Address adr1 = new Address("Koszykowa", "23", "113", "Warsaw", "12-345");
        Address adr2 = new Address("Polna", "12", "Warsaw", "67-890");
        Address adr3 = new Address("Spokojna", "666", "Warsaw", "11-222");
        Address adr4 = new Address("Księcia Janusza", "56A", "Wroclaw", "32-143");
        Address adr5 = new Address("Bonifacego", "11", "Cracow", "05-735");
        Address adr6 = new Address("Al. Solidarnosci", "4B", "Gdansk", "51-528");

        Restaurant local1 = new Restaurant("Koszykowa Bistro", adr1, EnumSet.of(RestaurantClass.Delivery));
        Restaurant local2 = new Restaurant("Polna Bistro", adr2, EnumSet.of(RestaurantClass.Delivery, RestaurantClass.OwnBuilding, RestaurantClass.DriveThru));
        Restaurant local3 = new Restaurant("Al. Jerozolimskie Bistro", adr3, EnumSet.of(RestaurantClass.Delivery, RestaurantClass.OwnBuilding));
        Restaurant local4 = new Restaurant("Księcia Janusza Bistro", adr4, EnumSet.of(RestaurantClass.OwnBuilding, RestaurantClass.DriveThru));
        Restaurant local5 = new Restaurant("Bonifacego Bistro", adr5, EnumSet.of(RestaurantClass.OwnBuilding));
        Restaurant local6 = new Restaurant("Al. Solidarnosci Bistro", adr6, EnumSet.of(RestaurantClass.Delivery, RestaurantClass.OwnBuilding, RestaurantClass.DriveThru));

        MenuCategory mains = new MenuCategory("mains");
        MenuCategory drinks = new MenuCategory("Drinks");
        MenuCategory desserts = new MenuCategory("deSsErts");
        MenuCategory soup = new MenuCategory("Soup");
        MenuCategory coffe = new MenuCategory("Coffee");

        local1.addMenuCategory(mains);
        local1.addMenuCategory(drinks);
        local1.addMenuCategory(desserts);
        drinks.addRestaurant(local2);
        desserts.addRestaurant(local2);

        MenuItemSeasonal drwal = new MenuItemSeasonal("Drwal", 26.0, LocalDate.of(2020, 12, 1), LocalDate.of(2026, 2, 24), 3);
        MenuItemSeasonal princess = new MenuItemSeasonal("McPrincess", 31, LocalDate.of(2010, 6, 1), LocalDate.of(2026, 8, 30), 1);

        MenuItemFixed mcchicken = new MenuItemFixed("McChicken", 15.0, LocalDate.of(1070, 4, 20), false);
        MenuItemFixed twofu = new MenuItemFixed("2FU", 10.50, LocalDate.of(2010, 2, 19), true);
        MenuItemFixed bicmac = new MenuItemFixed("BicMac", 20.0, LocalDate.of(1970, 11, 20), true);
        MenuItemFixed fries = new MenuItemFixed("frIes", 9.0, LocalDate.of(1955, 6, 9), false);
        MenuItemFixed salad = new MenuItemFixed("Salad", 15.0, LocalDate.of(1955, 1, 7), false);
        MenuItemFixed cheeseburger = new MenuItemFixed("CheeseBurger", 7.0, LocalDate.of(1955, 5, 2), false);
        MenuItemFixed burger = new MenuItemFixed("Burger", 5.0, LocalDate.of(1955, 4, 1), false);
        MenuItemFixed wrap = new MenuItemFixed("Wrap", 12.0, LocalDate.of(1985, 3, 1), false);

        MenuItemFixed cola = new MenuItemFixed("Coca Cola", 10.0, LocalDate.of(1070, 5, 17), false);
        MenuItemFixed sprite = new MenuItemFixed("Sprite", 10.0, LocalDate.of(1955, 3, 1), false);
        MenuItemFixed fanta = new MenuItemFixed("Fanta", 10.0, LocalDate.of(1955, 4, 1), false);
        MenuItemFixed coffee = new MenuItemFixed("Coffee", 8.0, LocalDate.of(1955, 1, 1), false);
        MenuItemFixed water = new MenuItemFixed("Water", 5, LocalDate.of(1955, 2, 1), false);

        MenuItemFixed icecream = new MenuItemFixed("Ice Cream", 11.0, LocalDate.of(1970, 1, 1), false);
        MenuItemFixed applepie = new MenuItemFixed("Apple Pie", 8.0, LocalDate.of(1965, 1, 1), false);
        MenuItemFixed muffin = new MenuItemFixed("Muffin", 6.0, LocalDate.of(1980, 1, 1), false);

        mains.addMenuItem(drwal);
        mains.addMenuItem(princess);
        mains.addMenuItem(mcchicken);
        mains.addMenuItem(twofu);
        mains.addMenuItem(bicmac);
        mains.addMenuItem(fries);
        mains.addMenuItem(salad);
        mains.addMenuItem(cheeseburger);
        mains.addMenuItem(burger);
        mains.addMenuItem(wrap);

        drinks.addMenuItem(cola);
        drinks.addMenuItem(sprite);
        drinks.addMenuItem(fanta);
        drinks.addMenuItem(coffee);
        drinks.addMenuItem(water);

        desserts.addMenuItem(icecream);
        desserts.addMenuItem(applepie);
        desserts.addMenuItem(muffin);

        ObjectPlus.saveExtent();

        /*
        valid PESELe
        79100383434
        55020244514
        72040874798
        88041624171
        66012395176
        */
    }
}

