package multi_tenant.db.navigation.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import multi_tenant.db.navigation.Entity.Global.Developer;
import multi_tenant.db.navigation.Repository.Global.DeveloperRepository;

@Service
public class DeveloperService {

	@Autowired
	DeveloperRepository developerRepository;
	
	public Optional<Developer> getDeveloperByEmail(String email) {
		// TODO Auto-generated method stub
		return developerRepository.findByEmail(email);
	}

}
