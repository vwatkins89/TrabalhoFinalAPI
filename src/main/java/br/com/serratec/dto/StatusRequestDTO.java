package br.com.serratec.dto;

import jakarta.validation.constraints.NotBlank;

public class StatusRequestDTO {

    @NotBlank
    private String status;

    public StatusRequestDTO() {}

    public StatusRequestDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}