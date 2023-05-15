package com.fsb.abonnement.Util;

import com.fsb.abonnement.entity.Abonnement;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class StripeUtil {

    @Value("${stripe.apikey}")
    String stripeKey;

    public Abonnement getLatestAbonnement() throws StripeException {
        Stripe.apiKey = stripeKey;

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 3);
        CustomerCollection customerList = Customer.list(params);
        List<Customer> customers = customerList.getData();
        customers.sort(Comparator.comparingLong(Customer::getCreated).reversed());
        if (!customers.isEmpty()) {
           Customer latestCustomer = customers.get(0);
            Abonnement data = setAbonnement(latestCustomer);
            return data;
        }
        return null;
    }
    public Abonnement setAbonnement(Customer customer) {
        Abonnement abonnement = new Abonnement();
        //Reformulate the type of customer.getCreated() to type Date();
        long unixTimestamp = customer.getCreated();
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(unixTimestamp, 0, ZoneOffset.UTC);
        ZonedDateTime tunisDateTime = dateTime.atZone(ZoneId.of("Africa/Tunis"));
        Date date = Date.from(tunisDateTime.toInstant());

        //Generate the dateFin after 30Days from dateDebut
        LocalDateTime dateDebut = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime dateFin = dateDebut.plus(1, ChronoUnit.MINUTES);

        abonnement.setId(customer.getId());
        abonnement.setUsername(customer.getEmail());
        abonnement.setDateDebut(date);
        abonnement.setDateFin(Date.from(dateFin.atZone(ZoneId.systemDefault()).toInstant()));

        return abonnement;
    }
}
