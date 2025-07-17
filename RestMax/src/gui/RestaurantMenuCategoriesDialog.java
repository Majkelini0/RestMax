package gui;

import model.MenuCategory;
import model.Restaurant;
import util.ObjectPlus;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * A dialog for adding existing {@link MenuCategory} instances to a specific {@link Restaurant}.
 * This JDialog displays a list of unassigned menu categories as checkboxes, allowing the user
 * to select which ones to associate with the given restaurant.
 * It requires a parent JFrame, the target restaurant, and a callback to refresh the parent view upon completion.
 */
public class RestaurantMenuCategoriesDialog extends JDialog {
    private JPanel mainPanel;
    private JLabel infoLabel;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel buttonPanel;
    private JPanel checkboxPanel;
    private JScrollPane scrollPane;
    private final Restaurant restaurant;
    private final Runnable refreshCallback;
    private final Map<JCheckBox, MenuCategory> checkboxCategoryMap = new HashMap<>();

    /**
     * Constructs a {@code RestaurantMenuCategoriesDialog}.
     * Initializes the dialog with its parent frame, the target {@link Restaurant}, and a {@link Runnable} callback.
     * The dialog populates a list of checkboxes representing {@link MenuCategory} instances that are not yet
     * assigned to the specified restaurant. If no such categories are available, an informational message is shown,
     * and the dialog is disposed.
     * Action listeners are set up for "OK" and "Cancel" buttons. The "OK" button adds the selected categories
     * to the restaurant and triggers the refresh callback. The "Cancel" button simply closes the dialog.
     *
     * @param parent The parent {@link JFrame} of this dialog.
     * @param restaurant The {@link Restaurant} to which menu categories will be added.
     * @param refreshCallback A {@link Runnable} that will be executed after categories are successfully added,
     *                        typically to refresh the display in the parent window.
     */
    public RestaurantMenuCategoriesDialog(JFrame parent, Restaurant restaurant, Runnable refreshCallback) {
        super(parent, "Add Menu Categories Dialog", true);
        this.restaurant = restaurant;
        this.refreshCallback = refreshCallback;
        setContentPane(mainPanel);
        setSize(300, 300);
        setResizable(false);
        setLocationRelativeTo(parent);
        setWindowLook();

        infoLabel.setText("Select menu categories to add to the restaurant:");
        okButton.setText("OK");
        cancelButton.setText("Cancel");

        List<MenuCategory> availableCategories = restaurant.getUnAssignedMenuCategories();

        if (availableCategories.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                    "No more menu categories available to add",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }

        JPanel checkboxesPanel = new JPanel();
        checkboxesPanel.setLayout(new BoxLayout(checkboxesPanel, BoxLayout.Y_AXIS));
        checkboxesPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        for (MenuCategory category : availableCategories) {
            JCheckBox checkBox = new JCheckBox(category.getMenuCategoryName());
            checkboxCategoryMap.put(checkBox, category);
            checkboxesPanel.add(checkBox);
        }

        scrollPane.setViewportView(checkboxesPanel);
        setupButtonListeners();
        setVisible(true);
    }

    /**
     * Sets up action listeners for the "OK" and "Cancel" buttons.
     * The "OK" button checks which checkboxes are selected, adds the corresponding
     * {@link MenuCategory} instances to the restaurant, and triggers the refresh callback.
     * If no categories are selected, a warning message is displayed.
     * The "Cancel" button simply closes the dialog without making any changes.
     */
    private void setupButtonListeners() {
        okButton.addActionListener(_ -> {
            boolean categoriesAdded = false;

            for (Map.Entry<JCheckBox, MenuCategory> entry : checkboxCategoryMap.entrySet()) {
                if (entry.getKey().isSelected()) {
                    restaurant.addMenuCategory(entry.getValue());
                    categoriesAdded = true;
                }
            }

            if (!categoriesAdded) {
                JOptionPane.showMessageDialog(this,
                        "Please select at least one category to add.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ObjectPlus.saveExtent();
            refreshCallback.run();
            dispose();
        });

        cancelButton.addActionListener(_ -> dispose());
    }

    /**
     * Sets the visual appearance of the dialog window.
     * This method customizes the background color, button colors, and label colors
     * to enhance the user interface aesthetics.
     */
    private void setWindowLook() {
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(0, 16, 55));
        infoLabel.setForeground(new Color(255, 255, 255));

        buttonPanel.setOpaque(false);

        cancelButton.setBackground(new Color(62, 74, 181));
        okButton.setBackground(new Color(62, 74, 181));

        cancelButton.setForeground(new Color(255, 255, 255));
        okButton.setForeground(new Color(255, 255, 255));

    }
}
