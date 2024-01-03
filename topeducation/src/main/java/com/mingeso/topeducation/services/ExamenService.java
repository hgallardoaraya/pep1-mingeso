package com.mingeso.topeducation.services;

import com.mingeso.topeducation.entities.Estudiante;
import com.mingeso.topeducation.entities.Examen;
import com.mingeso.topeducation.exceptions.FechaNoPermitidaException;
import com.mingeso.topeducation.exceptions.RegistroNoExisteException;
import com.mingeso.topeducation.exceptions.RegistrosFaltantesException;
import com.mingeso.topeducation.exceptions.ValorFueraDeRangoException;
import com.mingeso.topeducation.repositories.EstudianteRepository;
import com.mingeso.topeducation.repositories.ExamenRepository;
import com.mingeso.topeducation.utils.Util;
import jakarta.transaction.Transactional;
import lombok.Generated;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ExamenService {
    private final EstudianteRepository estudianteRepository;
    private final ExamenRepository examenRepository;

    @Autowired
    public ExamenService(EstudianteRepository estudianteRepository,
                         ExamenRepository examenRepository){
        this.estudianteRepository = estudianteRepository;
        this.examenRepository = examenRepository;
    }

    @Generated
    @Transactional
    public void importarExamen(MultipartFile archivo, LocalDate fechaActual) {
        LocalDate fechaInicioProceso = Util.obtenerFechaInicioProceso(fechaActual);
        LocalDate fechaExamenesProceso = Util.obtenerFechaExamenesProceso(fechaInicioProceso);

        if(fechaActual.isBefore(fechaExamenesProceso) || fechaActual.isAfter(fechaInicioProceso)){
            throw new FechaNoPermitidaException("La importación de exámenes del proceso correspondiente debe ser realizada" +
                    " entre el último viernes del més " + fechaExamenesProceso + " y el inicio de pago de arancel del proceso "
                    + fechaInicioProceso + ".");
        }

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(archivo.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error al abrir el archivo '.xlsx'.");
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);

        System.out.println(worksheet.getPhysicalNumberOfRows());
        int numeroFilasDocumento =  worksheet.getPhysicalNumberOfRows();
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        if(estudiantes.size() > 0 && estudiantes.size() != (numeroFilasDocumento - 1)) {
            throw new RegistrosFaltantesException("Faltan o sobran estudiantes en el archivo excel.");
        }
        Map<String, Boolean> estudiantesImportados = new HashMap<>();
        for(int i = 1; i < numeroFilasDocumento; i++) {
            XSSFRow row = worksheet.getRow(i);
            String rut = row.getCell(0).getStringCellValue();
            if(estudiantesImportados.containsKey(rut)){
                throw new ValorFueraDeRangoException("El estudiante con rut " + rut + " está repetido en el archivo excel.");
            }
            Optional<Estudiante> estudiante = estudianteRepository.findByRut(rut);
            if(estudiante.isEmpty()) throw new RegistroNoExisteException("El estudiante con rut " + rut + " no existe.");
            Date fecha = row.getCell(1).getDateCellValue();
            LocalDate fechaLocalDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int puntaje = (int) row.getCell(2).getNumericCellValue();

            if(puntaje < 0 || puntaje > 1000) throw new ValorFueraDeRangoException("El puntaje " + puntaje + " del " +
                    "estudiante con rut " + rut + " está fuera del rango permitido (0-1000).");
            estudiantesImportados.put(rut, true);
            examenRepository.save(new Examen(fechaLocalDate, puntaje, estudiante.get(), 0));
        }
    }

}
