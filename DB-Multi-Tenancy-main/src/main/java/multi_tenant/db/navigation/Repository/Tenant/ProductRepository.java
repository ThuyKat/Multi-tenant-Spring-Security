package multi_tenant.db.navigation.Repository.Tenant;

import java.util.List;

import org.springframework.context.annotation.Conditional;
import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.db.navigation.Config.TenantDatabaseCondition;
import multi_tenant.db.navigation.Entity.Tenant.Product;

@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
public interface ProductRepository extends JpaRepository<Product,Long>{

	List<Product> findProductByCategoryId(Long categoryId);

}
