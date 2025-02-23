# Multi-tenant-Spring-Security

1. Setup Docker: 
docker-compose -f docker-compose.yml up

2. Setup Database :
- global-multi-tenant/default: 
    - added "db_info" table. This table has only 1 column "message", value " welcome to global DB"
    - manually add some owners to owners table --> this table is used to test login function for global DB 

3. Testing method:
- After success login, CustomSuccessHandler is called:

```java
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		response.sendRedirect("/tenant/test/welcome");
	}
}
```
- Test login for global DB: 
    - no header 
    - username and password of an owner set up from step 2
    Expected outcome: access to db_info and display message
- Test login for tenant DB
    - manually create a tenant DB in "tenants" table and update "owners" table accordingly
    - manually create a database in mysql workbench 
    ( above two steps can be done with create-new-tenant POST method)
    - manually add 1 user inside the new tenant
    - set header as the tenant's name
    - test with username and password of the tenant's user
4. List of newly added packages / classes:

Config : 

    - SecurityConfig
    - CustomSuccessHandler
    - TenantFilter
    - WebConfig

Controller:

    - RoutingTestController (for testing purpose )

dto: (new package)

    - CustomUserDetails

Entity/Tenant:

    -  User --> TenantUser ( just changed name)

Repository/Tenant:

    - UserRepository --> TenantUserRepository

Service:

    - CustomUserDetailsService
    - RoutingDBTestService ( for testing purpose)
    
    

