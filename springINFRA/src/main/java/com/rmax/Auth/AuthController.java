package com.rmax.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rmax.Auth.AuthDTOs.LoginDTO;
import com.rmax.Auth.AuthDTOs.LoginResponse;
import com.rmax.Auth.JWT.JWTUtility;
import com.rmax.Auth.Security.CustomUserDetailsService;
import com.rmax.Core.Tenants.Tenant;
import com.rmax.Shared.TenantRepository;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JWTUtility jwtUtility;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @GetMapping("/logged")
    public String logged(){
        return "YOU'RE LOGGED!";
    }
    @PostMapping("/authenticate")
    @CrossOrigin()
    public LoginResponse authenticate(@RequestBody LoginDTO loginDTO) throws Exception{
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
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginDTO.getUsername()  );
        final String token = jwtUtility.generateToken(userDetails);
        return new LoginResponse(token);


    }

    @PostMapping("createUser")
    public Tenant addUser(@RequestBody Tenant tenant){
        passwordEncoder = new BCryptPasswordEncoder();
        Tenant newTenant = new Tenant();
        newTenant.setEmail(tenant.getEmail()); 
        newTenant.setFirstName(tenant.getFirstName()); 
        newTenant.setLastName(tenant.getLastName()); 
        newTenant.setEmail(tenant.getEmail()); 
        newTenant.setRole(tenant.getRole()); 
        newTenant.setPassword(passwordEncoder.encode(tenant.getPassword()));
        newTenant.setUsername(tenant.getUsername());
        return tenantRepository.save(newTenant);
    }
}
