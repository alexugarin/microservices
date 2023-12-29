package com.example.userservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name= "company-service",
        path="/api/companies",
        url="http://localhost:8082"
)
public interface CompanyServiceFeignClient {
    @GetMapping("/exist-by-id/{companyId}")
    Boolean existById(@PathVariable("companyId") Long companyId);

    @GetMapping("/name-by-id/{id}")
    String getNameById(@PathVariable("id") Long id);
}
