package multi_tenant.db.navigation.Repository.Global;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import multi_tenant.db.navigation.Entity.Global.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
	Tenant findByName(String name);
	List<Tenant> findByOwnerId(Long ownerId);
}
