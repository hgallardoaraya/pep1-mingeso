package com.mingeso.topeducation.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "interes_meses_atraso")
public class InteresMesesAtraso {
    @Id
    Integer id;
    @Column(name = "meses_atraso")
    Integer mesesAtraso;
    @Column(name = "porcentaje_interes")
    Integer porcentajeInteres;
}
