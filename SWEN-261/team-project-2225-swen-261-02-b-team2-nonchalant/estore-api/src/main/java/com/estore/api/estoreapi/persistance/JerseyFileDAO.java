package com.estore.api.estoreapi.persistance;

import java.io.File;
import java.io.IOException; 
import java.util.ArrayList; 
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Jersey;

/**
 * Implements the functionality for the JSON file-based persistance for jerseys. 
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed. 
 */
@Component
public class JerseyFileDAO implements JerseyDAO {
    private static final Logger LOG = Logger.getLogger(JerseyFileDAO.class.getName());
    Map<Integer, Jersey> jerseys; 
    private ObjectMapper objectMapper; 
    private static int nextId; 
    private String filename; 

    /**
     * Creates a Jersey File Data Access Object
     * @param filename Filename to read from/write to.
     * @param objectMapper Provides JSON Object to/from Java Object seriaization and deserialization. 
     * @throws IOException when file cannot be accessed or read from.  
     */
    public JerseyFileDAO(@Value("${jerseys.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the jerseys from the file
    }

    private synchronized static int nextId() {
        int id = nextId; 
        nextId++; 
        return id; 
    }

    private Jersey[] getJerseysArray() {
        return getJerseysArray(null); 
    }

    private Jersey[] getJerseysArray(String creator) {
        ArrayList<Jersey> jerseyArrayList = new ArrayList<>();

        for (Jersey jersey : jerseys.values()) {
            if (creator == null || jersey.getCreator().equals(creator)) {
                jerseyArrayList.add(jersey);
            }
        }

        Jersey[] jerseyArray = new Jersey[jerseyArrayList.size()];
        jerseyArrayList.toArray(jerseyArray);
        return jerseyArray;
    }


    private boolean save() throws IOException {
        Jersey[] jerseyArray = getJerseysArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), jerseyArray);
        return true;
    }

    private boolean load() throws IOException {
        jerseys = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of jerseys
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Jersey[] jerseyArray = objectMapper.readValue(new File(filename), Jersey[].class);

        // Add each jersey to the tree map and keep track of the greatest id
        for (Jersey jersey : jerseyArray) {
            jerseys.put(jersey.getId(),jersey);
            if (jersey.getId() > nextId)
                nextId = jersey.getId();
        }
        // Make the next id one greater than the maximum from the file
        nextId++;
        return true;
    }

    @Override
    public Jersey[] getJerseys() throws IOException {
        synchronized(jerseys) {
            return getJerseysArray(); 
        }
    }

    @Override
    public Jersey[] findJerseys(String creator) throws IOException {
        synchronized(jerseys) {
            return getJerseysArray(creator); 
        }
    }

    @Override
    public Jersey getJersey(int id) throws IOException {
        synchronized(jerseys) {
            if (jerseys.containsKey(id)) {
                return jerseys.get(id); 
            }
            else { return null; } 
        }
    }

    @Override
    public Jersey createJersey(Jersey jersey) throws IOException {
        // Create a new object since id is immutable and we need to set it to a new unique id. 
        // TODO Throw an error if any parameters passed aren't allowed. 
        Jersey newJersey = new Jersey(nextId(), jersey.getCreator(), jersey.getPrimary_color(), jersey.getSecondary_color(), jersey.getName(), jersey.getNumber(), jersey.getLogo(), jersey.getPrice()); 
        jerseys.put(newJersey.getId(), newJersey); 
        save(); // May throw an IOException
        return newJersey; 
    }

    @Override
    public Jersey updateJersey(Jersey jersey) throws IOException, IllegalArgumentException{
        synchronized(jerseys) {
            if (jerseys.containsKey(jersey.getId()) == false) {
                return null;  // Jersey does not exist. 
            }
            // TODO Throw an error if any parameters passed aren't allowed. 
            jerseys.put(jersey.getId(), jersey);
            save(); // May throw an IOException. 
            return jersey;
        }
    }

    @Override
    public boolean deleteJersey(int id) throws IOException {
        synchronized(jerseys) {
            if (jerseys.containsKey(id)) {
                jerseys.remove(id);
                return save();
            }
            else { return false; }
        }
    }

}
