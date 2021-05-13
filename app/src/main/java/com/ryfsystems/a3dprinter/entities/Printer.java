package com.ryfsystems.a3dprinter.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Printer implements Serializable {
    private Integer pId;
    private String pName;
    private String pBrand;
    private String pModel;
    private String pImage;
}
