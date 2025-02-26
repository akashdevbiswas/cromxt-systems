package com.cromxt.routeservice.controller;

import com.cromxt.routeservice.dtos.BucketInformationDTO;
import com.cromxt.routeservice.service.RouteService;
import com.cromxt.routeservice.service.impl.AvailableRouteDiscovererService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/admin")
public record AdminController (
        AvailableRouteDiscovererService availableRouteDiscovererService
){


    @GetMapping("/online-routes")
    public Flux<BucketInformationDTO> getAllOnlineBuckets() {
        return availableRouteDiscovererService.getAllOnlineBuckets();
    }
}
