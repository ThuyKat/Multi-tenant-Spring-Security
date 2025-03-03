package multi_tenant.db.navigation.Config;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
		
        // Generate JWT access token
        String jwt = jwtUtil.generateToken(userDetails);
        // Generate refresh token
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        
        System.out.println("jwt: "+ jwt);
        
        //add jwt as a header
        response.setHeader("Authorization", "Bearer " + jwt); // this only available for redirect request status code 302, then browser will generate totally new request to the redirect location which wont include this info in header
        
//     // For testing - write the token to the response
//        response.setContentType("application/json");
//        PrintWriter writer = response.getWriter();
//        writer.write("{\"jwt\": \"" + jwt + "\"}");
//        writer.flush();
     // Store JWT in session
        request.getSession().setAttribute("jwt_token", jwt);// only for testing purpose that I add jwt in session
        
//        //Store JWT in cookie
//        Cookie accessCookie = new Cookie("access_token", jwt);
//        accessCookie.setHttpOnly(true);
//        accessCookie.setPath("/");
//        accessCookie.setMaxAge(3600*24); // 24 hrs-1day
        
        // Store refresh token in cookie
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/auth/refresh"); //only include the cookie in this path
        refreshCookie.setMaxAge(604800); // 7 days
        
        
        //send freresh + access token
        response.addCookie(refreshCookie);
//        response.addCookie(accessCookie);
		response.sendRedirect("/tenant/test/welcome"); 
	}
}
