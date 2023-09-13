package com.estore.api.estoreapi.persistance;


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
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;

import com.estore.api.estoreapi.persistance.ProductDAO;
import com.estore.api.estoreapi.persistance.UserDAO;

import com.estore.api.estoreapi.persistance.ProductFileDAO;
import com.estore.api.estoreapi.persistance.UserFileDAO;

/**
 * Test the User File DAO class
 * 
 * @author Ethan Patterson
 * 
 * big ty to whoever made the Users api and left in the full suite of tests for me to yoink
 */
@Tag("Persistence-tier")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[3];
        ArrayList<Product> testCart = new ArrayList<Product>();

        testCart.add(new Product(105, "test product 1", 10, 1));
        testUsers[0] = new User(5, "unique test user", testCart);
        testCart.remove(0);

        testCart.add(new Product(107, "test product 2", 20, 2));
        testUsers[1] = new User(7, "original test user", testCart);
        testCart.remove(0);

        testCart.add(new Product(106, "test product 3", 30, 3));
        testUsers[2] = new User(6, "special test user", testCart);
        testCart.remove(0);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the user array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),User[].class))
                .thenReturn(testUsers);
        userFileDAO = new UserFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetUsers() {
        // Invoke
        User[] users = userFileDAO.getUsers();

        // Analyze
        assertEquals(users.length,testUsers.length);
        assertEquals(users[0],testUsers[0]);
        assertEquals(users[1],testUsers[2]);
        assertEquals(users[2],testUsers[1]);

    }

    @Test
    public void testFindUsers() {
        // Invoke
        User[] users = userFileDAO.findUsers("al");

        // Analyze
        assertEquals(users.length,2);
        assertEquals(users[0],testUsers[2]);
        assertEquals(users[1],testUsers[1]);
    }

    @Test
    public void testGetuser() {
        // Invoke
        User User = userFileDAO.getUser(5);

        // Analzye
        assertEquals(User,testUsers[0]);
    }

    @Test
    public void testDeleteUser() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(5),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test Users array - 1 (because of the delete)
        // Because Users attribute of UserFileDAO is package private
        // we can access it directly
        assertEquals(userFileDAO.users.size(),testUsers.length-1);
    }

    @Test
    public void testCreateUser() {
        // Setup
        ArrayList<Product> tempCart = new ArrayList<Product>();
        tempCart.add(new Product(108, "create test product 1", 40, 4));
        User User = new User(8, "create test user 1", tempCart);

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.createUser(User),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(User.getId());
        assertEquals(actual.getId(),User.getId());
        assertEquals(actual.getName(),User.getName());
    }

    @Test
    public void testUpdateUser() {
        // Setup
        ArrayList<Product> tempCart = new ArrayList<Product>();
        tempCart.add(new Product(105, "update test product 1", 50, 5));
        User User = new User(5, "update test user", tempCart);

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(User),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(User.getId());
        assertEquals(actual,User);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(User[].class));

        ArrayList<Product> tempCart = new ArrayList<Product>();
        tempCart.add(new Product(105, "test product 1", 10, 1));
        User User = new User(8, "unique test user", tempCart);

        assertThrows(IOException.class,
                        () -> userFileDAO.createUser(User),
                        "IOException not thrown");
    }

    @Test
    public void testGetUserNotFound() {
        // Invoke
        User User = userFileDAO.getUser(4);

        // Analyze
        assertEquals(User,null);
    }

    @Test
    public void testDeleteUserNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(4),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(userFileDAO.users.size(),testUsers.length);
    }

    @Test
    public void testUpdateUserNotFound() {
        // Setup
        ArrayList<Product> tempCart = new ArrayList<Product>();
        tempCart.add(new Product(105, "update not found test product 1", 50, 5));
        User User = new User(4, "update not found test user", tempCart);

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(User),
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
        // from the UserFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),User[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new UserFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}
