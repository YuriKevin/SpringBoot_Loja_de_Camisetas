package br.com.lojacamisetas.projetojavaloja.requests;

import javax.validation.constraints.NotEmpty;

import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import lombok.Data;

@Data
public class CamisetaVendaPutRequestBody {
	
	private long id;
	private Camiseta camiseta;
	private int quantidade;
	private float valor;
	private long vendaId;
}
