package br.com.lojacamisetas.projetojavaloja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lojacamisetas.projetojavaloja.model.Camiseta;

public interface CamisetaRepository extends JpaRepository<Camiseta, Long>{
	
	List<Camiseta> findByClubeContaining(String clube);
	List<Camiseta> findByPaisContaining(String pais);
	
}
