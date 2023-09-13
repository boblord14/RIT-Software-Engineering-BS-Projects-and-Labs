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

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Lootbox;


/**
 * Implements the functionality for JSON file-based peristance for lootboxes
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Ethan Patterson
 */
@Component
public class LootboxFileDAO implements LootboxDAO {
    private static final Logger LOG = Logger.getLogger(LootboxFileDAO.class.getName());
    Map<Integer,Lootbox> lootboxes;   // Provides a local cache of the product objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between lootboxes
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new lootbox
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Lootbox File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public LootboxFileDAO(@Value("${lootboxes.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the products from the file
    }

    /**
     * Generates the next id for a new {@linkplain Lootbox lootbox}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Lootbox lootbox} from the tree map
     * 
     * @return  The array of {@link Lootbox lootbox}, may be empty
     */
    private Lootbox[] getLootboxesArray() {
        return getLootboxesArray(null);
    }

    /**
     * Generates an array of {@linkplain Lootbox lootbox} from the tree map for any
     * {@linkplain Lootbox lootbox} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Lootbox Lootbox}
     * in the tree map
     * 
     * @return  The array of {@link Lootbox lootbox}, may be empty
     */
    private Lootbox[] getLootboxesArray(String containsText) { // if containsText == null, no filter
        ArrayList<Lootbox> lootboxArrayList = new ArrayList<>();

        for (Lootbox lootbox : lootboxes.values()) {
            if (containsText == null || lootbox.getName().contains(containsText)) {
                lootboxArrayList.add(lootbox);
            }
        }

        Lootbox[] lootboxArray = new Lootbox[lootboxArrayList.size()];
        lootboxArrayList.toArray(lootboxArray);
        return lootboxArray;
    }

    /**
     * Saves the {@linkplain Lootbox lootboxes} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Lootbox lootbox} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Lootbox[] lootboxArray = getLootboxesArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),lootboxArray);
        return true;
    }

    /**
     * Loads {@linkplain Lootbox lootboxes} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
      //  ArrayList<Product> testPool = new ArrayList<Product>();
      //  testPool.add(new Product(4, "testproduct", 23, 45));
       // Lootbox testLootbox = new Lootbox(5, "testadd", testPool);
       // createLootbox(testLootbox);
        //save();
       //above is the creation of a test lootbox object and adding it to json. dont question why its here. 
        lootboxes = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of products
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Lootbox[] lootboxArray = objectMapper.readValue(new File(filename),Lootbox[].class);

        // Add each product to the tree map and keep track of the greatest id
        for (Lootbox lootbox : lootboxArray) {
            lootboxes.put(lootbox.getId(),lootbox);
            if (lootbox.getId() > nextId)
                nextId = lootbox.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Lootbox[] getLootboxes() {
        synchronized(lootboxes) {
            return getLootboxesArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Lootbox[] findLootboxes(String containsText) {
        synchronized(lootboxes) {
            return getLootboxesArray(containsText);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Lootbox getLootbox(int id) {
        synchronized(lootboxes) {
            if (lootboxes.containsKey(id))
                return lootboxes.get(id);
            else
                return null;
        }
    }


    /**
    ** {@inheritDoc}
     */
    @Override
    public Lootbox createLootbox(Lootbox lootbox) throws IOException {
        synchronized(lootboxes) {
            // We create a new lootbox object because the id field is immutable
            // and we need to assign the next unique id
            System.out.println(lootbox.getName());
            Lootbox newLootbox = new Lootbox(nextId(),lootbox.getName(), lootbox.getPrice());
            lootboxes.put(newLootbox.getId(),newLootbox);
            save(); // may throw an IOException
            return newLootbox;
        }
    }

     /**
    ** {@inheritDoc}
     */
    @Override
    public Lootbox updateLootbox(Lootbox lootbox) throws IOException {
        synchronized(lootboxes) {
            if (lootboxes.containsKey(lootbox.getId()) == false)
                return null;  // lootbox does not exist

                lootboxes.put(lootbox.getId(),lootbox);
            save(); // may throw an IOException
            return lootbox;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteLootbox(int id) throws IOException {
        synchronized(lootboxes) {
            if (lootboxes.containsKey(id)) {
                lootboxes.remove(id);
                return save();
            }
            else
                return false;
        }
    }

 
}

