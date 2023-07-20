package com.rmax.Shared;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rmax.Core.Tenants.Tenant;
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long>{
    Tenant findByUsername(String username);
}
