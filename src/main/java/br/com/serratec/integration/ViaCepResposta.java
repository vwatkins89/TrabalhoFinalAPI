package br.com.serratec.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ViaCepResposta {
    private String logradouro; 
    private String localidade; 
    private String uf;         
    private boolean erro; 

    public String getLogradouro() {
        return logradouro;
    }
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }
    public String getLocalidade() {
        return localidade;
    }
    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }
    public String getUf() {
        return uf;
    }
    public void setUf(String uf) {
        this.uf = uf;
    }
    public boolean isErro() {
        return erro;
    }
    public void setErro(boolean erro) {
        this.erro = erro;
    }
}