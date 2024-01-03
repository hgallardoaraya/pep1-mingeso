package com.mingeso.topeducation.services;

import com.mingeso.topeducation.entities.*;
import com.mingeso.topeducation.exceptions.FechaNoPermitidaException;
import com.mingeso.topeducation.exceptions.RegistroNoExisteException;
import com.mingeso.topeducation.exceptions.ValorFueraDeRangoException;
import com.mingeso.topeducation.repositories.*;
import com.mingeso.topeducation.utils.Util;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;

@Service
public class RazonService {
    RazonRepository razonRepository;
    TipoRazonRepository tipoRazonRepository;
    EstadoRazonRepository estadoRazonRepository;
    EstudianteRepository estudianteRepository;
    TotalRazonRepository totalRazonRepository;
    DescuentoTipoPagoArancelRepository descuentoTipoPagoArancelRepository;
    DescuentoTipoColegioRepository descuentoTipoColegioRepository;
    DescuentoAnioEgresoRepository descuentoAnioEgresoRepository;
    DescuentoPuntajePruebaRepository descuentoPuntajePruebaRepository;
    ExamenRepository examenRepository;
    InteresMesesAtrasoRepository interesMesesAtrasoRepository;
    MaxCuotasTipoColegioRepository maxCuotasTipoColegioRepository;

    @Autowired
    RazonService(RazonRepository razonRepository,
         TipoRazonRepository tipoRazonRepository,
         EstadoRazonRepository estadoRazonRepository,
         EstudianteRepository estudianteRepository,
         TotalRazonRepository totalRazonRepository,
         DescuentoTipoPagoArancelRepository descuentoTipoPagoArancelRepository,
         DescuentoTipoColegioRepository descuentoTipoColegioRepository,
         DescuentoAnioEgresoRepository descuentoAnioEgresoRepository,
         DescuentoPuntajePruebaRepository descuentoPuntajePruebaRepository,
         InteresMesesAtrasoRepository interesMesesAtrasoRepository,
         ExamenRepository examenRepository,
         MaxCuotasTipoColegioRepository maxCuotasTipoColegioRepository
     ){
        this.razonRepository = razonRepository;
        this.tipoRazonRepository = tipoRazonRepository;
        this.estadoRazonRepository = estadoRazonRepository;
        this.estudianteRepository = estudianteRepository;
        this.totalRazonRepository = totalRazonRepository;
        this.descuentoTipoPagoArancelRepository = descuentoTipoPagoArancelRepository;
        this.descuentoTipoColegioRepository = descuentoTipoColegioRepository;
        this.descuentoAnioEgresoRepository = descuentoAnioEgresoRepository;
        this.descuentoPuntajePruebaRepository = descuentoPuntajePruebaRepository;
        this.examenRepository = examenRepository;
        this.interesMesesAtrasoRepository = interesMesesAtrasoRepository;
        this.maxCuotasTipoColegioRepository = maxCuotasTipoColegioRepository;
    }

