package br.com.lojacamisetas.projetojavaloja.requests;

import java.time.LocalDate;
import java.util.List;

import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import br.com.lojacamisetas.projetojavaloja.classe.CamisetaVenda;
import br.com.lojacamisetas.projetojavaloja.classe.Cliente;
import lombok.Data;

@Data
public class VendaPutRequestBody {
	private Long id;
	private LocalDate dia_venda;
	private float valor;
	private Cliente cliente;
	private List<CamisetaVenda> camisetaVendas; 
}