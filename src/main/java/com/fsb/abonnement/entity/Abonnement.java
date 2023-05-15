package com.fsb.abonnement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Abonnement {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 100)
    private String id;
    private String username;
    private Date dateDebut;
    private Date dateFin;
//    private Boolean valid ;

}
