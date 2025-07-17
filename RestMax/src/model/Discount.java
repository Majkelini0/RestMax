package model;

import util.ObjectPlus;

/**
 * Abstract base class for different types of discounts that can be applied to a {@link Client}.
 * This class extends {@link util.ObjectPlus} for extent management and defines the core functionality
 * for associating a discount with a client and determining its applicability and amount.
 * Subclasses must implement the {@link #getTotalDiscountAmount()} method.
 */
public abstract class Discount extends ObjectPlus {
    private Client client;

    /**
     * Abstract method to be implemented by subclasses to calculate the total discount amount.
     * The specific calculation will depend on the type of discount (e.g., fixed amount, percentage).
     *
     * @return The total amount of the discount as a double.
     */
    public abstract double getTotalDiscountAmount();

    /**
     * Checks if this discount is currently applicable.
     * A discount is considered applicable if it is associated with a {@link Client}.
     *
     * @return {@code true} if the discount is associated with a client, {@code false} otherwise.
     */
    public boolean isApplicable() {
        return getClient() != null;
    }

    /**
     * Associates this discount with a {@link Client}.
     * This method establishes a bidirectional relationship: it sets the client for this discount
     * and adds this discount to the client's list of discounts, but only if the discount is not already
     * associated with this client or another client.
     *
     * @param client The {@link Client} to associate with this discount. Cannot be null.
     * @throws IllegalArgumentException if the client is null.
     */
    public void addClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        if (this.client == null && !client.getDiscounts().contains(this)) {
            this.client = client;
            client.addDiscount(this);
        }
    }

    /**
     * Removes the association of this discount from a specific {@link Client}.
     * This method updates the bidirectional relationship: it removes this discount from the client's list
     * and nullifies the client reference in this discount, but only if the provided client is the one
     * currently associated with this discount.
     *
     * @param client The {@link Client} from whom to remove this discount. Cannot be null.
     * @throws IllegalArgumentException if the client is null.
     */
    public void removeClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        if (this.client != null && this.client.equals(client)) {
            this.client.removeDiscount(this);
            this.client = null;
        }
    }

    /**
     * Sets the {@link Client} for this discount.
     * This method is protected and is typically used by subclasses or related classes
     * to manage the client association directly, for example, when a client is removed.
     *
     * @param client The {@link Client} to set. Can be null if the discount is being disassociated.
     */
    protected void setClient(Client client) {
        this.client = client;
    }

    /**
     * Gets the {@link Client} associated with this discount.
     *
     * @return The {@link Client} to whom this discount applies, or {@code null} if not associated.
     */
    public Client getClient() {
        return client;
    }
}