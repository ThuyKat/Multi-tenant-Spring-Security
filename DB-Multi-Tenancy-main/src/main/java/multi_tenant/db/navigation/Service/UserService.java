package multi_tenant.db.navigation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import multi_tenant.db.navigation.Entity.Tenant.TenantUser;
import multi_tenant.db.navigation.Repository.Tenant.TenantUserRepository;

@Service
public class UserService {
	@Autowired
	private TenantUserRepository userRespository;
	
	public TenantUser getUserByEmail(String email) {
		return userRespository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Tenant User Not found: " + email));
	}
}
