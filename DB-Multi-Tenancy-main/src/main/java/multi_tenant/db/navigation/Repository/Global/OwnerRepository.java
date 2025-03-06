package multi_tenant.db.navigation.Repository.Global;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import multi_tenant.db.navigation.Entity.Global.Developer;
import multi_tenant.db.navigation.Entity.Global.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner,Long> {

	Optional<Owner> findByEmail(String email);
	
	@Query("SELECT o FROM Owner o LEFT JOIN FETCH o.tenants")
    List<Owner> findAllWithTenants();

}
