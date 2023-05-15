package com.fsb.abonnement.Controller;

import com.fsb.abonnement.Exceptions.AbonnementAlreadyExistException;
import com.fsb.abonnement.Exceptions.AbonnementNotFoundException;
import com.fsb.abonnement.Exceptions.NoAbonnementExistsInTheRepository;
import com.fsb.abonnement.Service.AbonnementService;
import com.fsb.abonnement.Service.StripePayementService;
import com.fsb.abonnement.entity.Abonnement;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("api/abonnement")
public class AbonnementController {
    @Autowired
    AbonnementService abonnementService;
    @GetMapping()
    public ResponseEntity<List<Abonnement>> getALL (){
        try {
            return  new ResponseEntity<List<Abonnement>>(abonnementService.getAll(),HttpStatus.CREATED);
        }catch (NoAbonnementExistsInTheRepository e){
            return  new ResponseEntity("There s no Abonnement for the moment ",HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getAbonnementById(@PathVariable String id){
        try {
            HashSet<Abonnement> abonnementHashSet=abonnementService.getById(id);
            return  new ResponseEntity<>(abonnementHashSet,HttpStatus.OK);
        }
        catch (AbonnementNotFoundException e){
            return new ResponseEntity("There is no abonnement with this id ",HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/getbyusername/{username}")
    public ResponseEntity<?> getAbonnementByUsername(@PathVariable String username){
        HashSet<Abonnement> abonnementHashSet=abonnementService.getByUsername(username);
        return  new ResponseEntity<>(abonnementHashSet,HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Abonnement> createAbonnement(@RequestBody Abonnement abonnement) throws IOException {
        try {
            return new ResponseEntity<Abonnement>(abonnementService.addAbonnement(abonnement), HttpStatus.CREATED);
        } catch (AbonnementAlreadyExistException e) {
            return new ResponseEntity("Abonnement Already Exist", HttpStatus.CONFLICT);
        }
    }
    @PostMapping("/addwithstripe")
    public ResponseEntity<Abonnement> createAbonnementWithStripe() throws IOException {
        try {
            return new ResponseEntity<Abonnement>(abonnementService.addAbonnementwithStripe(), HttpStatus.CREATED);
        } catch (AbonnementAlreadyExistException e) {
            return new ResponseEntity("Abonnement Already Exist", HttpStatus.CONFLICT);
        } catch (StripeException e) {
            return new ResponseEntity( "There's no List in Stripe", HttpStatus.CONFLICT);
        }
    }
    @Autowired
    StripePayementService stripePayementService;
//    public List<Abonnement> getall() throws StripeException {
//        List<Abonnement> abonnements=stripePayementService.getAllCustomer();
//        return abonnements;
//    }
    @GetMapping("stripe")
    public ResponseEntity<List<Abonnement>> getALLstripes () {
        try {
            return new ResponseEntity<List<Abonnement>>(stripePayementService.getAllCustomer(), HttpStatus.CREATED);
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return null;
    }

}
