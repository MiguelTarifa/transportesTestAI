USE transportes;

CREATE TABLE `versiones` (
  `id_version` varchar(10) NOT NULL,
  `fecha_creacion` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `cargas` (
  `id_carga` int NOT NULL AUTO_INCREMENT,
  `version` varchar(10) NOT NULL,
  `nombre_fichero` varchar(255) NOT NULL,
  `fecha_carga` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_carga`),
  KEY `versiones_fk_idx` (`version`),
  CONSTRAINT `carga_version_fk` FOREIGN KEY (`version`) REFERENCES `versiones` (`id_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `preguntas` (
  `id_pregunta` int NOT NULL AUTO_INCREMENT,
  `version` varchar(10) NOT NULL,
  `identificador` int NOT NULL,
  `enunciado` varchar(500) NOT NULL,
  `correcta` int NOT NULL,
  `norma` varchar(255) NOT NULL,
  PRIMARY KEY (`id_pregunta`),
  KEY `version_fk_idx` (`version`),
  CONSTRAINT `pregunta_version_fk` FOREIGN KEY (`version`) REFERENCES `versiones` (`id_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `respuestas` (
  `id_respuesta` int NOT NULL AUTO_INCREMENT,
  `pregunta` int NOT NULL,
  `indice` int NOT NULL,
  `respuesta` varchar(500) NOT NULL,
  PRIMARY KEY (`id_respuesta`),
  KEY `preguntas_fk_idx` (`pregunta`),
  CONSTRAINT `pregunta_respuesta_fk` FOREIGN KEY (`pregunta`) REFERENCES `preguntas` (`id_pregunta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
