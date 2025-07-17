package model;

import util.ObjectPlus;

import java.util.*;

/**
 * Represents a restaurant with a unique name, address, and a set of {@link RestaurantClass} types.
 * This class manages employees (via {@link Contract}), {@link MenuCategory} instances, and supports features
 * based on its {@link RestaurantClass}, such as drive-thru, delivery, and own building specifics (e.g., parking).
 * It extends {@link util.ObjectPlus} for extent management and implements {@link IDriveThru}, {@link IDelivery}, and {@link IOwnBuilding}.
 */
public class Restaurant extends ObjectPlus implements IDriveThru, IDelivery, IOwnBuilding {
    private String restaurantName;
    private Address address;
    private Double maxCarHeight;
    private Integer maxDeliveryDistance;
    private Integer parkingCapacity;
    private EnumSet<RestaurantClass> restaurantClasses;
    private static final Set<MenuCategory> allMenuCategoriesSet = new HashSet<>();
    private final List<Contract> employeeList = new ArrayList<>();
    private final TreeMap<String, MenuCategory> menuCategoriesMap = new TreeMap<>();

    /**
     * Constructs a new {@code Restaurant} object.
     * Initializes the restaurant with a name, an {@link Address}, and a set of {@link RestaurantClass} types.
     * If any exception occurs during initialization, the error is printed and the object is removed from the extent.
     *
     * @param restaurantName    The unique name for the restaurant. Must not be null or empty and must be unique.
     * @param address           The {@link Address} of the restaurant. Must not be null.
     * @param restaurantClasses An {@link EnumSet} of {@link RestaurantClass} defining the restaurant's capabilities. Must not be null or empty.
     */
    public Restaurant(String restaurantName, Address address, EnumSet<RestaurantClass> restaurantClasses) {
        try {
            setRestaurantName(restaurantName);
            setAddress(address);
            setRestaurantClasses(restaurantClasses);
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Adds a new {@link Employee} to this restaurant by creating a {@link Contract}.
     * A contract is only created if a contract of the same type for the given employee doesn't already exist in this restaurant.
     *
     * @param employee       The {@link Employee} to add.
     * @param contractType   The {@link ContractType} for the new contract.
     * @param description    A description for the contract.
     * @param durationInDays The duration of the contract in days.
     */
    public void addEmployee(Employee employee, Enum<ContractType> contractType, String description, int durationInDays) {
        if (employeeList.stream().noneMatch(contract -> contract.getEmployee() == employee && contract.getContractType() == contractType)) {
            new Contract(employee, this, contractType, description, durationInDays);
        } else {
            System.out.println("Contract already exists for this restaurant with the same contract type");
        }
    }

    /**
     * Adds a {@link Contract} to this restaurant's list of employee contracts.
     * This method is typically called from the {@link Contract} constructor to establish the bidirectional association.
     *
     * @param contract The {@link Contract} to add. Must not be null.
     * @throws NullPointerException if the contract is null.
     */
    protected void addContract(Contract contract) {
        if (contract == null) {
            throw new NullPointerException();
        }
        employeeList.add(contract);
    }

    /**
     * Removes a {@link Contract} from this restaurant's list of employee contracts.
     * This method is typically called when a contract is terminated or removed.
     *
     * @param contract The {@link Contract} to remove.
     */
    protected void removeContract(Contract contract) {
        if (employeeList.stream().anyMatch(contract::equals)) {
            employeeList.remove(contract);
        }
    }

    /**
     * Ends an existing {@link Contract} for a specific {@link Employee} and {@link ContractType}.
     * If found, the contract is removed from the system's extent.
     *
     * @param employee     The {@link Employee} whose contract is to be ended. Must not be null.
     * @param contractType The {@link ContractType} of the contract to be ended. Must not be null.
     * @throws NullPointerException if employee or contractType is null.
     */
    public void endContract(Employee employee, ContractType contractType) {
        if (employee == null || contractType == null) {
            throw new NullPointerException();
        }
        for (Contract contract : employeeList) {
            if (contract.getEmployee() == employee && contract.getContractType() == contractType) {
                contract.removeFromExtent();
                break;
            }
        }
    }

    /**
     * Adds a {@link MenuCategory} to this restaurant's menu.
     * If the menu category is not already associated with this restaurant, it is added,
     * and the association is also recorded in the {@link MenuCategory} object.
     *
     * @param menuCategory The {@link MenuCategory} to add. Its name is used as the key in the internal map.
     */
    public void addMenuCategory(MenuCategory menuCategory) {
        if (!menuCategoriesMap.containsKey(menuCategory.getMenuCategoryName())) {
            menuCategoriesMap.put(menuCategory.getMenuCategoryName(), menuCategory);
            menuCategory.addRestaurant(this);
        }
    }

    /**
     * Removes a {@link MenuCategory} from this restaurant's menu.
     * If the menu category is associated with this restaurant, it is removed,
     * and the association is also removed from the {@link MenuCategory} object.
     *
     * @param menuCategory The {@link MenuCategory} to remove.
     */
    public void removeMenuCategory(MenuCategory menuCategory) {
        if (menuCategoriesMap.containsKey(menuCategory.getMenuCategoryName())) {
            menuCategoriesMap.remove(menuCategory.getMenuCategoryName());
            menuCategory.removeRestaurant(this);
        }
    }

    /**
     * Retrieves a {@link MenuCategory} associated with this restaurant by its name.
     * The search is case-insensitive after trimming whitespace from the input name.
     *
     * @param name The name of the menu category to find.
     * @return The {@link MenuCategory} if found, otherwise {@code null}.
     */
    public MenuCategory getMenuCategoryByName(String name) {
        if (name != null) {
            name = name.trim().toLowerCase();
        }
        return menuCategoriesMap.getOrDefault(name, null);
    }

    /**
     * Checks if the provided restaurant name is unique among all existing restaurants, excluding itself.
     * This comparison is case-insensitive.
     *
     * @param thatName The name to check for uniqueness.
     * @return {@code true} if the name is unique, {@code false} otherwise.
     */
    private boolean isNameUniq(String thatName) {
        return ObjectPlus.getExtentFromClass(Restaurant.class).stream()
                .filter(obj -> obj != this)
                .noneMatch(r -> thatName.equalsIgnoreCase(r.getRestaurantName()));
    }

    /**
     * Sets the maximum car height for the drive-thru.
     * This operation is only valid if the restaurant is classified as {@link RestaurantClass#DriveThru}.
     *
     * @param maxCarHeight The maximum car height in meters. Can be {@code null}.
     * @throws UnsupportedOperationException if the restaurant does not have a DriveThru.
     */
    public void setMaxCarHeight(Double maxCarHeight) {
        if (!restaurantClasses.contains(RestaurantClass.DriveThru)) {
            throw new UnsupportedOperationException("Restaurant does not have DriveThru");
        }
        this.maxCarHeight = maxCarHeight;
    }

    /**
     * Gets the maximum car height allowed for the drive-thru.
     * This operation is only valid if the restaurant is classified as {@link RestaurantClass#DriveThru}.
     *
     * @return The maximum car height in meters, or {@code null} if not set.
     * @throws UnsupportedOperationException if the restaurant does not have a DriveThru.
     */
    public Double getMaxCarHeight() {
        if (!restaurantClasses.contains(RestaurantClass.DriveThru)) {
            throw new UnsupportedOperationException("Restaurant does not have DriveThru");
        }
        return maxCarHeight;
    }

    /**
     * Sets the maximum delivery distance for the restaurant.
     * This operation is only valid if the restaurant is classified as {@link RestaurantClass#Delivery}.
     *
     * @param maxDeliveryDistance The maximum delivery distance in meters. Can be {@code null}.
     * @throws UnsupportedOperationException if the restaurant does not offer delivery.
     */
    public void setMaxDeliveryDistance(Integer maxDeliveryDistance) {
        if (!restaurantClasses.contains(RestaurantClass.Delivery)) {
            throw new UnsupportedOperationException("Restaurant does not have DeliveryOnly");
        }
        this.maxDeliveryDistance = maxDeliveryDistance;
    }

    /**
     * Gets the maximum delivery distance for the restaurant.
     * This operation is only valid if the restaurant is classified as {@link RestaurantClass#Delivery}.
     *
     * @return The maximum delivery distance in meters, or {@code null} if not set.
     * @throws UnsupportedOperationException if the restaurant does not offer delivery.
     */
    public Integer getMaxDeliveryDistance() {
        if (!restaurantClasses.contains(RestaurantClass.Delivery)) {
            throw new UnsupportedOperationException("Restaurant does not have Delivery option");
        }
        return maxDeliveryDistance;
    }

    /**
     * Sets the parking capacity for the restaurant.
     * This operation is only valid if the restaurant is classified as {@link RestaurantClass#OwnBuilding}.
     *
     * @param parkingCapacity The number of parking spots. Can be {@code null}.
     * @throws UnsupportedOperationException if the restaurant does not have its own parking (is not OwnBuilding type).
     */
    public void setParkingCapacity(Integer parkingCapacity) {
        if (!restaurantClasses.contains(RestaurantClass.OwnBuilding)) {
            throw new UnsupportedOperationException("Restaurant does not have own Parking");
        }
        this.parkingCapacity = parkingCapacity;
    }

    /**
     * Gets the parking capacity of the restaurant.
     * This operation is only valid if the restaurant is classified as {@link RestaurantClass#OwnBuilding}.
     *
     * @return The number of parking spots, or {@code null} if not set.
     * @throws UnsupportedOperationException if the restaurant does not have its own parking (is not OwnBuilding type).
     */
    public Integer getParkingCapacity() {
        if (!restaurantClasses.contains(RestaurantClass.OwnBuilding)) {
            throw new UnsupportedOperationException("Restaurant does not have own Parking");
        }
        return parkingCapacity;
    }

    /**
     * Sets the {@link RestaurantClass} types for this restaurant.
     * The provided set must not be null or empty.
     *
     * @param restaurantClasses An {@link EnumSet} of {@link RestaurantClass}.
     * @throws IllegalArgumentException if restaurantClasses is null or empty.
     */
    private void setRestaurantClasses(EnumSet<RestaurantClass> restaurantClasses) {
        if (restaurantClasses == null || restaurantClasses.isEmpty()) {
            throw new IllegalArgumentException("Restaurant must have at least one class");
        }
        this.restaurantClasses = restaurantClasses;
    }

    /**
     * Gets the {@link EnumSet} of {@link RestaurantClass} types for this restaurant.
     *
     * @return An {@link EnumSet} containing the restaurant's classes.
     */
    public EnumSet<RestaurantClass> getRestaurantClasses() {
        return restaurantClasses;
    }

    /**
     * Gets a user-friendly, comma-separated string representation of the restaurant's classes.
     *
     * @return A string listing the names of the restaurant's {@link RestaurantClass} types.
     */
    public String getRestaurantClassesPretty() {
        return String.join(", ",
                restaurantClasses.stream()
                        .map(RestaurantClass::name)
                        .toArray(String[]::new)
        );
    }

    /**
     * Sets the unique name of the restaurant.
     * The name must not be null or empty and must be unique (case-insensitive) across all restaurants.
     *
     * @param restaurantName The name to set.
     * @throws IllegalArgumentException if the name is null, empty, or not unique.
     */
    private void setRestaurantName(String restaurantName) {
        if (restaurantName == null || restaurantName.isEmpty()) {
            throw new IllegalArgumentException("Unique name cannot be empty");
        }
        if (!isNameUniq(restaurantName)) {
            throw new IllegalArgumentException("Restaurant name must be unique");
        }
        this.restaurantName = restaurantName;
    }

    /**
     * Sets the {@link Address} for this restaurant.
     * The address must not be null.
     *
     * @param address The {@link Address} to set.
     * @throws NullPointerException if the address is null.
     */
    private void setAddress(Address address) {
        if (address == null) {
            throw new NullPointerException("Address cannot be null");
        }
        this.address = address;
    }

    /**
     * Gets the unique name of the restaurant.
     *
     * @return The restaurant's name.
     */
    public String getRestaurantName() {
        return restaurantName;
    }

    /**
     * Gets the {@link Address} of the restaurant.
     *
     * @return The restaurant's {@link Address}.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Gets an unmodifiable list of {@link Contract} objects associated with this restaurant's employees.
     *
     * @return An unmodifiable {@link List} of {@link Contract}s.
     */
    public List<Contract> getEmployeeList() {
        return Collections.unmodifiableList(employeeList);
    }

    /**
     * Gets an unmodifiable copy of the map of {@link MenuCategory} objects associated with this restaurant,
     * keyed by their names.
     *
     * @return An unmodifiable {@link Map} of menu category names to {@link MenuCategory} objects.
     */
    public Map<String, MenuCategory> getMenuCategoriesMap() {
        return Map.copyOf(menuCategoriesMap);
    }

    /**
     * Gets an unmodifiable set of all {@link MenuCategory} instances currently tracked by the system.
     * This set is static and shared across all {@code Restaurant} instances.
     *
     * @return An unmodifiable {@link Set} of all {@link MenuCategory}s.
     */
    public static Set<MenuCategory> getAllMenuCategoriesSet() {
        return Collections.unmodifiableSet(allMenuCategoriesSet);
    }

    /**
     * Adds a {@link MenuCategory} to the static set of all menu categories.
     * This method is typically called from the {@link MenuCategory} constructor.
     *
     * @param menuCategory The {@link MenuCategory} to add.
     */
    protected static void updateMenuCategoriesSet(MenuCategory menuCategory) {
        allMenuCategoriesSet.add(menuCategory);
    }

    /**
     * Removes a {@link MenuCategory} from the static set of all menu categories.
     * This method is typically called when a {@link MenuCategory} is removed from the system.
     *
     * @param menuCategory The {@link MenuCategory} to remove.
     */
    protected static void removeFromMenuCategoriesSet(MenuCategory menuCategory) {
        allMenuCategoriesSet.remove(menuCategory);
    }

    /**
     * Initializes or re-initializes the static set of all {@link MenuCategory} instances.
     * It clears the current set and populates it with all {@link MenuCategory} objects from the extent.
     */
    public static void initializeMenuCategoriesSet() {
        allMenuCategoriesSet.clear();
        allMenuCategoriesSet.addAll(ObjectPlus.getExtentFromClass(MenuCategory.class));
    }

    /**
     * Retrieves a list of {@link MenuCategory} objects that exist in the system ({@link #getAllMenuCategoriesSet()})
     * but are not currently assigned to this specific restaurant's menu ({@link #getMenuCategoriesMap()}).
     * The complexity is O(M + N), where M is the number of menu categories assigned to this restaurant,
     * and N is the total number of unique menu categories in the system.
     *
     * @return A {@link List} of {@link MenuCategory} instances not assigned to this restaurant.
     */
    public List<MenuCategory> getUnAssignedMenuCategories() { // O(M + N)
        Set<MenuCategory> assigned = new HashSet<>(this.getMenuCategoriesMap().values()); // O(M)
        return allMenuCategoriesSet.stream()
                .filter(category -> !assigned.contains(category)) // O(N) * O(1)
                .toList();
    }

    /**
     * Removes this restaurant from the system's extent.
     * Before removal, it ensures all associated {@link Contract}s are terminated
     * and all {@link MenuCategory} associations are severed.
     */
    @Override
    public void removeFromExtent() {
        while (!employeeList.isEmpty()) {
            employeeList.getFirst().removeFromExtent();
        }
        for (MenuCategory menuCategory : menuCategoriesMap.values()) {
            menuCategory.removeRestaurant(this);
        }
        super.removeFromExtent();
    }

    /**
     * Returns a string representation of the {@code Restaurant} object.
     * Includes the restaurant's unique name and address.
     *
     * @return A string representation of this restaurant.
     */
    @Override
    public String toString() {
        return "Restaurant{" +
                "uniqName='" + restaurantName + '\'' +
                ", address=" + address +
                '}';
    }
}