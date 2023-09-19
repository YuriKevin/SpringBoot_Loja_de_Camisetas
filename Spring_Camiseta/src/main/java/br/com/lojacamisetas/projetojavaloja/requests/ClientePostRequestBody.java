package br.com.lojacamisetas.projetojavaloja.requests;

import lombok.Data;

@Data
public class ClientePostRequestBody {
	private String nome;
	private String cpf;
}
