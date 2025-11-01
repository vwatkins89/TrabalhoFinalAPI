package br.com.serratec.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.serratec.dto.ViaCepResponseDTO;

@Service
public class ViaCepService {
    private final RestTemplate rest = new RestTemplate();

    public ViaCepResponseDTO consultarCep(String cep) {
        String cleanCep = cep == null ? "" : cep.replaceAll("\\D", "");
        String url = UriComponentsBuilder.fromHttpUrl("https://viacep.com.br/ws")
            .pathSegment(cleanCep, "json")
            .toUriString();
        try {
        	ViaCepResponseDTO resp = rest.getForObject(url, ViaCepResponseDTO.class);
        	if (resp == null) throw new RuntimeException("ViaCEP retornou null");
        	if (resp.isErro()) throw new RuntimeException("ViaCEP indicou erro para o CEP: " + cep);
            return resp;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Erro HTTP ao consultar ViaCEP: " + e.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar ViaCEP: " + e.getMessage());
        }
    }
}