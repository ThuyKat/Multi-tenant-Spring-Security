package multi_tenant.db.navigation.Entity.Tenant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Conditional;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import multi_tenant.db.navigation.Config.TenantDatabaseCondition;

@Data
@NoArgsConstructor	
@AllArgsConstructor
@Entity
@Table(name="permissions")
@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
public class Permission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (nullable = false, length = 45)
	private String name;
	
	@ManyToMany(mappedBy = "permissions")	
	@JsonBackReference //mapping - hide roles, only show permission
	private List<Role> roles = new ArrayList<>();
		
}