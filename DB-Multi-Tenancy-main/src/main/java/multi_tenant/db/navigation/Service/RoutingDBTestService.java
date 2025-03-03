package multi_tenant.db.navigation.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import multi_tenant.db.navigation.Utils.TenantContext;

@Service
public class RoutingDBTestService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
        

    public String getWelcomeMessage() {
        try {
            // Ensure the correct tenant database is being used
            String currentTenant = TenantContext.getCurrentTenant();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication != null ? authentication.getName() : null;
            System.out.println("getting welcome message in  "+ currentTenant);
            if (currentTenant == null || currentTenant.isEmpty()) {
                throw new IllegalStateException("No tenant database selected");
            }
            

            if (currentTenant.equals("default") || currentTenant.equals("global_multi_tenant")) {
            	System.out.println("getting welcome messsage from global DB" + currentTenant);

                // Fallback to database info message
                return jdbcTemplate.queryForObject(
                    "SELECT message FROM db_info LIMIT 1", String.class);

            } else {
            	
            	System.out.println("getting welcome messsage from tenant DB" + currentTenant);
            	// Query for user name
            	String name = jdbcTemplate.queryForObject(
            		    "SELECT name FROM users WHERE email = ?", 
            		    String.class,
            		    email);
            		return "Welcome " + name;
	
            }

        } catch (EmptyResultDataAccessException e) {
            return "No welcome message found";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching welcome message: " + e.getMessage();
        }
    }
}
