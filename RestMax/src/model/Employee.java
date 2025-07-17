package model;

import util.ObjectPlus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an employee, extending the {@link Person} class.
 * An employee is identified by a unique PESEL number and can have multiple {@link Contract}s
 * with different {@link Restaurant}s.
 * This class manages the employee's contracts and personal information.
 */
public class Employee extends Person {
    private String pesel;

    private final List<Contract> restaurantList = new ArrayList<>();

    /**
     * Constructs a new {@code Employee} object.
     * Initializes the employee with a PESEL number, name, surname, and email.
     * Inherits name, surname, and email handling from the {@link Person} class.
     * Validates the PESEL for format and uniqueness. If validation fails or any other exception occurs,
     * the error is printed, and the object is removed from the system's extent.
     *
     * @param pesel   The employee's unique PESEL number (Polish National Identification Number). Must be valid and unique.
     * @param name    The first name of the employee.
     * @param surname The last name of the employee.
     * @param email   The email address of the employee.
     */
    public Employee(String pesel, String name, String surname, String email) {
        super(name, surname, email);
        try {
            setPesel(pesel);
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Adds a new {@link Contract} for this employee with a specified {@link Restaurant}.
     * A new contract is created only if a contract of the same {@link ContractType}
     * with the same {@link Restaurant} does not already exist for this employee.
     *
     * @param restaurant     The {@link Restaurant} to associate with the new contract.
     * @param contractType   The {@link ContractType} for the new contract.
     * @param description    A description for the contract.
     * @param durationInDays The duration of the contract in days.
     */
    public void addRestaurant(Restaurant restaurant, Enum<ContractType> contractType, String description, int durationInDays) {
        if (restaurantList.stream().noneMatch(contract -> contract.getRestaurant() == restaurant && contract.getContractType() == contractType)) {
            new Contract(this, restaurant, contractType, description, durationInDays);
        } else {
            System.out.println("Contract already exists for this restaurant with the same contract type");
        }
    }

    /**
     * Adds a {@link Contract} to this employee's list of contracts.
     * This method is typically called from the {@link Contract} constructor to establish
     * the bidirectional association.
     *
     * @param contract The {@link Contract} to add. Cannot be null.
     * @throws NullPointerException if the contract is null.
     */
    protected void addContract(Contract contract) {
        if (contract == null) {
            throw new NullPointerException();
        }
        restaurantList.add(contract);
    }

    /**
     * Removes a {@link Contract} from this employee's list of contracts.
     * This method is typically called when a contract is terminated or removed.
     *
     * @param contract The {@link Contract} to remove.
     */
    protected void removeContract(Contract contract) {
        if (restaurantList.stream().anyMatch(contract::equals)) {
            restaurantList.remove(contract);
        }
    }

    /**
     * Ends an existing {@link Contract} for this employee with a specific {@link Restaurant} and {@link ContractType}.
     * If found, the contract is removed from the system's extent.
     *
     * @param restaurant   The {@link Restaurant} of the contract to be ended. Cannot be null.
     * @param contractType The {@link ContractType} of the contract to be ended. Cannot be null.
     * @throws NullPointerException if restaurant or contractType is null.
     */
    public void endContract(Restaurant restaurant, ContractType contractType) {
        if (restaurant == null || contractType == null) {
            throw new NullPointerException();
        }
        for (Contract contract : restaurantList) {
            if (contract.getRestaurant() == restaurant && contract.getContractType() == contractType) {
                contract.removeFromExtent();
                break;
            }
        }
    }

    /**
     * Sets the PESEL number for this employee.
     * The PESEL must not be null or empty, must be valid according to Polish checksum rules,
     * and must be unique among all employees.
     *
     * @param pesel The PESEL number to set.
     * @throws IllegalArgumentException if the PESEL is null, empty, invalid, or not unique.
     */
    private void setPesel(String pesel) {
        if (pesel == null || pesel.isEmpty()) {
            throw new IllegalArgumentException("PESEL cannot be empty");
        }
        if (!validatePesel(pesel)) {
            throw new IllegalArgumentException("Invalid PESEL");
        }
        if (!isPeselUnique(pesel)) {
            throw new IllegalArgumentException("PESEL already exists");
        }
        this.pesel = pesel;
    }

    /**
     * Validates a PESEL number based on its length, format (digits only), and checksum.
     *
     * @param pesel The PESEL number string to validate.
     * @return {@code true} if the PESEL is valid, {@code false} otherwise.
     */
    private boolean validatePesel(String pesel) {
        if (pesel.length() != 11) {
            return false;
        }

        if (!pesel.matches("\\d{11}")) {
            return false;
        }

        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Integer.parseInt(pesel.substring(i, i + 1)) * weights[i];
        }

        int checksum = (10 - (sum % 10)) % 10;
        int controlDigit = Integer.parseInt(pesel.substring(10, 11));

        return checksum == controlDigit;
    }

    /**
     * Checks if a PESEL number is unique among all existing employees, excluding the current employee instance.
     *
     * @param pesel The PESEL number to check for uniqueness.
     * @return {@code true} if the PESEL is unique, {@code false} otherwise.
     */
    private boolean isPeselUnique(String pesel) {
        return ObjectPlus.getExtentFromClass(Employee.class).stream()
                .filter(obj -> obj != this)
                .noneMatch(employee -> pesel.equals(employee.getPesel()));
    }

    /**
     * Gets the identity of the employee, which is their PESEL number.
     * Overrides the {@link Person#getIdentity()} method.
     *
     * @return The PESEL number of this employee.
     */
    @Override
    public String getIdentity() {
        return this.pesel;
    }

    /**
     * Checks if the employee's PESEL number is valid.
     * Overrides the {@link Person#isValid()} method (implicitly, as Person does not have isValid defined in provided snippets).
     *
     * @return {@code true} if the PESEL is valid, {@code false} otherwise.
     */
    @Override
    public boolean isValid() {
        return validatePesel(this.pesel);
    }

    /**
     * Gets the PESEL number of this employee.
     *
     * @return The employee's PESEL number.
     */
    public String getPesel() {
        return pesel;
    }


    /**
     * Gets an unmodifiable list of {@link Contract}s associated with this employee.
     * The list represents all restaurants the employee is or has been contracted with.
     *
     * @return An unmodifiable {@link List} of {@link Contract}s.
     */
    public List<Contract> getRestaurantList() {
        return Collections.unmodifiableList(restaurantList);
    }

    /**
     * Removes this employee from the system's extent.
     * Before removal, it ensures all associated {@link Contract}s are also removed from the extent.
     * Overrides the {@link Person#removeFromExtent()} method.
     */
    @Override
    public void removeFromExtent() {
        while (restaurantList != null && !restaurantList.isEmpty()) {
            restaurantList.getFirst().removeFromExtent();
        }
        super.removeFromExtent();
    }

    /**
     * Returns a string representation of the {@code Employee} object.
     * Includes the employee's name and surname. The PESEL is commented out for privacy but can be included if needed.
     *
     * @return A string detailing the employee's information.
     */
    @Override
    public String toString() {
        return "Employee{" +
                //"pesel='" + pesel + ', ' +
                "name='" + this.getName() + '\'' +
                ", surname='" + this.getSurname() + '\'' +
                //", restaurantList=" + getRestaurantList() +
                '}';
    }
}

/*
valid PESELe
79100383434
55020244514
72040874798
88041624171
66012395176*/
