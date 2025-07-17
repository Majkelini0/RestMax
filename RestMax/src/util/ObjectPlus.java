package util;

import model.Restaurant;

import java.io.*;
import java.util.*;

/**
 * A base class providing functionality for managing extents (collections of all instances)
 * of classes that inherit from it. It supports serialization and deserialization of these extents.
 * This allows for persisting and loading the state of all managed objects.
 */
public class ObjectPlus implements Serializable {
    /**
     * A static map holding all extents. The key is the class, and the value is a list of instances of that class.
     */
    private static Map<Class, List> extent = new HashMap<>();
    /**
     * The default filename used for serializing and deserializing the extents.
     */
    public static final String serName = "extent";

    /**
     * Constructs an {@code ObjectPlus} instance and automatically adds it to the extent of its class.
     */
    public ObjectPlus() {
        addToExtent();
    }

    /**
     * Adds the current instance ({@code this}) to the list of instances for its class in the {@code extent} map.
     * If a list for the class does not exist, it is created.
     * This method is protected and typically called by the constructor.
     */
    protected void addToExtent() {
        List list = extent.computeIfAbsent(this.getClass(), _ -> new ArrayList<>());
        list.add(this);
    }

    /**
     * Removes the current instance ({@code this}) from the list of instances for its class in the {@code extent} map.
     * If the list for the class exists and contains this instance, it is removed.
     */
    public void removeFromExtent() {
        List list = extent.get(this.getClass());
        if (list != null) {
            list.remove(this);
        }
    }

    /**
     * Saves (serializes) the entire {@code extent} map to a file specified by {@link #serName}.
     * This method is static and can be called to persist the state of all {@code ObjectPlus} instances.
     * If an {@link IOException} occurs during serialization, its stack trace is printed.
     */
    public static void saveExtent() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serName));
            oos.writeObject(extent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Loads (deserializes) the {@code extent} map from a file specified by {@link #serName}.
     * After loading, it specifically calls {@link Restaurant#initializeMenuCategoriesSet()} to ensure
     * that dependent static collections in the {@link Restaurant} class are correctly initialized based on the loaded data.
     * This method is static and can be called to restore the state of all {@code ObjectPlus} instances.
     * If an {@link IOException} or {@link ClassNotFoundException} occurs, its stack trace is printed.
     */
    public static void loadExtent() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serName));
            extent = (Map<Class, List>) ois.readObject();
            Restaurant.initializeMenuCategoriesSet();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves an unmodifiable list of all instances (the extent) for a given class {@code c}.
     * If no extent exists for the class, an empty list is created and associated with the class in the {@code extent} map.
     * This method is static and generic, allowing type-safe retrieval of extents.
     *
     * @param c   The class for which to retrieve the extent.
     * @param <T> The type of the class.
     * @return An unmodifiable {@link List} of all instances of class {@code c}.
     */
    public static <T> List<T> getExtentFromClass(Class<T> c) {
        extent.computeIfAbsent(c, _ -> new ArrayList<>());
        return Collections.unmodifiableList(extent.get(c));
    }

    /**
     * Checks if the extent map is empty, i.e., if there are no instances of any class managed by {@code ObjectPlus}.
     *
     * @return {@code true} if the extent map is empty; {@code false} otherwise.
     */
    public static boolean isExtentEmpty() {
        return extent.isEmpty();
    }
}
