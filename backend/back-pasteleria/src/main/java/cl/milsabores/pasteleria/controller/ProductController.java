package cl.milsabores.pasteleria.controller;

import cl.milsabores.pasteleria.dto.ProductResponse;
import cl.milsabores.pasteleria.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ProductResponse> findByCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(productService.findByCodigo(codigo));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> categories() {
        return ResponseEntity.ok(productService.findCategories());
    }
}

