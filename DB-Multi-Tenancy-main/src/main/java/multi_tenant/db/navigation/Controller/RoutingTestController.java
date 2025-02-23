package multi_tenant.db.navigation.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import multi_tenant.db.navigation.Service.RoutingDBTestService;


@RestController
@RequestMapping("/tenant/test")
public class RoutingTestController {
	
	@Autowired
	private RoutingDBTestService testService;
	
	@GetMapping("/welcome")
	public String getWelcomeMessage() {
        return testService.getWelcomeMessage();

	}
}
