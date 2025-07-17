package gui;

import gui.util.MenuCategoriesTableModel;
import model.MenuCategory;
import model.Restaurant;
import util.ObjectPlus;

import javax.swing.*;

/**
 * JFrame for managing the menu categories of a specific {@link Restaurant}.
 * This screen displays a table of {@link MenuCategory} instances currently associated with the restaurant.
 * It provides options to add new (existing) menu categories to the restaurant or remove existing ones.
 * When this window is closed, it re-opens the {@link StartScreen}.
 */
public class RestaurantMenu extends JFrame {
    private JPanel mainPanel;
    private JLabel menuCategories;
    private JTable menuCategoriesTable;
    private JButton addMenuCategoryButton;
    private JButton removeMenuCategoryButton;
    private final Restaurant restaurant;
    private final MenuCategoriesTableModel menuCategoriesTableModel;

    /**
     * Constructs the {@code RestaurantMenu} JFrame.
     * Initializes the UI components, sets window properties (title, size, custom close operation),
     * and populates the table with menu categories for the given restaurant.
     * Action listeners are set up for adding and removing menu categories.
     *
     * @param parent     The parent {@link JFrame} (typically {@link StartScreen}), used for positioning.
     * @param restaurant The {@link Restaurant} whose menu categories are to be managed.
     */
    public RestaurantMenu(JFrame parent, Restaurant restaurant) {
        this.restaurant = restaurant;
        setTitle("Menu Categories - " + restaurant.getRestaurantName());
        setDefaultCloseOperation();
        setSize(400, 400);
        setResizable(false);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        menuCategories.setText("Menu Categories for " + restaurant.getRestaurantName());
        addMenuCategoryButton.setText("Add Menu Category");
        removeMenuCategoryButton.setText("Remove Menu Category");

        menuCategoriesTableModel = new MenuCategoriesTableModel(restaurant);
        menuCategoriesTable.setModel(menuCategoriesTableModel);

        setWindowLook();
        setupButtonListeners();
        setVisible(true);
    }

    /**
     * Sets the default close operation for this JFrame.
     * When the window is disposed (closed), it triggers the creation of a new {@link StartScreen} instance,
     * effectively returning the user to the main application screen.
     */
    private void setDefaultCloseOperation() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                SwingUtilities.invokeLater(StartScreen::new);
            }
        });
    }

    /**
     * Sets up action listeners for the buttons on this screen.
     * - The "Add Menu Category" button opens the {@link RestaurantMenuCategoriesDialog} to allow
     * adding existing categories to the current restaurant.
     * - The "Remove Menu Category" button removes the selected category from the restaurant after confirmation.
     */
    private void setupButtonListeners() {
        addMenuCategoryButton.addActionListener(_ -> {
            new RestaurantMenuCategoriesDialog(this, restaurant, menuCategoriesTableModel::refreshRestaurantsMenuCategories);
        });

        removeMenuCategoryButton.addActionListener(_ -> {
            int selectedRow = menuCategoriesTable.getSelectedRow();
            if (selectedRow != -1) {
                MenuCategory selectedCategory = menuCategoriesTableModel.getMenuCategory(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to remove category: " + selectedCategory.getMenuCategoryName() + "?",
                        "Confirm Remove", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    restaurant.removeMenuCategory(selectedCategory);
                    ObjectPlus.saveExtent();
                    menuCategoriesTableModel.refreshRestaurantsMenuCategories();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select a category to remove",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    /**
     * Configures specific properties for the menu categories table.
     * Sets the maximum width for the second column (likely an action or count column)
     * and restricts selection to a single row at a time.
     */
    private void setWindowLook() {
        menuCategoriesTable.getColumnModel().getColumn(1).setMaxWidth(100);

        menuCategoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }
}