 package multi_tenant.db.navigation.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContext {
	private static final Logger logger = LoggerFactory.getLogger(TenantContext.class);

	private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

	public static String getCurrentTenant() {
		String tenant = CURRENT_TENANT.get();
        System.out.println("I am in TenantContext, getting current tenant: " + tenant);
		return tenant;
	}
	public static void setCurrentTenant(String databaseName) {
		logger.info("database name: {}", databaseName);
		System.out.println("I am in TenantContext, setting current tenant "+ databaseName+ "threadID: "+ Thread.currentThread().getId());
		CURRENT_TENANT.set(databaseName);
	}
	
	public static void clear() {
		CURRENT_TENANT.remove();
	}
	
	}
