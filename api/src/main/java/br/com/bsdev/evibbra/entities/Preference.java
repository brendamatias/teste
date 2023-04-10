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
@Table(name = "preferences")
@Getter // Lombok annotation to generate getters for all fields
@Setter // Lombok annotation to generate setters for all fields
@NoArgsConstructor // Lombok annotation to generate a constructor empty
public class Preference {
	
	public Preference(Double annualInvoiceLimit, Boolean emailNotificationsEnabled, Boolean smsNotificationsEnabled) {
		this.annualInvoiceLimit = annualInvoiceLimit;
		this.emailNotificationsEnabled = emailNotificationsEnabled;
		this.smsNotificationsEnabled = smsNotificationsEnabled;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private Double annualInvoiceLimit; // Limite anual para geração de notas fiscais

	@Column(nullable = false)
	private Boolean emailNotificationsEnabled = true; // Habilitar/Desabilitar envio de e-mail

	@Column(nullable = false)
	private Boolean smsNotificationsEnabled = false; // Habilitar/Desabilitar envio de SMS

	@CreationTimestamp
	@Column(updatable = false, name = "created_at")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}