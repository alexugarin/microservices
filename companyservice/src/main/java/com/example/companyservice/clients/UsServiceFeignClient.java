package com.example.companyservice.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name= "user-service",
        path="/api/users",
        url="http://localhost:8081"
)

public interface UsServiceFeignClient {
    @GetMapping("/exist-by-id/{directorId}")
    Boolean existById(@PathVariable("directorId") Long companyId);

    @GetMapping("/exist-by-id-for-company/{userId}")
    boolean existByIdCompany(@PathVariable("userId") Long userId);

    @GetMapping("/name-by-id/{id}")
    String getNameById(@PathVariable("id") Long id);
}
