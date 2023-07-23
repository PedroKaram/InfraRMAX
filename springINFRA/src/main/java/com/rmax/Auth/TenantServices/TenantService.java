package com.rmax.Auth.TenantServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.rmax.Auth.AuthDTOs.LoginDTO;
import com.rmax.Auth.AuthDTOs.LoginResponse;
import com.rmax.Auth.CustomValidators.PasswordValidator;
import com.rmax.Auth.JWT.JWTUtility;
import com.rmax.Auth.Security.CustomUserDetailsService;
import com.rmax.Core.Tenants.Tenant;
import com.rmax.Shared.TenantRepository;

@Service
public class TenantService {
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private PasswordValidator passwordValidation;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public LoginResponse AuthenticateService(@RequestBody LoginDTO loginDTO) throws Exception{
        try 
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
                   
            );
        }
        catch(BadCredentialsException ex) 
        {
            throw new Exception("Credenciais erradas", ex);
        }
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginDTO.getUsername());
        final String token = jwtUtility.generateToken(userDetails);
        return new LoginResponse(token);
    }

    public Tenant addTenant(Tenant tenant)  throws Exception {
        if(tenantRepository.findByUsername(tenant.getUsername()) != null || tenantRepository.findByEmail(tenant.getEmail()) != null){
            throw new Exception("Usuario já cadastrado");
        }

        Tenant newTenant = Tenant.builder()
                            .firstName(tenant.getFirstName())
                            .lastName(tenant.getLastName())
                            .email(tenant.getEmail())
                            .role(tenant.getRole())
                            .password(passwordValidation.encode(tenant.getPassword()))
                            .username(tenant.getUsername())
                            .build();

        return tenantRepository.save(newTenant);
    }
}
