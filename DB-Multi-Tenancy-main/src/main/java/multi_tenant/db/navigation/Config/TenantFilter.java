package multi_tenant.db.navigation.Config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import multi_tenant.db.navigation.Service.TenantService;
import multi_tenant.db.navigation.Utils.TenantContext;

@Component
public class TenantFilter extends OncePerRequestFilter{

	private final ObjectProvider<TenantService> tenantServiceProvider;
//	private final TenantService tenantService;
    private static final Logger logger = LoggerFactory.getLogger(TenantFilter.class);

	@Autowired
	public TenantFilter(ObjectProvider<TenantService> tenantServiceProvider) {
		this.tenantServiceProvider = tenantServiceProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("I am in tenant filter");
		try {
			TenantService tenantService = tenantServiceProvider.getIfAvailable();

            if (tenantService == null) {
                logger.error("TenantService is not available. Defaulting to default db.");
                TenantContext.setCurrentTenant("default");
                TenantContext.setCurrentUserRole("DEVELOPER");
                filterChain.doFilter(request, response);
                return;
            }

            String shopName = request.getHeader("shop-name");
            String userGlobalRole = request.getHeader("global-user");
          

            logger.info("Filter processing shop: {}", shopName);
            
            if (userGlobalRole !=null) {
        		TenantContext.setCurrentUserRole(userGlobalRole);
            }else {
            	if(shopName == null) {
            		//userGlobalRole == null and shopName == null
            		TenantContext.setCurrentUserRole("DEVELOPER"); //default is developer if user does not define userRole
            	}
            		//userGlobalRole == null and shopName !=null ==> role of user is in tenant db
            	
            }
            

            if (shopName == null) {
                logger.warn("Shop-name header missing");
                TenantContext.setCurrentTenant("default");
            } else {
            	 String databaseName = tenantService.getDatabaseNameByShopId(shopName).getDbName();
                 TenantContext.setCurrentTenant(databaseName);
                 logger.info("Setting DB to: {} in thread: {}", databaseName, Thread.currentThread().getId());
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
	}
}
