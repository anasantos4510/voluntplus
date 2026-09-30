package br.com.voluntplus.reviews.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "service_reviews")
public class ServiceReview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false) public Long serviceId;
    @Column(nullable = false) public Long authorId;
    @Column(nullable = false) public int nota;
    @Column(nullable = false, length = 500) public String comentario;
    public Instant dataCriacao = Instant.now();
    @Transient public String nomeAutor;
}
