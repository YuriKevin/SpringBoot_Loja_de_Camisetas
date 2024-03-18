package br.com.lojacamisetas.projetojavaloja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lojacamisetas.projetojavaloja.model.CamisetaVenda;

public interface CamisetaVendaRepository extends JpaRepository<CamisetaVenda, Long>{
	
	
}

