# Usar OpenJDK 21 como base
FROM openjdk:21

# Establecer directorio de trabajo
WORKDIR /app

# Copiar código fuente
COPY src/clienteServidor /app/clienteServidor
COPY src/logger /app/logger
COPY src/utils /app/utils

# Copiar librerías externas (Jackson, etc.)
COPY lib /app/lib

# Compilar con el classpath que incluye todas las librerías
WORKDIR /app
RUN javac -cp "lib/*:." -d . utils/Mapper.java utils/Mensaje.java
RUN javac -cp "lib/*:." -d . logger/Logger.java
RUN javac -cp "lib/*:." -d . clienteServidor/ClienteServidorTCP.java

# Exponer un puerto para la comunicación
EXPOSE 5000