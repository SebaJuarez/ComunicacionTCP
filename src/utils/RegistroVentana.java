package utils;

import java.time.LocalDateTime;
import java.util.List;

public class RegistroVentana {
    private int numeroVentana;
    private LocalDateTime hora;
    private List<String> nodosRegistrados;

    public RegistroVentana() {
    }

    public RegistroVentana(int numeroVentana, List<String> nodosRegistrados) {
        this.numeroVentana = numeroVentana;
        this.hora = LocalDateTime.now();
        this.nodosRegistrados = nodosRegistrados;
    }

    public int getNumeroVentana() {
        return numeroVentana;
    }

    public void setNumeroVentana(int numeroVentana) {
        this.numeroVentana = numeroVentana;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public List<String> getNodosRegistrados() {
        return nodosRegistrados;
    }

    public void setNodosRegistrados(List<String> nodosRegistrados) {
        this.nodosRegistrados = nodosRegistrados;
    }

    @Override
    public String toString() {
        return "RegistroVentana{" +
                "numeroVentana=" + numeroVentana +
                ", hora=" + hora.toString() +
                ", nodosRegistrados=" + nodosRegistrados +
                '}';
    }
}

