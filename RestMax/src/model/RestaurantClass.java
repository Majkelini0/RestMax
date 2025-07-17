package model;

/**
 * Represents the different classifications or types a {@link Restaurant} can have.
 * These classifications determine the features and capabilities of a restaurant,
 * such as offering drive-thru, delivery services, or having its own building (which might imply parking).
 */
public enum RestaurantClass {
    /**
     * Indicates that the restaurant has a drive-thru facility.
     */
    DriveThru,
    /**
     * Indicates that the restaurant offers delivery services.
     */
    Delivery,
    /**
     * Indicates that the restaurant operates in its own building, potentially with specific facilities like parking.
     */
    OwnBuilding
}
