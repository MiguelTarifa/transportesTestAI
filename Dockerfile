# Usa una imagen base de Alpine con OpenJDK
FROM openjdk:23-slim

# Crea un grupo con el GID 1001
RUN addgroup --gid 1001 devops

RUN adduser --uid 1001 --ingroup devops admin --disabled-password

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR al contenedor
COPY /target/transportesTestAI-1.0.0.jar /app/app.jar

RUN chown -R admin:devops /app

# Crear el directorio para los logs
RUN mkdir -p /logs/application/transportesTestAI

# Establecer los permisos adecuados para el directorio
RUN chown -R admin:devops /logs/application

USER admin

# Expone el puerto donde la aplicación estará escuchando
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

# Construcción
#docker build -t transportes-test-ai .

#Ejecución
#docker run -p 8081:8081 transportes-test-ai

#Verificar Java: Puedes iniciar un contenedor interactivo para verificar la versión de Java:
#docker run -it --rm transportes-test-ai java -version