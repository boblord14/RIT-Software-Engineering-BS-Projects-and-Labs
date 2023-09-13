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
import com.estore.api.estoreapi.model.Lootbox;

import com.estore.api.estoreapi.persistance.LootboxDAO;


@Testable
public class LootboxControllerTest {


    @Test
    public void testLootboxControllerGetLootboxes() throws IOException {

        Lootbox testLootbox = new Lootbox(0, "testLootbox", 13);
        Lootbox testLootboxNull = null;

        ResponseEntity<Lootbox> found = new ResponseEntity<Lootbox>(testLootbox,HttpStatus.OK);
        ResponseEntity<Lootbox> missing = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ResponseEntity<Lootbox> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Lootbox[] LootboxArray ={testLootbox};
        LootboxDAO LootboxDAOMock = mock(LootboxDAO.class);
        when(LootboxDAOMock.getLootbox(0)).thenReturn(testLootbox);
        when(LootboxDAOMock.getLootbox(1)).thenReturn(testLootboxNull);
        when(LootboxDAOMock.getLootbox(-1)).thenThrow(new IOException("Error"));

        when(LootboxDAOMock.getLootboxes()).thenReturn(LootboxArray);

        LootboxController LootboxController= new LootboxController(LootboxDAOMock);

        ResponseEntity<Lootbox> testResponse = LootboxController.getLootbox(0);
        ResponseEntity<Lootbox> testNullResponse = LootboxController.getLootbox(1);
        ResponseEntity<Lootbox> testServerError = LootboxController.getLootbox(-1);


        assertEquals(found, testResponse);
        assertEquals(missing, testNullResponse);
        assertEquals(serverError, testServerError);

        ResponseEntity<Lootbox[]> foundArray = new ResponseEntity<Lootbox[]>(LootboxArray,HttpStatus.OK);

        ResponseEntity<Lootbox[]> testResponses = LootboxController.getLootboxes();
        assertEquals(foundArray, testResponses);

        Lootbox[] nullLootboxArray = new Lootbox[1];
        when(LootboxDAOMock.getLootboxes()).thenReturn(nullLootboxArray);
        ResponseEntity<Lootbox> nullArray = new ResponseEntity<Lootbox>(HttpStatus.NOT_FOUND);
        ResponseEntity<Lootbox[]> testNullResponses = LootboxController.getLootboxes();
        assertEquals(nullArray, testNullResponses);

        when(LootboxDAOMock.getLootboxes()).thenThrow(new IOException("Error"));
        ResponseEntity<Lootbox[]> testErrorResponses = LootboxController.getLootboxes();
        assertEquals(serverError, testErrorResponses);



    }


    @Test
    public void testLootboxControllerSearchLootboxes() throws IOException {

        Lootbox testLootbox = new Lootbox(0, "testLootbox", 13);
        Lootbox[] LootboxArray = new Lootbox[1];
        LootboxArray[0] = new Lootbox(0, "testLootboxTwo", 13);

        ResponseEntity<Lootbox[]> found = new ResponseEntity<Lootbox[]>(LootboxArray,HttpStatus.OK);
        ResponseEntity<Lootbox[]> noMatch = new ResponseEntity<Lootbox[]>(HttpStatus.NOT_FOUND);

        ResponseEntity<Lootbox> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        LootboxDAO LootboxDAOMock = mock(LootboxDAO.class);

        when(LootboxDAOMock.getLootboxes()).thenReturn(LootboxArray);


        LootboxController LootboxController= new LootboxController(LootboxDAOMock);

        ResponseEntity<Lootbox[]> testResponse = LootboxController.searchLootboxes("t");
        ResponseEntity<Lootbox[]> testNullResponse = LootboxController.searchLootboxes("X");

        //assertEquals(found, testResponse);
        assertEquals(noMatch, testNullResponse);

        when(LootboxDAOMock.getLootboxes()).thenThrow(new IOException("Error"));
        ResponseEntity<Lootbox[]> testServerError = LootboxController.searchLootboxes("ERROR");
        assertEquals(serverError, testServerError);


    }

