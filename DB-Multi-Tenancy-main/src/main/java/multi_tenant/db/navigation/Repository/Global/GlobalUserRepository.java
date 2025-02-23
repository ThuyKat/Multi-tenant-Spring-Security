package multi_tenant.db.navigation.Repository.Global;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.db.navigation.Entity.Global.Developer;
import multi_tenant.db.navigation.Entity.Global.Owner;

public interface GlobalUserRepository extends JpaRepository<Owner,Long> {

	Optional<Owner> findByEmail(String username);

}
