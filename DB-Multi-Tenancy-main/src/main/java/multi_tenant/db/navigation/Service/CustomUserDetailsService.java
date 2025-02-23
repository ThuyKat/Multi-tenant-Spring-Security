package multi_tenant.db.navigation.Service;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import multi_tenant.db.navigation.Entity.Global.Owner;
import multi_tenant.db.navigation.Entity.Tenant.TenantUser;
import multi_tenant.db.navigation.Enum.Status;
import multi_tenant.db.navigation.Repository.Global.GlobalUserRepository;
import multi_tenant.db.navigation.Repository.Tenant.TenantUserRepository;
import multi_tenant.db.navigation.Utils.TenantContext;
import multi_tenant.db.navigation.dto.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
    private GlobalUserRepository globalUserRepository;
	
	@Autowired
	private TenantUserRepository tenantUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		// Check header/tenant context to decide which repo to use
        String dbName = TenantContext.getCurrentTenant();
        if (dbName.equals("global_multi_tenant") || dbName.equals("default")) {
        	System.out.println("I am in Custom User Details Service, db name is pointing to global");
        	// Use global repository
            Owner globalUser = globalUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Global User Not found: " + email)); 
            return new CustomUserDetails(
            		globalUser.getEmail(),
            		globalUser.getPassword(),
            		Status.ACTIVE,
            		true,
            		dbName
            		);
        }else {
        	System.out.println("I am in Custom user Details Service, dbName is "+ dbName);
        	try {
                TenantUser tenantUser = tenantUserRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Tenant User Not found: " + email));

                return new CustomUserDetails(
                    tenantUser.getEmail(),
                    tenantUser.getPassword(),
                    tenantUser.getStatus(),
                    false,
                    dbName
                );
            } catch (Exception e) {
                System.err.println("Error finding user in tenant " + dbName + ": " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
        
	}

}
