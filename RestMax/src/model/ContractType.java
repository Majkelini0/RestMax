package model;

/**
 * Represents the different types of contracts an {@link Employee} can have with a {@link Restaurant}.
 */
public enum ContractType {
    /**
     * Indicates a standard employment contract.
     */
    EMPLOYMENT,
    /**
     * Indicates a part-time employment contract.
     */
    PART_TIME,
    /**
     * Indicates a contract for an independent contractor.
     */
    CONTRACTOR,
    /**
     * Indicates an internship contract.
     */
    INTERNSHIP,
    /**
     * Indicates a volunteer agreement (unpaid).
     */
    VOLUNTEER
}