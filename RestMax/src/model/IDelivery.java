package model;

/**
 * Defines the contract for entities that offer delivery services.
 * Implementations of this interface should provide methods to set and get the maximum delivery distance.
 */
public interface IDelivery {

    /**
     * Sets the maximum delivery distance for the entity.
     *
     * @param maxDeliveryDistance The maximum distance (e.g., in kilometers or miles) the entity will deliver to.
     *                            Can be null if there is no limit or if delivery is not currently configured.
     */
    void setMaxDeliveryDistance(Integer maxDeliveryDistance);

    /**
     * Gets the maximum delivery distance for the entity.
     *
     * @return The maximum delivery distance as an {@link Integer}, or null if not set or not applicable.
     */
    Integer getMaxDeliveryDistance();
}
