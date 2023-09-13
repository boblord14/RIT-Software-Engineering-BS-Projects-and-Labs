package com.estore.api.estoreapi.persistance;

import java.io.IOException;
import com.estore.api.estoreapi.model.Lootbox;


/**
 * Defines the interface for Lootbox object persistence
 * 
 * @author Ethan Patterson
 */
public interface LootboxDAO {
/**
     * Retrieves all {@linkplain Lootbox lootboxes}
     * 
     * @return An array of {@link Lootbox lootbox} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Lootbox[] getLootboxes() throws IOException;

    /**
     * Finds all {@linkplain Lootbox lootboxes} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Lootbox lootboxes} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Lootbox[] findLootboxes(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Lootbox lootbox} with the given id
     * 
     * @param id The id of the {@link lootbox lootbox} to get
     * 
     * @return a {@link Lootbox lootboxes} lootbox with the matching id
     * <br>
     * null if no {@link Lootbox lootbox} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Lootbox getLootbox(int id) throws IOException;

  

    /**
     * Creates and saves a {@linkplain Lootbox lootbox}
     * 
     * @param lootbox {@linkplain Lootbox lootbox} object to be created and saved
     * <br>
     * The id of the lootbox object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Lootbox lootbox} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Lootbox createLootbox(Lootbox lootbox) throws IOException;

    /**
     * Updates and saves a {@linkplain Lootbox lootbox}
     * 
     * @param {@link Lootbox lootbox} object to be updated and saved
     * 
     * @return updated {@link Lootbox lootbox} if successful, null if
     * {@link Lootbox lootbox} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Lootbox updateLootbox(Lootbox lootbox) throws IOException;

    /**
     * Deletes a {@linkplain Lootbox lootbox} with the given id
     * 
     * @param id The id of the {@link Lootbox lootbox}
     * 
     * @return true if the {@link Lootbox lootbox} was deleted
     * <br>
     * false if lootbox with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteLootbox(int id) throws IOException;
}    

