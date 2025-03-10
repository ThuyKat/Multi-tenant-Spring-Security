package multi_tenant.db.navigation.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import multi_tenant.db.navigation.Entity.Tenant.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddItemRequestDto {
	
	Product product;
	
	int quantity;
	
	Long sizeId;
	

}
