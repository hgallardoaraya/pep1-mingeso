package com.mingeso.topeducation.requests;

import com.mingeso.topeducation.entities.Estudiante;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngresarEstudianteRequest {
    Estudiante estudiante;
    int idTipoColegio;
    int idTipoPagoArancel;
}
