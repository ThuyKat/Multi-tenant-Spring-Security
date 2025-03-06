package multi_tenant.db.navigation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantResponse {
	Long id; 
	String name;
	String dbName;
	String status;
	
}
