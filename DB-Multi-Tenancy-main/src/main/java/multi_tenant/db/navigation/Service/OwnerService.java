package multi_tenant.db.navigation.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import multi_tenant.db.navigation.Entity.Global.Owner;
import multi_tenant.db.navigation.Entity.Global.Tenant;
import multi_tenant.db.navigation.Repository.Global.OwnerRepository;

@Service
public class OwnerService {

	
	@Autowired
	private OwnerRepository ownerRepository;
	
	public Owner getOwnerByEmail(String email) {
		return ownerRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Owner not found: " + email));
	}

	public List<Owner> findAll() {
		// TODO Auto-generated method stub
		return ownerRepository.findAllWithTenants();
	}
}