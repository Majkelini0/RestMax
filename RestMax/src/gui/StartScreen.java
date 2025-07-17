package gui;

import gui.util.RestaurantTableModel;
import model.Restaurant;
import util.ObjectPlus;

import javax.swing.*;

/**
 * The main starting screen for the RestMax application.
 * This JFrame displays a list of restaurants and provides options to select a restaurant
 * to view its menu or to edit all available menu categories in the system.
 * It loads the application's data (extents) if they haven't been loaded already.
 */
public class StartScreen extends JFrame {
    private JPanel mainPanel;
    private JLabel appName;
    private JTable restaurantListTable;
    private JButton selectRestaurantButton;
    private JButton editMenuCategoriesButton;

    /**
     * Constructs the {@code StartScreen} JFrame.
     * Initializes the UI components, sets the window properties (title, size, close operation),
     * loads application data if necessary, and populates the restaurant list table.
     * It also sets up action listeners for the buttons to navigate to other parts of the application.
     */
    public StartScreen() {
        if (ObjectPlus.isExtentEmpty()) {
            ObjectPlus.loadExtent();
        }
        setTitle("RestMax APP");
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        appName.setText("Restaurants");
        editMenuCategoriesButton.setText("Edit Menu Categories");
        selectRestaurantButton.setText("Select Restaurant");

        RestaurantTableModel restaurantTableModel = new RestaurantTableModel(ObjectPlus.getExtentFromClass(Restaurant.class));
        restaurantListTable.setModel(restaurantTableModel);

        setWindowLook();
        setupButtonListeners(restaurantTableModel);
        setVisible(true);
    }

    /**
     * Sets up action listeners for the buttons in the start screen.
     * - The "Select Restaurant" button opens the {@link RestaurantMenu} to allows the user to select a restaurant from the list.
     * - The "Edit Menu Categories" button opens {@link AllMenuCategoriesDialog} to manage all menu categories in the system.
     *
     * @param restaurantTableModel The table model for the restaurant list table.
     */
    private void setupButtonListeners(RestaurantTableModel restaurantTableModel) {
        selectRestaurantButton.addActionListener(_ -> {
            int selectedRow = restaurantListTable.getSelectedRow();
            if (selectedRow != -1) {
                Restaurant selectedRestaurant = restaurantTableModel.getRestaurant(selectedRow);
                new RestaurantMenu(this, selectedRestaurant);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a restaurant from the list.");
            }
        });

        editMenuCategoriesButton.addActionListener(_ -> new AllMenuCategoriesDialog(this));
    }

    /**
     * Sets the look and feel of the window to the system's default look and feel.
     * If an exception occurs while setting the look and feel, its stack trace is printed.
     */
    private void setWindowLook() {
        restaurantListTable.getColumnModel().getColumn(0).setPreferredWidth(250);
        restaurantListTable.getColumnModel().getColumn(1).setPreferredWidth(75);
        restaurantListTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        restaurantListTable.getColumnModel().getColumn(3).setPreferredWidth(65);
        restaurantListTable.getColumnModel().getColumn(4).setPreferredWidth(250);

        restaurantListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
