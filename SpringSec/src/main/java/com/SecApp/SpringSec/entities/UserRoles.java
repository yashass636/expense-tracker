package com.SecApp.SpringSec.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data   //part of Project Lombok
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // generates unique va;ue for roleId field
    @Column(name = "role_id")                       // column will be named as role_id
    Long    roleId;
    String  name;
}
