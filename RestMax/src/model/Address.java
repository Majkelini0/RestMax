package model;

import util.ObjectPlus;

/**
 * Represents a physical address.
 * This class is part of the {@link model} package and extends {@link util.ObjectPlus} for extent management.
 * It includes details such as street, street number, flat number, city, and postal code.
 */
public class Address extends ObjectPlus {

    private String street;
    private String streetNumber;
    private String flatNumber;
    private String city;
    private String postalCode;

    /**
     * Constructs an {@code Address} object with a flat number.
     * Initializes the address components and adds the object to the extent.
     * If any validation fails (e.g., null or blank required fields), an {@link IllegalArgumentException} is caught,
     * its stack trace is printed, and the object is removed from the extent.
     *
     * @param street       The street name. Cannot be null or blank.
     * @param streetNumber The street number. Cannot be null or blank.
     * @param flatNumber   The flat or apartment number. Can be null or blank if not applicable.
     * @param city         The city name. Cannot be null or blank.
     * @param postalCode   The postal code. Cannot be null or blank.
     */
    public Address(String street, String streetNumber, String flatNumber, String city, String postalCode) {
        try {
            setStreet(street);
            setStreetNumber(streetNumber);
            setFlatNumber(flatNumber);
            setCity(city);
            setZip(postalCode);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Constructs an {@code Address} object without a flat number.
     * Initializes the address components (street, street number, city, postal code) and adds the object to the extent.
     * If any validation fails, an {@link IllegalArgumentException} is caught, its stack trace is printed,
     * and the object is removed from the extent.
     *
     * @param street       The street name. Cannot be null or blank.
     * @param streetNumber The street number. Cannot be null or blank.
     * @param city         The city name. Cannot be null or blank.
     * @param postalCode   The postal code. Cannot be null or blank.
     */
    public Address(String street, String streetNumber, String city, String postalCode) {
        try {
            setStreet(street);
            setStreetNumber(streetNumber);
            setCity(city);
            setZip(postalCode);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Sets the street name for this address.
     *
     * @param street The street name to set. Cannot be null or blank.
     * @throws IllegalArgumentException if the street is null or blank.
     */
    public void setStreet(String street) throws IllegalArgumentException {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be null or blank");
        }
        this.street = street;
    }

    /**
     * Sets the street number for this address.
     *
     * @param streetNumber The street number to set. Cannot be null or blank.
     * @throws IllegalArgumentException if the street number is null or blank.
     */
    public void setStreetNumber(String streetNumber) {
        if (streetNumber == null || streetNumber.isBlank()) {
            throw new IllegalArgumentException("Street number cannot be null or blank");
        }
        this.streetNumber = streetNumber;
    }

    /**
     * Sets the flat number for this address.
     * If the provided flat number is null or blank, the internal flatNumber field is set to null.
     *
     * @param flatNumber The flat number to set. Can be null or blank.
     */
    public void setFlatNumber(String flatNumber) {
        if (flatNumber == null || flatNumber.isBlank()) {
            this.flatNumber = null;
        }
        this.flatNumber = flatNumber;
    }

    /**
     * Sets the city name for this address.
     *
     * @param city The city name to set. Cannot be null or blank.
     * @throws IllegalArgumentException if the city is null or blank.
     */
    public void setCity(String city) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be null or blank");
        }
        this.city = city;
    }

    /**
     * Sets the postal code for this address.
     * The method name {@code setZip} is used, but it sets the postalCode field.
     *
     * @param postalCode The postal code to set. Cannot be null or blank.
     * @throws IllegalArgumentException if the postal code is null or blank.
     */
    public void setZip(String postalCode) {
        if (postalCode == null || postalCode.isBlank()) {
            throw new IllegalArgumentException("Zip cannot be null or blank");
        }
        this.postalCode = postalCode;
    }

    /**
     * Gets the street name of this address.
     *
     * @return The street name.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Gets the street number of this address.
     *
     * @return The street number.
     */
    public String getStreetNumber() {
        return streetNumber;
    }

    /**
     * Gets the flat number of this address.
     *
     * @return The flat number, or null if not set.
     */
    public String getFlatNumber() {
        return flatNumber;
    }

    /**
     * Gets the city name of this address.
     *
     * @return The city name.
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the postal code of this address.
     *
     * @return The postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Returns a string representation of the {@code Address} object.
     * This includes the street, street number, flat number (if present), city, and postal code.
     *
     * @return A string detailing the components of this address.
     */
    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", flatNumber='" + flatNumber + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
