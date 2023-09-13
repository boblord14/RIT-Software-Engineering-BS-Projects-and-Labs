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
import com.estore.api.estoreapi.model.Jersey;

import com.estore.api.estoreapi.persistance.JerseyDAO;

@Testable
public class JerseyControllerTest {
    @Test
    public void testJerseyControllerGetJersey() throws IOException {
        Jersey testJersey = new Jersey(0, "ben", "blue", "green", "jersey", 40, "image", 200);
        Jersey testNullJersey = null;

        ResponseEntity<Jersey> found = new ResponseEntity<Jersey>(testJersey,HttpStatus.OK);
        ResponseEntity<Jersey> missing = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ResponseEntity<Jersey> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Jersey[] JerseyArray = {testJersey};
        JerseyDAO JerseyDAOMock = mock(JerseyDAO.class);
        when(JerseyDAOMock.getJersey(0)).thenReturn(testJersey);
        when(JerseyDAOMock.getJersey(1)).thenReturn(testNullJersey);
        when(JerseyDAOMock.getJersey(-1)).thenThrow(new IOException("Error"));

        when(JerseyDAOMock.getJerseys()).thenReturn(JerseyArray);

        JerseyController JerseyController= new JerseyController(JerseyDAOMock);

        ResponseEntity<Jersey> testResponse = JerseyController.getJersey(0);
        ResponseEntity<Jersey> testNullResponse = JerseyController.getJersey(1);
        ResponseEntity<Jersey> testServerError = JerseyController.getJersey(-1);

        assertEquals(found, testResponse);
        assertEquals(missing, testNullResponse);
        assertEquals(serverError, testServerError);

        ResponseEntity<Jersey[]> foundArray = new ResponseEntity<Jersey[]>(JerseyArray,HttpStatus.OK);

        ResponseEntity<Jersey[]> testResponses = JerseyController.getJerseys();
        assertEquals(foundArray, testResponses);

        Jersey[] nullJerseyArray = new Jersey[1];
        when(JerseyDAOMock.getJerseys()).thenReturn(nullJerseyArray);
        ResponseEntity<Jersey> nullArray = new ResponseEntity<Jersey>(HttpStatus.NOT_FOUND);
        ResponseEntity<Jersey[]> testNullResponses = JerseyController.getJerseys();
        assertEquals(nullArray, testNullResponses);

        when(JerseyDAOMock.getJerseys()).thenThrow(new IOException("Error"));
        ResponseEntity<Jersey[]> testErrorResponses = JerseyController.getJerseys();
        assertEquals(serverError, testErrorResponses);
    }

    @Test
    public void testJerseyControllerSearchJersey() throws IOException {
        Jersey testJersey = new Jersey(0, "testJersey", "blue", "green", "jersey1", 40, "image", 200);
        Jersey[] JerseyArray = new Jersey[1];
        JerseyArray[0] = new Jersey(0, "testJerseyTwo", "blue", "green", "jersey2", 40, "image", 200);

        ResponseEntity<Jersey[]> found = new ResponseEntity<Jersey[]>(JerseyArray,HttpStatus.OK);
        ResponseEntity<Jersey[]> noMatch = new ResponseEntity<Jersey[]>(HttpStatus.NOT_FOUND);

        ResponseEntity<Jersey> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        JerseyDAO JerseyDAOMock = mock(JerseyDAO.class);

        when(JerseyDAOMock.getJerseys()).thenReturn(JerseyArray);

        JerseyController JerseyController= new JerseyController(JerseyDAOMock);

        ResponseEntity<Jersey[]> testResponse = JerseyController.searchJerseys("testJerseyTwo");
        ResponseEntity<Jersey[]> testNullResponse = JerseyController.searchJerseys("X");

        assertEquals(noMatch, testNullResponse);

        when(JerseyDAOMock.getJerseys()).thenThrow(new IOException("Error"));
        ResponseEntity<Jersey[]> testServerError = JerseyController.searchJerseys("ERROR");
        assertEquals(serverError, testServerError);
    }

    @Test
    public void testJerseyControllerCreateJersey() throws IOException {
        Jersey testJersey = new Jersey(0, "testJersey", "blue", "green", "jersey1", 40, "image", 200);
      
        ResponseEntity<Jersey> created = new ResponseEntity<Jersey>(testJersey,HttpStatus.CREATED);
        ResponseEntity<Jersey> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        JerseyDAO JerseyDAOMock = mock(JerseyDAO.class);

        when(JerseyDAOMock.createJersey(testJersey)).thenReturn(testJersey);

        JerseyController JerseyController= new JerseyController(JerseyDAOMock);

        ResponseEntity<Jersey> testResponse = JerseyController.createJersey(testJersey);

        assertEquals(created, testResponse);

        when(JerseyDAOMock.createJersey(testJersey)).thenThrow(new IOException("Error"));
        ResponseEntity<Jersey> testServerError = JerseyController.createJersey(testJersey);
        assertEquals(serverError, testServerError);
    }

    @Test
    public void testJerseyControllerDeleteJersey() throws IOException {
        Jersey testJersey = new Jersey(0, "testJersey", "blue", "green", "jersey1", 40, "image", 200);
      
        ResponseEntity<Jersey> deleted = new ResponseEntity<Jersey>(HttpStatus.OK);
        ResponseEntity<Jersey> notFound = new ResponseEntity<Jersey>(HttpStatus.NOT_FOUND);
        ResponseEntity<Jersey> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        JerseyDAO JerseyDAOMock = mock(JerseyDAO.class);

        when(JerseyDAOMock.deleteJersey(0)).thenReturn(true);
        when(JerseyDAOMock.deleteJersey(1)).thenReturn(false);


        JerseyController JerseyController= new JerseyController(JerseyDAOMock);

        ResponseEntity<Jersey> testResponse = JerseyController.deleteJersey(0);
        ResponseEntity<Jersey> nullTestResponse = JerseyController.deleteJersey(1);

        assertEquals(deleted, testResponse);
        assertEquals(notFound, nullTestResponse);

        when(JerseyDAOMock.deleteJersey(0)).thenThrow(new IOException("Error"));
        ResponseEntity<Jersey> testServerError = JerseyController.deleteJersey(0);
        assertEquals(serverError, testServerError);
    }

    @Test
    public void testJerseyControllerUpdateJersey() throws IOException {
        Jersey testJersey = new Jersey(0, "testJersey", "blue", "green", "jersey1", 40, "image", 200);
      
        ResponseEntity<Jersey> updated = new ResponseEntity<Jersey>(testJersey,HttpStatus.OK);
        ResponseEntity<Jersey> notFound = new ResponseEntity<Jersey>(HttpStatus.NOT_FOUND);
        ResponseEntity<Jersey> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        JerseyDAO JerseyDAOMock = mock(JerseyDAO.class);

        when(JerseyDAOMock.updateJersey(testJersey)).thenReturn(testJersey);
        when(JerseyDAOMock.updateJersey(null)).thenReturn(null);


        JerseyController JerseyController= new JerseyController(JerseyDAOMock);

        ResponseEntity<Jersey> testResponse = JerseyController.updateJersey(testJersey);
        ResponseEntity<Jersey> nullTestResponse = JerseyController.updateJersey(null);

        assertEquals(updated, testResponse);
        assertEquals(notFound, nullTestResponse);

        when(JerseyDAOMock.updateJersey(testJersey)).thenThrow(new IOException("Error"));
        ResponseEntity<Jersey> testServerError = JerseyController.updateJersey(testJersey);
        assertEquals(serverError, testServerError);
    }
}
