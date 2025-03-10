package multi_tenant.db.navigation.Entity.Tenant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Conditional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import multi_tenant.db.navigation.Config.TenantDatabaseCondition;

@Data
@NoArgsConstructor	
@AllArgsConstructor
@Entity
@Table(name="roles")
@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (nullable = false, length = 30)
	private String name;
	
	@OneToMany (mappedBy="role")
	@JsonIgnore
	private List<TenantUser> users; //or Set?! list can have @OrderBy
	
	
	@ManyToMany //owning side @JoinTable, no mappedBy
	//create table with 2 fk
	@JoinTable(name="role_permission",joinColumns=@JoinColumn(name="role_id"), inverseJoinColumns=@JoinColumn(name="permission_id"))
	@JsonManagedReference //owning - show permissons
	private List<Permission> permissions = new ArrayList<>();
		
}