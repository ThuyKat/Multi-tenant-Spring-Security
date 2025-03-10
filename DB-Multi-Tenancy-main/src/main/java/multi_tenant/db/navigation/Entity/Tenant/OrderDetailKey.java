package multi_tenant.db.navigation.Entity.Tenant;

import java.io.Serializable;

import org.springframework.context.annotation.Conditional;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import multi_tenant.db.navigation.Config.TenantDatabaseCondition;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
public class OrderDetailKey implements Serializable {
	
	private static final long serialVersionUID = 1L; 

	@Column(name="order_id")
	private Long orderId;
	
	@Column(name="product_id")
	private Long productId;

	
}
