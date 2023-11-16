package com.bimbiya.server.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "Byte_package")
public class BytePackage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Long packageId;

    @Lob
    @Column(name = "img", nullable = false)
    private byte[] img;

    @Column(name = "meal_name", nullable = false, length = 32)
    private String mealName;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "potion", nullable = false, length = 16)
    private String potion;

    @Column(name = "status", nullable = false, length = 16)
    private String status;

    @Column(name = "CREATED_USER", nullable = false, length = 64)
    private String createdUser;
    @Column(name = "LAST_UPDATED_USER", nullable = false, length = 64)
    private String lastUpdatedUser;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME", nullable = false, length = 23)
    private Date createdTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED_TIME", nullable = false, length = 23)
    private Date lastUpdatedTime;

//    @OneToMany(mappedBy = "bytePackage", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<BytePackageIngredients> packageIngredients = new ArrayList<>();

}
