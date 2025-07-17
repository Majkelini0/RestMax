package model;

import util.ObjectPlus;

/**
 * Abstract base class for representing a person in the system.
 * This class extends {@link util.ObjectPlus} for extent management and provides common properties
 * such as name, surname, and email.
 * Subclasses must implement {@link #getIdentity()} and {@link #isValid()} methods.
 */
public abstract class Person extends ObjectPlus {
    private String name;
    private String surname;
    private String email;
    private final static byte nameSurnameMinLength = 2;
    private final static byte nameSurnameMaxLength = 100;

    /**
     * Constructs a new {@code Person} object.
     * Initializes the person with a name, surname, and email.
     * Validates the length of the name and surname, and ensures email is not empty.
     * If any validation fails or an exception occurs, the error is printed,
     * and the object is removed from the system's extent.
     *
     * @param name    The first name of the person. Cannot be null or empty. Length must be between {@link #nameSurnameMinLength} and {@link #nameSurnameMaxLength}.
     * @param surname The last name of the person. Cannot be null or empty. Length must be between {@link #nameSurnameMinLength} and {@link #nameSurnameMaxLength}.
     * @param email   The email address of the person. Cannot be null or empty.
     */
    public Person(String name, String surname, String email) {
        try {
            setName(name);
            setSurname(surname);
            setEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Abstract method to be implemented by subclasses to get a unique identifier for the person.
     * This could be, for example, a PESEL number for an {@link Employee} or a username for a {@link Client}.
     *
     * @return A string representing the unique identity of the person.
     */
    public abstract String getIdentity();

    /**
     * Abstract method to be implemented by subclasses to validate the person's specific data.
     * For example, an {@link Employee} might validate their PESEL, and a {@link Client} might validate their email format.
     *
     * @return {@code true} if the person's data is valid, {@code false} otherwise.
     */
    public abstract boolean isValid();

    /**
     * Gets the first name of the person.
     *
     * @return The person's first name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the last name (surname) of the person.
     *
     * @return The person's surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Gets the email address of the person.
     *
     * @return The person's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the first name of the person.
     * The name cannot be null or empty, and its length must be within the defined minimum and maximum limits.
     *
     * @param name The first name to set.
     * @throws IllegalArgumentException if the name is null, empty, or its length is outside the allowed range.
     */
    private void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() < nameSurnameMinLength || name.length() > nameSurnameMaxLength) {
            throw new IllegalArgumentException("Name length must be between " + nameSurnameMinLength + " and " + nameSurnameMaxLength);
        }
        this.name = name;
    }

    /**
     * Sets the last name (surname) of the person.
     * The surname cannot be null or empty, and its length must be within the defined minimum and maximum limits.
     *
     * @param surname The surname to set.
     * @throws IllegalArgumentException if the surname is null, empty, or its length is outside the allowed range.
     */
    private void setSurname(String surname) {
        if (surname == null || surname.isEmpty()) {
            throw new IllegalArgumentException("Surname cannot be empty");
        }
        if (surname.length() < nameSurnameMinLength || name.length() > nameSurnameMaxLength) {
            throw new IllegalArgumentException("Surname length must be between " + nameSurnameMinLength + " and " + nameSurnameMaxLength);
        }
        this.surname = surname;
    }

    /**
     * Sets the email address of the person.
     * The email cannot be null or empty.
     * Further validation (e.g., format) might be done in subclasses like {@link Client#isValid()}.
     *
     * @param email The email address to set.
     * @throws IllegalArgumentException if the email is null or empty.
     */
    private void setEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        this.email = email;
    }

    /**
     * Returns a string representation of the {@code Person} object.
     * Includes the person's name, surname, and email.
     *
     * @return A string detailing the person's information.
     */
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
