package com.estore.api.estoreapi.persistance;

import java.io.IOException;
import com.estore.api.estoreapi.model.Jersey;

/**
 * Defines the interface for Jersey object persistance. 
 * 
 * @author Zach Conway
 */
public interface JerseyDAO {
    /**
     * Retrieves all {@linkplain Jersey jerseys}
     * @return An array of {@link Jersey jersey} objects, may be empty. 
     * @throws IOException if an issue with underlying storage. 
     */
    Jersey[] getJerseys() throws IOException; 

     /**
     * Finds all {@linkplain Jersey jerseys} created by a user with the given username. 
     * @param creator The user who created the jerseys. 
     * @return An array of {@link Jersey jerseys} created by the given user, may be empty. 
     * @throws IOException if an issue with underlying storage. 
     */
    Jersey[] findJerseys(String creator) throws IOException; 

    /**
     * Retrieves a jersey of the given id. 
     * @param id The id of the jersey being retrieved. 
     * @return The jersey of the given id. 
     * @throws IOException if an issue with underlying storage. 
     */
    Jersey getJersey(int id) throws IOException; 

    /**
     * Stores a jersey of the given state. 
     * @param jersey A jersey class containing the attributes of the jersey being stored. 
     * @return The newly created jersey. 
     * @throws IOException if an issue with underlying storage. 
     */
    Jersey createJersey(Jersey jersey) throws IOException; 

    /**
     * Updates an exising jersey to a given state. 
     * @param jersey The new state of the jersey being updated. 
     * @return The new state of the jersey. 
     * @throws IOException if an issue with underlying storage. 
     */
    Jersey updateJersey(Jersey jersey) throws IOException; 

    /**
     * Deletes a jersey of the given id. 
     * @param id The id of the jersey being deleted. 
     * @return Whether the jersey was successfully deleted. 
     * @throws IOException if an issue with underlying storage. 
     */
    boolean deleteJersey(int id) throws IOException; 
}
