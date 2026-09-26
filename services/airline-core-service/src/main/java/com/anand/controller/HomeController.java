package com.anand.controller;

import com.anand.payload.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ApiResponse homeController(){
        ApiResponse apiResponse = new ApiResponse("Hey I'm Airline core service");
        return apiResponse;
    }
}
