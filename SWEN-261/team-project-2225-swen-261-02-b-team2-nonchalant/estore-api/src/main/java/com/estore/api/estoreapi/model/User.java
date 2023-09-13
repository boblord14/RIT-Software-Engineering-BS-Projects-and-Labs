package com.estore.api.estoreapi.model;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Janked together reskin of the product system to do the same but with user accounts
 * 
 * @author Ethan Patterson
 */
public class User {
    private static final Logger LOG = Logger.getLogger(User.class.getName());
    // Package private for tests
    static final String STRING_FORMAT = "User [id=%d, name=%s, cart=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("cart") private ArrayList<Product> cart;


    /**
     * Create a user with the given id, name and empty cart
     * @param id The id of the user
     * @param name The name of the user
     * @param cart The cart of products

     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public User(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("cart") ArrayList<Product> cart) {
        this.id = id;
        this.name = name;
        this.cart = cart;

    }

    /**
     * Retrieves the id of the user
     * @return The id of the user
     */
    public int getId() {return id;}

    /**
     * Sets the name of the user- this should never really need to be used except maybe for user creation.
     * @param name The name of the user
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the user
     * @return The name of the user
     */
    public String getName() {return name;}


    /**
     * Retrieves the full shopping cart
     * @return The whole cart list
     */
    public ArrayList<Product> getCart() {return cart;}

    /**
     * adds an item to the end of the shopping cart
     * @param quantity The product to add
     */
    public void addToCart(Product productToAdd) {this.cart.add(productToAdd);}

    /**
     * removes an item from shopping cart. this assumes the given id is actually in the shopping cart, does nothing otherwise
     * @param id The id of the product to remove
     */
    public void removeFromCart(int id) {
        for (Product product : cart) {
            if(product.getId()==id){
                cart.remove(product);
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,cart);
    }
}



    

