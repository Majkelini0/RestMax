package model;

/**
 * Defines the contract for entities that offer drive-thru services.
 * Implementations of this interface should provide methods to set and get the maximum car height allowed.
 */
public interface IDriveThru {

    /**
     * Sets the maximum car height allowed for the drive-thru.
     *
     * @param maxCarHeight The maximum height (e.g., in meters or feet) a vehicle can be to use the drive-thru.
     *                     Can be null if there is no limit or if drive-thru is not currently configured.
     */
    void setMaxCarHeight(Double maxCarHeight);

    /**
     * Gets the maximum car height allowed for the drive-thru.
     *
     * @return The maximum car height as a {@link Double}, or null if not set or not applicable.
     */
    Double getMaxCarHeight();
}
