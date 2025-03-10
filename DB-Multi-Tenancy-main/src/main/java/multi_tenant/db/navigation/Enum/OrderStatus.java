package multi_tenant.db.navigation.Enum;

import org.springframework.context.annotation.Conditional;

import multi_tenant.db.navigation.Config.TenantDatabaseCondition;

@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
public enum OrderStatus {
	ORDERED, COMPLETED,CANCELLED,REFUNDED
}
