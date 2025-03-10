package multi_tenant.db.navigation.Config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import multi_tenant.db.navigation.Utils.TenantContext;

//Define a condition class
	public class TenantDatabaseCondition implements Condition {
	    @Override
	    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
	        String currentTenant = TenantContext.getCurrentTenant();
	        return !"global_multi_tenant".equals(currentTenant) && !"default".equals(currentTenant);
	    }
	}
