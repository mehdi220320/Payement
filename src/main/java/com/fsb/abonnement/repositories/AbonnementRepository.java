package com.fsb.abonnement.repositories;


import com.fsb.abonnement.entity.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement,String> {
    HashSet<Abonnement> getAbonnementById(String id );
    HashSet<Abonnement> getAbonnementByUsername(String username);

}
