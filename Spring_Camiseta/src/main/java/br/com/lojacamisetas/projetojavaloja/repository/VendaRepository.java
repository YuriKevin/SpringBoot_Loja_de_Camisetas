package br.com.lojacamisetas.projetojavaloja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lojacamisetas.projetojavaloja.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long>{
	
}
