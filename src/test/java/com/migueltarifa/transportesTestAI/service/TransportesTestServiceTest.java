package com.migueltarifa.transportesTestAI.service;

import com.migueltarifa.transportesTestAI.model.Carga;
import com.migueltarifa.transportesTestAI.model.Pregunta;
import com.migueltarifa.transportesTestAI.model.Version;
import com.migueltarifa.transportesTestAI.repository.CargaRepository;
import com.migueltarifa.transportesTestAI.repository.PreguntaRepository;
import com.migueltarifa.transportesTestAI.repository.VersionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransportesTestServiceTest {

    @InjectMocks
    private TransportesTestService transportesTestService;

    @Mock
    private VersionRepository versionRepository;

    @Mock
    private CargaRepository cargaRepository;

    @Mock
    private PreguntaRepository preguntaRepository;

    @Mock
    private MultipartFile mockFile;

    @Test
    void testCreateVersion_Success() {
        // Arrange
        Version version = new Version();
        version.setIdVersion("v1.0");
        Mockito.when(versionRepository.existsByIdVersion("v1.0")).thenReturn(false);
        Mockito.when(versionRepository.save(Mockito.any(Version.class))).thenReturn(version);

        // Act
        Version result = transportesTestService.createVersion(version);

        // Assert
        assertNotNull(result);
        assertEquals("v1.0", result.getIdVersion());
        Mockito.verify(versionRepository).save(version);
    }

    @Test
    void testCreateVersion_ThrowsExceptionWhenVersionExists() {
        // Arrange
        Version version = new Version();
        version.setIdVersion("v1.0");
        Mockito.when(versionRepository.existsByIdVersion("v1.0")).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transportesTestService.createVersion(version);
        });

        assertEquals("Ya existe una versión con el idVersion: v1.0", exception.getMessage());
    }

    @Test
    void testGetAllVersions_Success() {
        // Arrange
        List<Version> mockVersions = List.of(new Version(), new Version());
        Mockito.when(versionRepository.findAll()).thenReturn(mockVersions);

        // Act
        List<Version> result = transportesTestService.getAllVersions();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        Mockito.verify(versionRepository).findAll();
    }

    @Test
    void testProcessFileAndSaveData_Success() throws Exception {
        // Arrange
        String version = "v1.0";
        String charset = "UTF-8";
        String fileContent = "Identificador: 1\nEnunciado:\n¿Pregunta 1?\n1.- Respuesta 1\n2.- Respuesta 2\nRespuesta: 1\nNorma: Norma 1\n";
        Mockito.when(versionRepository.existsByIdVersion(version)).thenReturn(true);
        Mockito.when(mockFile.getBytes()).thenReturn(fileContent.getBytes(charset));
        Mockito.when(mockFile.getOriginalFilename()).thenReturn("testFile.txt");

        Carga mockCarga = new Carga(version, "testFile.txt", LocalDateTime.now());
        Mockito.when(cargaRepository.save(Mockito.any(Carga.class))).thenReturn(mockCarga);

        // Act
        Carga result = transportesTestService.processFileAndSaveData(version, charset, mockFile);

        // Assert
        assertNotNull(result);
        assertEquals("testFile.txt", result.getNombreFichero());
        Mockito.verify(preguntaRepository, Mockito.atLeastOnce()).save(Mockito.any(Pregunta.class));
        Mockito.verify(cargaRepository).save(Mockito.any(Carga.class));
    }

    @Test
    void testProcessFileAndSaveData_ThrowsExceptionForInvalidVersion() {
        // Arrange
        String version = "v1.0";
        Mockito.when(versionRepository.existsByIdVersion(version)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transportesTestService.processFileAndSaveData(version, "UTF-8", mockFile);
        });

        assertEquals("La versión v1.0 no está dada de alta", exception.getMessage());
    }

    @Test
    void testProcessFileAndSaveData_ThrowsExceptionForUnsupportedEncoding() throws Exception {
        // Arrange
        String version = "v1.0";
        Mockito.when(versionRepository.existsByIdVersion(version)).thenReturn(true);
        Mockito.when(mockFile.getBytes()).thenThrow(new UnsupportedEncodingException("Unsupported Encoding"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transportesTestService.processFileAndSaveData(version, "InvalidCharset", mockFile);
        });

        assertTrue(exception.getMessage().contains("Encoding InvalidCharset no soportado"));
    }
}
