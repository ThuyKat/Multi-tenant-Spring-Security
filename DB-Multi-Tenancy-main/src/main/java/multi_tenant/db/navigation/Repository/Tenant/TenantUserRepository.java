package multi_tenant.db.navigation.Repository.Tenant;

import java.util.Optional;

import org.springframework.context.annotation.Conditional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import multi_tenant.db.navigation.Config.TenantDatabaseCondition;
import multi_tenant.db.navigation.Entity.Tenant.TenantUser;

@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
@Repository
public interface TenantUserRepository extends JpaRepository<TenantUser, Long>{
	Optional<TenantUser> findByEmail(String email);

//	TenantUser findByEmailWithPermissions(String email);
}
