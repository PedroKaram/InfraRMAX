package com.rmax.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rmax.Application.TenantServices.TenantService;
import com.rmax.Auth.AuthDTOs.LoginDTO;
import com.rmax.Auth.AuthDTOs.LoginResponse;
import com.rmax.Core.Tenants.Tenant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private TenantService tenantService;

    @GetMapping("/logged")
    public String logged(){
        return "YOU'RE LOGGED!";
    }

    
    @PostMapping("/authenticate")
    @CrossOrigin()
    public LoginResponse authenticate(@RequestBody LoginDTO loginDTO) throws Exception{
        return tenantService.AuthenticateService(loginDTO);
    }

    @PostMapping("createUser")
    public Tenant addUser(@RequestBody Tenant tenant) throws Exception{
        return tenantService.addTenant(tenant);
    }
}
