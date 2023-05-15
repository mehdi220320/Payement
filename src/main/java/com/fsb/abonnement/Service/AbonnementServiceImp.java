package com.fsb.abonnement.Service;

import com.fsb.abonnement.Exceptions.AbonnementAlreadyExistException;
import com.fsb.abonnement.Exceptions.AbonnementNotFoundException;
import com.fsb.abonnement.Exceptions.NoAbonnementExistsInTheRepository;
import com.fsb.abonnement.Util.StripeUtil;
import com.fsb.abonnement.entity.Abonnement;
import com.fsb.abonnement.repositories.AbonnementRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
@Service
@EnableScheduling
public class AbonnementServiceImp implements AbonnementService{
    @Autowired
    AbonnementRepository abonnementRepository;
     @Autowired
    StripeUtil stripeUtil;
    @Value("${stripe.apikey}")
    String stripeKey;
    @Override
    public List<Abonnement> getAll() throws NoAbonnementExistsInTheRepository {
         if(abonnementRepository.findAll().isEmpty()){
          throw new NoAbonnementExistsInTheRepository();
         }
         else{
             return  abonnementRepository.findAll();
         }
    }

    @Override
    public HashSet<Abonnement> getById(String id) throws AbonnementNotFoundException {
        if (abonnementRepository.getAbonnementById(id).isEmpty()){
            throw new AbonnementNotFoundException();
        }
        else{
            return abonnementRepository.getAbonnementById(id);
        }
    }

    @Override
    public HashSet<Abonnement> getByUsername(String username)  {
        if (abonnementRepository.getAbonnementByUsername(username).isEmpty()){
            return null;
        }
        else{
            return abonnementRepository.getAbonnementByUsername(username);
        }    }

    @Override
    public Abonnement addAbonnementwithStripe() throws AbonnementAlreadyExistException, StripeException {
        Abonnement abonnement=stripeUtil.getLatestAbonnement();
        if(abonnementRepository.getAbonnementById(abonnement.getId()).isEmpty()){
            return abonnementRepository.save(abonnement);
        }
            return null;
    }

    @Override
    public Abonnement addAbonnement(Abonnement abonnement) throws AbonnementAlreadyExistException {
        if(abonnementRepository.getAbonnementById(abonnement.getId()).isEmpty()){
            abonnement.setDateDebut(new Date());

            LocalDateTime dateDebut = LocalDateTime.ofInstant(abonnement.getDateDebut().toInstant(), ZoneId.systemDefault());
            LocalDateTime dateFin = dateDebut.plus(30, ChronoUnit.DAYS);
            abonnement.setDateFin(Date.from(dateFin.atZone(ZoneId.systemDefault()).toInstant()));
            return  abonnementRepository.save(abonnement);
        }
        else{
            throw new AbonnementAlreadyExistException();
        }

    }

    @Override
    @Scheduled(fixedDelay = 60000) // Run every 1 heure
    public List<Abonnement> checkAbonnementDelay() throws NoAbonnementExistsInTheRepository, StripeException {

        if(abonnementRepository.findAll().isEmpty()){
            System.out.println("CheckDone");
            throw new NoAbonnementExistsInTheRepository();
        }
        else{
            for(Abonnement abonnements: abonnementRepository.findAll()){
                int datediff=new Date().compareTo(abonnements.getDateFin());
                if( datediff>0){
                    abonnementRepository.deleteById(abonnements.getId());
                    Stripe.apiKey = stripeKey;

                    Customer customer = Customer.retrieve(abonnements.getId());

                    Customer deletedCustomer = customer.delete();
                }
            }
            System.out.println("CheckDone");
            return  abonnementRepository.findAll();
        }
    }

}
