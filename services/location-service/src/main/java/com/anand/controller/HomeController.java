package com.anand.controller;

import com.anand.payload.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping()
    public ApiResponse HomeController(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("hello everyone i'm location service of airline microservice");
        return apiResponse;
    }
}
