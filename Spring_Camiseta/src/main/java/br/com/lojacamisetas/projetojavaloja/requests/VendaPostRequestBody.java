package br.com.lojacamisetas.projetojavaloja.requests;

import java.time.LocalDate;
import java.util.List;

import br.com.lojacamisetas.projetojavaloja.model.Camiseta;
import br.com.lojacamisetas.projetojavaloja.model.CamisetaVenda;
import br.com.lojacamisetas.projetojavaloja.model.Cliente;
import lombok.Data;

@Data
public class VendaPostRequestBody {
	private float valor;
	private LocalDate dia_venda;
	private Cliente cliente;
	private List<CamisetaVenda> camisetaVendas; 
}