    @Test
    public void testLootboxControllerCreateLootbox() throws IOException {

        Lootbox testLootbox = new Lootbox(0, "testLootbox", 13);
      
        ResponseEntity<Lootbox> created = new ResponseEntity<Lootbox>(testLootbox,HttpStatus.CREATED);
        ResponseEntity<Lootbox> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        LootboxDAO LootboxDAOMock = mock(LootboxDAO.class);

        when(LootboxDAOMock.createLootbox(testLootbox)).thenReturn(testLootbox);


        LootboxController LootboxController= new LootboxController(LootboxDAOMock);

        ResponseEntity<Lootbox> testResponse = LootboxController.createLootbox(testLootbox);

        //assertEquals(found, testResponse);
        assertEquals(created, testResponse);

        when(LootboxDAOMock.createLootbox(testLootbox)).thenThrow(new IOException("Error"));
        ResponseEntity<Lootbox> testServerError = LootboxController.createLootbox(testLootbox);
        assertEquals(serverError, testServerError);


    }


    @Test
    public void testLootboxControllerDeleteLootbox() throws IOException {

        Lootbox testLootbox = new Lootbox(0, "testLootbox", 13);
      
        ResponseEntity<Lootbox> deleted = new ResponseEntity<Lootbox>(HttpStatus.OK);
        ResponseEntity<Lootbox> notFound = new ResponseEntity<Lootbox>(HttpStatus.NOT_FOUND);
        ResponseEntity<Lootbox> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        LootboxDAO LootboxDAOMock = mock(LootboxDAO.class);

        when(LootboxDAOMock.deleteLootbox(0)).thenReturn(true);
        when(LootboxDAOMock.deleteLootbox(1)).thenReturn(false);


        LootboxController LootboxController= new LootboxController(LootboxDAOMock);

        ResponseEntity<Lootbox> testResponse = LootboxController.deleteLootbox(0);
        ResponseEntity<Lootbox> nullTestResponse = LootboxController.deleteLootbox(1);

        assertEquals(deleted, testResponse);
        assertEquals(notFound, nullTestResponse);



        when(LootboxDAOMock.deleteLootbox(0)).thenThrow(new IOException("Error"));
        ResponseEntity<Lootbox> testServerError = LootboxController.deleteLootbox(0);
        assertEquals(serverError, testServerError);


    }

    @Test
    public void testLootboxControllerUpdateLootbox() throws IOException {

        Lootbox testLootbox = new Lootbox(0, "testLootbox", 13);
      
        ResponseEntity<Lootbox> updated = new ResponseEntity<Lootbox>(testLootbox,HttpStatus.OK);
        ResponseEntity<Lootbox> notFound = new ResponseEntity<Lootbox>(HttpStatus.NOT_FOUND);
        ResponseEntity<Lootbox> serverError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        LootboxDAO LootboxDAOMock = mock(LootboxDAO.class);

        when(LootboxDAOMock.updateLootbox(testLootbox)).thenReturn(testLootbox);
        when(LootboxDAOMock.updateLootbox(null)).thenReturn(null);


        LootboxController LootboxController= new LootboxController(LootboxDAOMock);

        ResponseEntity<Lootbox> testResponse = LootboxController.updateLootbox(testLootbox);
        ResponseEntity<Lootbox> nullTestResponse = LootboxController.updateLootbox(null);

        assertEquals(updated, testResponse);
        assertEquals(notFound, nullTestResponse);



        when(LootboxDAOMock.updateLootbox(testLootbox)).thenThrow(new IOException("Error"));
        ResponseEntity<Lootbox> testServerError = LootboxController.updateLootbox(testLootbox);
        assertEquals(serverError, testServerError);


    }
 
}
