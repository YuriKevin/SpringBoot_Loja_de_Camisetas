package br.com.lojacamisetas.projetojavaloja.requests;

import lombok.Data;

@Data
public class CamisetaPutRequestBody {
	Long id;
	String clube;
	int ano;
	int quantidade;
	float valor;
}
