package cl.milsabores.pasteleria.service;

import cl.milsabores.pasteleria.dto.ProductResponse;
import cl.milsabores.pasteleria.entity.Product;
import cl.milsabores.pasteleria.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MapperService mapper;

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(mapper::toProduct).toList();
    }

    public ProductResponse findByCodigo(String codigo) {
        Product product = productRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        return mapper.toProduct(product);
    }

    public List<String> findCategories() {
        return productRepository.findAll().stream()
                .map(Product::getCategoria)
                .distinct()
                .toList();
    }
}

