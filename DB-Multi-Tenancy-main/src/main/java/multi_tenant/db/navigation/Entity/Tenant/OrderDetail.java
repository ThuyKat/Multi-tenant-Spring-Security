package multi_tenant.db.navigation.Entity.Tenant;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Conditional;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import multi_tenant.db.navigation.Config.TenantDatabaseCondition;

@Entity
@Table(name="order_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude= {"product","order","size"})
@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
public class OrderDetail  {

@EmbeddedId
OrderDetailKey id;

@ManyToOne
@JsonBackReference
@MapsId("productId") // attribute of OrderDetailKey
@JoinColumn(name="product_id")
private Product product;

@ManyToOne
@JsonBackReference
@MapsId("orderId") // attribute of OrderDetailKey
@JoinColumn(name="order_id")
private Order order;

private int quantity;

private double price;

@ManyToOne
@JoinColumn(name="size_id")
@JsonBackReference
private Size size;

@Column(name="created_by")	
private String createdBy;

@Column(name="created_at")
private LocalDateTime createdAt;

}
