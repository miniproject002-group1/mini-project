package com.example.categoryservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories/test")
public class TestController {

  @GetMapping("/hello")
  public String getAnonymous() {
    return "Hello from Category Service! This endpoint is now secure.";
  }
}
