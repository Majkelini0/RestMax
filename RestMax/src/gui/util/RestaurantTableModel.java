package gui.util;

import model.Restaurant;
import util.ObjectPlus;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * A {@link javax.swing.table.TableModel} for displaying {@link Restaurant} objects.
 * This model is used to populate a JTable with details about restaurants, including their name,
 * address components (city, street, number), and a summary of their available classes (e.g., DriveThru, Delivery).
 */
public class RestaurantTableModel extends AbstractTableModel {
    private List<Restaurant> restaurantList;
    private final String[] columnNames = {"Name", "City", "Street", "Number", "Available"};

    /**
     * Constructs a {@code RestaurantTableModel}.
     * Initializes the model with a provided list of {@link Restaurant} instances.
     *
     * @param restaurantList The initial list of {@link Restaurant}s to display.
     */
    public RestaurantTableModel(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    /**
     * Returns the number of rows in the model, which corresponds to the number of restaurants.
     * @return The row count.
     */
    @Override
    public int getRowCount() {
        return restaurantList.size();
    }

    /**
     * Returns the number of columns in the model.
     * @return The column count (currently 5: "Name", "City", "Street", "Number", "Available").
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
     * Displays restaurant name, city, street, street number, and a pretty string of restaurant classes.
     * @param rowIndex The row whose value is to be queried.
     * @param columnIndex The column whose value is to be queried.
     * @return The value at the specified cell, or {@code null} for an invalid column index.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Restaurant restaurant = restaurantList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> restaurant.getRestaurantName();
            case 1 -> restaurant.getAddress().getCity();
            case 2 -> restaurant.getAddress().getStreet();
            case 3 -> restaurant.getAddress().getStreetNumber();
            case 4 -> restaurant.getRestaurantClassesPretty();
            default -> null;
        };
    }

    /**
     * Retrieves the {@link Restaurant} object at the specified row index.
     *
     * @param rowIndex The index of the row.
     * @return The {@link Restaurant} at the given row.
     */
    public Restaurant getRestaurant(int rowIndex) {
        return restaurantList.get(rowIndex);
    }

    /**
     * Refreshes the table model with all {@link Restaurant} instances currently in the system.
     * This method should be called when the global list of restaurants might have changed.
     * It retrieves the current extent of restaurants from {@link ObjectPlus} and then fires a
     * table data changed event to update the JTable display.
     */
    public void refresh() {
        this.restaurantList = ObjectPlus.getExtentFromClass(Restaurant.class);
        fireTableDataChanged();
    }
}
