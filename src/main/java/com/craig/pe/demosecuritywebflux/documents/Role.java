package com.craig.pe.demosecuritywebflux.documents;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "roles")
public class Role {
    private String name;
    private String description;
}
