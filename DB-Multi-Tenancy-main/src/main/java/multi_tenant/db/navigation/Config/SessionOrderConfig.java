package multi_tenant.db.navigation.Config;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.annotation.RequestScope;

import lombok.extern.slf4j.Slf4j;
import multi_tenant.db.navigation.Entity.Tenant.Order;
import multi_tenant.db.navigation.Entity.Tenant.TenantUser;
import multi_tenant.db.navigation.Repository.Tenant.TenantUserRepository;
import multi_tenant.db.navigation.Utils.TenantContext;

@Configuration
@Slf4j
public class SessionOrderConfig {
	
	@Autowired
	RedisTemplate<String,Object> redisTemplate;
	
	@Autowired
	TenantUserRepository userRepository;

	@Bean(name = "sessionOrder")
	@RequestScope
	@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
	@Cacheable(value = "orders", key = "#root.method.name + ':' + "
			+ "T(multi_tenant.db.navigation.Utils.TenantContext).getCurrentTenant() + ':' "
			+ "+ T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")
	public Order sessionOrder() {
	    // Get current tenant from context
	    String currentTenant = TenantContext.getCurrentTenant();
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String cacheKey = "order:" + username + ":" + currentTenant;
	    log.info("Creating session order for user in tenant: {}", cacheKey);
	    
//	 // Check if order exists in Redis
//        Order order = (Order) redisTemplate.opsForValue().get(cacheKey);
//        if (order != null) {
//            log.info("Retrieved cached order for user: {} in tenant: {}", username, currentTenant);
//            return order;
//        }
	    // Create and return new order if none is found in cache
        Optional<TenantUser> userOptional = userRepository.findByEmail(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found with email: " + username);
        }
        TenantUser user = userOptional.get();
        Order order = new Order();
	    order.setTenantId(currentTenant);
	    order.setUser(user);
	    order.setOrderDetails(new HashSet<>());
	    
//	 // Cache the newly created order in Redis
//	    redisTemplate.opsForValue().set(cacheKey, order);
//	    log.info("Cached new order for user: {} in tenant: {}", username, currentTenant);
	    
	    return order;
	}

	
}
