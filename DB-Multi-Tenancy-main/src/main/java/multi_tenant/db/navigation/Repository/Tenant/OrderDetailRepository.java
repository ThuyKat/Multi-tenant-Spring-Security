package multi_tenant.db.navigation.Repository.Tenant;

import java.util.List;

import org.springframework.context.annotation.Conditional;
import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.db.navigation.Config.TenantDatabaseCondition;
import multi_tenant.db.navigation.Entity.Tenant.OrderDetail;
import multi_tenant.db.navigation.Entity.Tenant.OrderDetailKey;

@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey> {

	List<OrderDetail> findBySizeId(Long id);

	List<OrderDetail> findByProductId(Long id);

}
