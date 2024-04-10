package br.com.challange.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(schema = "tipos", name = "permissao")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome")
    private String name;
}
