# Usar OpenJDK 21 como base
FROM openjdk:21

# Establecer directorio de trabajo
WORKDIR /app

# Copiar código fuente
COPY src/clienteServidor /app/clienteServidor
COPY src/logger /app/logger

# Compilar respetando la estructura de paquetes
WORKDIR /app
RUN javac -d . clienteServidor/ClienteServidorTCP.java
RUN javac -d . logger/Logger.java

# Exponer un puerto para la comunicación
EXPOSE 5000


