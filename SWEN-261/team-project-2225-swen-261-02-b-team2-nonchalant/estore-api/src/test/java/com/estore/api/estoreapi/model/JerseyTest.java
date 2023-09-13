package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Console;
import java.util.ArrayList;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Jersey class
 * 
 * @author Ben Cerrone
 * 
 */
@Tag("Model-tier")
public class JerseyTest {
    @Test
    public void testCreate() {
        // Setup
        int id = 99;
        String creator = "me";
        String primary_color = "blue";
        String secondary_color = "green";
        String expected_name = "Custom Jersey";
        int number = 22;
        String logo = "image";
        int price = 15; 

        // Invoke
        Jersey Jersey1 = new Jersey(id, creator, primary_color, secondary_color, expected_name, number, logo, price);

        // Analyze
        assertEquals(id, Jersey1.getId());
        assertEquals(creator, Jersey1.getCreator());
        assertEquals(primary_color, Jersey1.getPrimary_color());
        assertEquals(secondary_color, Jersey1.getSecondary_color());
        assertEquals(expected_name, Jersey1.getName());
        assertEquals(number, Jersey1.getNumber());
        assertEquals(logo, Jersey1.getLogo());
        assertEquals(price, Jersey1.getPrice());
    }

    @Test
    public void testToString() {
        int id = 99;
        String creator = "me";
        String primary_color = "blue";
        String secondary_color = "green";
        String expected_name = "CustomJersey";
        int number = 22;
        String logo = "image";
        int price = 15; 
        
        String JerseyToString = "Jersey [id = 99, creator=me, primary_color=blue, secondary_color=green, name=CustomJersey, number=22, logo=image, price=15]";

        // Invoke
        Jersey Jersey1 = new Jersey(id, creator, primary_color, secondary_color, expected_name, number, logo, price);

        // Analyze
        String actual = Jersey1.toString();
        assertTrue(JerseyToString.equals(actual));
    }
}
