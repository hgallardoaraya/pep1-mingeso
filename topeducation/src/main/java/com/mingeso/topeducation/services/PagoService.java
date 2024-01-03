package com.mingeso.topeducation.services;

import com.mingeso.topeducation.entities.*;
import com.mingeso.topeducation.exceptions.FechaNoPermitidaException;
import com.mingeso.topeducation.exceptions.RegistroNoExisteException;
import com.mingeso.topeducation.exceptions.ValorFueraDeRangoException;
import com.mingeso.topeducation.repositories.EstadoRazonRepository;
import com.mingeso.topeducation.repositories.EstudianteRepository;
import com.mingeso.topeducation.repositories.PagoRepository;
import com.mingeso.topeducation.repositories.RazonRepository;
import com.mingeso.topeducation.requests.RegistrarPagoRequest;
import com.mingeso.topeducation.utils.EntradaReporteResumen;
import com.mingeso.topeducation.utils.Util;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {
    private final PagoRepository pagoRepository;
    private final EstudianteRepository estudianteRepository;
    private final RazonRepository razonRepository;
    private final EstadoRazonRepository estadoRazonRepository;
    @Autowired
    public PagoService(PagoRepository pagoRepository, EstudianteRepository estudianteRepository, RazonRepository razonRepository, EstadoRazonRepository estadoRazonRepository){
        this.pagoRepository = pagoRepository;
        this.estudianteRepository = estudianteRepository;
        this.razonRepository = razonRepository;
        this.estadoRazonRepository = estadoRazonRepository;
    }

    //Existe un pago que contiene todas las razones que se desean pagar.
    @Transactional
    public Pago registrarPago(RegistrarPagoRequest request, LocalDate fechaActual) {
        if(fechaActual.isBefore(Util.obtenerFechaInicioProceso(fechaActual))) throw new FechaNoPermitidaException("No está permitido pagar fuera del rango del 5 y el 10 de cada mes.");
        Pago pago = new Pago();

        Optional<Estudiante> estudiante = estudianteRepository.findByRut(request.getRut());
        if(estudiante.isEmpty()) throw new RegistroNoExisteException("El estudiante con rut " + request.getRut() + " no existe.");
        pago.setEstudiante(estudiante.get());

        List<Razon> razones = new ArrayList<>();
        Integer total = 0;
        for(Integer idRazon : request.getIdsRazones()){
            Optional<Razon> razon = razonRepository.findById(idRazon);
            if(razon.isEmpty()) throw new RegistroNoExisteException("No existe la razon con id " + idRazon + ".");
            if(razon.get().getMonto() <= 0) throw new ValorFueraDeRangoException("El monto de la cuota no debe ser menor" +
                    " o igual a 0.");
            //estado "PAGADA"
            Optional<EstadoRazon> estadoPagada = estadoRazonRepository.findById(0);
            if(estadoPagada.isEmpty()) throw new RegistroNoExisteException("Error al obtener el estado de id 0.");
            razon.get().setEstado(estadoPagada.get());
            razones.add(razon.get());
            razonRepository.save(razon.get());
            total += razon.get().getMonto();
        }

        if(razones.isEmpty()) throw new RegistroNoExisteException("No se indicaron o no existen razones.");

        pago.setRazones(razones);
        pago.setTotal(total);
        pago.setFecha(LocalDate.now());

        return pagoRepository.save(pago);
    }

    public List<Razon> obtenerRazonesAPagar(String rut, LocalDate fechaActual) {
        if(fechaActual.isBefore(Util.obtenerFechaInicioProceso(fechaActual))) throw new FechaNoPermitidaException("No está permitido pagar fuera del rango del 5 y el 10 de cada mes.");
        return razonRepository.findCuotasAPagarByRutAndDate(rut, Util.obtenerFechaInicioProceso(fechaActual));
    }

    /*▪ RUT estudiante <Listo>
    ▪ Nombre del estudiante <Listo>
    ▪ Nro. exámenes rendidos <Listo>
    ▪ Promedio puntaje exámenes <Listo>
    ▪ Monto total arancel a pagar <Listo>
    ▪ Tipo Pago (Contado/Cuotas) <Listo>
    ▪ Nro. total de cuotas pactadas <Listo>
    ▪ Nro. cuotas pagadas
    ▪ Monto total pagado
    ▪ Fecha último pago
    ▪ Saldo por pagar
    ▪ Nro. Cuotas con retraso*/
    public List<EntradaReporteResumen> calcularReporteResumen(){
        List<EntradaReporteResumen> reporte = new ArrayList<>();
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        if(estudiantes.isEmpty()) throw new RegistroNoExisteException("No existen estudiantes registrados.");
        for(Estudiante estudiante : estudiantes){
            List<Razon> razones = estudiante.getRazones();
            List<Pago> pagos = estudiante.getPagos();
            List<Examen> examenes = estudiante.getExamenes();
            EntradaReporteResumen entrada = EntradaReporteResumen.builder()
                    .rut(estudiante.getRut())
                    .numeroExamenesRendidos(examenes == null ? 0 : examenes.size())
                    .promedioExamenes(examenes == null ? 0 : calcularPromedioExamenes(examenes))
                    .totalArancel(calcularTotalArancel(razones))
                    .tipoPago(estudiante.getTipoPagoArancel().getTipo())
                    .numeroCuotasPactadas(estudiante.getCuotasPactadas())
                    .numeroCuotasPagadas(calcularNumeroCuotasPagadas(razones))
                    .arancelPagado(calcularArancelPagado(razones))
                    .totalPagado(calcularTotalPagado(razones))
                    .fechaUltimoPago(pagos == null ? null : calcularFechaUltimoPago(pagos))
                    .saldoArancelPendiente(calcularArancelPendiente(razones))
                    .saldoTotalPendiente(calcularTotalPendiente(razones))
                    .numeroCuotasAtrasadas(calcularNumeroCuotasAtrasadas(razones))
                    .build();
            reporte.add(entrada);
        }
        return reporte;
    }

    private int calcularPromedioExamenes(List<Examen> examenes){
        int promedioExamenes = 0;
        if(examenes.isEmpty()) return 0;
        for(Examen examen : examenes){
            if(examen.getPuntaje() == null) examen.setPuntaje(0);
            promedioExamenes += examen.getPuntaje();
        }
        promedioExamenes /= examenes.size();
        return promedioExamenes;
    }

    private int calcularTotalArancel(List<Razon> razones){
        int totalArancel = 0;
        int anioActual = LocalDate.now().getYear();
        for(Razon razon : razones){
            //Si es arancel calcular total
            if((razon.getTipo().getId() == 1)
                    && ((razon.getFechaInicio().getYear() == anioActual)
                        || ((razon.getFechaInicio().getYear() - anioActual) == 1
                        && razon.getFechaInicio().getMonth().equals(Month.JANUARY)))
            ){
                totalArancel += razon.getMonto();
            }
        }
        return totalArancel;
    }

    private int calcularTotalPendiente(List<Razon> razones){
        int totalPendiente = 0;
        for(Razon razon : razones){
            //Acá no hay restricción de año ya que puede ser un arancel con interes acumulado de años con una deuda millonaria :p.
            //se busca todos los pendientes y atrasados
            if(((razon.getEstado().getId() == 1) || (razon.getEstado().getId() == 2))){
                totalPendiente += razon.getMonto();
            }
        }
        return totalPendiente;
    }

    private int calcularArancelPendiente(List<Razon> razones){
        int arancelPendiente = 0;
        for(Razon razon : razones){
            //Acá no hay restricción de año ya que puede ser un arancel con interes acumulado de años con una deuda millonaria :p.
            if((razon.getTipo().getId() == 1)
                    && ((razon.getEstado().getId() == 1) || (razon.getEstado().getId() == 2))){
                arancelPendiente += razon.getMonto();
            }
        }
        return arancelPendiente;
    }

    private int calcularTotalPagado(List<Razon> razones){
        int totalPagado = 0;
        int anioActual = LocalDate.now().getYear();
        for(Razon razon : razones){
            if(((razon.getFechaInicio().getYear() == anioActual)
                    || ((razon.getFechaInicio().getYear() - anioActual) == 1
                    && razon.getFechaInicio().getMonth().equals(Month.JANUARY)))
                    && (razon.getEstado().getId() == 0)){
                totalPagado += razon.getMonto();
            }
        }
        return totalPagado;
    }

    private int calcularArancelPagado(List<Razon> razones){
        int arancelPagado = 0;
        int anioActual = LocalDate.now().getYear();
        for(Razon razon : razones){
            if((razon.getTipo().getId() == 1)
                    && ((razon.getFechaInicio().getYear() == anioActual)
                        || ((razon.getFechaInicio().getYear() - anioActual) == 1
                        && razon.getFechaInicio().getMonth().equals(Month.JANUARY)))
                    && (razon.getEstado().getId() == 0)){
                arancelPagado += razon.getMonto();
            }
        }
        return arancelPagado;
    }

    private int calcularNumeroCuotasPagadas(List<Razon> razones){
        int numeroCuotasPagadas = 0;
        int anioActual = LocalDate.now().getYear();
        for(Razon razon : razones){
            if((razon.getTipo().getId() == 1)
                    && ((razon.getFechaInicio().getYear() == anioActual)
                        || ((razon.getFechaInicio().getYear() - anioActual) == 1
                        && razon.getFechaInicio().getMonth().equals(Month.JANUARY)))
                    && (razon.getEstado().getId() == 0)){
                numeroCuotasPagadas += 1;
            }
        }
        return numeroCuotasPagadas;
    }

    private LocalDate calcularFechaUltimoPago(List<Pago> pagos){
        if(pagos.size() < 1) return null;
        LocalDate fechaUltimoPago = pagos.get(0).getFecha();
        for(Pago pago : pagos){
            if(pago.getFecha().isAfter(fechaUltimoPago)){
                fechaUltimoPago = pago.getFecha();
            }
        }
        return fechaUltimoPago;
    }

    private int calcularNumeroCuotasAtrasadas(List<Razon> razones){
        int numeroCuotasAtrasadas = 0;
        for(Razon razon : razones){
            //También se elimina restricción de año
            if((razon.getTipo().getId() == 1)
                    && (razon.getEstado().getId() == 2)){
                numeroCuotasAtrasadas += 1;
            }
        }
        return numeroCuotasAtrasadas;
    }

}

