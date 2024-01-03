package com.mingeso.topeducation.requests;

import com.mingeso.topeducation.entities.Razon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerarRazonesRequest {
    Razon razon;
    Integer idTipoRazon;
    Integer idEstadoRazon;
    String rutEstudiante;
}
