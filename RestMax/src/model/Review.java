package model;

import util.ObjectPlus;

/**
 * Represents a review written by a {@link Client}.
 * This class extends {@link util.ObjectPlus} for extent management and stores the review's title, content,
 * and the client who wrote it.
 * It establishes a bidirectional relationship with the {@link Client}.
 */
public class Review extends ObjectPlus {
    private Client client;
    private String title;
    private String content;

    /**
     * Constructs a new {@code Review} object.
     * Initializes the review with a title, content, and the {@link Client} who authored it.
     * Associates the review with the client. If any validation fails (e.g., null client, empty title/content)
     * or an exception occurs, the error is printed, and the object is removed from the system's extent.
     *
     * @param title   The title of the review. Cannot be null or empty.
     * @param content The main content of the review. Cannot be null or empty.
     * @param client  The {@link Client} who wrote this review. Cannot be null.
     */
    public Review(String title, String content, Client client) {
        try {
            setClient(client);
            setTitle(title);
            setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    /**
     * Sets the {@link Client} who authored this review and adds this review to the client's list of reviews.
     * This method is private and called during construction to establish the bidirectional relationship.
     *
     * @param client The {@link Client} to associate with this review. Cannot be null.
     * @throws NullPointerException if the client is null.
     */
    private void setClient(Client client) {
        if (client == null) {
            throw new NullPointerException("Client is missing (null)");
        }
        this.client = client;
        client.addReview(this);
    }

    /**
     * Sets the title of the review.
     * The title cannot be null or empty.
     *
     * @param title The title to set for the review.
     * @throws IllegalArgumentException if the title is null or empty.
     */
    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }

    /**
     * Sets the main content of the review.
     * The content cannot be null or empty.
     *
     * @param content The content to set for the review.
     * @throws IllegalArgumentException if the content is null or empty.
     */
    private void setContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        this.content = content;
    }

    /**
     * Gets the {@link Client} who wrote this review.
     *
     * @return The {@link Client} associated with this review.
     */
    public Client getClient() {
        return client;
    }

    /**
     * Gets the title of this review.
     *
     * @return The review's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the main content of this review.
     *
     * @return The review's content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Removes this review from the system's extent.
     * This method also ensures that the review is removed from the associated {@link Client}'s list of reviews
     * and nullifies its reference to the client before calling the superclass's {@code removeFromExtent} method.
     */
    @Override
    public void removeFromExtent() {
        client.removeReview(this);
        client = null;
        super.removeFromExtent();
    }

    /**
     * Returns a string representation of the {@code Review} object.
     * Includes the review's title, content, and the username of the client who wrote it.
     *
     * @return A string detailing the review's information.
     */
    @Override
    public String toString() {
        return "Review{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", client=" + client.getUserName() +
                '}';
    }
}
