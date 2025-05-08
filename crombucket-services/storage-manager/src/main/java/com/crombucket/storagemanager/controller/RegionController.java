package com.crombucket.storagemanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crombucket.common.ResponseBuilder;
import com.crombucket.storagemanager.dtos.requests.RegionRequest;
import com.crombucket.storagemanager.dtos.response.RegionResponse;
import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.service.RegionService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/regions")
public class RegionController {

  private final RegionService regionService;
  private final ResponseBuilder responseBuilder;

  @GetMapping(value = "/{regionCode}")
  public Mono<ResponseEntity<RegionResponse>> getRegionById(@PathVariable String regionCode) {
    Mono<RegionResponse> regionResponseMono = regionService.getRegionByCode(regionCode);
    return responseBuilder.buildResponseWithBody(regionResponseMono, HttpStatus.OK);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<ResponseEntity<RegionResponse>> createRegion(@RequestBody RegionRequest regionRequest) {
    // Mono<RegionResponse> regionResponse = regionService.createRegion(regionRequest);
    // return responseBuilder.buildResponseWithBody(regionResponse, HttpStatus.CREATED);
    return null;
  }

  @GetMapping(value = "/name/{regionName}")
  public Mono<ResponseEntity<Page<RegionResponse>>> findRegionsByName(
      @PathVariable String regionName,
      @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
      @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
    Mono<Page<RegionResponse>> regionNamesMono = regionService.findAllRegionsByName(regionName, pageNumber, pageSize);
    return responseBuilder.buildResponseWithBody(regionNamesMono, HttpStatus.OK);
  }

}
