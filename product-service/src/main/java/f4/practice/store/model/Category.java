package f4.practice.store.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_categories")
@Data
@Builder @AllArgsConstructor @NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

}
