package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.estore.api.estoreapi.model.Lootbox;
import com.estore.api.estoreapi.persistance.LootboxDAO;
/**
 * Handles the REST API requests for the Lootbox resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Ethan Patterson
 */
@RestController
@RequestMapping("lootboxes")
public class LootboxController {
    private static final Logger LOG = Logger.getLogger(LootboxController.class.getName());
    private LootboxDAO lootboxDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param lootboxDao The {@link lootboxDao Lootbox Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public LootboxController(LootboxDAO lootboxDao) {
        this.lootboxDao = lootboxDao;
    }
    

    /**
     * Responds to the GET request for a {@linkplain Lootbox lootbox} for the given id
     * 
     * @param id The id used to locate the {@link Lootbox lootbox}
     * 
     * @return ResponseEntity with {@link Lootbox lootbox} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lootbox> getLootbox(@PathVariable int id) {
        LOG.info("GET /lootboxes/" + id);
        try {
            Lootbox lootbox = lootboxDao.getLootbox(id);
            if (lootbox != null)
                return new ResponseEntity<Lootbox>(lootbox,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Responds to the GET request for multiple {@linkplain Lootbox lootbox}
     * 
     * 
     * @return ResponseEntity with {@link Lootbox lootbox} objects and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Lootbox[]> getLootboxes() {
        LOG.info("GET /lootboxes/");
        try {
            Lootbox[] lootboxes = lootboxDao.getLootboxes();
            if (lootboxes[0] != null)
                return new ResponseEntity<Lootbox[]>(lootboxes, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Responds to the GET request for all {@linkplain Lootbox lootboxes} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Lootbox Lootboxes}
     * 
     * @return ResponseEntity with array of {@link Lootbox Lootbox} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all Lootboxes that contain the text "ma"
     * GET http://localhost:8080/Lootboxes/?name=ma
     */
    @GetMapping("/")
    public ResponseEntity<Lootbox[]> searchLootboxes(@RequestParam String name) {
        LOG.info("GET /Lootboxes/?name="+name);
        try {
            Lootbox[] Lootboxlist = lootboxDao.getLootboxes();
            Lootbox[] Lootboxes = new Lootbox[lootboxDao.getLootboxes().length];
            int k = 0;
            for (int i = 0; i < Lootboxes.length; i++) {
                if(Lootboxlist[i].getName().contains(name)){
                    Lootboxes[k] = Lootboxlist[i];
                    k++;
                }
            }
            Lootbox[] Lootboxes2 = new Lootbox[k];
            for (int i = 0; i < Lootboxes2.length; i++) {
                Lootboxes2[i] = Lootboxes[i];
            }


            if (Lootboxes2.length != 0)
                return new ResponseEntity<Lootbox[]>(Lootboxes2,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Replace below with your implementation
    }

    /**
     * Creates a {@linkplain Lootbox Lootbox} with the provided Lootbox object
     * 
     * @param Lootbox - The {@link Lootbox Lootbox} to create
     * 
     * @return ResponseEntity with created {@link Lootbox Lootbox} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Lootbox Lootbox} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Lootbox> createLootbox(@RequestBody Lootbox lootbox) {
        LOG.info("POST /Lootboxes " + lootbox);
        try {
            //ArrayList<Product> prods = new ArrayList<>();
            Lootbox fileLootbox = lootboxDao.createLootbox(lootbox);
            return new ResponseEntity<Lootbox>(fileLootbox,HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Lootbox Lootbox} with the provided {@linkplain Lootbox Lootbox} object, if it exists
     * 
     * @param Lootbox The {@link Lootbox Lootbox} to update
     * 
     * @return ResponseEntity with updated {@link Lootbox Lootbox} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Lootbox> updateLootbox(@RequestBody Lootbox Lootbox) {
        LOG.info("PUT /Lootboxes " + Lootbox);
        try {
            Lootbox newLootbox = lootboxDao.updateLootbox(Lootbox);
        if (newLootbox != null)
            return new ResponseEntity<Lootbox>(newLootbox,HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Lootbox Lootbox} with the given id
     * 
     * @param id The id of the {@link Lootbox Lootbox} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Lootbox> deleteLootbox(@PathVariable int id) {
        LOG.info("DELETE /Lootboxes/" + id);
        try {
            boolean tf = lootboxDao.deleteLootbox(id);
        if (tf == true)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
