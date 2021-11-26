package com.craig.pe.demosecuritywebflux.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private int age;
    private double salary;
    @Indexed
    private String username;
    private String password;
    private List<Role> roles;
}
