package com.fsb.abonnement.Service;

import com.fsb.abonnement.Exceptions.AbonnementAlreadyExistException;
import com.fsb.abonnement.Exceptions.AbonnementNotFoundException;
import com.fsb.abonnement.Exceptions.NoAbonnementExistsInTheRepository;
import com.fsb.abonnement.entity.Abonnement;
import com.stripe.exception.StripeException;

import java.util.HashSet;
import java.util.List;

public interface AbonnementService {
    List<Abonnement> getAll() throws NoAbonnementExistsInTheRepository;
    HashSet<Abonnement> getById(String id) throws AbonnementNotFoundException;
    HashSet<Abonnement> getByUsername(String username) ;
    Abonnement addAbonnement(Abonnement abonnement) throws AbonnementAlreadyExistException;
    List<Abonnement> checkAbonnementDelay() throws NoAbonnementExistsInTheRepository,StripeException;
    Abonnement addAbonnementwithStripe() throws AbonnementAlreadyExistException, StripeException;

}
