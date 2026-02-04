package com.example.comparador_service.client;

import com.example.comparador_service.dto.AutoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auto-service", url = "AUTO_SERVICE_URL")
public interface AutoClient {

    @GetMapping("/api/autos/{id}")
    AutoDTO obtenerAuto(@PathVariable Long id);

}
