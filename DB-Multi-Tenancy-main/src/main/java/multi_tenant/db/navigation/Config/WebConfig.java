package multi_tenant.db.navigation.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.persistence.EntityManagerFactory;
import multi_tenant.db.navigation.Utils.TenantContext;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private DynamicEntityManagerConfig.EntityManagerFactorySelector entityManagerFactorySelector;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	
    }

    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
        OpenEntityManagerInViewInterceptor osivi = new OpenEntityManagerInViewInterceptor();
        
        // Dynamically select EntityManagerFactory based on current tenant
        EntityManagerFactory dynamicEntityManagerFactory = 
            entityManagerFactorySelector.determineEntityManagerFactory(
                TenantContext.getCurrentTenant()
            );
        
        osivi.setEntityManagerFactory(dynamicEntityManagerFactory);
        
        return osivi;
    }
}