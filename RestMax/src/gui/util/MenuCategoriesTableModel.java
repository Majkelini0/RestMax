package gui.util;

import model.MenuCategory;
import model.Restaurant;
import util.ObjectPlus;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link javax.swing.table.TableModel} for displaying {@link MenuCategory} objects.
 * This model can be configured to show menu categories for a specific {@link Restaurant}
 * or all menu categories in the system.
 * It displays the category name and the count of menu items within that category.
 */
public class MenuCategoriesTableModel extends AbstractTableModel {
    private List<MenuCategory> menuCategoryList;
    private final String[] columnNames = {"Category Name", "Item Count"};
    private Restaurant restaurant;

    /**
     * Constructs a {@code MenuCategoriesTableModel} for a specific {@link Restaurant}.
     * Initializes the model with the menu categories associated with the given restaurant.
     *
     * @param restaurant The {@link Restaurant} whose menu categories are to be displayed.
     */
    public MenuCategoriesTableModel(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.menuCategoryList = new ArrayList<>(restaurant.getMenuCategoriesMap().values());
    }

    /**
     * Constructs a {@code MenuCategoriesTableModel} to display all {@link MenuCategory} instances in the system.
     * Initializes the model with all menu categories retrieved from the {@link ObjectPlus} extent.
     */
    public MenuCategoriesTableModel(){
        this.menuCategoryList = new ArrayList<>(ObjectPlus.getExtentFromClass(MenuCategory.class));
    }

    /**
     * Returns the number of rows in the model, which corresponds to the number of menu categories.
     * @return The row count.
     */
    @Override
    public int getRowCount() {
        return menuCategoryList.size();
    }

    /**
     * Returns the number of columns in the model.
     * @return The column count (currently 2: "Category Name", "Item Count").
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Returns the name of the column at the given column index.
     * @param column The index of the column.
     * @return The name of the column.
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Returns the value for the cell at {@code rowIndex} and {@code columnIndex}.
     * Column 0 displays the menu category name.
     * Column 1 displays the number of menu items in that category.
     * @param rowIndex The row whose value is to be queried.
     * @param columnIndex The column whose value is to be queried.
     * @return The value at the specified cell, or {@code null} for an invalid column index.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MenuCategory category = menuCategoryList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> category.getMenuCategoryName();
            case 1 -> category.getMenuItemList().size();
            default -> null;
        };
    }

    /**
     * Retrieves the {@link MenuCategory} object at the specified row index.
     *
     * @param rowIndex The index of the row.
     * @return The {@link MenuCategory} at the given row.
     */
    public MenuCategory getMenuCategory(int rowIndex) {
        return menuCategoryList.get(rowIndex);
    }

    /**
     * Refreshes the table model with the current menu categories for the associated {@link Restaurant}.
     * This method should be called when the underlying data for the specific restaurant's categories might have changed.
     * It then fires a table data changed event to update the JTable display.
     * This method is only effective if the model was constructed with a specific restaurant.
     */
    public void refreshRestaurantsMenuCategories() {
        this.menuCategoryList = new ArrayList<>(restaurant.getMenuCategoriesMap().values());
        fireTableDataChanged();
    }

    /**
     * Refreshes the table model with all {@link MenuCategory} instances currently in the system.
     * This method should be called when the global list of menu categories might have changed.
     * It then fires a table data changed event to update the JTable display.
     */
    public void refreshAllMenuCategories() {
        this.menuCategoryList = new ArrayList<>(ObjectPlus.getExtentFromClass(MenuCategory.class));
        fireTableDataChanged();
    }
}
