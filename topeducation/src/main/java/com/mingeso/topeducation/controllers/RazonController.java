package com.mingeso.topeducation.controllers;

import com.mingeso.topeducation.entities.Razon;
import com.mingeso.topeducation.responses.Response;
import com.mingeso.topeducation.services.RazonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/razones")
public class RazonController {
    private final RazonService razonService;

    @Autowired
    public RazonController(RazonService razonService){
        this.razonService = razonService;
    }

    @GetMapping("/ingresar-estudiante")
    public String vistaListarRazones(){
        return "listar-razones-ingreso-estudiante.html";
    }

    @GetMapping
    public String listarRazones(@RequestParam String rut, Model model) {
        List<Razon> razones = razonService.obtenerRazones(rut);
        model.addAttribute("razones", razones);

        return "listar-razones.html";
    }

    @GetMapping("/generar")
    public String vistaGenerarCuotas(){
        return "generar-cuotas.html";
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generarCuotas(@RequestParam String rut,
                                           @RequestParam Integer numCuotas
                                           ){
        razonService.generarCuotas(rut, numCuotas);
        Response response = new Response(HttpStatus.CREATED.value(), "Cuotas generadas correcamente", "/exito");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/planilla/calcular")
    public String vistaCalcularPlanilla(){
        return "calcular-planilla.html";
    }

    @PostMapping("/planilla")
    public String calcularPlanilla(Model model){
        razonService.calcularPlanilla(LocalDate.now());
        model.addAttribute("message", "Planilla calculada con éxito");
        return "/calcular-planilla.html";
    }
}
