package model;

import util.ObjectPlus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for items available on a restaurant's menu.
 * This class extends {@link util.ObjectPlus} for extent management and defines common properties
 * such as item name, price, and first debut date. It can be associated with a {@link MenuCategory}.
 * Subclasses like {@link MenuItemFixed} and {@link MenuItemSeasonal} provide concrete implementations.
 */
public abstract class MenuItem extends ObjectPlus {
    private String menuItemName;
    private double price;
    private LocalDate firstDebut;
    private MenuCategory menuCategory;

    /**
     * Constructs a new {@code MenuItem} object.
     * Initializes the menu item with a name, price, and first debut date.
     * Validates that the name is unique (case-insensitive across all MenuItem types), price is non-negative,
     * and the first debut date is not null and not in the future.
     * If any validation fails or an exception occurs, the error is printed,
     * and the object is removed from the system's extent.
     *
     * @param menuItemName The unique name for the menu item. Cannot be null or empty.
     * @param price        The price of the menu item. Cannot be negative.
     * @param firstDebut   The date when the menu item was first introduced. Cannot be null or in the future.
     */
    public MenuItem(String menuItemName, double price, LocalDate firstDebut) {
        try {
            setMenuItemName(menuItemName);
            setPrice(price);
            setFirstDebut(firstDebut);
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Protected constructor for creating a {@code MenuItem} (likely a {@link MenuItemFixed})
     * from an existing {@link MenuItemSeasonal} instance.
     * This is used for the "dynamic inheritance" scenario where a seasonal item becomes a fixed item.
     * It copies the name, price, and first debut date from the seasonal item.
     *
     * @param menuItemSeasonal The {@link MenuItemSeasonal} instance to copy data from.
     */
    protected MenuItem(MenuItemSeasonal menuItemSeasonal) {
        try {
            setMenuItemName(menuItemSeasonal.getMenuItemName());
            setPrice(menuItemSeasonal.getPrice());
            setFirstDebut(menuItemSeasonal.getFirstDebut());
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Associates this menu item with a {@link MenuCategory}.
     * Establishes a bidirectional relationship: sets the category for this item
     * and adds this item to the category's list of menu items, but only if the item
     * is not already associated with this or another category.
     *
     * @param menuCategory The {@link MenuCategory} to associate with this item. Cannot be null.
     * @throws NullPointerException if menuCategory is null.
     */
    public void addMenuCategory(MenuCategory menuCategory) {
        if (menuCategory == null) {
            throw new NullPointerException("Menu category cannot be null");
        }
        if (this.menuCategory == null && !menuCategory.getMenuItemList().contains(this)) {
            this.menuCategory = menuCategory;
            menuCategory.addMenuItem(this);
        }
    }

    /**
     * Removes the association of this menu item from a specific {@link MenuCategory}.
     * Breaks the bidirectional relationship: removes this item from the category's list
     * and nullifies the category reference in this item, but only if the provided category
     * is the one currently associated with this item.
     *
     * @param menuCategory The {@link MenuCategory} from which to disassociate this item. Cannot be null.
     * @throws NullPointerException if menuCategory is null.
     */
    public void removeMenuCategory(MenuCategory menuCategory) {
        if (menuCategory == null) {
            throw new NullPointerException("Menu category cannot be null");
        }
        if (this.menuCategory != null && this.menuCategory.equals(menuCategory)) {
            this.menuCategory.removeMenuItem(this);
            this.menuCategory = null;
        }
    }

    /**
     * Sets the {@link MenuCategory} for this menu item.
     * This method is protected and is typically used by {@link MenuCategory}
     * to manage the bidirectional association when adding or removing menu items.
     *
     * @param menuCategory The {@link MenuCategory} to set. Can be null if the item is being disassociated.
     */
    protected void setMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
    }

    /**
     * Sets the name for this menu item.
     * The name must not be null or empty and must be unique across all menu item types (case-insensitive).
     *
     * @param menuItemName The name to set for the menu item.
     * @throws IllegalArgumentException if the name is null, empty, or not unique.
     */
    private void setMenuItemName(String menuItemName) {
        if (menuItemName == null || menuItemName.isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty");
        }
        if (!isMenuItemNameUnique(menuItemName)) {
            throw new IllegalArgumentException("Item name must be unique");
        }
        this.menuItemName = menuItemName;
    }

    /**
     * Sets the price for this menu item.
     * The price cannot be negative.
     *
     * @param price The price to set.
     * @throws IllegalArgumentException if the price is negative.
     */
    private void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    /**
     * Sets the first debut date for this menu item.
     * The date cannot be null and cannot be in the future.
     *
     * @param firstDebut The first debut date to set.
     * @throws IllegalArgumentException if the date is null or in the future.
     */
    private void setFirstDebut(LocalDate firstDebut) {
        if (firstDebut == null) {
            throw new IllegalArgumentException("First debut date cannot be null");
        }
        if (firstDebut.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("First debut date cannot be in the future");
        }
        this.firstDebut = firstDebut;
    }

    /**
     * Finds all menu items (both {@link MenuItemFixed} and {@link MenuItemSeasonal})
     * whose price is less than or equal to the specified maximum price.
     * This is a static utility method.
     *
     * @param maxPrice The maximum price to filter by.
     * @return A {@link List} of {@link MenuItem}s matching the criteria.
     */
    public static List<MenuItem> findItemsBelowPrice(double maxPrice) {
        List<MenuItem> result = new ArrayList<>();

        result.addAll(ObjectPlus.getExtentFromClass(MenuItemSeasonal.class).stream()
                .map(item -> (MenuItem) item)
                .filter(item -> item.getPrice() <= maxPrice)
                .toList());


        result.addAll(ObjectPlus.getExtentFromClass(MenuItemFixed.class).stream()
                .map(item -> (MenuItem) item)
                .filter(item -> item.getPrice() <= maxPrice)
                .toList());

        return result;
    }

    /**
     * Finds all menu items (both {@link MenuItemFixed} and {@link MenuItemSeasonal})
     * whose price falls within the specified minimum and maximum price range (inclusive).
     * This is a static utility method.
     *
     * @param minPrice The minimum price of the range.
     * @param maxPrice The maximum price of the range.
     * @return A {@link List} of {@link MenuItem}s matching the criteria.
     */
    public static List<MenuItem> findItemsByPrice(double minPrice, double maxPrice) {
        List<MenuItem> result = new ArrayList<>();

        result.addAll(ObjectPlus.getExtentFromClass(MenuItemSeasonal.class).stream()
                .map(item -> (MenuItem) item)
                .filter(item -> item.getPrice() >= minPrice && item.getPrice() <= maxPrice)
                .toList());

        result.addAll(ObjectPlus.getExtentFromClass(MenuItemFixed.class).stream()
                .map(item -> (MenuItem) item)
                .filter(item -> item.getPrice() >= minPrice && item.getPrice() <= maxPrice)
                .toList());

        return result;
    }

    /**
     * Checks if the provided menu item name is unique across all {@link MenuItemFixed} and {@link MenuItemSeasonal} instances.
     * This comparison is case-insensitive.
     * This is a static utility method used during name validation.
     *
     * @param thatName The menu item name to check for uniqueness.
     * @return {@code true} if the name is unique, {@code false} otherwise.
     */
    private static boolean isMenuItemNameUnique(String thatName) {
        boolean unique1 = ObjectPlus.getExtentFromClass(MenuItemFixed.class).stream()
                .noneMatch(i -> thatName.equalsIgnoreCase(i.getMenuItemName()));

        boolean unique2 = ObjectPlus.getExtentFromClass(MenuItemSeasonal.class).stream()
                .noneMatch(i -> thatName.equalsIgnoreCase(i.getMenuItemName()));
        return unique1 && unique2;
    }

    /**
     * Gets the name of this menu item.
     *
     * @return The menu item's name.
     */
    public String getMenuItemName() {
        return menuItemName;
    }

    /**
     * Gets the price of this menu item.
     *
     * @return The menu item's price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the first debut date of this menu item.
     *
     * @return The menu item's first debut date as a {@link LocalDate}.
     */
    public LocalDate getFirstDebut() {
        return firstDebut;
    }

    /**
     * Gets the {@link MenuCategory} this item belongs to.
     *
     * @return The associated {@link MenuCategory}, or {@code null} if not categorized.
     */
    public MenuCategory getMenuCategory() {
        return menuCategory;
    }

    /**
     * Gets the name of the menu category this item belongs to.
     * If the item is not categorized, returns {@code null}.
     *
     * @return The name of the menu category, or {@code null} if not categorized.
     */
    public String getMenuCategoryName() {
        return (menuCategory != null) ? menuCategory.getMenuCategoryName() : null;
    }

    /**
     * Removes this menu item from the system's extent.
     * Before removal, it ensures that if it is associated with a {@link MenuCategory},
     * the association is severed by calling {@link #removeMenuCategory(MenuCategory)}.
     */
    @Override
    public void removeFromExtent() {
        if (this.menuCategory != null) {
            removeMenuCategory(this.menuCategory);
        }
        super.removeFromExtent();
    }

    /**
     * Returns a string representation of the {@code MenuItem} object.
     * Includes the menu item's name, price, and first debut date.
     *
     * @return A string detailing the menu item's information.
     */
    @Override
    public String toString() {
        return "MenuItem{" +
                "menuItemName='" + menuItemName + '\'' +
                ", price=" + price +
                ", firstDebut=" + firstDebut +
                '}';
    }
}
