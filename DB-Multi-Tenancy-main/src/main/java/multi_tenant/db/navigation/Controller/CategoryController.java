package multi_tenant.db.navigation.Controller;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import multi_tenant.db.navigation.Config.TenantDatabaseCondition;

@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
@Controller
@RequestMapping("/tenant/category")
public class CategoryController {
	
}
