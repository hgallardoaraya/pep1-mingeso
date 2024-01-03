package com.mingeso.topeducation;

import com.mingeso.topeducation.entities.*;
import com.mingeso.topeducation.exceptions.FechaNoPermitidaException;
import com.mingeso.topeducation.exceptions.RegistroNoExisteException;
import com.mingeso.topeducation.repositories.*;
import com.mingeso.topeducation.services.RazonService;
import com.mingeso.topeducation.utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.shortThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RazonServiceTest {
    @Mock
    RazonRepository razonRepository;
    @Mock
    TipoRazonRepository tipoRazonRepository;
    @Mock
    EstadoRazonRepository estadoRazonRepository;
    @Mock
    EstudianteRepository estudianteRepository;
    @Mock
    TotalRazonRepository totalRazonRepository;
    @Mock
    DescuentoTipoPagoArancelRepository descuentoTipoPagoArancelRepository;
    @Mock
    DescuentoTipoColegioRepository descuentoTipoColegioRepository;
    @Mock
    DescuentoAnioEgresoRepository descuentoAnioEgresoRepository;
    @Mock
    DescuentoPuntajePruebaRepository descuentoPuntajePruebaRepository;
    @Mock
    ExamenRepository examenRepository;
    @Mock
    InteresMesesAtrasoRepository interesMesesAtrasoRepository;
    @Mock
    MaxCuotasTipoColegioRepository maxCuotasTipoColegioRepository;
    @InjectMocks
    RazonService razonService;

    @Test
    void generarCuotasTest(){
        Estudiante estudiante = crearEstudiantePorDefecto();

        when(estudianteRepository.findByRut(estudiante.getRut())).thenReturn(Optional.of(estudiante));

        //colegio municipal
        when(maxCuotasTipoColegioRepository.findMaxCuotasByTipoColegio(estudiante.getTipoColegio().getId())).thenReturn(10);
        when(totalRazonRepository.findTotalByTipoRazon("MATRICULA")).thenReturn(70000);
        when(totalRazonRepository.findTotalByTipoRazon("ARANCEL")).thenReturn(1500000);
        //paga en cuotas, tiene 0 descuento por este motivo
        when(descuentoTipoPagoArancelRepository.findDescuentoByIdTipoPagoArancel(estudiante.getTipoPagoArancel().getId())).thenReturn(0);
        when(descuentoTipoColegioRepository.findDescuentoByIdTipoColegio(estudiante.getTipoColegio().getId())).thenReturn(20);
        when(descuentoAnioEgresoRepository.findDescuentoByAnioEgreso(estudiante.getAnioEgreso())).thenReturn(4);

        TipoRazon tipoMatricula = new TipoRazon();
        tipoMatricula.setId(0);
        tipoMatricula.setTipo("MATRICULA");

        EstadoRazon estadoPendiente = new EstadoRazon();
        estadoPendiente.setId(1);
        estadoPendiente.setEstado("PENDIENTE");

        EstadoRazon estadoAtrasada = new EstadoRazon();
        estadoAtrasada.setId(2);
        estadoAtrasada.setEstado("ATRASADA");

        when(tipoRazonRepository.findById(0)).thenReturn(Optional.of(tipoMatricula));
        when(estadoRazonRepository.findById(1)).thenReturn(Optional.of(estadoPendiente));
        when(estadoRazonRepository.findById(2)).thenReturn(Optional.of(estadoAtrasada));
        when(razonRepository.save(ArgumentMatchers.any(Razon.class))).thenAnswer(invocation -> invocation.getArgument(0, Razon.class));

        TipoRazon tipoArancel = new TipoRazon();
        tipoMatricula.setId(1);
        tipoMatricula.setTipo("ARANCEL");
        when(tipoRazonRepository.findById(1)).thenReturn(Optional.of(tipoArancel));

        List<Razon> resultados = razonService.generarCuotas(estudiante.getRut(), 2);

        Razon matriculaEsperada = new Razon(0, 70000, LocalDate.of(LocalDate.now().getYear(), Month.MARCH, 1).minusDays(5), LocalDate.of(LocalDate.now().getYear(), Month.MARCH, 1), tipoMatricula, estadoAtrasada, estudiante);
        Razon arancel1Esperado = new Razon(1, 570000, LocalDate.of(LocalDate.now().getYear(), Month.APRIL, 5), LocalDate.of(LocalDate.now().getYear(), Month.APRIL, 10), tipoArancel, estadoAtrasada, estudiante);
        Razon arancel2Esperado = new Razon(2, 570000, LocalDate.of(LocalDate.now().getYear(), Month.MAY, 5), LocalDate.of(LocalDate.now().getYear(), Month.MAY, 10), tipoArancel, estadoAtrasada, estudiante);

        List<Razon> razonesEsperadas = new ArrayList<>();
        razonesEsperadas.add(matriculaEsperada);
        razonesEsperadas.add(arancel1Esperado);
        razonesEsperadas.add(arancel2Esperado);

        assertEquals(razonesEsperadas, resultados);
    }

    @Test
    void generarCuotasEstudianteNoExisteTest(){
        Estudiante estudiante = crearEstudiantePorDefecto();

        when(estudianteRepository.findByRut(estudiante.getRut())).thenReturn(Optional.empty());
        assertThrows(RegistroNoExisteException.class, () -> razonService.generarCuotas(estudiante.getRut(), 2));

    }


    @Test
    void obtenerRazonesTest(){
        String rut = "11.222.333-4";
        ArrayList<Razon> razones = new ArrayList<>();

        when(razonRepository.findAllByRut(rut)).thenReturn(razones);

        List<Razon> resultado = razonService.obtenerRazones(rut);

        assertEquals(razones, resultado);
    }

    @Test
    void obtenerPuntajesPorRutTest(){
        List<Examen> examenes = crearExamenes();


        Estudiante estudiante1 = crearEstudiantePorDefecto();
        estudiante1.setRut("r1");
        examenes.get(0).setEstudiante(estudiante1);
        examenes.get(1).setEstudiante(estudiante1);
        Estudiante estudiante2 = crearEstudiantePorDefecto();
        estudiante2.setRut("r2");
        examenes.get(2).setEstudiante(estudiante2);
        examenes.get(3).setEstudiante(estudiante2);


        Map<String, List<Integer>> resultados = razonService.obtenerPuntajesPorRut(examenes);

        Map<String, List<Integer>> esperados = new HashMap<>();
        esperados.put("r1", List.of(900, 900));
        esperados.put("r2", List.of(500, 700));

        assertEquals(esperados, resultados);
    }

    @Test
    void actualizarRevisionExamenesTest(){
        List<Examen> examenes = crearExamenes();

        when(examenRepository.save(any(Examen.class))).thenReturn(any(Examen.class));

        List<Examen> resultados = razonService.actualizarRevisionExamenes(examenes);

        List<Examen> esperados = List.copyOf(examenes);
        esperados.get(0).setRevision(1);
        esperados.get(1).setRevision(1);

        assertEquals(esperados, resultados);
    }

    @Test
    void aplicarInteresesPorMesesAtrasoTest(){
        LocalDate fechaActualPrueba = LocalDate.of(2023, Month.OCTOBER, 3);

        Estudiante estudiante1 = crearEstudiantePorDefecto();
        Estudiante estudiante2 = crearEstudiantePorDefecto();
        estudiante2.setRut("r1");

        List<Razon> razones1 = crearRazones(1, 5);
        List<Razon> razones2 = crearRazones(6, 11);

        estudiante1.setRazones(razones1);
        estudiante2.setRazones(razones2);

        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(estudiante1);
        estudiantes.add(estudiante2);

        when(estudianteRepository.findAll()).thenReturn(estudiantes);

        //Función obtener porcentajes interés
        EstadoRazon estadoAtrasada = new EstadoRazon();
        estadoAtrasada.setId(2);
        estadoAtrasada.setEstado("ATRASADA");
        when(estadoRazonRepository.findById(2)).thenReturn(Optional.of(estadoAtrasada));
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(1)).thenReturn(3);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(2)).thenReturn(6);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(3)).thenReturn(9);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(4)).thenReturn(15);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(5)).thenReturn(15);

        Map<String, List<Razon>> resultados = razonService.aplicarInteresesPorMesesAtraso(fechaActualPrueba);

        List<Razon> razonesPendientesYAtrasadas1 = razones1
                .stream()
                .filter(razon -> razon.getNumero() > 1)
                .toList();

        List<Razon> razonesPendientesYAtrasadas2 = razones2
                .stream()
                .filter(razon -> razon.getNumero() > 1)
                .toList();

        Map<String, List<Razon>> esperados = new HashMap<>();
        for(Razon razon : razonesPendientesYAtrasadas1){
            razon.setMonto(144);
        }

        for(Razon razon : razonesPendientesYAtrasadas2){
            razon.setMonto(157);
        }

        esperados.put(estudiante1.getRut(), razonesPendientesYAtrasadas1);
        esperados.put(estudiante2.getRut(), razonesPendientesYAtrasadas2);

        assertEquals(esperados, resultados);
    }

    @Test
    void obtenerPorcentajesInteresTest(){
        LocalDate fechaActualPrueba = LocalDate.of(2023, Month.OCTOBER, 3);
        LocalDate fechaInicioProcesoPrueba = Util.obtenerFechaInicioProceso(fechaActualPrueba);
        List<Razon> razones2 = crearRazones(6, 11);

        List<Razon> razonesAntiguasPendientesYAtrasadas2 = razones2
                .stream()
                .filter(razon -> razon.getNumero() > 1 && razon.getNumero() <= 6)
                .toList();

        EstadoRazon estadoAtrasada = new EstadoRazon();
        estadoAtrasada.setId(2);
        estadoAtrasada.setEstado("ATRASADA");
        when(estadoRazonRepository.findById(2)).thenReturn(Optional.of(estadoAtrasada));
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(1)).thenReturn(3);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(2)).thenReturn(6);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(3)).thenReturn(9);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(4)).thenReturn(15);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(5)).thenReturn(15);

        List<Integer> resultados = razonService.obtenerPorcentajesInteres(razonesAntiguasPendientesYAtrasadas2, fechaInicioProcesoPrueba);

        List<Integer> esperados = List.of(15, 15, 9, 6, 3);

        assertEquals(esperados, resultados);
    }

    @Test
    void aplicarDescuentosPorPuntajesTest(){
        List<Examen> examenes = crearExamenes();
        LocalDate fechaActualPrueba = LocalDate.of(2023, Month.OCTOBER, 3);
        List<Razon> razones1 = crearRazones(1, 5);
        List<Razon> razones2 = crearRazones(6, 11);

        //No debería considerarse en el resultado
        Examen examenFechaAnterior = new Examen();
        examenFechaAnterior.setId(5);
        examenFechaAnterior.setPuntaje(0);
        examenFechaAnterior.setRevision(0);
        examenFechaAnterior.setFecha(LocalDate.of(2023, Month.APRIL, 27));
        examenes.add(examenFechaAnterior);

        Examen examenRevisado = new Examen();
        examenRevisado.setId(6);
        examenRevisado.setPuntaje(0);
        examenRevisado.setRevision(0);
        examenRevisado.setFecha(LocalDate.of(2023, Month.AUGUST, 27));
        examenes.add(examenRevisado);

        Estudiante estudiante1 = crearEstudiantePorDefecto();
        Estudiante estudiante2 = crearEstudiantePorDefecto();
        estudiante2.setRut("r1");

        estudiante1.setRazones(razones1);
        estudiante2.setRazones(razones2);

        examenes.get(0).setEstudiante(estudiante1);
        examenes.get(1).setEstudiante(estudiante1);
        examenes.get(2).setEstudiante(estudiante2);
        examenes.get(3).setEstudiante(estudiante2);
        examenes.get(4).setEstudiante(estudiante2);
        examenes.get(5).setEstudiante(estudiante1);

        when(examenRepository.findAllSinRevision()).thenReturn(examenes);
        when(descuentoPuntajePruebaRepository.findDescuentoByPuntajePromedio(900)).thenReturn(5);
        when(descuentoPuntajePruebaRepository.findDescuentoByPuntajePromedio(600)).thenReturn(0);
        List<Razon> razonesPendientesYAtrasadas1 = razones1
                .stream()
                .filter(razon -> razon.getNumero() > 1)
                .toList();

        List<Razon> razonesPendientesYAtrasadas2 = razones2
                .stream()
                .filter(razon -> razon.getNumero() > 1)
                .toList();

        when(razonRepository.findCuotasPendientesAndAtrasadasByRut(estudiante1.getRut())).thenReturn(razonesPendientesYAtrasadas1);
        when(razonRepository.findCuotasPendientesAndAtrasadasByRut(estudiante2.getRut())).thenReturn(razonesPendientesYAtrasadas2);

        Map<String, List<Razon>> resultados = razonService.aplicarDescuentosPorPuntajes(fechaActualPrueba);

        Map<String, List<Razon>> esperados = new HashMap<>();
        for(Razon razon : razonesPendientesYAtrasadas1){
            razon.setMonto(95);
        }

        esperados.put(estudiante1.getRut(), razonesPendientesYAtrasadas1);
        esperados.put(estudiante2.getRut(), razonesPendientesYAtrasadas2);

        assertEquals(esperados, resultados);
    }

    @Test
    void aplicarDescuentosPorPuntajesSinExamenes(){
        when(examenRepository.findAllSinRevision()).thenReturn(null);

        assertThrows(RegistroNoExisteException.class, () -> razonService.aplicarDescuentosPorPuntajes(LocalDate.now()));
    }

    @Test
    void calcularPlanillaTest(){
        LocalDate fechaActualPrueba = LocalDate.of(2023, Month.OCTOBER, 3);

        //Descuentos por puntajes
        List<Examen> examenes = crearExamenes();
        List<Razon> razones1 = crearRazones(1, 5);
        List<Razon> razones2 = crearRazones(6, 11);

        //No debería considerarse en el resultado
        Examen examenFechaAnterior = new Examen();
        examenFechaAnterior.setId(5);
        examenFechaAnterior.setPuntaje(0);
        examenFechaAnterior.setRevision(0);
        examenFechaAnterior.setFecha(LocalDate.of(2023, Month.APRIL, 27));
        examenes.add(examenFechaAnterior);

        Examen examenRevisado = new Examen();
        examenRevisado.setId(6);
        examenRevisado.setPuntaje(0);
        examenRevisado.setRevision(0);
        examenRevisado.setFecha(LocalDate.of(2023, Month.AUGUST, 27));
        examenes.add(examenRevisado);

        Estudiante estudiante1 = crearEstudiantePorDefecto();
        Estudiante estudiante2 = crearEstudiantePorDefecto();
        estudiante2.setRut("r1");

        estudiante1.setRazones(razones1);
        estudiante2.setRazones(razones2);

        examenes.get(0).setEstudiante(estudiante1);
        examenes.get(1).setEstudiante(estudiante1);
        examenes.get(2).setEstudiante(estudiante2);
        examenes.get(3).setEstudiante(estudiante2);
        examenes.get(4).setEstudiante(estudiante2);
        examenes.get(5).setEstudiante(estudiante1);

        when(examenRepository.findAllSinRevision()).thenReturn(examenes);
        when(descuentoPuntajePruebaRepository.findDescuentoByPuntajePromedio(900)).thenReturn(5);
        when(descuentoPuntajePruebaRepository.findDescuentoByPuntajePromedio(600)).thenReturn(0);
        List<Razon> razonesPendientesYAtrasadas1 = razones1
                .stream()
                .filter(razon -> razon.getNumero() > 1)
                .toList();

        List<Razon> razonesPendientesYAtrasadas2 = razones2
                .stream()
                .filter(razon -> razon.getNumero() > 1)
                .toList();

        when(razonRepository.findCuotasPendientesAndAtrasadasByRut(estudiante1.getRut())).thenReturn(razonesPendientesYAtrasadas1);
        when(razonRepository.findCuotasPendientesAndAtrasadasByRut(estudiante2.getRut())).thenReturn(razonesPendientesYAtrasadas2);

        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(estudiante1);
        estudiantes.add(estudiante2);

        when(estudianteRepository.findAll()).thenReturn(estudiantes);

        //Función obtener porcentajes interés
        EstadoRazon estadoAtrasada = new EstadoRazon();
        estadoAtrasada.setId(2);
        estadoAtrasada.setEstado("ATRASADA");
        when(estadoRazonRepository.findById(2)).thenReturn(Optional.of(estadoAtrasada));
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(1)).thenReturn(3);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(2)).thenReturn(6);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(3)).thenReturn(9);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(4)).thenReturn(15);
        when(interesMesesAtrasoRepository.findInteresByMesesAtraso(5)).thenReturn(15);

        Map<String, List<Razon>> resultados = razonService.calcularPlanilla(fechaActualPrueba);

        Map<String, List<Razon>> esperados = new HashMap<>();
        for(Razon razon : razonesPendientesYAtrasadas1){
            razon.setMonto(136);
        }

        for(Razon razon : razonesPendientesYAtrasadas2){
            razon.setMonto(157);
        }

        esperados.put(estudiante1.getRut(), razonesPendientesYAtrasadas1);
        esperados.put(estudiante2.getRut(), razonesPendientesYAtrasadas2);

        assertEquals(esperados, resultados);
    }

    @Test
    void calcularPlanillaFechaNoPermitidaAntesTest(){

        LocalDate fechaActualPrueba = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 5);
        LocalDate fechaInicioProceso = Util.obtenerFechaInicioProceso(fechaActualPrueba);
        LocalDate fechaExamenesProceso = Util.obtenerFechaExamenesProceso(fechaInicioProceso);
        LocalDate fechaActualPruebaDef = fechaExamenesProceso.minusDays(1);

        assertThrows(FechaNoPermitidaException.class, () -> razonService.calcularPlanilla(fechaActualPruebaDef));
    }

    @Test
    void calcularPlanillaFechaNoPermitidaDespuesTest(){

        LocalDate fechaActualPrueba = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 5);

        assertThrows(FechaNoPermitidaException.class, () -> razonService.calcularPlanilla(fechaActualPrueba));
    }

    private Estudiante crearEstudiantePorDefecto(){
        Estudiante estudiante = new Estudiante();
        estudiante.setRut("11.222.333-4");
        estudiante.setNombre1("NombreTest 1");
        estudiante.setNombre2("NombreTest 2");
        estudiante.setApellido1("ApellidoTest 1");
        estudiante.setApellido2("ApellidoTest 2");
        estudiante.setFechaNacimiento(LocalDate.of(1999, 1, 1));
        estudiante.setAnioEgreso(2018);
        estudiante.setNombreColegio("Colegio Test");

        TipoPagoArancel pagoEnCuotas = new TipoPagoArancel();
        pagoEnCuotas.setId(0);
        pagoEnCuotas.setTipo("CUOTAS");
        estudiante.setTipoPagoArancel(pagoEnCuotas);

        TipoColegio tipoMunicipal = new TipoColegio();
        tipoMunicipal.setId(0);
        tipoMunicipal.setTipo("MUNICIPAL");
        estudiante.setTipoColegio(tipoMunicipal);

        return estudiante;
    }

    private List<Examen> crearExamenes(){
        List<Examen> examenes = new ArrayList<>();
        Examen examen1 = new Examen();
        examen1.setId(0);
        examen1.setPuntaje(900);
        examen1.setRevision(0);
        examen1.setFecha(LocalDate.of(2023, Month.SEPTEMBER, 29));

        Examen examen2 = new Examen();
        examen2.setId(1);
        examen2.setPuntaje(900);
        examen2.setRevision(0);
        examen2.setFecha(LocalDate.of(2023, Month.SEPTEMBER, 29));

        Examen examen3 = new Examen();
        examen3.setId(2);
        examen3.setPuntaje(500);
        examen3.setRevision(0);
        examen3.setFecha(LocalDate.of(2023, Month.SEPTEMBER, 29));

        Examen examen4 = new Examen();
        examen4.setId(3);
        examen4.setPuntaje(700);
        examen4.setRevision(0);
        examen4.setFecha(LocalDate.of(2023, Month.SEPTEMBER, 29));

        examenes.add(examen1);
        examenes.add(examen2);
        examenes.add(examen3);
        examenes.add(examen4);

        return examenes;
    }

    private ArrayList<Razon> crearRazones(Integer k, Integer n){
        ArrayList<Razon> razones = new ArrayList<>();
        LocalDate fechaInicial = LocalDate.of(2023, 4, 5);
        LocalDate fechaFinal = LocalDate.of(2023, 4, 10);

        for(int i = 0; i < n; i++){
            Razon cuota = new Razon();
            cuota.setNumero(i);
            cuota.setId(k+i);
            cuota.setMonto(100);
            if(i == 0){
                cuota.setFechaInicio(LocalDate.of(2023, 2, 28));
                cuota.setFechaFin(LocalDate.of(2023, 3, 4));
            }else{
                cuota.setFechaInicio(fechaInicial);
                cuota.setFechaFin(fechaFinal);
                fechaInicial = fechaInicial.plusMonths(1);
                fechaFinal = fechaFinal.plusMonths(1);
            }

            EstadoRazon estadoPagada = new EstadoRazon();
            estadoPagada.setId(0);
            estadoPagada.setEstado("Pagada");
            EstadoRazon estadoPendiente = new EstadoRazon();
            estadoPendiente.setId(1);
            estadoPendiente.setEstado("Pendiente");
            EstadoRazon estadoAtrasada = new EstadoRazon();
            estadoAtrasada.setId(2);
            estadoAtrasada.setEstado("Atrasada");

            if(i == 0 || i == 1){
                cuota.setEstado(estadoPagada);
            }else if(i <= 4){
                cuota.setEstado(estadoAtrasada);
            }else{
                cuota.setEstado(estadoPendiente);
            }

            TipoRazon tipoMatricula = new TipoRazon();
            tipoMatricula.setId(0);
            TipoRazon tipoArancel = new TipoRazon();
            tipoArancel.setId(1);
            if(i == 0) cuota.setTipo(tipoMatricula);
            else cuota.setTipo(tipoArancel);

            razones.add(cuota);
        }

        return razones;
    }
}
