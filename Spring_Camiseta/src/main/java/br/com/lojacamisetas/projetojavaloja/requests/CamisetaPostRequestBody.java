package br.com.lojacamisetas.projetojavaloja.requests;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.NonNull;

import lombok.Data;

@Data
public class CamisetaPostRequestBody {

	@NotEmpty(message = "Camiseta não pode estar vazio.")
	private String clube;
	private String pais;
	private int ano;
	private int quantidade;
	private float valor;
	private String imagem;
}
