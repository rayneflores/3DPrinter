package com.ryfsystems.a3dprinter.models;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServiceOrder implements Serializable {
    private String oId;
    private String uName;
    private String pName;
    private String pSerial;
    private Date oDate;
}