    @Transactional
    public List<Razon> generarCuotas(String rut, Integer numCuotas){
        Optional<Estudiante> estudiante = estudianteRepository.findByRut(rut);
        if(estudiante.isEmpty()) throw new RegistroNoExisteException("El estudiante con rut " + rut + " no existe.");
        if(estudiante.get().getRazones() != null
                && estudiante.get().getRazones().size() > 0
                && estudiante.get().getRazones().get(0).getFechaInicio().getYear() == LocalDate.now().getYear()){
            throw new FechaNoPermitidaException("Ya se generaron las cuotas correspondientes a este proceso.");
        }
        if(numCuotas < 0) throw new ValorFueraDeRangoException("El número de cuotas es menor a 0.");
        if(numCuotas > maxCuotasTipoColegioRepository.findMaxCuotasByTipoColegio(estudiante.get().getTipoColegio().getId())) throw new ValorFueraDeRangoException("El número de cuotas excede el máximo.");

        Integer totalMatricula = totalRazonRepository.findTotalByTipoRazon("MATRICULA");
        Integer totalArancel = totalRazonRepository.findTotalByTipoRazon("ARANCEL");

        //Descuento por pagar en cuotas es 0, pero podría cambiar.
        Integer porcentajeDescuento = descuentoTipoPagoArancelRepository.findDescuentoByIdTipoPagoArancel(estudiante.get().getTipoPagoArancel().getId());

        //Si paga en cuotas
        if(estudiante.get().getTipoPagoArancel().getId() == 0){
            porcentajeDescuento += descuentoTipoColegioRepository.findDescuentoByIdTipoColegio(estudiante.get().getTipoColegio().getId());
            porcentajeDescuento += descuentoAnioEgresoRepository.findDescuentoByAnioEgreso(estudiante.get().getAnioEgreso());
        }

        //Aplicar porcentaje descuentos
        totalArancel = totalArancel - ((totalArancel * porcentajeDescuento) / 100);

        // Obtén la fecha actual
        LocalDate fechaActual = LocalDate.now();

        LocalDate fechaInicioClases = LocalDate.of(fechaActual.getYear(), Month.MARCH, 1);
        LocalDate fechaInicioMatricula = fechaInicioClases.minusDays(5);


        TipoRazon tipoMatricula = tipoRazonRepository.findById(0).get();
        EstadoRazon estadoPendiente = estadoRazonRepository.findById(1).get();
        EstadoRazon estadoAtrasada = estadoRazonRepository.findById(2).get();

        // Matricula
        EstadoRazon estadoMatricula = LocalDate.now().isAfter(fechaInicioMatricula) ? estadoAtrasada : estadoPendiente;
        Razon matricula = new Razon(0, totalMatricula, fechaInicioMatricula, fechaInicioClases, tipoMatricula, estadoMatricula, estudiante.get());
        Razon resultadoMatricula = razonRepository.save(matricula);

        List<Razon> razones = new ArrayList<>();
        razones.add(resultadoMatricula);

        //Generar cuotas arancel
        LocalDate fechaInicioArancel = LocalDate.of(fechaInicioClases.getYear(), fechaInicioClases.getMonth().plus(1), 5);
        LocalDate fechaFinArancel = LocalDate.of(fechaInicioClases.getYear(), fechaInicioClases.getMonth().plus(1), 10);

        Integer cuota = totalArancel / numCuotas;
        TipoRazon tipoArancel = tipoRazonRepository.findById(1).get();

        for(int i = 0; i < numCuotas; i++){
            Integer numero = i + 1;
            EstadoRazon estadoArancel = LocalDate.now().isAfter(fechaFinArancel) ? estadoAtrasada : estadoPendiente;
            Razon arancel = new Razon(numero, cuota, fechaInicioArancel, fechaFinArancel, tipoArancel, estadoArancel, estudiante.get());
            Razon resultadoArancel = razonRepository.save(arancel);
            razones.add(resultadoArancel);
            fechaInicioArancel = fechaInicioArancel.plusMonths(1);
            fechaFinArancel = fechaFinArancel.plusMonths(1);
        }

        estudiante.get().setCuotasPactadas(numCuotas);
        estudianteRepository.save(estudiante.get());

        return razones;
    }

    public List<Razon> obtenerRazones(String rut) {
        return razonRepository.findAllByRut(rut);
    }

    @Transactional
    public Map<String, List<Razon>> calcularPlanilla(LocalDate fechaActual){
        LocalDate fechaInicioProceso = Util.obtenerFechaInicioProceso(fechaActual);
        LocalDate fechaExamenesProceso = Util.obtenerFechaExamenesProceso(fechaInicioProceso);
        if(fechaActual.isBefore(fechaExamenesProceso) || fechaActual.isAfter(fechaInicioProceso.minusDays(1))){
            throw new FechaNoPermitidaException("El cálculo de planilla del proceso correspondiente debe ser realizado"
                    + " entre el último viernes del més (cuando se rindieron los últimos exámenes) " + fechaExamenesProceso
                    + " y el inicio de pago de arancel del proceso "
                    + fechaInicioProceso + ".");
        }

        aplicarDescuentosPorPuntajes(fechaActual);

        return aplicarInteresesPorMesesAtraso(fechaActual);
    }

    @Transactional
    public Map<String, List<Razon>> aplicarDescuentosPorPuntajes(LocalDate fechaActual){
        List<Examen> examenes = examenRepository.findAllSinRevision();

        if(examenes == null || examenes.isEmpty()){
            throw new RegistroNoExisteException("No hay exámenes para ser revisados.");
        }

        //Recibe exactamente los exámenes rendidos el último viernes del mes antes del próximo proceso de pago.
        //Es importante por lo mismo no equivocarse en las fechas de los exámenes del Excel.
        List<Examen> examenesProcesoActual = examenes
                .stream()
                .filter(examen -> examen.getFecha().isEqual(Util.obtenerFechaExamenesProceso(Util.obtenerFechaInicioProceso(fechaActual))))
                .toList();

        Map<String, List<Integer>> rutPuntajes = obtenerPuntajesPorRut(examenesProcesoActual);

        actualizarRevisionExamenes(examenesProcesoActual);

        HashMap<String, List<Razon>> cuotasResultantes = new HashMap<>();
        for(String rut : rutPuntajes.keySet()){
            //Calcular total
            Integer total = rutPuntajes.get(rut).stream().reduce(0, Integer::sum);
            //Calcular promedio
            //No es necesario verificar los limites del promedio ya que los datos fueron limpiados al importar los
            //exámenes.
            Integer promedio = total/rutPuntajes.get(rut).size();
            Integer porcentajeDescuento = descuentoPuntajePruebaRepository.findDescuentoByPuntajePromedio(promedio);
            List<Razon> cuotasArancel = razonRepository.findCuotasPendientesAndAtrasadasByRut(rut);
            if(cuotasArancel != null && cuotasArancel.size() > 0){
                for(Razon cuota : cuotasArancel){
                    //aplicar descuento
                    cuota.setMonto(cuota.getMonto() - ((cuota.getMonto() * porcentajeDescuento) / 100));
                    if(cuotasResultantes.containsKey(rut)){
                        cuotasResultantes.get(rut).add(cuota);
                        cuotasResultantes.put(rut, cuotasResultantes.get(rut));
                    }else{
                        List<Razon> cuotas = new ArrayList<>();
                        cuotas.add(cuota);
                        cuotasResultantes.put(rut, cuotas);
                    }
                    razonRepository.save(cuota);
                }
            }
        }
        return cuotasResultantes;
    }

