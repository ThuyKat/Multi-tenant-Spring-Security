package multi_tenant.db.navigation.Entity.Tenant;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Conditional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import multi_tenant.db.navigation.Config.TenantDatabaseCondition;
import multi_tenant.db.navigation.Enum.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Conditional(TenantDatabaseCondition.class)  // Only create for tenant databases
public class TenantUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 45)
	private String name;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false, length = 255)
	private String password;

	@ManyToOne
	@JoinColumn(name = "role_id") //have column role_id in users
	private Role role;
	
	@Enumerated(EnumType.STRING)
	@Column
	private Status status;

	@Column(name = "created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;

	
}
