package multi_tenant.db.navigation.Repository.Tenant;

import java.util.List;

import org.springframework.context.annotation.Conditional;
import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.db.navigation.Config.TenantDatabaseCondition;
import multi_tenant.db.navigation.Entity.Tenant.Category;

@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
public interface CategoryRepository extends JpaRepository<Category,Long> {

	List<Category> findByParentIsNull();

	boolean existsByNameIgnoreCase(String name);

}
