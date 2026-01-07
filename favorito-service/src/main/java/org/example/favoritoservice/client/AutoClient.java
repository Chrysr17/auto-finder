package org.example.favoritoservice.client;

import org.example.favoritoservice.dto.AutoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auto-service", url = "http://localhost:9031")
public interface AutoClient {

    @GetMapping("/api/autos/{id}")
    AutoDTO obtenerAuto(@PathVariable Long id);

}