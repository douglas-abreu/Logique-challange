package br.com.challange.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(schema = "entidades", name = "marcacao")
public class Marking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "marcacao_abertura")
    private Date openingMark;

    @Column(name = "marcacao_fechamento")
    private Date closingMark;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private User user;
}
