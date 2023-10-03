package br.com.lojacamisetas.projetojavaloja.classe;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class CamisetaVenda {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne
    @JoinColumn(name = "camiseta_id")
    Camiseta camiseta;
	
	@ManyToOne
    @JoinColumn(name = "venda_id")
	@JsonBackReference
    Venda venda;
    
	int quantidade;
	float valor;
}
