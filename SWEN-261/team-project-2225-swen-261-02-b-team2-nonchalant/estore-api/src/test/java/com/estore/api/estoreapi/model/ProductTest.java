package com.estore.api.estoreapi.model;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;


@Testable
public class ProductTest {

    @Test
    public void testGetters() {
    Product product = new Product(5, "TestProduct", 3 ,4);
    int testID = product.getId();
    int testPrice = product.getPrice();
    int testQuantity = product.getQuantity();
    String testName = product.getName();

    assertEquals(5, testID);
    assertEquals(3, testPrice);
    assertEquals(4, testQuantity);
    assertEquals("TestProduct", testName);

    }

    @Test
    public void testSetters() {
    Product product = new Product(10, "BADNAME", 0 ,0);
    product.setPrice(100);
    int testPrice = product.getPrice();
    product.setQuantity(12);
    int testQuantity = product.getQuantity();
    product.setName("TestProduct");
    String testName = product.getName();

    assertEquals(100, testPrice);
    assertEquals(12, testQuantity);
    assertEquals("TestProduct", testName);

    }

    @Test
    public void testToString() {
    Product product = new Product(15, "exampleProduct", 12 ,4);
    assertEquals("Product [id=15, name=exampleProduct, price=12, quantity=4]", product.toString());
    }

    @Test
    public void testProductEquality(){
        Product product1 = new Product(15, "exampleProduct", 12, 4);
        Product product2 = new Product(15, "exampleProduct", 12, 4);

        boolean expected = false;
        boolean actual = (product1 == product2);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyName(){
        Product product = new Product(15, "", 12, 4);
        String expected = "NO_NAME";
        String actual = product.getName();

        assertEquals(expected, actual);

    }

    @Test
    public void testEquals() {
    Product product = new Product(15, "exampleProduct", 12 ,4);
    Product product2 = new Product(15, "exampleProduct", 234 ,5678);
    Product product3 = new Product(51, "exampleProduct", 345 ,1234);
    assertTrue(product.equals(product2));
    assertFalse(product.equals(product3));
    }
}
