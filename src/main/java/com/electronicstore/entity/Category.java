package com.electronicstore.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="categories")
public class Category {

    @Id
    private String categoryId;

    @Column(name="cate_title")
    private String title;

    @Column(name="cate_desc")
    private String description;

    @Column(name="cate_coverImage")
    private String coverImage;

    @OneToMany(mappedBy ="category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products=new ArrayList<>();
}
