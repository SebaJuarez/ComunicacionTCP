package utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class Mensaje {
    private String ipOrigen;
    private String mensaje;
    private LocalDateTime fechaHora;
    private String destino;


    private TipoMensaje tipoMensaje;

    // Constructor vacío necesario para Jackson
    public Mensaje() {}

    // Constructor con parámetros
    @JsonCreator
    public Mensaje(
            @JsonProperty("ipOrigen") String ipOrigen,
            @JsonProperty("destino") String destino,
            @JsonProperty("mensaje") String mensaje,
            @JsonProperty("tipoMensaje") TipoMensaje tipoMensaje){
        this.ipOrigen = ipOrigen;
        this.destino = destino;
        this.mensaje = mensaje;
        this.tipoMensaje = tipoMensaje;
        this.fechaHora = LocalDateTime.now();
    }

    // Getters y Setters
    public String getIpOrigen() {
        return ipOrigen;
    }

    public void setIpOrigen(String ipOrigen) {
        this.ipOrigen = ipOrigen;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public TipoMensaje getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(TipoMensaje tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "ipOrigen='" + ipOrigen + '\'' +
                ", destino='" + destino + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", tipoMensaje=" + tipoMensaje.toString() + '\'' +
                ", fechaHora=" + fechaHora +
                '}';
    }
}