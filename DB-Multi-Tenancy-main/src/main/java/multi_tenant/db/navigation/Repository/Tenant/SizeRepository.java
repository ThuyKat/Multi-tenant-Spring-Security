package multi_tenant.db.navigation.Repository.Tenant;

import java.util.List;

import org.springframework.context.annotation.Conditional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import multi_tenant.db.navigation.Config.TenantDatabaseCondition;
import multi_tenant.db.navigation.Entity.Tenant.Size;

@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
@Repository
public interface SizeRepository extends JpaRepository<Size,Long> {

	List<Size> findByProductId(Long productId);


}
