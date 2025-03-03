package multi_tenant.db.navigation.Repository.Global;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import multi_tenant.db.navigation.Entity.Global.Developer;
import multi_tenant.db.navigation.Entity.Global.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner,Long> {

	Optional<Owner> findByEmail(String username);

}
