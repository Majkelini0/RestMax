package model;

/**
 * Defines the contract for entities that own their building and may have parking facilities.
 * Implementations of this interface should provide methods to set and get the parking capacity.
 */
public interface IOwnBuilding {

    /**
     * Sets the parking capacity for the entity.
     *
     * @param parkingCapacity The number of parking spots available.
     *                        Can be null if there is no parking or if the capacity is not defined.
     */
    void setParkingCapacity(Integer parkingCapacity);

    /**
     * Gets the parking capacity of the entity.
     *
     * @return The parking capacity as an {@link Integer}, or null if not set or not applicable.
     */
    Integer getParkingCapacity();
}
