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
    	return camisetaRepository.findByClube(clube);
    }
    
    public Camiseta findByIdOrThrowBadRequestException(long id) {
        return camisetaRepository.findById(id)
        		.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Camisa Não encontrada"));
 	    }
    
    @Transactional
    public Camiseta save(CamisetaPostRequestBody camisetaPostRequestBody) {
    	return camisetaRepository.save(Camiseta.builder()
    			.clube(camisetaPostRequestBody.getClube())
    			.ano(camisetaPostRequestBody.getAno())
                .quantidade(camisetaPostRequestBody.getQuantidade())
                .valor(camisetaPostRequestBody.getValor())
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
                .ano(camisetaPutRequestBody.getAno())
                .quantidade(camisetaPutRequestBody.getQuantidade())
                .valor(camisetaPutRequestBody.getValor())
                .build();

        camisetaRepository.save(camiseta);
    }
	     
	    
}









//private static List<Camiseta> camisetas = new ArrayList<Camiseta>();
/*public CamisetaService(){
Camiseta camisetaMilan = new Camiseta(1L, "Milan", 2003, 52, 500);
camisetas.add(camisetaMilan);
Camiseta camisetaBarcelona = new Camiseta(2L, "Barcelona", 2015, 52, 700);
camisetas.add(camisetaBarcelona);
Camiseta camisetaBrasil = new Camiseta(3L, "Brasil", 2002, 1, 20000);
camisetas.add(camisetaBrasil);
Camiseta camisetaSantos = new Camiseta(4L, "Santos", 2011, 2, 2500);
camisetas.add(camisetaSantos);
Camiseta camisetaManchesterCity = new Camiseta(5L, "Manchester City", 2002, 1, 20000);
camisetas.add(camisetaManchesterCity);
Camiseta camisetaParma = new Camiseta(6L, "Parma", 2002, 1, 10000);
camisetas.add(camisetaParma);
Camiseta camisetaBayernLeverkusen = new Camiseta(7L, "Bayern Leverkusen", 2023, 1, 500);
camisetas.add(camisetaBayernLeverkusen);
}*/
