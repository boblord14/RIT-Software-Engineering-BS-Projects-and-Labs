package com.estore.api.estoreapi.persistance;

import org.apache.catalina.webresources.Cache;
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
import com.estore.api.estoreapi.model.Jersey;

@Testable
public class JerseyFileDAOTest {
    JerseyFileDAO JerseyFileDAO; 
    Jersey[] testJerseys; 
    ObjectMapper mockObjectMapper; 

    @BeforeEach
    public void setupJerseyFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class); 
        testJerseys = new Jersey[4]; 
        testJerseys[0] = new Jersey(0, "ben", "blue", "green", "jersey", 40, "image", 200);
        testJerseys[1] = new Jersey(1, "ben", "blue", "green", "jersey", 40, "image", 200);
        testJerseys[2] = new Jersey(2, "ben", "blue", "green", "jersey", 40, "image", 200);
        testJerseys[3] = new Jersey(3, null, "blue", "green", "jersey", 40, "image", 200);
        
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"), Jersey[].class))
                .thenReturn(testJerseys);
        JerseyFileDAO = new JerseyFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetJerseys() {
        try {
            Jersey[] Jerseys = JerseyFileDAO.getJerseys();
            assertEquals(testJerseys[0], Jerseys[0]);
            assertEquals(testJerseys[1], Jerseys[1]);
            assertEquals(testJerseys[2], Jerseys[2]);
            assertEquals(testJerseys[3], Jerseys[3]);
        } catch (Exception e) {
        }
    }

    @Test
    public void testFindJerseys() {
        try {
        Jersey[] Jerseys = JerseyFileDAO.findJerseys("ben");

        assertEquals(Jerseys[0], testJerseys[0]); 
        assertEquals(Jerseys[1], testJerseys[2]); 
        } catch (Exception e) {
        }
    }
    @Test
    public void testFindEmptyJerseys() {
        try {
            Jersey[] j = JerseyFileDAO.findJerseys("no");
            Jersey[] empty = new Jersey[0];

            assertEquals(j, empty); 
        } catch (Exception e) {}
    }

    @Test
    public void testDeleteJersey() {
        try {
        boolean result = assertDoesNotThrow(() -> JerseyFileDAO.deleteJersey(2),
                            "Unexpected exception thrown");

        // Analyze
        assertEquals(result, true);
        assertEquals(JerseyFileDAO.jerseys.size(), testJerseys.length - 1);
        } catch (Exception e) {}
    }

    @Test
    public void testCreateJersey() {
        try {
        Jersey Jersey = new Jersey(4, "ben", "blue", "green", "jersey", 40, "image", 200);

        // Invoke
        Jersey result = assertDoesNotThrow(() -> JerseyFileDAO.createJersey(Jersey),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        // New Jersey will have ID of the greatest ID + 1. 
        Jersey actual = JerseyFileDAO.getJersey(4);
        assertEquals(actual.getId(), 4);
        assertEquals(actual.getName(), Jersey.getName());
        assertEquals(actual.getPrice(), Jersey.getPrice());
        } catch (Exception e) {}
    }

    @Test
    public void testUpdateJersey() {
        try {
        Jersey Jersey = new Jersey(2, "ven", "blue", "green", "jersey", 40, "image", 200);

        // Invoke
        Jersey result = assertDoesNotThrow(() -> JerseyFileDAO.updateJersey(Jersey),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Jersey actual = JerseyFileDAO.getJersey(Jersey.getId());
        assertEquals(actual, Jersey);
        } catch (Exception e) {}
    }

    @Test
    public void testGetJerseyNotFound() {
        try {
        Jersey Jersey = JerseyFileDAO.getJersey(50);

        // Analyze
        assertEquals(Jersey, null);
        } catch (Exception e) {}
    }

    @Test
    public void testUpdateJerseyNotFound() {
        try {
        Jersey Jersey = new Jersey(20, "ven", "blue", "green", "jersey", 40, "image", 200);

        // Invoke
        Jersey result = assertDoesNotThrow(() -> JerseyFileDAO.updateJersey(Jersey),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
        } catch (Exception e) {}
    }

    @Test
    public void testDeleteJerseyNotFound() {
        try {
        boolean result = assertDoesNotThrow(() -> JerseyFileDAO.deleteJersey(50),
                                                "Unexpected exception thrown");

        assertEquals(result,false);
        assertEquals(JerseyFileDAO.jerseys.size(),testJerseys.length);
        } catch (Exception e) {}
    }

}
