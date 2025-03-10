package multi_tenant.db.navigation.dto;

import org.springframework.context.annotation.Conditional;
import org.springframework.web.multipart.MultipartFile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import multi_tenant.db.navigation.Config.TenantDatabaseCondition;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
public class ProductDto {

	String name;
	String description;
	MultipartFile imageData;
}
