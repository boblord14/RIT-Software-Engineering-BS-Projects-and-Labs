package com.estore.api.estoreapi.model;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;


@Testable
public class LootboxTest {

    @Test
    public void testGetters() {
        Lootbox lootbox = new Lootbox(5, "lootbox", 400);
        int testID = lootbox.getId();
        int testPrice = lootbox.getPrice();
        String testName = lootbox.getName();

        assertEquals(5, testID);
        assertEquals(400, testPrice);
        assertEquals("lootbox", testName);

    }

    @Test
    public void testSetName(){
        Lootbox lootbox = new Lootbox(5, "BADNAME", 400);
        lootbox.setName("GOODNAME");
        String expected = "GOODNAME";
        String actual = lootbox.getName();

        assertEquals(expected, actual);

    }


    @Test
    public void testToString() {
    Lootbox lootbox = new Lootbox(15, "exampleLootbox", 12);
    assertEquals("Lootbox [id=15, name=exampleLootbox, price=12, pool=[]]", lootbox.toString());
    }

    @Test
    public void testLootboxEquality(){
        Lootbox lootbox1 = new Lootbox(15, "exampleLootbox", 12);
        Lootbox lootbox2 = new Lootbox(15, "exampleLootbox", 12);

        boolean expected = false;
        boolean actual = (lootbox1 == lootbox2);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyName(){
        Lootbox lootbox = new Lootbox(15, "", 12);
        String expected = "";
        String actual = lootbox.getName();

        assertEquals(expected, actual);

    }

    @Test
    public void testAddToPool(){
        Lootbox lootbox = new Lootbox(5, "Lootbox", 15);
        Product product = new Product(1, "Product", 5, 200);
        ArrayList<Product> expected = new ArrayList<Product>();
        expected.add(product);
        lootbox.addToPool(product);

        assertEquals(expected, lootbox.getPool());
    }

    @Test
    public void testRemoveFromPool(){
        Lootbox lootbox = new Lootbox(5, "Lootbox", 15);
        Product product = new Product(1, "Product", 5, 200);
        lootbox.addToPool(product);
        lootbox.removeFromPool(1);

        ArrayList<Product> expected = new ArrayList<>();

        assertEquals(expected, lootbox.getPool());
    }

}
