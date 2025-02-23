package multi_tenant.db.navigation.Repository.Tenant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.db.navigation.Entity.Tenant.TenantUser;

public interface TenantUserRepository extends JpaRepository<TenantUser, Long>{
	Optional<TenantUser> findByEmail(String email);
}
