package br.com.bsdev.evibbra.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter // Lombok annotation to generate getters for all fields
@Setter // Lombok annotation to generate setters for all fields
@NoArgsConstructor // Lombok annotation to generate a constructor empty
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;  // CNPJ da empresa (deve ser único)

    @Column(nullable = false, length = 100)
    private String name;  // Nome da empresa

    @Column(nullable = false, length = 100)
    private String legalName;  // Razão social

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "inactivated_at")
    private LocalDateTime inactivatedAt;

    public Company(String cnpj, String name, String legalName) {
        this.cnpj = cnpj;
        this.name = name;
        this.legalName = legalName;
    }
}