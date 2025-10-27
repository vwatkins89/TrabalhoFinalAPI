package br.com.serratec.integration;

import br.com.serratec.exception.ViacepException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

@Component
public class ViaCepCliente {

    private RestTemplate RestTemplate;

	public ViaCepCliente(RestTemplate restTemplate) {
        this.setRestTemplate(restTemplate);
    }

    public ViaCepResposta obterEnderecoPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        try {
            ViaCepResposta resposta = RestTemplate.getForObject(url, ViaCepResposta.class);
            
            if (resposta == null || resposta.isErro()) {
                throw new ViacepException(cep); 
            }
            return resposta;
        } catch (ResourceAccessException e) {
            throw new ViacepException(cep, e);
        } catch (Exception e) {
            throw new ViacepException(cep, e);
        }
    }

	public RestTemplate getRestTemplate() {
		return RestTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		RestTemplate = restTemplate;
	}
}