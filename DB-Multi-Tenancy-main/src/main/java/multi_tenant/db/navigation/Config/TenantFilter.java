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
                logger.error("TenantService is not available. Defaulting to DatabaseA.");
                TenantContext.setCurrentTenant("databaseA");
                filterChain.doFilter(request, response);
                return;
            }

            String shopName = request.getHeader("shop-name");
          

            logger.info("Filter processing shop: {}", shopName);

            if (shopName != null) {
                String databaseName = tenantService.getDatabaseNameByShopId(shopName).getDbName();
                TenantContext.setCurrentTenant(databaseName);
                logger.info("Setting DB to: {} in thread: {}", databaseName, Thread.currentThread().getId());
            } else {
                logger.warn("Shop-name header missing");
                TenantContext.setCurrentTenant("default");
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
	}
}
