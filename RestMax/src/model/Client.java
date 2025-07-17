package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents a client in the system, extending the {@link Person} class.
 * A client has a unique username and can have {@link Review}s and {@link Discount}s associated with them.
 * This class manages the client's reviews and discounts, including active discounts.
 */
public class Client extends Person {
    private String userName;
    private final List<Review> reviews = new ArrayList<>();
    private final List<Discount> discounts = new ArrayList<>();
    private final List<Discount> activeDiscounts = new ArrayList<>();

    /**
     * Constructs a new {@code Client} object.
     * Initializes the client with a username, name, surname, and email.
     * Inherits name, surname, and email handling from the {@link Person} class.
     * If setting the username fails, an exception is caught, its stack trace is printed,
     * and the object is removed from the system's extent.
     *
     * @param userName The unique username for the client. Cannot be null or empty.
     * @param name     The first name of the client.
     * @param surname  The last name of the client.
     * @param email    The email address of the client. Should be a valid email format.
     */
    public Client(String userName, String name, String surname, String email) {
        super(name, surname, email);
        try {
            setUserName(userName);
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Adds a {@link Review} to this client's list of reviews.
     * The review is only added if it is not already present in the list.
     *
     * @param review The {@link Review} to add. Assumed to be non-null.
     */
    public void addReview(Review review) {
        if (!reviews.contains(review)) {
            reviews.add(review);
        }
    }

    /**
     * Removes a {@link Review} from this client's list of reviews.
     * If the review is present, it is removed from the list and also removed from the system's extent.
     *
     * @param review The {@link Review} to remove. Assumed to be non-null.
     */
    public void removeReview(Review review) {
        if (reviews.contains(review)) {
            reviews.remove(review);
            review.removeFromExtent();
        }
    }

    /**
     * Adds a {@link Discount} to this client's list of discounts.
     * Establishes a bidirectional association by setting this client on the discount.
     * The discount is only added if it is not already present in the list.
     *
     * @param discount The {@link Discount} to add. Cannot be null.
     * @throws IllegalArgumentException if the discount is null.
     */
    public void addDiscount(Discount discount) {
        if (discount == null) {
            throw new IllegalArgumentException("Discount cannot be null");
        }
        if (!discounts.contains(discount)) {
            discounts.add(discount);
            discount.setClient(this);
        }
    }

    /**
     * Removes a {@link Discount} from this client's list of discounts and active discounts.
     * Also removes the client association from the discount.
     *
     * @param discount The {@link Discount} to remove. Cannot be null.
     * @throws IllegalArgumentException if the discount is null.
     */
    public void removeDiscount(Discount discount) {
        if (discount == null) {
            throw new IllegalArgumentException("Discount cannot be null");
        }
        if (discounts.contains(discount)) {
            activeDiscounts.remove(discount);
            discounts.remove(discount);
            discount.removeClient(Client.this);
        }
    }

    /**
     * Adds a {@link Discount} to this client's list of active discounts.
     * The discount must already be in the general list of discounts for this client
     * and not already in the active discounts list.
     *
     * @param discount The {@link Discount} to activate. Cannot be null.
     * @throws IllegalArgumentException if the discount is null.
     */
    public void addActiveDiscount(Discount discount) {
        if (discount == null) {
            throw new IllegalArgumentException("Active discount cannot be null");
        }
        if (discounts.contains(discount) && !activeDiscounts.contains(discount)) {
            activeDiscounts.add(discount);
        }
    }

    /**
     * Removes a {@link Discount} from this client's list of active discounts.
     *
     * @param discount The {@link Discount} to deactivate. Cannot be null.
     * @throws IllegalArgumentException if the discount is null.
     */
    public void removeActiveDiscount(Discount discount) {
        if (discount == null) {
            throw new IllegalArgumentException("Active discount cannot be null");
        }
        activeDiscounts.remove(discount);
    }

    /**
     * Validates the client's email address format.
     * Overrides the {@link Person#isValid()} method (implicitly, as Person does not have isValid defined in provided snippets).
     * Uses a regular expression to check for a valid email pattern.
     *
     * @return {@code true} if the email is valid, {@code false} otherwise (e.g., if null or does not match the pattern).
     */
    @Override
    public boolean isValid() {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return getEmail() != null && pattern.matcher(getEmail()).matches();
    }

    /**
     * Gets the identity of the client, which is their username.
     * Overrides the {@link Person#getIdentity()} method.
     *
     * @return The username of this client.
     */
    @Override
    public String getIdentity() {
        return this.userName;
    }

    /**
     * Gets the username of this client.
     *
     * @return The client's username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username for this client.
     * The username cannot be null or empty.
     *
     * @param userName The username to set.
     * @throws IllegalArgumentException if the username is null or empty.
     */
    private void setUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        this.userName = userName;
    }

    /**
     * Gets an unmodifiable list of currently active {@link Discount}s for this client.
     *
     * @return A {@link List} of active {@link Discount}s.
     */
    public List<Discount> getActiveDiscounts() {
        return Collections.unmodifiableList(activeDiscounts);
    }

    /**
     * Gets an unmodifiable list of all {@link Discount}s associated with this client (both active and inactive).
     *
     * @return A {@link List} of all {@link Discount}s for this client.
     */
    public List<Discount> getDiscounts() {
        return Collections.unmodifiableList(discounts);
    }

    /**
     * Gets an unmodifiable list of {@link Review}s made by this client.
     *
     * @return An unmodifiable {@link List} of {@link Review}s.
     */
    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }

    /**
     * Removes this client from the system's extent.
     * Before removal, it ensures all associated {@link Review}s and {@link Discount}s are also removed from the extent.
     * Overrides the {@link Person#removeFromExtent()} method.
     */
    @Override
    public void removeFromExtent() {
        while (!reviews.isEmpty()) {
            removeReview(reviews.getFirst());
        }
        while (!discounts.isEmpty()) {
            removeDiscount(discounts.getFirst());
        }
        super.removeFromExtent();
    }

    /**
     * Returns a string representation of the {@code Client} object.
     * Includes the username, and lists of reviews, discounts, and active discounts.
     *
     * @return A string detailing the client's information.
     */
    @Override
    public String toString() {
        return "Client{" +
                "userName='" + userName + '\'' +
                ", reviews=" + reviews +
                ", discounts=" + discounts +
                ", activeDiscounts=" + activeDiscounts +
                '}';
    }
}
