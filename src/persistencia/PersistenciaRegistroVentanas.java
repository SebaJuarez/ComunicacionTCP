package persistencia;

import com.fasterxml.jackson.databind.ObjectMapper;
import logger.Logger;
import utils.Mapper;
import utils.RegistroVentana;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaRegistroVentanas {
    private static final String DIRECTORIO_LOGS = "logs-registros";
    private static final String ARCHIVO_JSON = DIRECTORIO_LOGS + "/inscripciones.json";
    private static final ObjectMapper objectMapper = Mapper.getInstance();

    // Guarda los registros de las ventanas de tiempo en un archivo JSON
    public static void guardarRegistro(RegistroVentana registro) {
        try {
            // Crear el directorio si no existe
            File directorio = new File(DIRECTORIO_LOGS);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // Leer los registros existentes
            List<RegistroVentana> registrosExistentes = new ArrayList<>();
            File archivo = new File(ARCHIVO_JSON);
            if (archivo.exists()) {
                registrosExistentes = objectMapper.readValue(archivo, objectMapper.getTypeFactory().constructCollectionType(List.class, RegistroVentana.class));
            }

            // Agregar el nuevo registro a la lista
            registrosExistentes.add(registro);

            // Sobrescribir el archivo con la lista actualizada de registros
            objectMapper.writeValue(archivo, registrosExistentes);
            Logger.sysoConHora("Registro guardado correctamente en " + ARCHIVO_JSON);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al guardar el registro.");
        }
    }
}