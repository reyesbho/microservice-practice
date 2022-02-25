package f4.practice.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import f4.practice.store.model.Category;
import f4.practice.store.model.Product;
import f4.practice.store.service.ProductService;
import f4.practice.store.utils.ErrorMenssage;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<List<Product>> listProducts(@RequestParam(name = "categoryId", required = false) Long idCategory){
        List<Product> products = new ArrayList<>();

        if (null == idCategory){
            products = this.productService.listAllProduct();
            if (products.isEmpty()){
                return ResponseEntity.noContent().build();
            }
        }else{
            products = this.productService.findByCategory(Category.builder().id(idCategory).build());
            if (products.isEmpty()){
                return  ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable(name = "id") Long id){
        Product product = this.productService.getProduct(id);
        if (null == product){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result){
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }

        Product productCreated = this.productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreated);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") long id,@RequestBody Product product){
        product.setId(id);
        Product productDb = this.productService.updateProduct(product);
        if (productDb == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDb);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable(name = "id") Long id){

        Product product = this.productService.deleteProduct(id);
        if (null == product){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateStockProduct(@PathVariable("id") Long id,@RequestParam(value = "quantity", required = true) Double quantity){
        Product product = this.productService.updateStock(id,quantity);
        if (null == product){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(product);

    }


    private String formatMessage(BindingResult result){
        List<Map<String, String>> errors = result.getFieldErrors().stream()
                .map( err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMenssage errorMenssage = ErrorMenssage.builder()
                .code("01")
                .messages(errors).build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try{
            jsonString = mapper.writeValueAsString(errorMenssage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;
    }
}
