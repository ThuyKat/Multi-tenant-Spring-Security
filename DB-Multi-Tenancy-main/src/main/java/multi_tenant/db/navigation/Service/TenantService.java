package multi_tenant.db.navigation.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import multi_tenant.db.navigation.Entity.Global.Tenant;
import multi_tenant.db.navigation.Repository.Global.TenantRepository;
import multi_tenant.db.navigation.Utils.DataSourceUtil;
import multi_tenant.db.navigation.Utils.TenantRoutingDataSource;

@Service
public class TenantService {
    private ApplicationContext applicationContext;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private DataSourceUtil dataSourceUtil;
    
    @Autowired
    @Qualifier("globalDataSource") 
    private DataSource globalDataSource; 
    
    @Autowired
    @Lazy
    private TenantRoutingDataSource tenantRoutingDataSource;
	
	public List<Tenant> getAllTenant(){
		return tenantRepository.findAll();
	}
	public Tenant getDatabaseNameByShopId(String shopName) {
		return tenantRepository.findByName(shopName);
	}
	
	@Transactional(transactionManager = "globalTransactionManager") //to rollback if error occurs
	public void saveTenantToGlobalDB(String shopName, Long ownerId, String databaseName) {	
		
		try(Connection connection = globalDataSource.getConnection()){			
			connection.setAutoCommit(false);
			
			//Add new tenant to global db: tenants table
			String insertNewTenantToGlobalDB = "INSERT INTO tenants (owner_id, name, db_name, status) VALUES (?, ?, ?, ?)";			
			try(PreparedStatement prepare = connection.prepareStatement(insertNewTenantToGlobalDB)){
				prepare.setLong(1, ownerId);
				prepare.setString(2, shopName);
				prepare.setString(3, databaseName);
				prepare.setString(4, "ACTIVE");
				prepare.executeUpdate();
			}
			connection.commit();	
		} catch (SQLException e) {
            throw new RuntimeException("Error inserting tenant into Global DB: " + e.getMessage());
        }
	}
	public void createNewTenant(String shopName, Long ownerId) {
			//connect to global db
			String databaseName = shopName.toLowerCase() + "_db";
			
			saveTenantToGlobalDB(shopName, ownerId, databaseName);
			
			try(Connection connection = globalDataSource.getConnection()){
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			
			//Create new tenant db
			statement.execute("CREATE DATABASE " + databaseName);
			
			//connect to new tenant db and run Flyway
			DataSource tenantDataSource = dataSourceUtil.createDataSource(databaseName);
			Flyway flyway = Flyway.configure().
					dataSource(tenantDataSource)
					.locations("classpath:db/migration/tenant")
					.baselineOnMigrate(false)
					.load();
			try {
				flyway.migrate(); 
			}catch (Exception e) {
				statement.execute("DROP DATABASE " + databaseName);
				throw new RuntimeException("Flyway migration failed for " + databaseName + ": " + e.getMessage());
			}			
			
			// TenantRoutingDataSource tenantRoutingDataSource = applicationContext.getBean(MultiTenantDataSource.class);
			tenantRoutingDataSource.addDataSource(databaseName, tenantDataSource);
			
			connection.commit();			
					
		} catch (SQLException e) {
			throw new RuntimeException("Error when creating new tenant database: " + e.getMessage());
		}
	}

}
