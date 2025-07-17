package model;

import util.ObjectPlus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a category for {@link MenuItem}s within a {@link Restaurant} menu.
 * This class extends {@link util.ObjectPlus} for extent management.
 * A menu category has a unique name and can be associated with multiple {@link Restaurant}s
 * and can contain multiple {@link MenuItem}s.
 * The list of menu items is kept sorted by item name (case-insensitive).
 */
public class MenuCategory extends ObjectPlus {
    private String menuCategoryName;

    private final List<Restaurant> restaurantList = new ArrayList<>();

    private final List<MenuItem> menuItemList = new ArrayList<>() {
        @Override
        public boolean add(MenuItem menuItem) {
            boolean result = super.add(menuItem);
            this.sort((o1, o2) -> o1.getMenuItemName().compareToIgnoreCase(o2.getMenuItemName()));
            return result;
        }
    };

    /**
     * Constructs a new {@code MenuCategory} object.
     * Initializes the menu category with a unique name (case-insensitive, stored as lowercase).
     * Adds the newly created category to the static set of all menu categories in the {@link Restaurant} class.
     * If setting the name fails (e.g., not unique, null, or empty) or any other exception occurs,
     * the error is printed, and the object is removed from the system's extent.
     *
     * @param menuCategoryName The unique name for this menu category. Cannot be null, empty, or a duplicate (case-insensitive).
     */
    public MenuCategory(String menuCategoryName) {
        try {
            setMenuCategoryName(menuCategoryName);
            Restaurant.updateMenuCategoriesSet(this);
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Adds a {@link MenuItem} to this menu category.
     * Establishes a bidirectional relationship by setting this category on the menu item.
     * The menu item is only added if it is not null and not already present in the list.
     * The internal list of menu items is kept sorted by item name.
     *
     * @param menuItem The {@link MenuItem} to add. Cannot be null.
     * @throws NullPointerException if menuItem is null.
     */
    public void addMenuItem(MenuItem menuItem) {
        if (menuItem == null) {
            throw new NullPointerException("MenuItem cannot be null");
        }
        if (!menuItemList.contains(menuItem) && menuItem.getMenuCategory() == null) {
            menuItemList.add(menuItem);
            menuItem.setMenuCategory(this);
        }
    }

    /**
     * Removes a {@link MenuItem} from this menu category.
     * Breaks the bidirectional relationship by setting the menu item's category to null.
     * The menu item is only removed if it is not null and present in the list.
     *
     * @param menuItem The {@link MenuItem} to remove. Cannot be null.
     * @throws NullPointerException if menuItem is null.
     */
    public void removeMenuItem(MenuItem menuItem) {
        if (menuItem == null) {
            throw new NullPointerException("MenuItem cannot be null");
        }
        if (menuItemList.contains(menuItem)) {
            menuItemList.remove(menuItem);
            menuItem.setMenuCategory(null);
        }
    }

    /**
     * Associates this menu category with a {@link Restaurant}.
     * Establishes a bidirectional relationship by adding this category to the restaurant's list of menu categories.
     * The association is only made if the restaurant is not already associated with this category.
     *
     * @param restaurant The {@link Restaurant} to associate with this menu category.
     */
    public void addRestaurant(Restaurant restaurant) {
        if (!restaurantList.contains(restaurant)) {
            restaurantList.add(restaurant);
            restaurant.addMenuCategory(this);
        }
    }

    /**
     * Removes the association of this menu category from a {@link Restaurant}.
     * Breaks the bidirectional relationship by removing this category from the restaurant's list of menu categories.
     * The disassociation only occurs if the restaurant is currently associated with this category.
     *
     * @param restaurant The {@link Restaurant} to disassociate from this menu category.
     */
    public void removeRestaurant(Restaurant restaurant) {
        if (restaurantList.contains(restaurant)) {
            restaurantList.remove(restaurant);
            restaurant.removeMenuCategory(this);
        }
    }

    /**
     * Gets the name of this menu category.
     * The name is stored in lowercase.
     *
     * @return The menu category's name.
     */
    public String getMenuCategoryName() {
        return menuCategoryName;
    }

    /**
     * Gets an unmodifiable list of {@link MenuItem}s belonging to this category.
     * The list is sorted by menu item name (case-insensitive).
     *
     * @return An unmodifiable {@link List} of {@link MenuItem}s.
     */
    public List<MenuItem> getMenuItemList() {
        return Collections.unmodifiableList(menuItemList);
    }

    /**
     * Gets an unmodifiable list of {@link Restaurant}s that feature this menu category.
     *
     * @return An unmodifiable {@link List} of {@link Restaurant}s.
     */
    public List<Restaurant> getRestaurantList() {
        return Collections.unmodifiableList(restaurantList);
    }

    /**
     * Sets the unique name for this menu category.
     * The name is trimmed and converted to lowercase before being set.
     * It must not be null, empty, or a duplicate of an existing category name (case-insensitive).
     *
     * @param menuCategoryName The name to set for the menu category.
     * @throws IllegalArgumentException if the name is null, empty, or not unique.
     */
    private void setMenuCategoryName(String menuCategoryName) {
        if (menuCategoryName == null || menuCategoryName.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (!isCategoryNameUnique(menuCategoryName)) {
            throw new IllegalArgumentException("Category name must be unique");
        }
        this.menuCategoryName = menuCategoryName.trim().toLowerCase();
    }

    /**
     * Checks if the provided category name is unique among all existing menu categories, excluding itself.
     * This comparison is case-insensitive.
     *
     * @param thatCategoryName The category name to check for uniqueness.
     * @return {@code true} if the name is unique, {@code false} otherwise.
     */
    private boolean isCategoryNameUnique(String thatCategoryName) {
        return ObjectPlus.getExtentFromClass(MenuCategory.class).stream()
                .filter(obj -> obj != this)
                .noneMatch(c -> thatCategoryName.equalsIgnoreCase(c.getMenuCategoryName()));
    }

    /**
     * Removes this menu category from the system's extent.
     * Before removal, it ensures all associations with {@link Restaurant}s are severed,
     * and all contained {@link MenuItem}s are disassociated from this category.
     * It also removes itself from the static set of all menu categories in the {@link Restaurant} class.
     */
    @Override
    public void removeFromExtent() {
        while (!restaurantList.isEmpty()) {
            restaurantList.getFirst().removeMenuCategory(this);
        }
        while (!menuItemList.isEmpty()) {
            menuItemList.getFirst().removeMenuCategory(this);
        }
        Restaurant.removeFromMenuCategoriesSet(this);
        super.removeFromExtent();
    }

    /**
     * Returns a string representation of the {@code MenuCategory} object.
     * Includes the menu category name.
     *
     * @return A string detailing the menu category's name.
     */
    @Override
    public String toString() {
        return "MenuCategory{" +
                menuCategoryName +
                ", items=" + menuItemList +
                '}';
    }
}