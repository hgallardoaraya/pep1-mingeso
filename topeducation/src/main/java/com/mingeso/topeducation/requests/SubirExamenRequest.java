package com.mingeso.topeducation.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SubirExamenRequest {
    String rut;
    MultipartFile archivoExamen;
}
