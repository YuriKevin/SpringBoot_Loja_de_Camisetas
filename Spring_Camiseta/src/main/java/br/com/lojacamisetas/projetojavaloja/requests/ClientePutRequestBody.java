package br.com.lojacamisetas.projetojavaloja.requests;

import java.util.List;

import br.com.lojacamisetas.projetojavaloja.classe.Venda;
import lombok.Data;

@Data
public class ClientePutRequestBody {
	private Long id;
	private String nome;
	private String cpf;
	private List<Venda> vendas;
}

