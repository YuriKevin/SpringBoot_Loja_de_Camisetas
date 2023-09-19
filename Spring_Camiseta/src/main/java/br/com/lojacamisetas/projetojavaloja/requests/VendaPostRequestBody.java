package br.com.lojacamisetas.projetojavaloja.requests;

import java.time.LocalDate;
import java.util.List;

import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import br.com.lojacamisetas.projetojavaloja.classe.Cliente;
import lombok.Data;

@Data
public class VendaPostRequestBody {
	private float valor;
	private LocalDate dia_venda;
	private Cliente cliente;
	private List<Camiseta> camisetas; // Lista de camisetas associadas a esta venda
}

