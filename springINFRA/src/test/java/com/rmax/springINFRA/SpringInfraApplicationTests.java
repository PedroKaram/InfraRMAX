package com.rmax.springINFRA;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rmax.Auth.CustomValidators.PasswordValidator;
import com.rmax.Auth.TenantServices.TenantService;
import com.rmax.Core.Tenants.Roles;
import com.rmax.Core.Tenants.Tenant;
import com.rmax.Shared.TenantRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SpringInfraApplicationTests {
	 @Mock
    private TenantRepository tenantRepository;

    @Mock
    private PasswordValidator passwordValidation;

    @InjectMocks
    private TenantService tenantService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

	@Test
    public void testAddTenant() throws Exception {
		Roles givenRole = Roles.USER;
		Tenant tenant = new Tenant();
		tenant.setUsername("testuser");
		tenant.setEmail("test@example.com");
		tenant.setFirstName("John");
		tenant.setLastName("Doe");
		tenant.setRole(givenRole);
		tenant.setPassword("Coxinha31@");

		when(tenantRepository.findByUsername(tenant.getUsername())).thenReturn(null);
		when(tenantRepository.findByEmail(tenant.getEmail())).thenReturn(null);

		when(passwordValidation.encode(tenant.getPassword())).thenReturn("encodedPassword");

		tenantService.addTenant(tenant);

		verify(tenantRepository).save(argThat(savedTenant ->
				savedTenant.getUsername().equals(tenant.getUsername()) &&
				savedTenant.getEmail().equals(tenant.getEmail()) &&
				savedTenant.getFirstName().equals(tenant.getFirstName()) &&
				savedTenant.getLastName().equals(tenant.getLastName()) &&
				savedTenant.getRole().equals(tenant.getRole()) &&
				savedTenant.getPassword().equals("encodedPassword")
		));
    }

	@Test
	public void testSetInvalidPasswordFormat() throws Exception {
		PasswordValidator passwordValidator = new PasswordValidator();
		String invalidPassword = "123";
	
		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> passwordValidator.encode(invalidPassword));
	
		assertEquals("Invalid password format", exception.getMessage());
	}

    @Test
    public void testAddTenantDuplicateUsername() throws Exception {
		Roles givenRole = Roles.USER;
        Tenant tenant = new Tenant();
        tenant.setUsername("existinguser");
        tenant.setEmail("test@example.com");
        tenant.setFirstName("John");
        tenant.setLastName("Doe");
        tenant.setRole(givenRole);
        tenant.setPassword("password123");

        when(tenantRepository.findByUsername(tenant.getUsername())).thenReturn(new Tenant());

        Exception exception = assertThrows(Exception.class, () -> tenantService.addTenant(tenant));
		assertEquals("Usuario já cadastrado", exception.getMessage());

    }

    @Test
    public void testAddTenantDuplicateEmail() throws Exception {
		Roles givenRole = Roles.USER;
        Tenant tenant = new Tenant();
        tenant.setUsername("testuser");
        tenant.setEmail("existing@example.com");
        tenant.setFirstName("John");
        tenant.setLastName("Doe");
        tenant.setRole(givenRole);
        tenant.setPassword("password123");
		
        when(tenantRepository.findByEmail(tenant.getEmail())).thenReturn(new Tenant());

        Exception exception = assertThrows(Exception.class, () -> tenantService.addTenant(tenant));
		assertEquals("Usuario já cadastrado", exception.getMessage());

        
    }

}
