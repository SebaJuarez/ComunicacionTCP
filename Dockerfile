# Usar OpenJDK 21 como base
FROM openjdk:21

# Establecer directorio de trabajo
WORKDIR /app

# Copiar código fuente
COPY src/clienteServidor /app/clienteServidor
COPY src/logger /app/logger
COPY src/utils /app/utils
COPY src/registro /app/registro

# Copiar librerías externas (Jackson, etc.)
COPY lib /app/lib

# Compilar con el classpath que incluye todas las librerías
RUN javac -cp "lib/*:." -d . utils/*.java
RUN javac -cp "lib/*:." -d . logger/*.java
RUN javac -cp "lib/*:." -d . registro/*.java
RUN javac -cp "lib/*:." -d . clienteServidor/*.java

# Exponer puertos para la comunicación
EXPOSE 5000 5001 4000

