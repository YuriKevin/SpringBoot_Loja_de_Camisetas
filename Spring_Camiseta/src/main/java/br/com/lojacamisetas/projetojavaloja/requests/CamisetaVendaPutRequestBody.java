package br.com.lojacamisetas.projetojavaloja.requests;

import javax.validation.constraints.NotEmpty;

import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import lombok.Data;

@Data
public class CamisetaVendaPutRequestBody {
	
	long id;
	private Camiseta camiseta;
	int quantidade;
	float valor;
}
