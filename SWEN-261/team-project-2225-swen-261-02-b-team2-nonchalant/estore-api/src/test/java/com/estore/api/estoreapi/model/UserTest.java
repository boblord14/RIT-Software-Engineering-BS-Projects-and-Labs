package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the User class
 * 
 * @author Ethan Patterson
 * 
 * praise the heroes api authors for having unit test code thats very easily adaptable to our estore api
 */
@Tag("Model-tier")
public class UserTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "Generic User";
        ArrayList<Product> expected_cart = new ArrayList<>();
        expected_cart.add(new Product(10, "generic product", 11, 12));

        // Invoke
        User user = new User(expected_id,expected_name, expected_cart);

        // Analyze
        assertEquals(expected_id,user.getId());
        assertEquals(expected_name,user.getName());
        assertEquals(expected_cart, user.getCart());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Generic User";
        ArrayList<Product> cart = new ArrayList<>();
        cart.add(new Product(10, "generic product", 11, 12));
        User user = new User(id,name, cart);

        String expected_name = "Updated User";

        // Invoke
        user.setName(expected_name);

        // Analyze
        assertEquals(expected_name,user.getName());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String name = "Generic User";
        ArrayList<Product> cart = new ArrayList<>();
        cart.add(new Product(10, "generic product", 11, 12));
        String expected_string = String.format(User.STRING_FORMAT,id,name,cart);
        User user = new User(id,name, cart);

        // Invoke
        String actual_string = user.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }

    @Test
    public void testCartAdd() {
        // Setup
        int id = 99;
        String name = "Generic User";
        ArrayList<Product> cart = new ArrayList<>();
        cart.add(new Product(10, "generic product", 11, 12));
        User user = new User(id,name, cart);

        ArrayList<Product> expected_cart = new ArrayList<>();
        expected_cart.add(new Product(10, "generic product", 11, 12));

        Product expected_add = new Product(11, "generic added product", 110, 120);
        expected_cart.add(expected_add);

        // Invoke
        user.addToCart(expected_add);

        // Analyze
        assertTrue(expected_cart.get(0).equals(user.getCart().get(0)));
        assertTrue(expected_cart.get(1).equals(user.getCart().get(1)));
    }

    @Test
    public void testCartRemove() {
        // Setup
        int id = 99;
        String name = "Generic User";
        ArrayList<Product> cart = new ArrayList<>();
        cart.add(new Product(10, "generic product", 11, 12));
        cart.add(new Product(11, "generic added product", 110, 120));
        User user = new User(id,name, cart);

        ArrayList<Product> expected_cart = new ArrayList<>();
        expected_cart.add(new Product(11, "generic added product", 110, 120));

        // Invoke
        user.removeFromCart(10);
        // Another funny test that shouldnt do any actual action(its already not there so its removed as is)
        user.removeFromCart(255);

        // Analyze
        assertTrue(expected_cart.get(0).equals(user.getCart().get(0)));
    }
}
