package academy.digitallab.store.shopping.client;


import academy.digitallab.store.shopping.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service")
@RequestMapping("api/v1/products")
public interface ProductClient {

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable(name = "id") Long id);

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateStockProduct(@PathVariable("id") Long id,@RequestParam(value = "quantity", required = true) Double quantity);
}
