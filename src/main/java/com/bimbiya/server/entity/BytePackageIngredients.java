package com.bimbiya.server.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Byte_package_ingredients")
public class BytePackageIngredients implements Serializable {

    @EmbeddedId
    private BytePackageIngredientsId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredients_id", referencedColumnName = "ingredients_id", insertable = false, updatable = false)
    private Ingredients ingredients;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", referencedColumnName = "package_id", insertable = false, updatable = false)
    private BytePackage bytePackage;
}
