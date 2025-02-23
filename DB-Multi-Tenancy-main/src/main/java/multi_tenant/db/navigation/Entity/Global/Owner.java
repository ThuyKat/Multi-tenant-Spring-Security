package multi_tenant.db.navigation.Entity.Global;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import multi_tenant.db.navigation.Enum.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "owners", schema = "global_multi_tenant")
public class Owner {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;	
	
	@Column (nullable = false, unique = true, length = 45) 
	private String name;
	
	@Column(nullable = false, unique = true, length = 100)
	private String email;
	
	@Column(nullable = false, length = 255)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column
	private Status status;
	
	@Column(name = "created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@OneToMany (mappedBy = "owner", cascade = CascadeType.ALL) //owner lowecase, field in Tenant
	@JsonIgnore
	List<Tenant> tenants = new ArrayList<>();
	
	
	
}
