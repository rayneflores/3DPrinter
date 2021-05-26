package com.ryfsystems.a3dprinter.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Integer uId;
    private Integer rType;
    private String rName;

    @Override
    public String toString() {
        return rType + " - " + rName;
    }
}
