package gui;

import gui.util.MenuCategoriesTableModel;
import model.MenuCategory;
import model.MenuItem;
import model.MenuItemFixed;
import model.MenuItemSeasonal;
import util.ObjectPlus;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A JDialog for managing all {@link MenuCategory} instances in the system.
 * This dialog allows users to view, create, select (to view items), and delete menu categories.
 * It is intended for administrative or testing purposes and is explicitly marked as not part of the core MAS project GUI requirements.
 * It uses a {@link MenuCategoriesTableModel} to display the categories.
 */
public class AllMenuCategoriesDialog extends JDialog {

//    todo -> UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA UWAGA
//    todo -> TEN EKRAN NIE PODLEGA OCENIE !!!
//    todo -> TEN EKRAN NIE WCHODZI W SKŁAD IMPLEMENTACJI GUI PROJEKTU MAS
//    todo -> TEN EKRAN SŁUŻY JEDYNIE DO TESTÓW POPRAWNOŚCI DZIAŁANIA KODU I GUI WCHODZĄCEGO W SKŁAD PROJEKTU MAS
//    todo -> TEN EKRAN NIE BYŁ SPRAWDZANY POD KĄTEM WYMAGAŃ MAS

    private JPanel mainPanel;
    private JLabel menuCategories;
    private JTable menuCategoriesTable;
    private JButton createMenuCategoryButton;
    private JButton selectMenuCategoryButton;
    private JButton deleteMenuCategoryButton;
    private MenuCategoriesTableModel menuCategoriesTableModel;

    /**
     * Constructs the {@code AllMenuCategoriesDialog}.
     * Initializes the dialog with its parent frame, sets up UI components, and populates the table
     * with all existing menu categories from the system.
     * Action listeners are configured for creating, selecting, and deleting menu categories.
     *
     * @param parent The parent {@link JFrame} of this dialog.
     */
    public AllMenuCategoriesDialog(JFrame parent) {
        super(parent, "Menu Categories Dialog", true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        menuCategories.setText("All Menu Categories: All");
        createMenuCategoryButton.setText("Create Menu Category");
        selectMenuCategoryButton.setText("Select Menu Category");
        deleteMenuCategoryButton.setText("Delete Menu Category");

        menuCategoriesTableModel = new MenuCategoriesTableModel();
        menuCategoriesTable.setModel(menuCategoriesTableModel);
        menuCategoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setupButtonListeners();

        setVisible(true);
    }

    /**
     * Sets up action listeners for the buttons in this dialog.
     * - "Create Menu Category": Prompts the user for a new category name and creates a {@link MenuCategory}.
     * - "Select Menu Category": Opens a new dialog displaying all {@link MenuItem}s (both {@link MenuItemFixed} and {@link MenuItemSeasonal})
     * belonging to the selected category, along with their specific details.
     * - "Delete Menu Category": Prompts for confirmation and then deletes the selected {@link MenuCategory} from the system.
     * Handles potential errors during creation or deletion by showing error messages.
     */
    private void setupButtonListeners() {

        createMenuCategoryButton.addActionListener(e -> {
            String categoryName = JOptionPane.showInputDialog(this, "Enter category name:");
            if (categoryName != null && !categoryName.trim().isEmpty()) {
                try {
                    new MenuCategory(categoryName);
                    ObjectPlus.saveExtent();
                    menuCategoriesTableModel.refreshAllMenuCategories();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error creating category: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        selectMenuCategoryButton.addActionListener(e -> {
            int selectedRow = menuCategoriesTable.getSelectedRow();
            if (selectedRow != -1) {
                MenuCategory selectedCategory = menuCategoriesTableModel.getMenuCategory(selectedRow);
                List<MenuItem> menuItems = selectedCategory.getMenuItemList();

                JDialog menuItemsDialog = new JDialog(this, "Menu Items for " + selectedCategory.getMenuCategoryName(), true);
                menuItemsDialog.setSize(600, 300);
                menuItemsDialog.setLocationRelativeTo(this);

                JPanel panel = new JPanel(new BorderLayout());

                String[] columnNames = {"Name", "Price", "First Debut", "Type", "Details"};
                Object[][] data = new Object[menuItems.size()][5];

                for (int i = 0; i < menuItems.size(); i++) {
                    MenuItem item = menuItems.get(i);
                    data[i][0] = item.getMenuItemName();
                    data[i][1] = String.format("%.2f", item.getPrice());
                    data[i][2] = item.getFirstDebut().toString();

                    if (item instanceof MenuItemFixed) {
                        MenuItemFixed fixedItem = (MenuItemFixed) item;
                        data[i][3] = "Fixed";
                        data[i][4] = "Popularity: " + fixedItem.getPopularityScore() +
                                (fixedItem.isSignatureDish() ? ", Signature Dish" : "");
                    } else if (item instanceof MenuItemSeasonal) {
                        MenuItemSeasonal seasonalItem = (MenuItemSeasonal) item;
                        data[i][3] = "Seasonal";
                        data[i][4] = "End Date: " + seasonalItem.getEndDate() +
                                ", Max Order: " + seasonalItem.getMaxOrderQuantity();
                    } else {
                        data[i][3] = "Unknown";
                        data[i][4] = "";
                    }
                }

                JTable table = new JTable(data, columnNames);
                table.getColumnModel().getColumn(0).setPreferredWidth(175);
                table.getColumnModel().getColumn(1).setPreferredWidth(50);
                table.getColumnModel().getColumn(2).setPreferredWidth(80);
                table.getColumnModel().getColumn(3).setPreferredWidth(75);
                table.getColumnModel().getColumn(4).setPreferredWidth(300);

                table.getColumnModel().getColumn(0).setMaxWidth(150);
                table.getColumnModel().getColumn(1).setMaxWidth(50);
                table.getColumnModel().getColumn(2).setMaxWidth(80);
                table.getColumnModel().getColumn(3).setMaxWidth(75);
                table.getColumnModel().getColumn(4).setMaxWidth(400);

                JScrollPane scrollPane = new JScrollPane(table);
                panel.add(scrollPane, BorderLayout.CENTER);

                JButton closeButton = new JButton("Close");
                closeButton.addActionListener(closeEvent -> menuItemsDialog.dispose());
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(closeButton);
                panel.add(buttonPanel, BorderLayout.SOUTH);

                menuItemsDialog.getContentPane().add(panel);
                menuItemsDialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select a category from the list.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteMenuCategoryButton.addActionListener(e -> {
            int selectedRow = menuCategoriesTable.getSelectedRow();
            if (selectedRow != -1) {
                MenuCategory selectedCategory = menuCategoriesTableModel.getMenuCategory(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete category: " + selectedCategory.getMenuCategoryName() + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    selectedCategory.removeFromExtent();
                    ObjectPlus.saveExtent();
                    menuCategoriesTableModel.refreshAllMenuCategories();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select a category to delete.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
