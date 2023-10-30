package br.com.lojacamisetas.projetojavaloja.requests;

import javax.validation.constraints.NotEmpty;
import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import br.com.lojacamisetas.projetojavaloja.classe.Venda;
import lombok.Data;

@Data
public class CamisetaVendaPostRequestBody {

	private Camiseta camiseta;
	private int quantidade;
	private float valor;
	private Venda venda;
	private long vendaId;

}
