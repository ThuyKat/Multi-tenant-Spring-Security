package multi_tenant.db.navigation.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import multi_tenant.db.navigation.Config.TenantDatabaseCondition;
import multi_tenant.db.navigation.Entity.Tenant.Order;
import multi_tenant.db.navigation.Entity.Tenant.OrderDetail;
import multi_tenant.db.navigation.Entity.Tenant.Product;
import multi_tenant.db.navigation.Entity.Tenant.Size;
import multi_tenant.db.navigation.Entity.Tenant.TenantUser;
import multi_tenant.db.navigation.dto.AddItemRequestDto;

@Slf4j
@Conditional(TenantDatabaseCondition.class) // Only create for tenant databases
@Service
public class OrderService {

	private Order sessionOrder;
	private UserService userService;
	private ProductService productService;
	private SizeService sizeService;
	private OrderDetailService orderDetailService;

	@Autowired
	public OrderService(@Qualifier("sessionOrder") Order sessionOrder, UserService userService,
			ProductService productService, SizeService sizeService) {
		this.sessionOrder = sessionOrder;
		this.userService = userService;
		this.productService = productService;
		this.sizeService = sizeService;
	}

//SESSION ORDER RELATED METHOD
	@Cacheable(value = "orders", key = "#currentTenant + ':' + #username")
	public Order getSessionOrder(String currentTenant, String username) {
		log.info("Retrieving session order for user: {} in tenant: {}", username, currentTenant);

		// Try to find user
		TenantUser user = userService.getUserByEmail(username);
		// User exists, create a new order if one doesn't exist in cache
		Order order = new Order();
		order.setTenantId(currentTenant);
		order.setUser(user);
		order.setOrderDetails(new HashSet<>());
		order.setTotalPrice(0.0f);

		log.info("Created new session order for user: {} in tenant: {}", username, currentTenant);
		return order;
	}

	@CachePut(value = "orders", key = "#sessionOrder.tenantId + ':' + #sessionOrder.user.email")
	public Order addToSessionOrder(List<AddItemRequestDto> items) {

		for (AddItemRequestDto orderItem : items) {
			// get id and quantity of each product
			Product product = orderItem.getProduct();
			int quantity = orderItem.getQuantity();
			Long sizeId = orderItem.getSizeId();
			if (quantity > 0) {
				// find the product in DB by productId
				Product productDB = productService.getProductById(product.getId());
				if (!productDB.getSizes().isEmpty() && sizeId != null) {
					Size sizeDB = sizeService.getSizesById(sizeId);
					orderDetailService.updateOrCreateSessionOrderDetail(productDB, quantity, sizeDB);
				} else {
					log.info("product doesnt have size"); // Product doesn't have sizes, add it without a size
					orderDetailService.updateOrCreateSessionOrderDetail(productDB, quantity, null);
				}
				
			} else {
				// quantity = 0, find if product is in sessionOrder -> remove product from
				// sessionOrder
				orderDetailService.removeSessionOrderDetail(product, sizeId);
				   // Recalculate total price after removing the item	
			}
			updateSessionOrderTotalPrice();
		}
		return sessionOrder;

	}

	@CachePut(value = "orders", key = "#sessionOrder.tenantId + ':' + #sessionOrder.user.email")
	public Order updateSessionOrderTotalPrice() {

		// get all orderDetails from sessionOrder
		Set<OrderDetail> orderDetails = sessionOrder.getOrderDetails();
		// calculate the total
		if (orderDetails != null && !orderDetails.isEmpty()) {
			double totalPrice = orderDetails.stream().mapToDouble(od -> {
				double itemPrice = (od.getSize() != null && od.getSize().getPrice() != null) ? od.getSize().getPrice()
						: od.getProduct().getPrice();
				return od.getQuantity() * itemPrice;
			}).sum();

			// Convert to float for storage
			float roundedTotal = (float) totalPrice;
			log.info("Total of order is: {}", roundedTotal);
			sessionOrder.setTotalPrice(roundedTotal);
		}
		// return order, update cache
		return sessionOrder;
	}

	@CachePut(value = "orders", key = "#sessionOrder.tenantId + ':' + #sessionOrder.user.email")
	public Order resetSessionOrder(Order sessionOrder) {
	    // Get current tenant and user information
	    String currentTenant = sessionOrder.getTenantId();
	    TenantUser user = sessionOrder.getUser();
	    
	    // Reset order to initial state
	    sessionOrder.getOrderDetails().clear();
	    sessionOrder.setTotalPrice(0.0f);
	    
	    log.info("Reset session order for user: {} in tenant: {}", user.getEmail(), currentTenant);
	    
	    return sessionOrder;
	}
	
	//note: When user logout, we can select to evict the order cache of that user. 

// DB ORDER RELATED METHOD
}
