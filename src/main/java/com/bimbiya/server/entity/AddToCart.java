package com.bimbiya.server.entity;

import com.bimbiya.server.util.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "add_to_cart")
public class AddToCart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "byte_id", referencedColumnName = "package_id")
    private Product bpackage;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private SystemUser systemUser;

    @Column(name = "qty", nullable = false, length = 16)
    private Integer qty;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "person_count")
    private Integer personCount;
}
