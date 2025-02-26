package multi_tenant.db.navigation.Config;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import multi_tenant.db.navigation.Service.CustomUserDetailsService;
import multi_tenant.db.navigation.Utils.JwtUtil;




@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
	
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		System.out.println(" I am in sucess handler");
		
		// Get authenticated user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
		
        // Generate JWT token
        String jwt = jwtUtil.generateToken(userDetails);
        
        System.out.println("jwt: "+ jwt);
        
        //add jwt as a header
        response.setHeader("Authorization", "Bearer " + jwt);
        
//     // For testing - write the token to the response
//        response.setContentType("application/json");
//        PrintWriter writer = response.getWriter();
//        writer.write("{\"jwt\": \"" + jwt + "\"}");
//        writer.flush();
        
        //send freresh + access token
		response.sendRedirect("/tenant/test/welcome");
	}
}
