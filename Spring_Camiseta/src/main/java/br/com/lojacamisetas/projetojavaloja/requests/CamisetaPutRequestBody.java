package br.com.lojacamisetas.projetojavaloja.requests;

import lombok.Data;

@Data
public class CamisetaPutRequestBody {
	private Long id;
	private String clube;
	private String pais;
	private int ano;
	private int quantidade;
	private float valor;
	private String imagem;
}
