package model;

import java.time.LocalDate;

/**
 * Represents a seasonal menu item that is available for a limited time.
 * This class extends {@link MenuItem} and adds properties specific to seasonal items,
 * such as an end date for availability and a maximum order quantity.
 * It provides a method {@link #makeFixed(boolean)} to convert a seasonal item into a {@link MenuItemFixed},
 * facilitating a form of dynamic inheritance.
 */
public class MenuItemSeasonal extends MenuItem {
    private LocalDate endDate;
    private int maxOrderQuantity;

    /**
     * Constructs a new {@code MenuItemSeasonal} object.
     * Initializes the seasonal menu item with a name, price, first debut date, end date, and maximum order quantity.
     * Inherits common properties and validations from the {@link MenuItem} constructor.
     * Validates that the end date is not null and not in the past.
     * If any exception occurs during initialization, the error is printed,
     * and the object is removed from the system's extent.
     *
     * @param menuItemName     The unique name for the menu item.
     * @param price            The price of the menu item.
     * @param firstDebut       The date when the menu item was first introduced.
     * @param endDate          The date until which this seasonal item is available. Cannot be null or in the past.
     * @param maxOrderQuantity The maximum quantity of this item that can be ordered at once.
     */
    public MenuItemSeasonal(String menuItemName, double price, LocalDate firstDebut, LocalDate endDate, int maxOrderQuantity) {
        super(menuItemName, price, firstDebut);
        try {
            setEndDate(endDate);
            setMaxOrderQuantity(maxOrderQuantity);
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Converts this seasonal menu item into a {@link MenuItemFixed}.
     * This method facilitates a form of "dynamic inheritance" where a seasonal item becomes a permanent one.
     * A new {@link MenuItemFixed} is created using the properties of this seasonal item.
     * The original seasonal item is typically removed from the extent by the {@link MenuItemFixed} constructor.
     *
     * @param isSignatureDish {@code true} if the new fixed item should be a signature dish, {@code false} otherwise.
     * @return The newly created {@link MenuItemFixed} instance.
     */
    public MenuItemFixed makeFixed(boolean isSignatureDish) {
        return new MenuItemFixed(this, isSignatureDish);
    }

    /**
     * Sets the end date for this seasonal menu item's availability.
     * The end date cannot be null and cannot be in the past.
     *
     * @param endDate The end date to set.
     * @throws IllegalArgumentException if the endDate is null or in the past.
     */
    private void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (endDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("End date cannot be in the past");
        }
        this.endDate = endDate;
    }

    /**
     * Sets the maximum order quantity for this seasonal menu item.
     *
     * @param maxOrderQuantity The maximum quantity that can be ordered.
     */
    private void setMaxOrderQuantity(int maxOrderQuantity) {
        this.maxOrderQuantity = maxOrderQuantity;
    }

    /**
     * Gets the end date for this seasonal menu item's availability.
     *
     * @return The end date as a {@link LocalDate}.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Gets the maximum order quantity for this seasonal menu item.
     *
     * @return The maximum order quantity as an integer.
     */
    public int getMaxOrderQuantity() {
        return maxOrderQuantity;
    }

    /**
     * Returns a string representation of the {@code MenuItemSeasonal} object.
     * Includes the properties from {@link MenuItem#toString()} and adds the end date and maximum order quantity.
     *
     * @return A string detailing the seasonal menu item's information.
     */
    @Override
    public String toString() {
        return "MenuItemSeasonal{" +
                super.toString() +
                "endDate=" + endDate +
                ", maxOrderQuantity=" + maxOrderQuantity +
                '}';
    }
}
