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
public class User implements Serializable {
    private String uId;
    private String uName;
    private String uPassword;
    private String uEmail;
    private String uPhone;
    private Long uIsAdmin;
}
