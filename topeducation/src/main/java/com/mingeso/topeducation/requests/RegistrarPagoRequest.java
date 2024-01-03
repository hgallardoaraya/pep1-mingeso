package com.mingeso.topeducation.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarPagoRequest {
    String rut;
    Integer[] idsRazones;
}
