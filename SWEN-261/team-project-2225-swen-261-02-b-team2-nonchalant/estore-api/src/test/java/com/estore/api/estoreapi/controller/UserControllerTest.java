package com.estore.api.estoreapi.controller;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle.Control;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistance.ProductDAO;
import com.estore.api.estoreapi.persistance.UserDAO;


@Testable
public class UserControllerTest {
    @Test
    public void testUserControllerGetUsers() throws IOException {
        User testUser = new User(0, "USER", new ArrayList<>());
        User testUserNull = null;

        ResponseEntity<User> found = new ResponseEntity<User>(testUser,HttpStatus.OK);
        ResponseEntity<User> missing = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ResponseEntity<User> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        User[] userArray = {testUser};
        UserDAO userDAOMock = mock(UserDAO.class);
        when(userDAOMock.getUser(0)).thenReturn(testUser);
        when(userDAOMock.getUser(1)).thenReturn(testUserNull);
        when(userDAOMock.getUser(-1)).thenThrow(new IOException("Error"));

        when(userDAOMock.getUsers()).thenReturn(userArray);

        UserController userController = new UserController(userDAOMock);

        ResponseEntity<User> testResponse = userController.getUser(0);
        ResponseEntity<User> testNullResponse = userController.getUser(1);
        ResponseEntity<User> testServerError = userController.getUser(-1);


        assertEquals(found, testResponse);
        assertEquals(missing, testNullResponse);
        assertEquals(serverError, testServerError);

        ResponseEntity<User[]> foundArray = new ResponseEntity<User[]>(userArray,HttpStatus.OK);

        ResponseEntity<User[]> testResponses = userController.getUsers();
        assertEquals(foundArray, testResponses);

        User[] nullUserArray = new User[1];
        when(userDAOMock.getUsers()).thenReturn(nullUserArray);
        ResponseEntity<User> nullArray = new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        ResponseEntity<User[]> testNullResponses = userController.getUsers();
        assertEquals(nullArray, testNullResponses);

        when(userDAOMock.getUsers()).thenThrow(new IOException("Error"));
        ResponseEntity<User[]> testErrorResponses = userController.getUsers();
        assertEquals(serverError, testErrorResponses);
    }
 
    @Test
    public void testUserControllerSearchUsers() throws IOException {
        User testUser = new User(0, "a", new ArrayList<>());
        User[] userArray = new User[2];
        userArray[0] = new User(1, "Ben", new ArrayList<>());
        userArray[1] = new User(2, "Ben", new ArrayList<>());

        ResponseEntity<User[]> found = new ResponseEntity<User[]>(userArray,HttpStatus.OK);
        ResponseEntity<User[]> noMatch = new ResponseEntity<User[]>(HttpStatus.NOT_FOUND);

        ResponseEntity<User> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        UserDAO userDAOMock = mock(UserDAO.class);

        when(userDAOMock.getUsers()).thenReturn(userArray);


        UserController userController= new UserController(userDAOMock);

        ResponseEntity<User[]> testResponse = userController.searchUsers("B");
        ResponseEntity<User[]> testNullResponse = userController.searchUsers("X");

        //assertEquals(found, testResponse);
        assertEquals(noMatch, testNullResponse);

        when(userDAOMock.getUsers()).thenThrow(new IOException("Error"));
        ResponseEntity<User[]> testServerError = userController.searchUsers("ERROR");
        assertEquals(serverError, testServerError);
    }

    @Test
    public void testUserControllerCreateUser() throws IOException {
        User testUser = new User(0, "testUser", new ArrayList<>());
      
        ResponseEntity<User> created = new ResponseEntity<User>(testUser,HttpStatus.CREATED);
        ResponseEntity<User> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        UserDAO userDAOMock = mock(UserDAO.class);

        when(userDAOMock.createUser(testUser)).thenReturn(testUser);


        UserController userController = new UserController(userDAOMock);

        ResponseEntity<User> testResponse = userController.createUser(testUser);

        //assertEquals(found, testResponse);
        assertEquals(created, testResponse);

        when(userDAOMock.createUser(testUser)).thenThrow(new IOException("Error"));
        ResponseEntity<User> testServerError = userController.createUser(testUser);
        assertEquals(serverError, testServerError);
    }

    @Test
    public void testUserControllerDeleteUser() throws IOException {
        User testUser = new User(0, "testProduct", new ArrayList<>());
      
        ResponseEntity<User> deleted = new ResponseEntity<User>(HttpStatus.OK);
        ResponseEntity<User> notFound = new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        ResponseEntity<User> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        UserDAO userDAOMock = mock(UserDAO.class);

        when(userDAOMock.deleteUser(0)).thenReturn(true);
        when(userDAOMock.deleteUser(1)).thenReturn(false);


        UserController userController= new UserController(userDAOMock);

        ResponseEntity<User> testResponse = userController.deleteUser(0);
        ResponseEntity<User> nullTestResponse = userController.deleteUser(1);

        assertEquals(deleted, testResponse);
        assertEquals(notFound, nullTestResponse);



        when(userDAOMock.deleteUser(0)).thenThrow(new IOException("Error"));
        ResponseEntity<User> testServerError = userController.deleteUser(0);
        assertEquals(serverError, testServerError);
    }

    @Test
    public void testUserControllerUpdateUser() throws IOException {
        User testProduct = new User(0, "testUser", new ArrayList<>());
      
        ResponseEntity<User> updated = new ResponseEntity<User>(testProduct,HttpStatus.OK);
        ResponseEntity<User> notFound = new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        ResponseEntity<User> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        UserDAO userDAOMock = mock(UserDAO.class);

        when(userDAOMock.updateUser(testProduct)).thenReturn(testProduct);
        when(userDAOMock.updateUser(null)).thenReturn(null);


        UserController userController= new UserController(userDAOMock);

        ResponseEntity<User> testResponse = userController.updateUser(testProduct);
        ResponseEntity<User> nullTestResponse = userController.updateUser(null);

        assertEquals(updated, testResponse);
        assertEquals(notFound, nullTestResponse);

        when(userDAOMock.updateUser(testProduct)).thenThrow(new IOException("Error"));
        ResponseEntity<User> testServerError = userController.updateUser(testProduct);
        assertEquals(serverError, testServerError);
    }
}
