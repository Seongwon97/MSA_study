package com.example.productservice;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/recommendation/{userId}")
    @CircuitBreaker(name = "productsBreaker", fallbackMethod = "productsFallback")
    public ResponseEntity<List<String>> findRecommendationProduct(@PathVariable Long userId) {
        System.out.println("OrderController.findRecommendationProduct() 호출");

        ResponseEntity<List<String>> response = restTemplate.exchange(
                "http://localhost:9090/recommendation/products/" + userId,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<String> products = response.getBody();

        return ResponseEntity.ok(products);
    }

    private ResponseEntity<List<String>> productsFallback(Throwable t) {
        List<String> defaultRecommendations = Arrays.asList("아이스아메리카노", "카페 라떼", "카라멜 마끼야또", "아이스 카페 모카", "카푸치노");

        System.out.println("OrderController.productsFallback() 호출");
        System.out.println(t.getMessage());

        return ResponseEntity.ok(defaultRecommendations);
    }
}
