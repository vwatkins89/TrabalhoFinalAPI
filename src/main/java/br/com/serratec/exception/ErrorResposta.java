package br.com.serratec.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResposta {

    private int status;
    private String titulo;
    private LocalDateTime timestamp;
    private List<String> erros;

    public ErrorResposta() {}

    public ErrorResposta(int status, String titulo, LocalDateTime timestamp, List<String> erros) {
        this.status = status;
        this.titulo = titulo;
        this.timestamp = timestamp;
        this.erros = erros;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getErros() {
        return erros;
    }

    public void setErros(List<String> erros) {
        this.erros = erros;
    }
}