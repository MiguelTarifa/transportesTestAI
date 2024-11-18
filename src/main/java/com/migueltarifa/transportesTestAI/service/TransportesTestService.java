package com.migueltarifa.transportesTestAI.service;

import com.migueltarifa.transportesTestAI.model.Carga;
import com.migueltarifa.transportesTestAI.model.Pregunta;
import com.migueltarifa.transportesTestAI.model.Respuesta;
import com.migueltarifa.transportesTestAI.model.Version;
import com.migueltarifa.transportesTestAI.repository.CargaRepository;
import com.migueltarifa.transportesTestAI.repository.PreguntaRepository;
import com.migueltarifa.transportesTestAI.repository.VersionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransportesTestService {

    private static final Logger logger = LoggerFactory.getLogger(TransportesTestService.class);

    @Autowired
    private VersionRepository versionRepository;

    @Autowired
    private CargaRepository cargaRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    // Metodo para insertar un nuevo registro en la tabla "versiones"
    public Version createVersion(Version version) {

        logger.debug("createVersion {}", version);

        if (version.getIdVersion() == null || version.getIdVersion().isEmpty()) {
            logger.error("El idVersion es obligatorio y debe ser único.");
            throw new IllegalArgumentException("El idVersion es obligatorio y debe ser único.");
        }

        // Verifica si ya existe un registro con el mismo idVersion
        if (versionRepository.existsByIdVersion(version.getIdVersion())) {
            logger.error("Ya existe una versión con el idVersion: {} ", version.getIdVersion());
            throw new IllegalArgumentException("Ya existe una versión con el idVersion: " + version.getIdVersion());
        }

        version.setFechaCreacion(LocalDateTime.now());
        return versionRepository.save(version); // Inserta el registro y devuelve el objeto guardado
    }

    // Metodo para consultar todos los registros de la tabla "versiones"
    public List<Version> getAllVersions() {
        logger.debug("getAllVersions");
        return versionRepository.findAll(); // Retorna todos los registros de la tabla
    }

    public Carga processFileAndSaveData(String version, String charset, MultipartFile fichero)  {

        logger.debug("processFileAndSaveData version {}, charset{}, fichero {}", version, charset, fichero);

        //charset Cp1252
        if (!versionRepository.existsByIdVersion(version)){
            logger.error("La versión {} no está dada de alta", version);
            throw new IllegalArgumentException("La versión " + version + " no está dada de alta");
        }

        try {
            String contenido = new String(fichero.getBytes(), charset);
            List<Pregunta> preguntas = procesarContenido(version, contenido);


            logger.debug("processFileAndSaveData guardando preguntas y respuestas");
            guardarPreguntas(preguntas);

            Carga carga = new Carga(version, fichero.getOriginalFilename(), LocalDateTime.now());
            return cargaRepository.save(carga);
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding {} no soportado", charset, e);
            throw new IllegalArgumentException("Encoding " + charset + " no soportado");
        } catch (IOException e) {
            logger.error("Error leyendo fichero {}", fichero.getOriginalFilename(), e);
            throw new IllegalArgumentException("Error leyendo fichero " + fichero.getOriginalFilename());
        }
    }

    private List<Pregunta> procesarContenido(String version, String contenido) {

        logger.debug("procesarContenido");

        List<Pregunta> preguntas = new ArrayList<>();

        String[] bloques = contenido.split("\n\n"); // Divide por línea en blanco
        for (String bloque : bloques) {
            Pregunta pregunta = new Pregunta();

            pregunta.setVersion(version);

            String[] lineas = bloque.split("\n");
            pregunta.setIdentificador(Integer.parseInt(lineas[0].split(": ")[1]));

            try {
                int contador = 1;
                int aux = 1;
                StringBuilder sbEnunciado = new StringBuilder();
                while (!lineas[++contador].startsWith(aux + ".-")) {
                    if (sbEnunciado.toString().isEmpty()) {
                        sbEnunciado.append(lineas[contador]);
                    } else {
                        sbEnunciado.append(" ").append(lineas[contador]);
                    }
                }
                pregunta.setEnunciado(sbEnunciado.toString());

                List<Respuesta> opciones = new ArrayList<>();

                while (!lineas[contador + 1].startsWith("Norma:") || aux <= 4) {
                    StringBuilder sbRespuesta = new StringBuilder();
                    Respuesta respuesta = new Respuesta();


                    while (!lineas[contador].startsWith(aux + 1 + ".-") && !lineas[contador + 1].startsWith("Norma:")) {
                        if (sbRespuesta.toString().isEmpty()) {
                            sbRespuesta.append(lineas[contador]);
                        } else {
                            sbRespuesta.append(" ").append(lineas[contador]);
                        }
                        contador++;
                    }
                    respuesta.setRespuesta(sbRespuesta.toString());
                    respuesta.setIndice(aux);

                    aux++;
                    opciones.add(respuesta);
                }

                pregunta.setRespuestas(opciones);

                // Línea de respuesta correcta
                pregunta.setCorrecta(Integer.parseInt(lineas[contador].split(": ")[1]));
                // Línea de norma
                pregunta.setNorma(lineas[contador + 1].split(": ")[1]);

                preguntas.add(pregunta);
            } catch (ArrayIndexOutOfBoundsException e) {
                logger.error("procesarContenido Error procesando el fichero en el identificador {}", lineas[0], e);
                throw new IllegalArgumentException("Error procesando el fichero. Comprobar que no contiene saltos de linea en los bloques.");
            }
        }

        return preguntas;
    }

    private void guardarPreguntas(List<Pregunta> preguntas) {
        for (Pregunta pregunta : preguntas) {
            // Guardar la pregunta
            preguntaRepository.save(pregunta);
        }
    }
}
