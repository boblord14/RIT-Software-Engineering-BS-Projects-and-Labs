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
import com.estore.api.estoreapi.model.Lootbox;

/**
 * Tests the Lootbox File DAO class. 
 * 
 * @author Zach Conway, zrc5943@rit.edu
 */
@Testable
public class LootboxFileDAOTest {
    LootboxFileDAO LootboxFileDAO; 
    Lootbox[] testLootboxes; 
    ObjectMapper mockObjectMapper; 

    @BeforeEach
    public void setupLootboxFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class); 
        testLootboxes = new Lootbox[4]; 
        testLootboxes[0] = new Lootbox(1, "Shoes", 30); 
        testLootboxes[1] = new Lootbox(2, "Volleyball", 15); 
        testLootboxes[2] = new Lootbox(5, "Shirt", 20); 
        testLootboxes[3] = new Lootbox(3, "Bat", 60);
        
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"), Lootbox[].class))
                .thenReturn(testLootboxes);
        LootboxFileDAO = new LootboxFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetLootboxes() {
        // Invoke
        Lootbox[] Lootboxes = LootboxFileDAO.getLootboxes();

        // Analyze
        assertEquals(testLootboxes[0], Lootboxes[0]);
        assertEquals(testLootboxes[1], Lootboxes[1]);
        assertEquals(testLootboxes[2], Lootboxes[3]);
        assertEquals(testLootboxes[3], Lootboxes[2]);
    }

    @Test
    public void testFindLootboxes() {
        // Invoke
        Lootbox[] Lootboxes = LootboxFileDAO.findLootboxes("Sh");
        
        // Analyze
        assertEquals(Lootboxes[0], testLootboxes[0]); 
        assertEquals(Lootboxes[1], testLootboxes[2]); 
    }

    @Test
    public void testDeleteLootbox() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> LootboxFileDAO.deleteLootbox(2),
                            "Unexpected exception thrown");

        // Analyze
        assertEquals(result, true);
        assertEquals(LootboxFileDAO.lootboxes.size(), testLootboxes.length - 1);
    }

    @Test
    public void testCreateLootbox() {
        // Setup
        Lootbox Lootbox = new Lootbox(4, "Athletic Socks", 12);

        // Invoke
        Lootbox result = assertDoesNotThrow(() -> LootboxFileDAO.createLootbox(Lootbox),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        // New Lootbox will have ID of the greatest ID + 1. 
        Lootbox actual = LootboxFileDAO.getLootbox(6);
        assertEquals(actual.getId(), 6);
        assertEquals(actual.getName(), Lootbox.getName());
        assertEquals(actual.getPrice(), Lootbox.getPrice());
    }

    @Test
    public void testUpdateLootbox() {
        // Setup
        Lootbox Lootbox = new Lootbox(2, "Knee Brace", 20);

        // Invoke
        Lootbox result = assertDoesNotThrow(() -> LootboxFileDAO.updateLootbox(Lootbox),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Lootbox actual = LootboxFileDAO.getLootbox(Lootbox.getId());
        assertEquals(actual, Lootbox);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Lootbox[].class));

        Lootbox Lootbox = new Lootbox(2, "Shirt", 15);

        assertThrows(IOException.class,
                        () -> LootboxFileDAO.createLootbox(Lootbox),
                        "IOException not thrown");
    }

    @Test
    public void testNegativePriceException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Lootbox[].class));

        Lootbox Lootbox = new Lootbox(5, "Shirt", -15);
        Lootbox updateLootbox = new Lootbox(1, "Shoes", -30); 

        assertThrows(IOException.class,
                        () -> LootboxFileDAO.createLootbox(Lootbox),
                        "IllegalArgumentException not thrown on create Lootbox");

        assertThrows(IOException.class,
                        () -> LootboxFileDAO.updateLootbox(updateLootbox),
                        "IllegalArgumentException not thrown on update Lootbox");
    }

    @Test
    public void testGetLootboxNotFound() {
        // Invoke
        Lootbox Lootbox = LootboxFileDAO.getLootbox(50);

        // Analyze
        assertEquals(Lootbox, null);
    }

    @Test
    public void testDeleteLootboxNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> LootboxFileDAO.deleteLootbox(50),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(LootboxFileDAO.lootboxes.size(),testLootboxes.length);
    }

    @Test
    public void testUpdateLootboxNotFound() {
        // Setup
        Lootbox Lootbox = new Lootbox(10, "Pizza", 9);

        // Invoke
        Lootbox result = assertDoesNotThrow(() -> LootboxFileDAO.updateLootbox(Lootbox),
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
        // from the LootboxFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Lootbox[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new LootboxFileDAO("doesnt_matter.txt", mockObjectMapper),
                        "IOException not thrown");
    }
}
