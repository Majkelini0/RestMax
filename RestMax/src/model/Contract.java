package model;

import util.ObjectPlus;

import java.time.LocalDate;

/**
 * Represents a contract between an {@link Employee} and a {@link Restaurant}.
 * This class extends {@link util.ObjectPlus} for extent management and tracks details such as
 * contract type, description, sign date, and end date.
 * It establishes a bidirectional relationship with both {@link Employee} and {@link Restaurant}.
 */
public class Contract extends ObjectPlus {
    private Employee employee;
    private Restaurant restaurant;
    private final Enum<ContractType> contractType;
    private String description;
    private final LocalDate signDate;
    private LocalDate endDate;

    /**
     * Constructs a new {@code Contract} object.
     * Initializes the contract with an employee, restaurant, contract type, description, and duration.
     * The sign date is set to the current date. The end date is calculated based on the duration.
     * This constructor also adds the contract to the respective employee and restaurant.
     *
     * @param employee       The {@link Employee} for this contract. Cannot be null.
     * @param restaurant     The {@link Restaurant} for this contract. Cannot be null.
     * @param contractType   The {@link ContractType} of this contract (e.g., FullTime, PartTime). Cannot be null.
     * @param description    A description of the contract. Cannot be null or blank.
     * @param durationInDays The duration of the contract in days. Must be greater than 0.
     */
    protected Contract(Employee employee, Restaurant restaurant, Enum<ContractType> contractType, String description, int durationInDays) {
        this.contractType = contractType;
        setDescription(description);
        this.signDate = LocalDate.now();
        setEndDate(durationInDays);

        setEmployee(employee);
        setRestaurant(restaurant);

        employee.addContract(this);
        restaurant.addContract(this);
    }

    /**
     * Sets the {@link Employee} for this contract.
     * This method is private and called during construction.
     *
     * @param employee The {@link Employee} to associate with this contract. Cannot be null.
     * @throws NullPointerException if the employee is null.
     */
    private void setEmployee(Employee employee) {
        if (employee == null) {
            throw new NullPointerException();
        }
        this.employee = employee;
    }

    /**
     * Sets the {@link Restaurant} for this contract.
     * This method is private and called during construction.
     *
     * @param restaurant The {@link Restaurant} to associate with this contract. Cannot be null.
     * @throws NullPointerException if the restaurant is null.
     */
    private void setRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            throw new NullPointerException();
        }
        this.restaurant = restaurant;
    }

    /**
     * Sets the description for this contract.
     * The description cannot be null or blank.
     *
     * @param description The contract description.
     * @throws IllegalArgumentException if the description is null or blank.
     */
    private void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        this.description = description;
    }

    /**
     * Sets the end date of the contract based on its duration in days from the current date.
     * The duration must be a positive value.
     *
     * @param durationInDays The duration of the contract in days. Must be greater than 0.
     * @throws IllegalArgumentException if durationInDays is not greater than 0.
     */
    private void setEndDate(int durationInDays) {
        if (durationInDays <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        this.endDate = LocalDate.now().plusDays(durationInDays);
    }

    /**
     * Gets the {@link ContractType} of this contract.
     *
     * @return The type of the contract (e.g., FullTime, PartTime) as an {@link Enum}.
     */
    public Enum<ContractType> getContractType() {
        return contractType;
    }

    /**
     * Gets the description of this contract.
     *
     * @return The contract description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the end date of this contract.
     *
     * @return The contract's end date as a {@link LocalDate}.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Gets the {@link Employee} associated with this contract.
     *
     * @return The {@link Employee} object.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Gets the {@link Restaurant} associated with this contract.
     *
     * @return The {@link Restaurant} object.
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Gets the sign date of this contract.
     * The sign date is set to the date of contract creation and is final.
     *
     * @return The contract's sign date as a {@link LocalDate}.
     */
    public LocalDate getSignDate() {
        return signDate;
    }

    /**
     * Removes this contract from the system's extent.
     * This method also ensures that the contract is removed from the associated {@link Employee}
     * and {@link Restaurant}, and nullifies its references to them before calling the superclass's
     * {@code removeFromExtent} method.
     */
    @Override
    public void removeFromExtent() {
        employee.removeContract(this);
        restaurant.removeContract(this);

        this.employee = null;
        this.restaurant = null;

        super.removeFromExtent();
    }

    /**
     * Returns a string representation of the {@code Contract} object.
     * Includes the employee's full name, restaurant name, sign date, end date, contract type, and description.
     *
     * @return A string detailing the contract's information.
     */
    @Override
    public String toString() {
        return "Contract{" +
                "employee=" + employee.getName() +
                " " + employee.getSurname() +
                ", restaurant=" + restaurant.getRestaurantName() +
                ", signDate=" + signDate +
                ", endDate=" + endDate +
                ", contractType=" + contractType +
                ", description='" + description + '\'' +
                '}';
    }
}
