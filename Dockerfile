# Usar OpenJDK 21 como base
FROM openjdk:21

# Establecer directorio de trabajo
WORKDIR /app

# Copiar todo el código fuente
COPY src /app

# Compilar el código respetando la estructura de paquetes
RUN javac -d . clienteServidor/ClienteServidorTCP.java