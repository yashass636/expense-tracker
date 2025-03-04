package com.SecApp.SpringSec.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserInfo {

    @Id
    @Column(name = "user_id")
    private String userId;
    private String username;
    private String password;

    //FetchType.EAGER means that the related entities will be fetched eagerly (immediately) along with the main entity.
    // This means when you load a user, their roles will be loaded at the same time.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", //The name of the join table. In this case, it's user_roles
            joinColumns = @JoinColumn(name = "user_id"), //joinColumns: Specifies the foreign key column(s) in the join table that refer to the primary key of the current entity (i.e., user_id).
            inverseJoinColumns = @JoinColumn(name = "role_id") //Specifies the foreign key column(s) in the join table that refer to the primary key of the other entity in the relationship
    )
    private Set<UserRoles> roles = new HashSet<>();

}
