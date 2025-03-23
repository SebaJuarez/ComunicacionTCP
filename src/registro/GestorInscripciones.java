package registro;

import logger.Logger;
import persistencia.PersistenciaRegistroVentanas;
import utils.RegistroVentana;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GestorInscripciones {
    private static final PersistenciaRegistroVentanas persistencia = new PersistenciaRegistroVentanas();
    private final ConcurrentLinkedQueue<String> inscripcionesActuales = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<String> inscripcionesFuturas = new ConcurrentLinkedQueue<>();
    private int numeroVentana = 1; // Número de ventanas de tiempo


    // Actualiza la ventana y guarda el registro de la ventana
    public void actualizarVentana() {
        // Guardamos el registro de la ventana actual
        RegistroVentana registro = new RegistroVentana(numeroVentana++, List.copyOf(inscripcionesActuales));
        persistencia.guardarRegistro(registro);

        // Limpiar las inscripciones y mover las futuras a las actuales
        inscripcionesActuales.clear();
        inscripcionesActuales.addAll(inscripcionesFuturas);
        inscripcionesFuturas.clear();
        Logger.sysoConHora("Ventana actualizada: " + inscripcionesActuales);
    }

    // Registrar un nodo en la ventana futura
    public synchronized void registrarNodo(String nodoInfo) {
        String ip = nodoInfo.split(":")[0];
        inscripcionesFuturas.removeIf(nodo -> nodo.split(":")[0].equals(ip));
        inscripcionesFuturas.add(nodoInfo);
    }

    public ConcurrentLinkedQueue<String> obtenerInscripcionesActuales() {
        return inscripcionesActuales;
    }

    public int getVentanaActual() {
        return numeroVentana;
    }
}

