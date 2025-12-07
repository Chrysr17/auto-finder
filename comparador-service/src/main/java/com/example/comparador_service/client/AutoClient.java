package com.example.comparador_service.client;

import com.example.comparador_service.dto.AutoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auto-service", url = "http://localhost:9031")
public interface AutoClient {

    AutoDTO obtenerAuto(@PathVariable Long id);
}
