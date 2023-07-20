package com.rmax.Auth.Security;

import com.rmax.Core.Tenants.Tenant;
import com.rmax.Shared.TenantRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private TenantRepository tenantRepository;

    public CustomUserDetailsService(TenantRepository tenantRepository){
        this.tenantRepository = tenantRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Tenant user = tenantRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Usuario não encontrado");
        }
        return new CustomUserDetails(user);
    }
}
