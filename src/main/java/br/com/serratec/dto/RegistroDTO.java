package br.com.serratec.dto;

import java.util.Set;

public record RegistroDTO(String nome, String email, String password, Set<String> roles) {}