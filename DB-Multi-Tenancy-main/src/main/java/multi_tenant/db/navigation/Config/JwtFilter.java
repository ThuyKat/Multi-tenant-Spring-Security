package multi_tenant.db.navigation.Config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.filters.ExpiresFilter.XServletOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import multi_tenant.db.navigation.Service.CustomUserDetailsService;
import multi_tenant.db.navigation.Utils.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;
		Map<String, Object> errorDetails = new HashMap<>();
		try {
			System.out.println("I am in jwt filter");
			//decode jwt to get username
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				username = jwtUtil.extractUsername(jwt);
			}
			System.out.println("username : "+ username);
			//validate jwt to bypass authentication
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				System.out.println("username is not null and no user been authenticated yet");
				// runtime poly
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
				// verify if the token is validate
				System.out.println("before validate token");
				if (jwtUtil.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					//set userDetail, bypass authentication 
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					
					System.out.println("Token is validated, done Jwt Filter...moving to successHandler? ");

				}
			}
		} catch (Exception e) {
			System.out.println("caught exception somehow!!");
			errorDetails.put("message", "Authentication Error");
			errorDetails.put("detail", e.getMessage());
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getWriter(), errorDetails);
		}
		// hand off the control to the next filter in the filter chain
		filterChain.doFilter(request, response);

	}

}
