package multi_tenant.db.navigation.Repository.Global;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import multi_tenant.db.navigation.Entity.Global.Developer;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer,Long> {

	Optional<Developer> findByEmail(String email);

}
