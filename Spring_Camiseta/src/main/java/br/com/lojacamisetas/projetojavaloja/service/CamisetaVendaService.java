package br.com.lojacamisetas.projetojavaloja.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import br.com.lojacamisetas.projetojavaloja.classe.CamisetaVenda;
import br.com.lojacamisetas.projetojavaloja.repository.CamisetaRepository;
import br.com.lojacamisetas.projetojavaloja.repository.CamisetaVendaRepository;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPutRequestBody;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CamisetaVendaService {
	
private final CamisetaVendaRepository camisetaVendaRepository;
private final CamisetaService camisetaService;
    
    public List<CamisetaVenda> listAll() {
    	return camisetaVendaRepository.findAll();
    }
    
    
    public CamisetaVenda findByIdOrThrowBadRequestException(long id) {
        return camisetaVendaRepository.findById(id)
        		.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Camiseta Não encontrada"));
 	    }
    
    @Transactional
    public CamisetaVenda save(CamisetaVendaPostRequestBody camisetaVendaPostRequestBody) {
    	Long camisetaId = camisetaVendaPostRequestBody.getCamiseta().getId();
    	Camiseta camisetaBanco = camisetaService.findByIdOrThrowBadRequestException(camisetaId);
    	
    	return camisetaVendaRepository.save(CamisetaVenda.builder()
    			.camiseta(camisetaBanco)
                .quantidade(camisetaVendaPostRequestBody.getQuantidade())
                .valor(camisetaBanco.getValor())
                .venda(camisetaVendaPostRequestBody.getVenda())
    			.build());
    }

	public void delete(long id) {
		camisetaVendaRepository.delete(findByIdOrThrowBadRequestException(id));
		
	}
	
	public void replace(CamisetaVendaPutRequestBody camisetaVendaPutRequestBody) {
        CamisetaVenda savedCamiseta = findByIdOrThrowBadRequestException(camisetaVendaPutRequestBody.getId());
        CamisetaVenda camiseta = CamisetaVenda.builder()
                .id(savedCamiseta.getId())
                .camiseta(savedCamiseta.getCamiseta())
                .quantidade(camisetaVendaPutRequestBody.getQuantidade())
                .valor(savedCamiseta.getValor())
                .build();

        camisetaVendaRepository.save(camiseta);
    }
}
