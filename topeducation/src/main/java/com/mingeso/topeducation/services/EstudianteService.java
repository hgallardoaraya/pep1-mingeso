package com.mingeso.topeducation.services;

import com.mingeso.topeducation.entities.*;
import com.mingeso.topeducation.exceptions.RegistroNoExisteException;
import com.mingeso.topeducation.repositories.*;
import com.mingeso.topeducation.requests.IngresarEstudianteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstudianteService {
    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private TipoColegioRepository tipoColegioRepository;
    @Autowired
    private TipoPagoArancelRepository tipoPagoArancelRepository;
    @Autowired
    private MaxCuotasTipoColegioRepository maxCuotasTipoColegioRepository;

    public Estudiante ingresarEstudiante(IngresarEstudianteRequest request){
        Estudiante estudiante = request.getEstudiante();
        Integer idTipoColegio = request.getIdTipoColegio();
        Integer idTipoPagoArancel = request.getIdTipoPagoArancel();
        System.out.println(estudiante);

        Optional<TipoColegio> tipoColegio = tipoColegioRepository.findById(idTipoColegio);
        if(tipoColegio.isEmpty()) throw new RegistroNoExisteException("El colegio " + idTipoColegio + " no existe.");

        Optional<TipoPagoArancel> tipoPagoArancel = tipoPagoArancelRepository.findById(idTipoPagoArancel);
        if(tipoPagoArancel.isEmpty()) throw new RegistroNoExisteException("El tipo de pago arancel " + idTipoPagoArancel + " no existe.");

        estudiante.setTipoColegio(tipoColegio.get());
        estudiante.setTipoPagoArancel(tipoPagoArancel.get());

        return estudianteRepository.save(estudiante);
    }

    public Integer obtenerMaxCuotas(String rut){
        Optional<Estudiante> estudiante = estudianteRepository.findByRut(rut);
        if(estudiante.isEmpty()) throw new RegistroNoExisteException("El estudiante con rut " + rut + " no existe.");
        return maxCuotasTipoColegioRepository.findMaxCuotasByTipoColegio(estudiante.get().getTipoColegio().getId());
    }
}
