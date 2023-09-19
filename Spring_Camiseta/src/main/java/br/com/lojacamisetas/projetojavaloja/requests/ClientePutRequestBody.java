package br.com.lojacamisetas.projetojavaloja.requests;

import lombok.Data;

@Data
public class ClientePutRequestBody {
	private Long id;
	private String nome;
	private String cpf;
}
