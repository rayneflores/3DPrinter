package com.ryfsystems.a3dprinter.models;

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
    private String pId;
    private String pName;
    private String pBrand;
    private String pModel;
    private String pImage;

    @Override
    public String toString() {
        return "Printer{" +
                "pId=" + pId +
                ", pName='" + pName + '\'' +
                ", pBrand='" + pBrand + '\'' +
                ", pModel='" + pModel + '\'' +
                ", pImage='" + pImage + '\'' +
                '}';
    }
}
