package com.example.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/properties/description")
public class PropertyController {
    private final Config config;

    @GetMapping
    public String getDescriptionProperty() {
        return config.getDescriptionProperty();
    }
}
