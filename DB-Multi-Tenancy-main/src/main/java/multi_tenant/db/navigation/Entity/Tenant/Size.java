package multi_tenant.db.navigation.Entity.Tenant;

import java.util.List;

import org.springframework.context.annotation.Conditional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import multi_tenant.db.navigation.Config.TenantDatabaseCondition;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sizes")
@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
public class Size {


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_id")
    private Product product;
    
    @OneToMany(mappedBy ="size")
    @JsonManagedReference
    private List<OrderDetail> orderDetails;
    
    @Column(name="size_price")
    private Double sizePrice;
    
    public Double getPrice() {
        return sizePrice != null ? sizePrice : product.getPrice();
    }
}
