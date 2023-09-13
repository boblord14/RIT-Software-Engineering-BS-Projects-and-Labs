package com.estore.api.estoreapi.controller;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import com.estore.api.estoreapi.model.Product;

import com.estore.api.estoreapi.persistance.ProductDAO;


@Testable
public class ProductControllerTest {


    @Test
    public void testProductControllerGetProducts() throws IOException {

        Product testProduct = new Product(0, "testProduct", 13, 13);
        Product testProductNull = null;

        ResponseEntity<Product> found = new ResponseEntity<Product>(testProduct,HttpStatus.OK);
        ResponseEntity<Product> missing = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ResponseEntity<Product> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Product[] productArray ={testProduct};
        ProductDAO productDAOMock = mock(ProductDAO.class);
        when(productDAOMock.getProduct(0)).thenReturn(testProduct);
        when(productDAOMock.getProduct(1)).thenReturn(testProductNull);
        when(productDAOMock.getProduct(-1)).thenThrow(new IOException("Error"));

        when(productDAOMock.getProducts()).thenReturn(productArray);

        ProductController productController= new ProductController(productDAOMock);

        ResponseEntity<Product> testResponse = productController.getProduct(0);
        ResponseEntity<Product> testNullResponse = productController.getProduct(1);
        ResponseEntity<Product> testServerError = productController.getProduct(-1);


        assertEquals(found, testResponse);
        assertEquals(missing, testNullResponse);
        assertEquals(serverError, testServerError);

        ResponseEntity<Product[]> foundArray = new ResponseEntity<Product[]>(productArray,HttpStatus.OK);

        ResponseEntity<Product[]> testResponses = productController.getProducts();
        assertEquals(foundArray, testResponses);

        Product[] nullProductArray = new Product[1];
        when(productDAOMock.getProducts()).thenReturn(nullProductArray);
        ResponseEntity<Product> nullArray = new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        ResponseEntity<Product[]> testNullResponses = productController.getProducts();
        assertEquals(nullArray, testNullResponses);

        when(productDAOMock.getProducts()).thenThrow(new IOException("Error"));
        ResponseEntity<Product[]> testErrorResponses = productController.getProducts();
        assertEquals(serverError, testErrorResponses);



    }


    @Test
    public void testProductControllerSearchProducts() throws IOException {

        Product testProduct = new Product(0, "testProduct", 13, 13);
        Product[] productArray = new Product[1];
        productArray[0] = new Product(0, "testProductTwo", 13, 13);

        ResponseEntity<Product[]> found = new ResponseEntity<Product[]>(productArray,HttpStatus.OK);
        ResponseEntity<Product[]> noMatch = new ResponseEntity<Product[]>(HttpStatus.NOT_FOUND);

        ResponseEntity<Product> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        ProductDAO productDAOMock = mock(ProductDAO.class);

        when(productDAOMock.getProducts()).thenReturn(productArray);


        ProductController productController= new ProductController(productDAOMock);

        ResponseEntity<Product[]> testResponse = productController.searchProducts("t");
        ResponseEntity<Product[]> testNullResponse = productController.searchProducts("X");

        //assertEquals(found, testResponse);
        assertEquals(noMatch, testNullResponse);

        when(productDAOMock.getProducts()).thenThrow(new IOException("Error"));
        ResponseEntity<Product[]> testServerError = productController.searchProducts("ERROR");
        assertEquals(serverError, testServerError);


    }

    @Test
    public void testProductControllerCreateProduct() throws IOException {

        Product testProduct = new Product(0, "testProduct", 13, 13);
      
        ResponseEntity<Product> created = new ResponseEntity<Product>(testProduct,HttpStatus.CREATED);
        ResponseEntity<Product> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        ProductDAO productDAOMock = mock(ProductDAO.class);

        when(productDAOMock.createProduct(testProduct)).thenReturn(testProduct);


        ProductController productController= new ProductController(productDAOMock);

        ResponseEntity<Product> testResponse = productController.createProduct(testProduct);

        //assertEquals(found, testResponse);
        assertEquals(created, testResponse);

        when(productDAOMock.createProduct(testProduct)).thenThrow(new IOException("Error"));
        ResponseEntity<Product> testServerError = productController.createProduct(testProduct);
        assertEquals(serverError, testServerError);


    }


    @Test
    public void testProductControllerDeleteProduct() throws IOException {

        Product testProduct = new Product(0, "testProduct", 13, 13);
      
        ResponseEntity<Product> deleted = new ResponseEntity<Product>(HttpStatus.OK);
        ResponseEntity<Product> notFound = new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        ResponseEntity<Product> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        ProductDAO productDAOMock = mock(ProductDAO.class);

        when(productDAOMock.deleteProduct(0)).thenReturn(true);
        when(productDAOMock.deleteProduct(1)).thenReturn(false);


        ProductController productController= new ProductController(productDAOMock);

        ResponseEntity<Product> testResponse = productController.deleteProduct(0);
        ResponseEntity<Product> nullTestResponse = productController.deleteProduct(1);

        assertEquals(deleted, testResponse);
        assertEquals(notFound, nullTestResponse);



        when(productDAOMock.deleteProduct(0)).thenThrow(new IOException("Error"));
        ResponseEntity<Product> testServerError = productController.deleteProduct(0);
        assertEquals(serverError, testServerError);


    }

    @Test
    public void testProductControllerUpdateProduct() throws IOException {

        Product testProduct = new Product(0, "testProduct", 13, 13);
      
        ResponseEntity<Product> updated = new ResponseEntity<Product>(testProduct,HttpStatus.OK);
        ResponseEntity<Product> notFound = new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        ResponseEntity<Product> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        ProductDAO productDAOMock = mock(ProductDAO.class);

        when(productDAOMock.updateProduct(testProduct)).thenReturn(testProduct);
        when(productDAOMock.updateProduct(null)).thenReturn(null);


        ProductController productController= new ProductController(productDAOMock);

        ResponseEntity<Product> testResponse = productController.updateProduct(testProduct);
        ResponseEntity<Product> nullTestResponse = productController.updateProduct(null);

        assertEquals(updated, testResponse);
        assertEquals(notFound, nullTestResponse);



        when(productDAOMock.updateProduct(testProduct)).thenThrow(new IOException("Error"));
        ResponseEntity<Product> testServerError = productController.updateProduct(testProduct);
        assertEquals(serverError, testServerError);


    }
 
}
