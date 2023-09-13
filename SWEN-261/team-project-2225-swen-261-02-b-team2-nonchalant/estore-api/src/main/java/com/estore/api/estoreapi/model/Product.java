package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Product entity
 * 
 * @author Noah Pangilinan
 */
public class Product {
    // Package private for tests
    static final String STRING_FORMAT = "Product [id=%d, name=%s, price=%d, quantity=%d]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private int price;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("image") private String image;



    /**
     * Create a product with the given id, name and price
     * @param id The id of the product
     * @param name The name of the product
     * @param price The price of the product

     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Product(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("price") int price, @JsonProperty("quantity") int quantity) {
        this.id = id;
        if(name == ""){
            this.name = "NO_NAME";
        }
        else{
            this.name = name;
        }
        this.price = price;
        this.quantity = quantity;
        this.image = "https://www.wheelco.com/images/product-images/no_image_available.jpeg?resizeid=112&resizeh=600&resizew=600";
    
    }

   
    /**
     * Retrieves the id of the product
     * @return The id of the product
     */
    public int getId() {return id;}

    /**
     * Sets the name of the product - necessary for JSON object to Java object deserialization
     * @param name The name of the product
     */
    public void setName(String name) {this.name = name;}

    public void setImage(String image) {this.image = image;}


    /**
     * Retrieves the name of the product
     * @return The name of the product
     */
    public String getName() {return name;}

    public String getImage() {return image;}



    /**
     * Sets the price of the product - necessary for JSON object to Java object deserialization
     * @param price The price of the product
     */
    public void setPrice(int price) {this.price = price;}

    /**
     * Retrieves the price of the product
     * @return The price of the product
     */
    public int getPrice() {return price;}

    /**
     * Sets the quantity of the product - necessary for JSON object to Java object deserialization
     * @param quantity The quantity of the product
     */
    public void setQuantity(int quantity) {this.quantity = quantity;}

    /**
     * Retrieves the quantity of the product
     * @return The quantity of the product
     */
    public int getQuantity() {return quantity;}



    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,price,quantity);
    }

        /**
     * quick equals test
     */
    public boolean equals(Product input) {
        return (id == input.getId());
    }

}



    

