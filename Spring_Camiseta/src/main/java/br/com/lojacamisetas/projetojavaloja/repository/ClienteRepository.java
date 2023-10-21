package br.com.lojacamisetas.projetojavaloja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import br.com.lojacamisetas.projetojavaloja.classe.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

	Cliente findByCpf(String cpf);
	
}
