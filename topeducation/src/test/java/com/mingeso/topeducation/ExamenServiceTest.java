package com.mingeso.topeducation;

import com.mingeso.topeducation.entities.Estudiante;
import com.mingeso.topeducation.entities.Examen;
import com.mingeso.topeducation.entities.Pago;
import com.mingeso.topeducation.repositories.EstudianteRepository;
import com.mingeso.topeducation.repositories.ExamenRepository;
import com.mingeso.topeducation.services.ExamenService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamenServiceTest {
    @Mock
    EstudianteRepository estudianteRepository;

    @Mock
    ExamenRepository examenRepository;

    @InjectMocks
    ExamenService examenService;

    @Test
    void importarExamenTest() throws ParseException, IOException {

        Estudiante estudiante1 = new Estudiante();
        estudiante1.setRut("rut1");

        Estudiante estudiante2 = new Estudiante();
        estudiante2.setRut("rut2");

        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(estudiante1);
        estudiantes.add(estudiante2);

        when(estudianteRepository.findAll()).thenReturn(estudiantes);
        when(estudianteRepository.findByRut("rut1")).thenReturn(Optional.of(estudiante1));
        when(estudianteRepository.findByRut("rut2")).thenReturn(Optional.of(estudiante2));

        XSSFWorkbook workbook = new XSSFWorkbook();

        // Crear una nueva hoja en el archivo
        XSSFSheet sheet = workbook.createSheet("examentest");

        //Encabezados
        XSSFRow filaEncabezados = sheet.createRow(0);
        Cell colRut = filaEncabezados.createCell(0);
        colRut.setCellValue("rut");

        Cell colFecha = filaEncabezados.createCell(1);
        colFecha.setCellValue("fecha");

        Cell colPuntaje = filaEncabezados.createCell(2);
        colPuntaje.setCellValue("puntaje");

        //1 estudiante
        XSSFRow filaEstudiante1 = sheet.createRow(1);
        Cell colRutEstudiante1 = filaEstudiante1.createCell(0);
        colRutEstudiante1.setCellValue("rut1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2023-09-29");
        Cell colFechaEstudiante1 = filaEstudiante1.createCell(1);
        colFechaEstudiante1.setCellValue(date);
        Cell colPuntajeEstudiante1 = filaEstudiante1.createCell(2);
        colPuntajeEstudiante1.setCellValue(900);

        //2 estudiante
        XSSFRow filaEstudiante2 = sheet.createRow(2);
        Cell colRutEstudiante2 = filaEstudiante2.createCell(0);
        colRutEstudiante2.setCellValue("rut2");
        date = sdf.parse("2023-09-29");
        Cell colFechaEstudiante2 = filaEstudiante2.createCell(1);
        colFechaEstudiante2.setCellValue(date);
        Cell colPuntajeEstudiante2 = filaEstudiante2.createCell(2);
        colPuntajeEstudiante2.setCellValue(950);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);

        byte[] content = bos.toByteArray();

        MultipartFile archivo = new MockMultipartFile("file", "test", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content);

        examenService.importarExamen(archivo, LocalDate.of(2023, Month.NOVEMBER, 5));

        verify(examenRepository, times(2)).save(any(Examen.class));
    }
}
