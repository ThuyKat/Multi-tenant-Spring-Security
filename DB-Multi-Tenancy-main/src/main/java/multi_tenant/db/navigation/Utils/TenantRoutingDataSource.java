package multi_tenant.db.navigation.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class TenantRoutingDataSource extends AbstractRoutingDataSource{
	
	private final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();
		
	@Override
	protected Object determineCurrentLookupKey() {	
		System.out.println("I am in TenantRouting DS, determining the lookup key on threadID: "+ Thread.currentThread().getId());
		String tenant = TenantContext.getCurrentTenant();
        System.out.println("Routing to database: " + tenant);

		return tenant != null ? tenant : "default";
	}

	//hibernatea
	// avoid null pointer execption if databaseName not exist
	public DataSource getDataSource(String databaseName) {
		System.out.println("I am getting dataSource "+ databaseName);
		 DataSource dataSource= (DataSource) dataSourceMap.getOrDefault(databaseName, getDefaultDataSource()); 
		 return dataSource;
	}
	//hibernate
	public DataSource getDefaultDataSource() {
		System.out.println(" I am getting default dataSource");
		DataSource defaultDS = (DataSource) dataSourceMap.get("default");
        if (defaultDS == null) {
            throw new IllegalStateException("Default datasource not configured");
        }
        return defaultDS;
	}
	
    public void addDataSource(String databaseName, DataSource dataSource) {
    	System.out.println("I am adding dataSource name: "+ databaseName);
    	if(!dataSourceMap.containsKey(databaseName)) {
    		  dataSourceMap.put(databaseName, dataSource);        
    	        setTargetDataSources(dataSourceMap);
    	        afterPropertiesSet(); // Reload data sources dynamically
    	}      
    }
    
    @Override
    public void afterPropertiesSet() {
        // Set a default tenant if none is set
        if (TenantContext.getCurrentTenant() == null) {
        	TenantContext.setCurrentTenant("default");
        }
        super.afterPropertiesSet();
    }
}
