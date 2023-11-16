package com.bimbiya.server.entity;

import com.bimbiya.server.util.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ingredients")
public class Ingredients extends CommonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredients_id")
    private Long ingredientsId;

    @Column(name = "ingredients_name", nullable = false, length = 32)
    private String ingredientsName;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private Status statusCode;
}