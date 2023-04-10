package br.com.bsdev.evibbra.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.bsdev.evibbra.controllers.response.CategoryExpenseResponse;
import br.com.bsdev.evibbra.controllers.response.CompetenceExpenseResponse;
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
        name = "ExpenseFindTotalAmountByCompetenceYear",
        classes = @ConstructorResult(
                targetClass = CompetenceExpenseResponse.class,
                columns = {
                        @ColumnResult(name = "competenceYear", type = Integer.class),
                        @ColumnResult(name = "competenceMonth", type = Integer.class),
                        @ColumnResult(name = "totalAmount", type = Double.class)
                }
        )
)
@SqlResultSetMapping(
        name = "ExpenseFindTotalAmountByCategoryAndCompetenceYear",
        classes = @ConstructorResult(
                targetClass = CategoryExpenseResponse.class,
                columns = {
                        @ColumnResult(name = "category", type = String.class),
                        @ColumnResult(name = "totalAmount", type = Double.class)
                }
        )
)
@Entity
@Table(name = "expenses")
@Getter // Lombok annotation to generate getters for all fields
@Setter // Lombok annotation to generate setters for all fields
@NoArgsConstructor // Lombok annotation to generate a constructor empty
@NamedNativeQuery(
        name = "Expense.findTotalAmountByCompetenceYear",
        query = "SELECT e.competence_year as competenceYear, e.competence_month as competenceMonth, SUM(e.amount) as totalAmount FROM expenses e WHERE e.competence_year = :competenceYear GROUP BY e.competence_year, e.competence_month order by e.competence_month",
        resultSetMapping = "ExpenseFindTotalAmountByCompetenceYear"
)
@NamedNativeQuery(
        name = "Expense.findTotalAmountByCategoryAndCompetenceYear",
        query = "select c.name as category, SUM(e.amount) as totalAmount from expenses e inner join categories c on c.id = e.category_id where e.competence_year = :competenceYear group by c.name order by c.name",
        resultSetMapping = "ExpenseFindTotalAmountByCategoryAndCompetenceYear"
)
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String expenseName;  // Nome da despesa

    @Column(nullable = false)
    private Double amount;  // Valor da despesa

    @Column(nullable = false)
    private LocalDate paymentDate;  // Data de pagamento

    @Column(nullable = false)
    private Integer competenceMonth;  // Mês de competência da despesa

    @Column(nullable = false)
    private Integer competenceYear;  // Ano de competência da despesa

    // Relacionamento com a tabela de categorias
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;  // Referência à categoria da despesa

    // Relacionamento opcional com a tabela de empresas
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;  // Referência opcional à empresa relacionada à despesa

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Expense(String expenseName, Double amount, LocalDate paymentDate, Integer competenceMonth, Integer competenceYear, Category category, Company company) {
        this.expenseName = expenseName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.competenceMonth = competenceMonth;
        this.competenceYear = competenceYear;
        this.category = category;
        this.company = company;
    }
}