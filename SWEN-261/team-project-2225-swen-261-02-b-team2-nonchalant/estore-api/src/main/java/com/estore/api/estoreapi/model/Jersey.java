package com.estore.api.estoreapi.model;
import java.util.Objects; 
import java.util.logging.Logger; 

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Jersey entity. 
 * 
 * @author Zach Conway
 */
public class Jersey {
    private static final Logger LOG = Logger.getLogger(Product.class.getName()); 
    static final String STRING_FORMAT = "Jersey [id = %d, creator=%s, primary_color=%s, secondary_color=%s, name=%s, number=%d, logo=%s, price=%d]"; 

    @JsonProperty("id") int id; 
    @JsonProperty("creator") private String creator; 
    @JsonProperty("primary_color") private String primary_color; 
    @JsonProperty("secondary_color") private String secondary_color; 
    @JsonProperty("name") private String name; 
    @JsonProperty("number") private int number; 
    @JsonProperty("logo") private String logo; 
    @JsonProperty("price") private int price; 

    /**
     * Creates a jersey with the given properties. 
     * @param creator The username of the user who made the product. 
     * @param primary_color The primary color of the jersey. 
     * @param secondary_color The secondary color of the jersey. 
     * @param name The name of the person the jersey is meant for. 
     * @param number The number of the player the jersey is meant for. 
     * @param logo_url The url of the logo image on the jersey.
     * @param price The cost of the jersey.
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Jersey( @JsonProperty("id") int id, 
        @JsonProperty("creator") String creator,
        @JsonProperty("primary_color") String primary_color,
        @JsonProperty("secondary_color") String secondary_color,
        @JsonProperty("name") String name,
        @JsonProperty("number") int number,
        @JsonProperty("logo") String logo,
        @JsonProperty("price") int price ) 
    {
        this.id = id; 
        this.creator = creator; 
        this.primary_color = primary_color; 
        this.secondary_color = secondary_color; 
        this.name = name; 
        this.number = number; 
        this.logo = logo; 
        this.price = price; 
    }

    /**
     * Retrieves the id of the jersey. 
     * @return The id of the jersey. 
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the creator of the jersey. 
     * @return The creator of the jersey. 
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Retrieves the primary color of the jersey.
     * @return The primary color of the jersey. 
     */
    public String getPrimary_color() {
        return primary_color;
    }

    /**
     * Retrieves the secondary color of the jersey.
     * @return The secondary color of the jersey. 
     */
    public String getSecondary_color() {
        return secondary_color;
    }

    /**
     * Retrieves the name on the jersey.
     * @return The name on the jersey. 
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the number on the jersey.
     * @return The number on the jersey. 
     */
    public int getNumber() {
        return number;
    }

    /**
     * Retrieves the url of the logo on the jersey.
     * @return The url of the logo on the jersey. 
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Retrieves the price of the jersey. 
     * @return The price of the jersey. 
     */
    public int getPrice() {
        return price; 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id,creator, primary_color, secondary_color, name, number, logo, price); 
    }
}
