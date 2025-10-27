package br.com.serratec.exception;

public class ViacepException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ViacepException(String cep) {
        super("CEP inválido ou não encontrado: " + cep);
    }
    
    public ViacepException(String cep, Throwable causa) {
        super("Erro de comunicação com ViaCEP para o CEP: " + cep, causa);
    }
}