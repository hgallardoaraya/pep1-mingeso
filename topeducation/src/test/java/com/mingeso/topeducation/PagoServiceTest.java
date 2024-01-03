package com.mingeso.topeducation;

import com.mingeso.topeducation.entities.*;
import com.mingeso.topeducation.exceptions.RegistroNoExisteException;
import com.mingeso.topeducation.exceptions.ValorFueraDeRangoException;
import com.mingeso.topeducation.repositories.EstadoRazonRepository;
import com.mingeso.topeducation.repositories.EstudianteRepository;
import com.mingeso.topeducation.repositories.PagoRepository;
import com.mingeso.topeducation.repositories.RazonRepository;
import com.mingeso.topeducation.requests.RegistrarPagoRequest;
import com.mingeso.topeducation.services.PagoService;
import com.mingeso.topeducation.utils.EntradaReporteResumen;
import com.mingeso.topeducation.utils.Util;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {
    @Mock
    PagoRepository pagoRepository;
    @Mock
    EstudianteRepository estudianteRepository;
    @Mock
    RazonRepository razonRepository;
    @Mock
    EstadoRazonRepository estadoRazonRepository;
    @InjectMocks
    PagoService pagoService;
    private Estudiante estudiante;

    @BeforeEach
    public void setUp() {
        estudiante = crearEstudiantePorDefecto();
    }


    @Transactional
    @Test
    void testRegistrarPago() {
        // Mock de objetos necesarios
        Estudiante estudiante = new Estudiante();
        estudiante.setRut("123456789");
        when(estudianteRepository.findByRut("123456789")).thenReturn(Optional.of(estudiante));

        Razon razon1 = new Razon();
        razon1.setId(1);
        razon1.setMonto(100);
        when(razonRepository.findById(1)).thenReturn(Optional.of(razon1));

        Razon razon2 = new Razon();
        razon2.setId(2);
        razon2.setMonto(300);
        when(razonRepository.findById(2)).thenReturn(Optional.of(razon2));

        EstadoRazon estadoPagada = new EstadoRazon();
        estadoPagada.setId(0);
        when(estadoRazonRepository.findById(0)).thenReturn(Optional.of(estadoPagada));

        RegistrarPagoRequest request = new RegistrarPagoRequest();
        request.setRut("123456789");
        Integer[] idsRazones = {1, 2};
        request.setIdsRazones(idsRazones);

        // Configura el comportamiento de pagoRepository.save para devolver el objeto pagodb
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0, Pago.class));

        LocalDate fechaActualPrueba = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 5);

        Pago pago = pagoService.registrarPago(request, fechaActualPrueba);

        // Verificación de resultados
        assertNotNull(pago);
        assertEquals(estudiante, pago.getEstudiante());
        assertEquals(400, pago.getTotal()); // La suma de los montos de razon1 y razon2
        assertEquals(LocalDate.now(), pago.getFecha());
        assertEquals(2, pago.getRazones().size()); // Deben haberse agregado dos razones a la lista de razones
        assertTrue(pago.getRazones().contains(razon1));
        assertTrue(pago.getRazones().contains(razon2));
    }

    @Transactional
    @Test
    void testRegistrarPagoNoExistenRazones() {
        // Mock de objetos necesarios
        Estudiante estudiante = new Estudiante();
        estudiante.setRut("123456789");
        when(estudianteRepository.findByRut("123456789")).thenReturn(Optional.of(estudiante));

        RegistrarPagoRequest request = new RegistrarPagoRequest();
        request.setRut("123456789");
        Integer[] idsRazones = {};
        request.setIdsRazones(idsRazones);

        LocalDate fechaActualPrueba = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 5);
        assertThrows(RegistroNoExisteException.class, () -> pagoService.registrarPago(request, fechaActualPrueba));

        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Transactional
    @Test
    void testRegistrarPagoMontoNegativo() {
        // Mock de objetos necesarios
        Estudiante estudiante = new Estudiante();
        estudiante.setRut("123456789");
        when(estudianteRepository.findByRut("123456789")).thenReturn(Optional.of(estudiante));

        Razon razon = new Razon();
        razon.setId(2);
        razon.setMonto(-500);
        when(razonRepository.findById(2)).thenReturn(Optional.of(razon));

        RegistrarPagoRequest request = new RegistrarPagoRequest();
        request.setRut("123456789");
        Integer[] idsRazones = {2};
        request.setIdsRazones(idsRazones);

        LocalDate fechaActualPrueba = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 5);
        assertThrows(ValorFueraDeRangoException.class, () -> pagoService.registrarPago(request, fechaActualPrueba));

        verify(razonRepository, never()).save(any(Razon.class));
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    //Puede que este TEST parezca obvio, pero la gracia no es testear el repositorio, de hecho, asumimos que funciona
    //de manera correcta. Lo que estamos haciendo es testear la lógica del servicio y viendo si existe algún error
    //que se interponga en la devolución correcta del dato que retorna la búsqueda de cuotas a pagar por rut en la
    //base de datos.
    @Test
    void testObtenerRazonesAPagar(){
        String rut = this.estudiante.getRut();

        ArrayList<Razon> razones = new ArrayList<>();

        LocalDate fechaActualDePrueba = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 7);

        when(razonRepository.findCuotasAPagarByRutAndDate(rut, Util.obtenerFechaInicioProceso(fechaActualDePrueba))).thenReturn(razones);

        List<Razon> resultado = pagoService.obtenerRazonesAPagar(rut, fechaActualDePrueba);

        assertEquals(razones, resultado);
    }

    @Test
    void testCalcularReporteResumen(){
        List<Estudiante> estudiantes = crearListaEstudiantesPorDefectoConRelaciones();

        when(estudianteRepository.findAll()).thenReturn(estudiantes);

        EntradaReporteResumen entradaEstudiante1 = EntradaReporteResumen.builder()
                .rut(estudiantes.get(0).getRut())
                .numeroExamenesRendidos(2)
                .promedioExamenes(550)
                .totalArancel(600)
                .tipoPago("CUOTAS")
                .numeroCuotasPactadas(2)
                .numeroCuotasPagadas(0)
                .arancelPagado(0)
                .totalPagado(100)
                .fechaUltimoPago(LocalDate.now())
                .saldoArancelPendiente(600)
                .saldoTotalPendiente(600)
                .numeroCuotasAtrasadas(1)
                .build();

        EntradaReporteResumen entradaEstudiante2 = EntradaReporteResumen.builder()
                .rut(estudiantes.get(1).getRut())
                .numeroExamenesRendidos(2)
                .promedioExamenes(50)
                .totalArancel(500)
                .tipoPago("CUOTAS")
                .numeroCuotasPactadas(2)
                .numeroCuotasPagadas(1)
                .arancelPagado(200)
                .totalPagado(200)
                .fechaUltimoPago(null)
                .saldoArancelPendiente(300)
                .saldoTotalPendiente(300)
                .numeroCuotasAtrasadas(0)
                .build();


        List<EntradaReporteResumen> resultado = pagoService.calcularReporteResumen();
        System.out.println(resultado.get(0));
        System.out.println(entradaEstudiante1);
        System.out.println(resultado.get(1));
        System.out.println(entradaEstudiante2);
        assertEquals(entradaEstudiante1, resultado.get(0));
        assertEquals(entradaEstudiante2, resultado.get(1));
    }

    public Estudiante crearEstudiantePorDefecto(){
        Estudiante estudiante = new Estudiante();
        estudiante.setRut("11.222.333-4");
        estudiante.setNombre1("NombreTest 1");
        estudiante.setNombre2("NombreTest 2");
        estudiante.setApellido1("ApellidoTest 1");
        estudiante.setApellido2("ApellidoTest 2");
        estudiante.setFechaNacimiento(LocalDate.of(1999, 1, 1));
        estudiante.setAnioEgreso(2018);
        estudiante.setNombreColegio("Colegio Test");
        return estudiante;
    }

    public List<Estudiante> crearListaEstudiantesPorDefectoConRelaciones(){
        List<Estudiante> estudiantes = new ArrayList<>();
        Estudiante estudiante2 = crearEstudiantePorDefecto();
        estudiante2.setNombre1("Test nombre 2");

        TipoPagoArancel tipoPagoArancel = new TipoPagoArancel();
        tipoPagoArancel.setId(1);
        tipoPagoArancel.setTipo("CUOTAS");

        this.estudiante.setTipoPagoArancel(tipoPagoArancel);
        this.estudiante.setCuotasPactadas(2);
        estudiante2.setTipoPagoArancel(tipoPagoArancel);
        estudiante2.setCuotasPactadas(2);

        TipoRazon tipoArancel = new TipoRazon();
        tipoArancel.setId(1);
        tipoArancel.setTipo("ARANCEL");

        TipoRazon tipoMatricula = new TipoRazon();
        tipoMatricula.setId(0);
        tipoMatricula.setTipo("MATRICULA");

        EstadoRazon estadoPendiente = new EstadoRazon();
        estadoPendiente.setId(1);
        estadoPendiente.setEstado("PENDIENTE");

        EstadoRazon estadoAtrasada = new EstadoRazon();
        estadoAtrasada.setId(2);
        estadoAtrasada.setEstado("ATRASADA");

        EstadoRazon estadoPagada = new EstadoRazon();
        estadoPagada.setId(0);
        estadoPagada.setEstado("PAGADA");

        Razon razon1 = new Razon();
        razon1.setId(1);
        razon1.setMonto(100);
        razon1.setTipo(tipoMatricula);
        razon1.setFechaInicio(LocalDate.of(2023, 3, 1));
        razon1.setFechaFin(LocalDate.of(2023, 3, 5));
        razon1.setEstado(estadoPagada);
        Razon razon2 = new Razon();
        razon2.setId(2);
        razon2.setMonto(300);
        razon2.setTipo(tipoArancel);
        razon2.setFechaInicio(LocalDate.of(2023, 6, 5));
        razon2.setFechaFin(LocalDate.of(2023, 6, 10));
        razon2.setEstado(estadoPendiente);
        Razon razon3 = new Razon();
        razon3.setId(3);
        razon3.setMonto(300);
        razon3.setTipo(tipoArancel);
        razon3.setFechaInicio(LocalDate.of(2023, 7, 5));
        razon3.setFechaFin(LocalDate.of(2023, 7, 10));
        razon3.setEstado(estadoAtrasada);

        ArrayList<Razon> razones1 = new ArrayList<>();
        razones1.add(razon1);
        razones1.add(razon2);
        razones1.add(razon3);
        this.estudiante.setRazones(razones1);


        Razon razon4 = new Razon();
        razon4.setId(4);
        razon4.setMonto(300);
        razon4.setTipo(tipoArancel);
        razon4.setEstado(estadoPendiente);
        razon4.setFechaInicio(LocalDate.of(2023, 6, 5));
        razon4.setFechaFin(LocalDate.of(2023, 6, 10));
        Razon razon5 = new Razon();
        razon5.setId(5);
        razon5.setMonto(200);
        razon5.setTipo(tipoArancel);
        razon5.setFechaInicio(LocalDate.of(2023, 7, 5));
        razon5.setFechaFin(LocalDate.of(2023, 7, 10));
        razon5.setEstado(estadoPagada);
        ArrayList<Razon> razones2 = new ArrayList<>();
        razones2.add(razon4);
        razones2.add(razon5);
        estudiante2.setRazones(razones2);

        Pago pago1 = new Pago();
        pago1.setId(1);
        pago1.setTotal(100);
        pago1.setFecha(LocalDate.now());
        pago1.setRazones(List.of(razon1));
        this.estudiante.setPagos(List.of(pago1));

        //promedio (500+600)/2 = 550
        Examen examen1 = new Examen();
        examen1.setId(1);
        examen1.setPuntaje(500);
        Examen examen2 = new Examen();
        examen2.setId(2);
        examen2.setPuntaje(600);
        this.estudiante.setExamenes(List.of(examen1, examen2));

        //promedio (0+100)/2 = 50
        Examen examen3 = new Examen();
        examen3.setId(3);
        examen3.setPuntaje(0);
        Examen examen4 = new Examen();
        examen4.setId(4);
        examen4.setPuntaje(100);
        estudiante2.setExamenes(List.of(examen3, examen4));

        estudiantes.add(this.estudiante);
        estudiantes.add(estudiante2);

        return estudiantes;
    }

}
