package multi_tenant.db.navigation.Config;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import multi_tenant.db.navigation.Service.CustomUserDetailsService;
import multi_tenant.db.navigation.Service.TenantService;
import multi_tenant.db.navigation.Utils.TenantContext;


@Configuration
public class SecurityConfig {
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	CustomSuccessHandler successHandler;
	
	@Autowired
	private TenantFilter tenantFilter;
	
	@Autowired
	private TenantService tenantService;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
//		 return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.addFilterBefore(tenantFilter, UsernamePasswordAuthenticationFilter.class)
		.addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter.class)
		.authorizeHttpRequests(
				(requests) -> requests
				.requestMatchers("/login/**")
				.permitAll()
				.anyRequest().authenticated())
				.formLogin(
				(formLogin) -> formLogin.permitAll().successHandler(successHandler)
				.failureHandler((request, response, exception) -> {
                    // Log detailed error
                    System.out.println("Login failed: " + exception.getMessage());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login failed");
                })
				)
				.logout(
				(logout) -> logout.permitAll()
				).csrf(csrf -> csrf.disable());
		
		return http.build();
		
	}
//	   @Bean
//	    public Filter tenantFilter() {
//	        return (Filter) new OncePerRequestFilter() {
//	            @Override
//	            protected void doFilterInternal(HttpServletRequest request, 
//	                                          HttpServletResponse response, 
//	                                          FilterChain filterChain) throws ServletException, IOException {
//	                
//	            	String shopName = request.getHeader("shop-name");
////	            	String shopName = request.getParameter("shop-name");
//	                System.out.println("Interceptor: " + shopName);
//	        		if(shopName != null) {
//	        			String databaseName = tenantService.getDatabaseNameByShopId(shopName).getDbName();
//	        			TenantContext.setCurrentTenant(databaseName);
//	        			System.out.println("I am in tenantFilter method , setting DB "+databaseName);
//
//	        		}else {
//	        			logger.warn("Shop-name header is missing");
//	        			TenantContext.setCurrentTenant("databaseA");
//	        			System.out.println("database A is set in threadID : "+ Thread.currentThread().getId()
//	        					);
//	        		}
//	                
//	                try {
//	                    filterChain.doFilter(request, response);
//	                } finally {
//	                    TenantContext.clear();
//	                }
//	            }
//	        };}
}
