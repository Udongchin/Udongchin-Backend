package com.api.udc.randomAnimal.controller;

import com.api.udc.randomAnimal.service.RandomService;
import com.api.udc.util.response.CustomApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/random")
public class RandomController {
    private final RandomService randomService;

    public RandomController(RandomService randomService) {
        this.randomService = randomService;
    }

    @PostMapping
    public CustomApiResponse<List<double[]>> randomAnimal() {
        CustomApiResponse<List<double[]>> response=randomService.randomAnimal();
        return response;
    }
}
