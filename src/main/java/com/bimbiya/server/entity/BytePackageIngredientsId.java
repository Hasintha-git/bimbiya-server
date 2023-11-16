package com.bimbiya.server.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Embeddable
@ToString
@NoArgsConstructor
public class BytePackageIngredientsId implements Serializable {
    @NotNull
    @Column(name = "ingredients_id")
    private Long ingredients;

    @NotNull
    @Column(name = "package_id")
    private Long bytePackage;

}