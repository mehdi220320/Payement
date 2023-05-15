package com.fsb.abonnement.Service;

import com.fsb.abonnement.Util.StripeUtil;
import com.fsb.abonnement.entity.Abonnement;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StripePayementService {
    @Value("${stripe.apikey}")
    String stripeKey;
    @Autowired
    StripeUtil stripeUtil;

    public List<Abonnement> getAllCustomer() throws StripeException {
        Stripe.apiKey = stripeKey;

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 3);

        CustomerCollection customers = Customer.list(params);
        List<Abonnement> allAbonnements = new ArrayList<Abonnement>();
        for (int i = 0; i < customers.getData().size(); i++) {

            long unixTimestamp = customers.getData().get(i).getCreated();
            LocalDateTime dateTime = LocalDateTime.ofEpochSecond(unixTimestamp, 0, ZoneOffset.UTC);
            ZonedDateTime tunisDateTime = dateTime.atZone(ZoneId.of("Africa/Tunis"));
            Date date = Date.from(tunisDateTime.toInstant());


            Abonnement abonnement = new Abonnement();
            abonnement.setId(customers.getData().get(i).getId());
            abonnement.setUsername(customers.getData().get(i).getEmail());
            abonnement.setDateDebut(date);
            allAbonnements.add(abonnement);
        }
//        System.out.println(allAbonnements);
        return allAbonnements;
    }

}
