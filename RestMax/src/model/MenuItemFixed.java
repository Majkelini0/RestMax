package model;

import java.time.LocalDate;

/**
 * Represents a fixed (non-seasonal) menu item in a restaurant.
 * This class extends {@link MenuItem} and adds properties specific to fixed items,
 * such as a popularity score and whether it is a signature dish.
 * It supports a constructor for creating a fixed item from a {@link MenuItemSeasonal} instance,
 * facilitating a form of dynamic inheritance where a seasonal item becomes permanent.
 */
public class MenuItemFixed extends MenuItem {
    private int popularityScore = 5;
    private boolean isSignatureDish;

    /**
     * Constructs a new {@code MenuItemFixed} object.
     * Initializes the fixed menu item with a name, price, first debut date, and signature dish status.
     * Inherits common properties and validations from the {@link MenuItem} constructor.
     * If any exception occurs during initialization, the error is printed,
     * and the object is removed from the system's extent.
     *
     * @param menuItemName The unique name for the menu item.
     * @param price The price of the menu item.
     * @param firstDebut The date when the menu item was first introduced.
     * @param isSignatureDish {@code true} if this is a signature dish, {@code false} otherwise.
     */
    public MenuItemFixed(String menuItemName, double price, LocalDate firstDebut, boolean isSignatureDish) {
        super(menuItemName, price, firstDebut);
        try {
            setSignatureDish(isSignatureDish);
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Protected constructor to create a {@code MenuItemFixed} from a {@link MenuItemSeasonal} instance.
     * This facilitates changing a seasonal item to a fixed item ("dynamic inheritance").
     * The new fixed item inherits the name, price, and first debut date from the seasonal item.
     * It also attempts to transfer the {@link MenuCategory} association and then removes the original seasonal item from the extent.
     *
     * @param menuItemSeasonal The {@link MenuItemSeasonal} to convert into a fixed item.
     * @param isSignatureDish {@code true} if this new fixed item is a signature dish, {@code false} otherwise.
     */
    protected MenuItemFixed(MenuItemSeasonal menuItemSeasonal, boolean isSignatureDish) {
        super(menuItemSeasonal);
        try {
            setSignatureDish(isSignatureDish);

            MenuCategory menuCategory = menuItemSeasonal.getMenuCategory();
            if(menuCategory != null) {
                menuCategory.addMenuItem(this);
                menuCategory.removeMenuItem(menuItemSeasonal);
            }
            menuItemSeasonal.removeFromExtent();
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Gets the popularity score of this fixed menu item.
     * The score defaults to 5.
     *
     * @return The popularity score as an integer.
     */
    public int getPopularityScore() {
        return popularityScore;
    }

    /**
     * Sets the popularity score for this fixed menu item.
     * The score must be between 0 and 10 (inclusive).
     *
     * @param popularityScore The popularity score to set.
     * @throws IllegalArgumentException if the score is not between 0 and 10.
     */
    public void setPopularityScore(int popularityScore) {
        if (popularityScore < 1) {
            throw new IllegalArgumentException("Popularity score cannot be less than 1");
        }
        if(popularityScore > 10) {
            throw new IllegalArgumentException("Popularity score cannot be greater than 10");
        }
        this.popularityScore = popularityScore;
    }

    /**
     * Checks if this fixed menu item is a signature dish.
     *
     * @return {@code true} if it is a signature dish, {@code false} otherwise.
     */
    public boolean isSignatureDish() {
        return isSignatureDish;
    }

    /**
     * Sets whether this fixed menu item is a signature dish.
     *
     * @param signatureDish {@code true} to mark as a signature dish, {@code false} otherwise.
     */
    private void setSignatureDish(boolean signatureDish) {
        this.isSignatureDish = isSignatureDish;
    }

    /**
     * Returns a string representation of the {@code MenuItemFixed} object.
     * Includes the properties from {@link MenuItem#toString()} and adds the popularity score and signature dish status.
     *
     * @return A string detailing the fixed menu item's information.
     */
    @Override
    public String toString() {
        return "MenuItemFixed{" +
                super.toString() +
                "popularityScore=" + popularityScore +
                ", isSignatureDish=" + isSignatureDish +
                '}';
    }
}
