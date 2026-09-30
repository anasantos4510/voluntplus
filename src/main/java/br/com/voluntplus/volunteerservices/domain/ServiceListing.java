package br.com.voluntplus.volunteerservices.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "service_listings")
public class ServiceListing {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false) public Long ownerId;
    @Column(nullable = false) public String name;
    @Column(length = 4000) public String descricao;
    public String modalities;
    public String idCategoria;
    public String status = "ATIVO";
    @Column(columnDefinition = "text") public String providerImage;
    public String diaDaSemana;
    public String turno;
    public String cep;
    public String estado;
    public String cidade;
    public String bairro;
    public String tipoLocalizacao;
    public String whatsapp;
    public String telefone;
    public String instagram;
    public String site;
    public Instant publicationDate = Instant.now();
    @Transient public List<?> avaliacoes;
    @Transient public java.util.Map<String, Object> usuario;
}
