package br.com.voluntplus.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "user_accounts")
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true, updatable = false)
    public String clerkUserId;

    @Column(nullable = false)
    public String personType;

    @Column(name = "active_role", nullable = false)
    public String currentRole;

    public String fullName;
    public LocalDate birthDate;
    public String gender;
    public String organizationName;
    public String cnpj;
    public String organizationEmail;
    public String phone;
    public String availability;
    public String logoUrl;
    @Column(length = 4000) public String description;
    @Column(length = 4000) public String location;
}
