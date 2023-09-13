package com.estore.api.estoreapi.model;
import java.util.ArrayList;
//import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Janked together reskin of the product system to do the same but with lootbox accounts
 * 
 * @author Ethan Patterson
 */
public class Lootbox {
    //private static final Logger LOG = Logger.getLogger(Lootbox.class.getName());
    // Package private for tests
    static final String STRING_FORMAT = "Lootbox [id=%d, name=%s, price=%d, pool=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private int price;
    @JsonProperty("pool") private ArrayList<Product> pool;
    @JsonProperty("image") private String image;



    /**
     * Create a lootbox with the given id, name and empty pool
     * @param id The id of the lootbox
     * @param name The name of the lootbox
     * @param pool The pool of products

     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Lootbox(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("price") int price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pool = new ArrayList<Product>();
        this.image = "https://www.wheelco.com/images/product-images/no_image_available.jpeg?resizeid=112&resizeh=600&resizew=600";


    }

    /**
     * Retrieves the id of the lootbox
     * @return The id of the lootbox
     */
    public int getId() {return id;}

    /**
     * Sets the name of the lootbox- this should never really need to be used except maybe for lootbox creation.
     * @param name The name of the lootbox
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the lootbox
     * @return The name of the lootbox
     */
    public String getName() {return name;}


    /**
     * Retrieves the full shopping pool
     * @return The whole pool list
     */
    public ArrayList<Product> getPool() {return pool;}

    /**
     * adds an item to the end of the shopping pool
     * @param quantity The product to add
     */
    public void addToPool(Product productToAdd) {this.pool.add(productToAdd);}

    /**
     * removes an item from shopping pool. this assumes the given id is actually in the shopping pool, does nothing otherwise
     * @param id The id of the product to remove
     */
    public void removeFromPool(int id) {
        for (Product product : pool) {
            if(product.getId()==id){
                pool.remove(product);
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id, name,price,pool);
    }

    public int getPrice() {
        return price;
    }
}



    

