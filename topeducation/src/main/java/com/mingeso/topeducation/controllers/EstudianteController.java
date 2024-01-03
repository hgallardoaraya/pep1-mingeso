package com.mingeso.topeducation.controllers;

import com.mingeso.topeducation.entities.Estudiante;
import com.mingeso.topeducation.requests.IngresarEstudianteRequest;
import com.mingeso.topeducation.responses.Response;
import com.mingeso.topeducation.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {
    private final EstudianteService estudianteService;

    @Autowired
    public EstudianteController(EstudianteService estudianteService){
        this.estudianteService = estudianteService;
    }

    @GetMapping("/ingresar")
    public String vistaIngresarEstudiante(){
        return "ingresar-estudiante.html";
    }

    @GetMapping("/exito")
    public String exito(){
        return "exito.html";
    }

    @PostMapping
    public ResponseEntity<Response> ingresarEstudiante(@RequestBody IngresarEstudianteRequest request) {
        Estudiante estudiante = estudianteService.ingresarEstudiante(request);
        Map<String, Estudiante> data = new HashMap<>();
        data.put("estudiante", estudiante);
        return new ResponseEntity<>(
            new Response(
                HttpStatus.CREATED.value(),
                "Estudiante ingresado correctamente.",
                "/estudiantes/ingresar",
                data
            ),
            HttpStatus.CREATED);
    }

    @GetMapping("/maxcuotas")
    public ResponseEntity<?> getMaxCuotasByRut(@RequestParam("rut") String rut){
        Integer maxCuotas = estudianteService.obtenerMaxCuotas(rut);
        Map<String, Integer> data = new HashMap<>();
        data.put("maxCuotas", maxCuotas);
        return new ResponseEntity<Response>(
            new Response(
                HttpStatus.FOUND.value(),
                "Número máximo de cuotas encontrado con éxito.",
                data
            ),
            HttpStatus.FOUND);
    }

}
