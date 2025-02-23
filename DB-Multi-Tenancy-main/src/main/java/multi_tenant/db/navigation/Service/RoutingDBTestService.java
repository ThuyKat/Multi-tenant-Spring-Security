package multi_tenant.db.navigation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
                String userName = jdbcTemplate.queryForObject(
                    "SELECT name FROM users LIMIT 1", String.class);
                return "Welcome " + userName;	
            }

        } catch (EmptyResultDataAccessException e) {
            return "No welcome message found";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching welcome message: " + e.getMessage();
        }
    }
}
