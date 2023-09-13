package com.estore.api.estoreapi.persistance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test; 
import org.junit.platform.commons.annotation.Testable; 
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.Product;

/**
 * Tests the Product File DAO class. 
 * 
 * @author Zach Conway, zrc5943@rit.edu
 */
@Testable
public class ProductFileDAOTest {
    ProductFileDAO productFileDAO; 
    Product[] testProducts; 
    ObjectMapper mockObjectMapper; 

    @BeforeEach
    public void setupProductFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class); 
        testProducts = new Product[4]; 
        testProducts[0] = new Product(1, "Shoes", 30, 4); 
        testProducts[1] = new Product(2, "Volleyball", 15, 2); 
        testProducts[2] = new Product(5, "Shirt", 20, 5); 
        testProducts[3] = new Product(3, "Bat", 60, 30);
        
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"), Product[].class))
                .thenReturn(testProducts);
        productFileDAO = new ProductFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetProducts() {
        // Invoke
        Product[] products = productFileDAO.getProducts();

        // Analyze
        assertEquals(testProducts[0], products[0]);
        assertEquals(testProducts[1], products[1]);
        assertEquals(testProducts[2], products[3]);
        assertEquals(testProducts[3], products[2]);
    }

    @Test
    public void testFindProducts() {
        // Invoke
        Product[] products = productFileDAO.findProducts("Sh");
        
        // Analyze
        assertEquals(products[0], testProducts[0]); 
        assertEquals(products[1], testProducts[2]); 
    }

    @Test
    public void testDeleteProduct() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> productFileDAO.deleteProduct(2),
                            "Unexpected exception thrown");

        // Analyze
        assertEquals(result, true);
        assertEquals(productFileDAO.products.size(), testProducts.length - 1);
    }

    @Test
    public void testCreateProduct() {
        // Setup
        Product product = new Product(4, "Athletic Socks", 12, 3);

        // Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.createProduct(product),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        // New Product will have ID of the greatest ID + 1. 
        Product actual = productFileDAO.getProduct(6);
        assertEquals(actual.getId(), 6);
        assertEquals(actual.getName(), product.getName());
        assertEquals(actual.getPrice(), product.getPrice());
        assertEquals(actual.getQuantity(), product.getQuantity());
    }

    @Test
    public void testUpdateProduct() {
        // Setup
        Product product = new Product(2, "Knee Brace", 20, 1);

        // Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.updateProduct(product),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Product actual = productFileDAO.getProduct(product.getId());
        assertEquals(actual, product);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Product[].class));

        Product product = new Product(2, "Shirt", 15, 10);

        assertThrows(IOException.class,
                        () -> productFileDAO.createProduct(product),
                        "IOException not thrown");
    }

    @Test
    public void testNegativePriceException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Product[].class));

        Product product = new Product(5, "Shirt", -15, 10);
        Product updateProduct = new Product(1, "Shoes", -30, 4); 

        assertThrows(IllegalArgumentException.class,
                        () -> productFileDAO.createProduct(product),
                        "IllegalArgumentException not thrown on create product");

        assertThrows(IllegalArgumentException.class,
                        () -> productFileDAO.updateProduct(updateProduct),
                        "IllegalArgumentException not thrown on update product");
    }

    @Test
    public void testGetProductNotFound() {
        // Invoke
        Product product = productFileDAO.getProduct(50);

        // Analyze
        assertEquals(product, null);
    }

    @Test
    public void testDeleteProductNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> productFileDAO.deleteProduct(50),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(productFileDAO.products.size(),testProducts.length);
    }

    @Test
    public void testUpdateProductNotFound() {
        // Setup
        Product product = new Product(10, "Pizza", 9, 5);

        // Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.updateProduct(product),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the ProductFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Product[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new ProductFileDAO("doesnt_matter.txt", mockObjectMapper),
                        "IOException not thrown");
    }
}
