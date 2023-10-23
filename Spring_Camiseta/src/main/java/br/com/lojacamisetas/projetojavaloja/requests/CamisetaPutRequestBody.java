package br.com.lojacamisetas.projetojavaloja.requests;

import lombok.Data;

@Data
public class CamisetaPutRequestBody {
	Long id;
	String clube;
	String pais;
	int ano;
	int quantidade;
	float valor;
	String imagem;
}
