package br.com.bsdev.evibbra.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.bsdev.evibbra.controllers.response.CompetenceInvoiceResponse;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SqlResultSetMapping(
        name = "FindTotalAmountByCompetenceYear",
        classes = @ConstructorResult(
                targetClass = CompetenceInvoiceResponse.class,
                columns = {
                        @ColumnResult(name = "competenceYear", type = Integer.class),
                        @ColumnResult(name = "competenceMonth", type = Integer.class),
                        @ColumnResult(name = "totalAmount", type = Double.class)
                }
        )
)
@Entity
@Table(name = "invoices")
@Getter // Lombok annotation to generate getters for all fields
@Setter // Lombok annotation to generate setters for all fields
@NoArgsConstructor // Lombok annotation to generate an empty constructor
@NamedNativeQuery(
        name = "Invoice.findTotalAmountByCompetenceYear",
        query = "SELECT i.competence_year as competenceYear, i.competence_month as competenceMonth, SUM(i.amount) as totalAmount FROM invoices i WHERE i.competence_year = :competenceYear GROUP BY i.competence_year, i.competence_month order by i.competence_month",
        resultSetMapping = "FindTotalAmountByCompetenceYear"
)
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private Integer invoiceNumber; // Invoice number

	@Column(nullable = false)
	private Double amount; // Invoice amount in BRL (Brazilian Reais)

	@Column(nullable = false, length = 500)
	private String serviceDescription; // Description of the service provided

	@Column(nullable = false)
	private Integer competenceMonth; // Competence month (the month the service refers to)

	@Column(nullable = false)
	private Integer competenceYear; // Competence year (the year the service refers to)

	@Column(nullable = true)
	private LocalDate paymentDate; // Payment date, if paid

	// Relationship with the company table
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id", nullable = false)
	private Company company; // Reference to the company that issued the invoice

	// Automatically stores the creation timestamp
	@CreationTimestamp
	@Column(updatable = false, name = "created_at")
	private LocalDateTime createdAt;

	// Automatically updates with the last modification timestamp
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// Constructor with all necessary fields for creating an invoice
	public Invoice(Integer invoiceNumber, Double amount, String serviceDescription, Integer competenceMonth,
			Integer competenceYear, LocalDate paymentDate, Company company) {
		this.invoiceNumber = invoiceNumber;
		this.amount = amount;
		this.serviceDescription = serviceDescription;
		this.competenceMonth = competenceMonth;
		this.competenceYear = competenceYear;
		this.paymentDate = paymentDate;
		this.company = company;
	}
}