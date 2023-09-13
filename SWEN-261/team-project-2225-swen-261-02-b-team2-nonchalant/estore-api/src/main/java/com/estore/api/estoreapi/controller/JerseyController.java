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

import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.persistance.JerseyDAO;

@RestController
@RequestMapping("jerseys")
public class JerseyController {
    private static final Logger LOG = Logger.getLogger(JerseyController.class.getName());
    private JerseyDAO jerseyDao;

    public JerseyController(JerseyDAO jerseyDao) {
        this.jerseyDao = jerseyDao; 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jersey> getJersey( @PathVariable int id ) {
        LOG.info("GET /jerseys/" + id);
        try {
            Jersey jersey = jerseyDao.getJersey(id);
            if (jersey != null)
                return new ResponseEntity<Jersey>(jersey,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<Jersey[]> getJerseys() {
        LOG.info("GET /jerseys/");
        try {
            Jersey[] jerseys = jerseyDao.getJerseys();
            if ( jerseys[0] != null ) {
                return new ResponseEntity<Jersey[]>( jerseys, HttpStatus.OK );
            }
            else {
                return new ResponseEntity<>( HttpStatus.NOT_FOUND );
            }
        }
        catch( IOException e ) {
            LOG.log( Level.SEVERE,e.getLocalizedMessage() );
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }

    @GetMapping("/")
    public ResponseEntity<Jersey[]> searchJerseys( @RequestParam String creator ) {
        LOG.info( "GET /jerseys/?creator=" + creator );
        try {
            Jersey[] allJerseyArray = jerseyDao.getJerseys();
            Jersey[] filteredJerseyArray = new Jersey[ jerseyDao.getJerseys().length ];
            int k = 0;
            for ( int i = 0; i < filteredJerseyArray.length; i++ ) {
                if ( allJerseyArray[i].getCreator().equals( creator ) ) {
                    filteredJerseyArray[k] = allJerseyArray[i];
                    k++;
                }
            }

            Jersey[] reducedJerseyArray = new Jersey[k];
            for ( int i = 0; i < reducedJerseyArray.length; i++ ) {
                reducedJerseyArray[i] = filteredJerseyArray[i];
            }


            if ( reducedJerseyArray.length != 0 ) {
                return new ResponseEntity<Jersey[]>( reducedJerseyArray, HttpStatus.OK );
            }
            else {
                return new ResponseEntity<>( HttpStatus.NOT_FOUND );
            }
        }
        catch( IOException e ) {
            LOG.log( Level.SEVERE,e.getLocalizedMessage() );
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }

    @PostMapping("")
    public ResponseEntity<Jersey> createJersey(@RequestBody Jersey jersey) {
        LOG.info( "POST /jerseys " + jersey );
        try {
            Jersey newJersey = jerseyDao.createJersey(jersey);
            return new ResponseEntity<Jersey>(newJersey,HttpStatus.CREATED);
        }
        catch( IOException e ) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<Jersey> updateJersey( @RequestBody Jersey jersey ) {
        LOG.info( "PUT /jerseys " + jersey );
        try {
            Jersey newJersey = jerseyDao.updateJersey( jersey );
            if ( newJersey != null ) {
                return new ResponseEntity<Jersey>( newJersey,HttpStatus.OK );
            }
            else {
                return new ResponseEntity<>( HttpStatus.NOT_FOUND );
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Jersey> deleteJersey( @PathVariable int id ) {
        LOG.info( "DELETE /jerseys/" + id );
        try {
            boolean isSuccessful = jerseyDao.deleteJersey( id );
            if ( isSuccessful ) {
                return new ResponseEntity<>( HttpStatus.OK );
            }
            else {
                return new ResponseEntity<>( HttpStatus.NOT_FOUND );
            }
        }
        catch( IOException e ) {
            LOG.log( Level.SEVERE,e.getLocalizedMessage() );
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }

}