    public Map<String, List<Razon>> aplicarInteresesPorMesesAtraso(LocalDate fechaActual){
        LocalDate fechaInicioProceso = Util.obtenerFechaInicioProceso(fechaActual);
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        Map<String, List<Razon>> cuotasConInteres = new HashMap<>();
        for(Estudiante estudiante : estudiantes){
            //obtener razones que sean de tipo arancel, estén pendientes o atrasadas y que sean anteriores a la fecha del proceso.
            List<Razon> cuotasArancelPendientesOAtrasadas = estudiante.getRazones().stream().filter(razon ->
                            (razon.getTipo().getId().equals(1))
                            && (razon.getEstado().getId().equals(1) || razon.getEstado().getId().equals(2))
            ).toList();

            List<Razon> cuotasArancelAntiguasPendientesOAtrasadas = cuotasArancelPendientesOAtrasadas.stream().filter(razon ->
                    (razon.getFechaFin().isBefore(fechaInicioProceso))
            ).toList();

            List<Integer> porcentajesInteres = obtenerPorcentajesInteres(cuotasArancelAntiguasPendientesOAtrasadas, fechaInicioProceso);
            cuotasConInteres.put(estudiante.getRut(), new ArrayList<>());
//            se aplican los intereses
            for(Razon cuota : cuotasArancelPendientesOAtrasadas){
                for(Integer porcentajeInteres : porcentajesInteres){
                    cuota.setMonto(cuota.getMonto() + ((cuota.getMonto() * porcentajeInteres) / 100));
                }
                cuotasConInteres.get(estudiante.getRut()).add(cuota);
                razonRepository.save(cuota);
            }
        }
        return cuotasConInteres;
    }

    public Map<String, List<Integer>> obtenerPuntajesPorRut(List<Examen> examenes){
        Map<String, List<Integer>> rutPuntajes = new HashMap<>();

        for(Examen examen : examenes){
            if(rutPuntajes.containsKey(examen.getEstudiante().getRut())){
                rutPuntajes.get(examen.getEstudiante().getRut()).add(examen.getPuntaje());
            }else{
                List<Integer> nuevaListaPuntajes = new ArrayList<>();
                nuevaListaPuntajes.add(examen.getPuntaje());
                rutPuntajes.put(examen.getEstudiante().getRut(), nuevaListaPuntajes);
            }
        }
        return rutPuntajes;
    }
    public List<Examen> actualizarRevisionExamenes(List<Examen> examenes){
        for(Examen examen : examenes){
            System.out.println(examen.getRevision());
            examen.setRevision(1);
            System.out.println(examen.getRevision());
            examenRepository.save(examen);
        }
        return examenes;
    }

    public List<Integer> obtenerPorcentajesInteres(List<Razon> cuotasArancelAntiguasPendientesOAtrasadas, LocalDate fechaInicioProceso){
        List<Integer> porcentajesInteres = new ArrayList<>();

        for(Razon cuota : cuotasArancelAntiguasPendientesOAtrasadas){
            //Si está pendiente, pasa a atrasada
            if(cuota.getEstado().getId() == 1){
                EstadoRazon estadoCuotaAtrasada = estadoRazonRepository.findById(2).get();
                cuota.setEstado(estadoCuotaAtrasada);
                razonRepository.save(cuota);
            }

            Period periodo = Period.between(cuota.getFechaInicio(), fechaInicioProceso);
            //usualmente los años son 0 y se reduce a solo la diferencia en meses.
            int mesesAtraso = periodo.getYears() * 12 + periodo.getMonths();
            Integer porcentajeInteres = interesMesesAtrasoRepository.findInteresByMesesAtraso(mesesAtraso);
            porcentajesInteres.add(porcentajeInteres);
        }

        return porcentajesInteres;
    }

}
