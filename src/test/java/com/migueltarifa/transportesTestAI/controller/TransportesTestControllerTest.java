package com.migueltarifa.transportesTestAI.controller;

import com.migueltarifa.transportesTestAI.model.Carga;
import com.migueltarifa.transportesTestAI.model.Version;
import com.migueltarifa.transportesTestAI.service.TransportesTestService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransportesTestController.class)
class TransportesTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransportesTestService transportesTestService;

    @Test
    void testCreateVersion_Success() throws Exception {
        // Arrange
        Version mockVersion = new Version();
        mockVersion.setIdVersion("v1.0");
        Mockito.when(transportesTestService.createVersion(Mockito.any(Version.class))).thenReturn(mockVersion);

        // Act & Assert
        mockMvc.perform(post("/api/transportes/crearVersion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"idVersion\": \"v1.0\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idVersion").value("v1.0"));
    }

    @Test
    void testGetAllVersions_Success() throws Exception {
        // Arrange
        List<Version> mockVersions = List.of(new Version("v1.0"), new Version("v2.0"));
        Mockito.when(transportesTestService.getAllVersions()).thenReturn(mockVersions);

        // Act & Assert
        mockMvc.perform(get("/api/transportes/versiones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idVersion").value("v1.0"))
                .andExpect(jsonPath("$[1].idVersion").value("v2.0"));
    }

    @Test
    void testCargarFichero_Success() throws Exception {
        // Arrange
        Carga mockCarga = new Carga();
        mockCarga.setIdCarga(1);
        mockCarga.setNombreFichero("testFile.txt");
        mockCarga.setVersion("v1.0");
        mockCarga.setFechaCarga(LocalDateTime.now());

        Mockito.when(transportesTestService.processFileAndSaveData(
                Mockito.eq("v1.0"), Mockito.eq("UTF-8"), Mockito.any(MultipartFile.class)))
                .thenReturn(mockCarga);

        MockMultipartFile mockFile = new MockMultipartFile(
                "fichero", "testFile.txt", "text/plain", "Contenido del archivo".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/transportes/cargarFichero")
                .file(mockFile)
                .param("version", "v1.0")
                .param("charset", "UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCarga").value(1L))
                .andExpect(jsonPath("$.nombreFichero").value("testFile.txt"))
                .andExpect(jsonPath("$.version").value("v1.0"))
                .andExpect(jsonPath("$.mensaje").value("Archivo procesado y datos insertados correctamente."));
    }

    @Test
    void testCargarFichero_ThrowsExceptionForInvalidVersion() throws Exception {
        // Arrange
        Mockito.when(transportesTestService.processFileAndSaveData(
                Mockito.eq("v1.0"), Mockito.eq("UTF-8"), Mockito.any(MultipartFile.class)))
                .thenThrow(new IllegalArgumentException("La versión v1.0 no está dada de alta"));

        MockMultipartFile mockFile = new MockMultipartFile(
                "fichero", "testFile.txt", "text/plain", "Contenido del archivo".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/transportes/cargarFichero")
                .file(mockFile)
                .param("version", "v1.0")
                .param("charset", "UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"La versión v1.0 no está dada de alta\",\"status\":400}"));
    }
}
