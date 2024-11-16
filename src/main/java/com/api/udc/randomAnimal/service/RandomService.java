package com.api.udc.randomAnimal.service;

import com.api.udc.util.response.CustomApiResponse;

import java.util.List;

public interface RandomService {
    CustomApiResponse<List<double[]>> randomAnimal();
}
