package com.migueltarifa.transportesTestAI.controller;

import com.migueltarifa.transportesTestAI.model.Carga;
import com.migueltarifa.transportesTestAI.model.Version;
import com.migueltarifa.transportesTestAI.service.TransportesTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transportes")
@Tag(name = "Transportes", description = "API de servicios para Test de transportes")
public class TransportesTestController {

    @Autowired
    private TransportesTestService transportesTestService;

    @Autowired
    public TransportesTestController(TransportesTestService transportesTestService) {
        this.transportesTestService = transportesTestService;
    }

    // Endpoint para insertar un nuevo registro
    @PostMapping("/crearVersion")
    @Operation(summary = "Crear una nueva version", description = "Crea una nueva version")
    public Version createVersion(@RequestBody Version version) {
        return transportesTestService.createVersion(version);
    }

    // Endpoint para consultar todos los registros
    @GetMapping("versiones")
    @Operation(summary = "Obtener todas las versiones", description = "Devuelve una lista de versiones disponibles")
    public List<Version> getAllVersions() {
        return transportesTestService.getAllVersions();
    }

    // Metodo para recibir un parámetro String y un archivo de texto
    @PostMapping("/cargarFichero")
    @Operation(summary = "Cargar un fichero", description = "Carga un fichero de preguntas y respuestas")
    public ResponseEntity<Map<String, Object>> cargarFichero(
            @RequestParam(value = "version", required = true) String version,
            @RequestParam(value = "charset", required = true) String charset,
            @RequestPart(value = "fichero", required = true) MultipartFile fichero) {
            Carga carga = transportesTestService.processFileAndSaveData(version, charset, fichero);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("idCarga", carga.getIdCarga());
        respuesta.put("nombreFichero", carga.getNombreFichero());
        respuesta.put("version", carga.getVersion());
        respuesta.put("fechaCarga", carga.getFechaCarga());
        respuesta.put("mensaje", "Archivo procesado y datos insertados correctamente.");
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
    }
}
