package br.com.lojacamisetas.projetojavaloja.service;

import org.springframework.web.server.ResponseStatusException;
import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import br.com.lojacamisetas.projetojavaloja.repository.CamisetaRepository;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPutRequestBody;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CamisetaService {
	

	private final CamisetaRepository camisetaRepository;
    
    public List<Camiseta> listAll() {
    	return camisetaRepository.findAll();
    }
    
    public List<Camiseta> findByClube(String clube) {
    	return camisetaRepository.findByClubeContaining(clube);
    }
    
    public List<Camiseta> findByPaisContaining(String pais_continente) {
    	return camisetaRepository.findByPaisContaining(pais_continente);
    }
    
    public Camiseta findByIdOrThrowBadRequestException(long id) {
        return camisetaRepository.findById(id)
        		.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Camisa Não encontrada"));
 	    }
    
    @Transactional
    public Camiseta save(CamisetaPostRequestBody camisetaPostRequestBody) {
    	return camisetaRepository.save(Camiseta.builder()
    			.clube(camisetaPostRequestBody.getClube())
    			.pais(camisetaPostRequestBody.getPais())
    			.ano(camisetaPostRequestBody.getAno())
                .quantidade(camisetaPostRequestBody.getQuantidade())
                .valor(camisetaPostRequestBody.getValor())
                .imagem(camisetaPostRequestBody.getImagem())
    			.build());
    }

	public void delete(long id) {
		camisetaRepository.delete(findByIdOrThrowBadRequestException(id));
		
	}
	
	public void replace(CamisetaPutRequestBody camisetaPutRequestBody) {
        Camiseta savedCamiseta = findByIdOrThrowBadRequestException(camisetaPutRequestBody.getId());
        Camiseta camiseta = Camiseta.builder()
                .id(savedCamiseta.getId())
                .clube(camisetaPutRequestBody.getClube())
                .pais(camisetaPutRequestBody.getPais())
                .ano(camisetaPutRequestBody.getAno())
                .quantidade(camisetaPutRequestBody.getQuantidade())
                .valor(camisetaPutRequestBody.getValor())
                .imagem(camisetaPutRequestBody.getImagem())
                .build();

        camisetaRepository.save(camiseta);
    }
	     
	    
}