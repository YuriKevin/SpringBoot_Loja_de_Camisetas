package br.com.lojacamisetas.projetojavaloja.classe;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Venda {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate dia_venda;
	private float valor;
	
	@ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties("vendas")
    private Cliente cliente;
	
	@OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
	@JsonManagedReference
    private List<CamisetaVenda> camisetaVendas;
	
	

}

