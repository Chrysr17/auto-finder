package com.example.autoservice.controller;

import com.example.autoservice.service.AutoImagenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autos")
public class AutoImagenController {

    private final AutoImagenService autoImagenService;

    public AutoImagenController(AutoImagenService autoImagenService) {
        this.autoImagenService = autoImagenService;
    }
}
