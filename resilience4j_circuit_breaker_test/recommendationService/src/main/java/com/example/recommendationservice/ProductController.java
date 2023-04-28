package com.example.recommendationservice;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendation")
public class ProductController {

    @GetMapping("/products/{userId}")
    public ResponseEntity<List<String>> recommendProduct(@PathVariable Long userId) {
        List<String> products = Arrays.asList("유자민트티", "슈크림 가득 바움쿠헨",
                "아이스 자몽 허니 블랙티", "더블 에스프레소 칩 프라푸치노", "아이스 카페 라떼");

        return ResponseEntity.ok(products);
    }
}
