package com.bimbiya.server.entity;

import com.bimbiya.server.util.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Hasintha_S
 * @date 1/8/2024.
 */


@Data
@Entity
@Table(name = "product_category")
public class ProductCategory extends CommonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "status", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private Status status;

}