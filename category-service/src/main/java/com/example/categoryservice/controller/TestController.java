package com.example.categoryservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories/test")
@SecurityRequirement(name = "bearerAuth")
public class TestController {

  @GetMapping("/hello")
  public String getAnonymous() {
    return "Hello from Category Service! This endpoint is now secure.";
  }
}
