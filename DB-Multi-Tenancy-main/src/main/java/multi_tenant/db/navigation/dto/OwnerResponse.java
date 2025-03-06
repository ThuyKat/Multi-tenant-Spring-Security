package multi_tenant.db.navigation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import multi_tenant.db.navigation.Entity.Global.Tenant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerResponse {
	Long id; 
	String name;
	String status;
	List<Tenant>tenants;
	
}
