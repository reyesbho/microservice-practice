package f4.practice.store;

import f4.practice.store.model.Category;
import f4.practice.store.model.Product;
import f4.practice.store.repository.ProductRepository;
import f4.practice.store.service.ProductService;
import f4.practice.store.service.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class ProductServiceMockTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;


    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        productService = new ProductServiceImpl(this.productRepository);
        Product computer = Product.builder()
                .name("Computer")
                .category(Category.builder().id(1l).build())
                .description("")
                .stock(Double.parseDouble("10"))
                .price(Double.parseDouble("1240.99"))
                .status("Created")
                .createAt(new Date())
                .build();

        Mockito.when(productRepository.findById(1l)).thenReturn(Optional.of(computer));
        Mockito.when(productRepository.save(computer)).thenReturn(computer);
    }

    @Test
    public void whenValidGetId_thenReturnProduct(){
        Product found = this.productService.getProduct(1l);
        Assertions.assertThat(found.getName()).isEqualTo("Computer");
    }

    @Test
    public void whenValidUpdateStock_then_ReturnNewStock(){
        Product newStock = this.productService.updateStock(1L, Double.parseDouble("8"));
        Assertions.assertThat(newStock.getStock()).isEqualTo(18);
    }

}
