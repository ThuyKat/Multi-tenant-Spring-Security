package multi_tenant.db.navigation.Entity.Global;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenants", schema = "global_multi_tenant")
public class Tenant {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;	
	
	@Column (nullable = false, unique = true, length = 45) 
	private String name;
	
	@Column(name="db_name", nullable = false, unique = true, length = 45)
	private String dbName;
	
	@Enumerated(EnumType.STRING)
	@Column
	private Status status;
	
	@Column(name = "created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name="owner_id")
	@JsonBackReference
	private Owner owner;
	
	public enum Status{
		ACTIVE, DISABLE
	}
}
