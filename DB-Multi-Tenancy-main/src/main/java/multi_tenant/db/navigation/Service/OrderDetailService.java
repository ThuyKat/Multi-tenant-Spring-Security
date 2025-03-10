package multi_tenant.db.navigation.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
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
import multi_tenant.db.navigation.Entity.Tenant.OrderDetailKey;
import multi_tenant.db.navigation.Entity.Tenant.Product;
import multi_tenant.db.navigation.Entity.Tenant.Size;
import multi_tenant.db.navigation.Repository.Tenant.OrderDetailRepository;

@Slf4j
@Conditional(TenantDatabaseCondition.class) // Only create for tenant databases
@Service
public class OrderDetailService {

	private Order sessionOrder;

	@Autowired
	public OrderDetailService(@Qualifier("sessionOrder") Order sessionOrder,
			OrderDetailRepository orderDetailRepository) {
		this.sessionOrder = sessionOrder;
	}

	@CachePut(value = "orders", key = "#sessionOrder.tenantId + ':' + #sessionOrder.user.email")
	public void updateOrCreateSessionOrderDetail(Product product, int quantity, Size size) {
		Set<OrderDetail> orderDetails = sessionOrder.getOrderDetails();
		// ensure orderDetails is not null
		if (orderDetails == null) {
			orderDetails = new HashSet<>();
			sessionOrder.setOrderDetails(orderDetails);
		}
		/*
		 * find the existing orderDetail in orderDetails that contains the selected
		 * product with the selected size
		 */
		OrderDetail matchedItem = orderDetails.stream()
				.filter(od -> od.getProduct().getId().equals(product.getId())
						&& (size == null || od.getSize() != null && od.getSize().getId().equals(size.getId())))
				.findFirst().orElse(null);
		// if the matchedItem is not found
		if (matchedItem == null) {

			// Create OrderDetailKey
			OrderDetailKey orderDetailKey = new OrderDetailKey(sessionOrder.getId(), product.getId());

			// build orderDetail
			OrderDetail orderDetail = OrderDetail.builder().id(orderDetailKey) // Set the composite key
					.product(product).quantity(quantity).price(size != null ? size.getSizePrice() : product.getPrice())
					.order(sessionOrder).createdBy(sessionOrder.getUser().getEmail()).size(size)
					.createdAt(LocalDateTime.now()).build();
			// Add the new OrderDetail
			sessionOrder.getOrderDetails().add(orderDetail);
			System.out.println("added orderDetail: " + orderDetail.getProduct().getName());
		} else {
			Integer updatedQuantity = quantity;

			matchedItem.setQuantity(updatedQuantity);
			matchedItem.setSize(size);
			matchedItem.setPrice(size != null ? size.getSizePrice() : product.getPrice());
			log.info("Updated orderDetail: " + matchedItem.getProduct().getName()
					+ (size != null ? " with size " + size.getName() : " without size") + ", new quantity: "
					+ quantity);

		}

	}

	@CachePut(value = "orders", key = "#sessionOrder.tenantId + ':' + #sessionOrder.user.email")
	public Order removeSessionOrderDetail(Product product, Long sizeId) {
		Set<OrderDetail> orderDetails = sessionOrder.getOrderDetails();
	    if (orderDetails != null && !orderDetails.isEmpty()) {
	        OrderDetail orderDetailToRemove = orderDetails.stream().filter(
	            od -> od.getProduct().getId().equals(product.getId()) && 
	                 ((sizeId == null && od.getSize() == null) || 
	                  (sizeId != null && od.getSize() != null && od.getSize().getId().equals(sizeId))))
	            .findAny()
	            .orElse(null);

	        if (orderDetailToRemove != null) {
	            orderDetails.remove(orderDetailToRemove);
	         
	        }
	    }
	    return sessionOrder;

	}
	
	

}
