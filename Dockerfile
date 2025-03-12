# Fase de construcción
FROM openjdk:21 AS build

# Directorios para los proyectos cliente y servidor
WORKDIR /app

# Copiar el código fuente de ambos proyectos al contenedor
COPY src/cliente/ClienteTCP.java /app/cliente/ClienteTCP.java
COPY src/servidor/ServidorTCP.java /app/servidor/ServidorTCP.java

# Compilar los archivos Java
RUN javac /app/cliente/ClienteTCP.java /app/servidor/ServidorTCP.java

# Fase de ejecución
FROM openjdk:21

# Directorio de trabajo para ejecutar el servidor o cliente
WORKDIR /app

# Copiar los archivos compilados desde la fase anterior
COPY --from=build /app/cliente /app/cliente
COPY --from=build /app/servidor /app/servidor

# Exponer el puerto en el que el servidor escuchará
EXPOSE 5000

# Ejecutar el servidor y luego el cliente
CMD ["sh", "-c", "java servidor.ServidorTCP & java cliente.ClienteTCP"]
