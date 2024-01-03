package com.mingeso.topeducation.controllers;

import com.mingeso.topeducation.entities.Razon;
import com.mingeso.topeducation.exceptions.FechaNoPermitidaException;
import com.mingeso.topeducation.requests.RegistrarPagoRequest;
import com.mingeso.topeducation.responses.Response;
import com.mingeso.topeducation.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pagos")
public class PagoController {
    private final PagoService pagoService;

    @Autowired
    public PagoController(PagoService pagoService){
        this.pagoService = pagoService;
    }

    @GetMapping("/registrar/ingresar-estudiante")
    public String vistaIngresarEstudiantePago(){
        return "registrar-pago-ingreso-estudiante.html";
    }

    @GetMapping("/registrar")
    public String vistaSeleccionarRazonesPago(@RequestParam String rut, Model model){
        try{
            List<Razon> razones = pagoService.obtenerRazonesAPagar(rut, LocalDate.now());
            model.addAttribute("razones", razones);
            model.addAttribute("rut", rut);
            return "registrar-pago-seleccionar-razones.html";
        }catch (FechaNoPermitidaException ex){
            model.addAttribute("message", ex.getMessage());
            return "error.html";
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<Response> registrarPago(@RequestBody RegistrarPagoRequest request){
        pagoService.registrarPago(request, LocalDate.now());
        return new ResponseEntity<Response>(
                new Response(
                        HttpStatus.CREATED.value(),
                        "Pago realizado con éxito.",
                        "/pagos/registrar/ingresar-estudiante"
                ),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/reporte")
    public String calcularReporteResumen(Model model){
        model.addAttribute("reporte", pagoService.calcularReporteResumen());
        return "reporte-resumen.html";
    }
}
