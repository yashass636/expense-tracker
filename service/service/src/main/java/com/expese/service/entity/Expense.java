package com.expese.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Expense {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "amount")
    private String amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "merchant")
    private String merchant;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @PrePersist  //This method is called before the entity is saved for the first time (INSERT).
    @PreUpdate  //This method is called before an existing entity is updated (UPDATE).
    private void generateExternalId(){

        if (this.externalId == null){
           this.externalId = UUID.randomUUID().toString();
        }

        if (this.createdAt == null){
            this.createdAt = new Timestamp(System.currentTimeMillis());
        }
    }

}
