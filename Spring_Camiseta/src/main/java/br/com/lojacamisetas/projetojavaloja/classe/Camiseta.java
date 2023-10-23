package br.com.lojacamisetas.projetojavaloja.classe;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Camiseta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String clube;
	String pais;
	int ano;
	int quantidade;
	float valor;
	String imagem;
}
