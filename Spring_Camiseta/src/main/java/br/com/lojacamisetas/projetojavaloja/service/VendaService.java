package br.com.lojacamisetas.projetojavaloja.service;

import org.springframework.web.server.ResponseStatusException;
import br.com.lojacamisetas.projetojavaloja.classe.Venda;
import br.com.lojacamisetas.projetojavaloja.repository.CamisetaRepository;
import br.com.lojacamisetas.projetojavaloja.repository.CamisetaVendaRepository;
import br.com.lojacamisetas.projetojavaloja.repository.ClienteRepository;
import br.com.lojacamisetas.projetojavaloja.repository.VendaRepository;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.VendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.VendaPutRequestBody;
import lombok.RequiredArgsConstructor;
import br.com.lojacamisetas.projetojavaloja.classe.Cliente;
import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import br.com.lojacamisetas.projetojavaloja.classe.CamisetaVenda;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPutRequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class VendaService {
	
	private final VendaRepository vendaRepository;
	private final ClienteRepository clienteRepository;
	private final CamisetaRepository camisetaRepository;
	private final CamisetaVendaRepository camisetaVendaRepository;
    private final ClienteService clienteService;
    private final CamisetaVendaService camisetaVendaService;
    private final CamisetaService camisetaService;
	   
	public List<Venda> listAll() {
    	return vendaRepository.findAll();
    }

	public Venda findByIdOrThrowBadRequestException(long id) {
        return vendaRepository.findById(id)
        		.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Venda Não encontrada"));
 	    }

	public List<Venda> findVendas(long clienteId) {
		Cliente cliente = clienteService.findByIdOrThrowBadRequestException(clienteId);
		List<Venda> arrayDeVendas = cliente.getVendas();
		
		return arrayDeVendas;
	}

	@Transactional
	public Venda save(VendaPostRequestBody vendaPostRequestBody) {
	    List<CamisetaVenda> camisetas = vendaPostRequestBody.getCamisetaVendas();
	    LocalDate localDate = LocalDate.now();
	    Cliente clienteBanco = clienteRepository.findById(vendaPostRequestBody.getCliente().getId())
	    	    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente não encontrado"));
	    
	    Venda venda = Venda.builder()
		        .dia_venda(localDate)
		        .valor(0)
		        .cliente(clienteBanco)
		        .build();

	    Venda savedVenda = vendaRepository.save(venda);

	   
	    	
	    	List<CamisetaVenda> camisetasBanco = new ArrayList<>();

	    	 
	    for (CamisetaVenda camiseta : camisetas) {
	   
    	        Camiseta camisetaBanco = camisetaRepository.findById(camiseta.getCamiseta().getId())
    	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Camiseta não encontrada"));
    	        
    	       
    	        if (camisetaBanco.getQuantidade() >= camiseta.getQuantidade()) {
    	            camisetaBanco.setQuantidade(camisetaBanco.getQuantidade() - camiseta.getQuantidade());

    	            camisetaRepository.save(camisetaBanco);
    	        }
    	        else {
    	        	throw new RuntimeException("A camiseta "+camiseta.getCamiseta().getClube()+" possui "+camisetaBanco.getQuantidade()+" disponíveis.");
    			    }
    	        savedVenda.setValor(savedVenda.getValor() + camisetaBanco.getValor() * camiseta.getQuantidade());
    	        
    	        CamisetaVenda nova_camiseta_venda = new CamisetaVenda();
    	        nova_camiseta_venda.setCamiseta(camisetaBanco);
    	        nova_camiseta_venda.setQuantidade(camiseta.getQuantidade());
    	        nova_camiseta_venda.setValor(camisetaBanco.getValor());
    	        nova_camiseta_venda.setVenda(savedVenda);
    	        
    	        
    	       
    	        
    	        camisetasBanco.add(nova_camiseta_venda);
	    }
	    savedVenda.setCamisetaVendas(camisetasBanco);
	    
    	    
	    return vendaRepository.save(savedVenda);
	}



	public void delete(long id) {
		vendaRepository.delete(findByIdOrThrowBadRequestException(id));	
	}

	@Transactional
	public void replace(VendaPutRequestBody vendaPutRequestBody) {
	    Venda savedVenda = findByIdOrThrowBadRequestException(vendaPutRequestBody.getId());
	    savedVenda.setDia_venda(vendaPutRequestBody.getDia_venda());
	    
	    long id_cliente_atual = savedVenda.getCliente().getId();
	    long id_novo_cliente = vendaPutRequestBody.getCliente().getId();
	    long id_venda = savedVenda.getId();
	    
	    if(id_cliente_atual == id_novo_cliente) {
	    	
	    }
	    else{
	    	Cliente cliente_antigo = clienteService.findByIdOrThrowBadRequestException(id_cliente_atual);
	    	Cliente cliente_novo = clienteService.findByIdOrThrowBadRequestException(id_novo_cliente);
	    	savedVenda.setCliente(cliente_novo);
	    }
	    
	    vendaRepository.save(savedVenda);
	}

	     @Transactional
		public Venda addCamisetasVenda(CamisetaVendaPostRequestBody camiseta) {
			Long camisetaId = camiseta.getCamiseta().getId();
		    Camiseta camisetaBanco = camisetaService.findByIdOrThrowBadRequestException(camisetaId);
		    if(camisetaBanco.getQuantidade() < camiseta.getQuantidade()) {
		    	throw new RuntimeException("A camiseta "+camiseta.getCamiseta().getClube()+" possui "+camisetaBanco.getQuantidade()+" disponíveis.");
		    }
		    
		    
		    Venda venda = findByIdOrThrowBadRequestException(camiseta.getVendaId());
		    
		    List<CamisetaVenda> camisetas = venda.getCamisetaVendas();
		    
		    boolean camisetaJaExistente = camisetas.stream()
		            .anyMatch(camisetaVenda -> camisetaVenda.getCamiseta().getId().equals(camisetaId));

		    if (camisetaJaExistente) {
		       
		    	CamisetaVenda camisetaVendaEncontrada = camisetas.stream()
		                .filter(camisetaVenda -> camisetaVenda.getCamiseta().getId().equals(camisetaId))
		                .findFirst()
		                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao procurar camiseta"));
		    	
		    	int quantidade = camisetaVendaEncontrada.getQuantidade() + camiseta.getQuantidade();
		    	
		    	camisetaVendaEncontrada.setQuantidade(quantidade);
		    	
		    	
		    }
		    else {
			    CamisetaVenda nova_camiseta = new CamisetaVenda();
			    nova_camiseta.setCamiseta(camisetaBanco);
			    nova_camiseta.setValor(camisetaBanco.getValor());
			    nova_camiseta.setVenda(venda);
			    nova_camiseta.setQuantidade(camiseta.getQuantidade());
			    camisetas.add(nova_camiseta);
		    }
		    camisetaBanco.setQuantidade(camisetaBanco.getQuantidade() - camiseta.getQuantidade());
		    camisetaRepository.save(camisetaBanco);
		    
		    venda.setCamisetaVendas(camisetas);
		    
		    float valor = 0;
		    for(CamisetaVenda camiseta_array : camisetas) {
		    	
		    	valor+= camiseta_array.getValor() * camiseta_array.getQuantidade();
		    	
		    }
		    
		    venda.setValor(valor);
	
		    return vendaRepository.save(venda);
		}
	     
	     @Transactional
			public Venda atualizaCamisetasVenda(CamisetaVendaPutRequestBody camiseta) {
				Long camisetaId = camiseta.getCamiseta().getId();
			    CamisetaVenda camisetaVendaBanco = camisetaVendaService.findByIdOrThrowBadRequestException(camiseta.getId());
			    Camiseta camisetaBanco = camisetaService.findByIdOrThrowBadRequestException(camisetaId);
			    Venda venda = findByIdOrThrowBadRequestException(camisetaVendaBanco.getVenda().getId());
			    
			    
			    List<CamisetaVenda> camisetas = venda.getCamisetaVendas();
			    
	
			    CamisetaVenda camisetaVendaEncontrada = camisetas.stream()
		                .filter(camisetaVenda -> camisetaVenda.getCamiseta().getId().equals(camisetaId))
		                .findFirst()
		                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao procurar camiseta"));
		    	
			    if(camiseta.getQuantidade() == 0) {
			    	camisetaBanco.setQuantidade(camisetaBanco.getQuantidade()+camisetaVendaEncontrada.getQuantidade());
			    	camisetaVendaService.delete(camisetaVendaEncontrada.getId());
			    	camisetas.remove(camisetaVendaEncontrada);
			    	
			    }
			    else {
			    	if(camiseta.getQuantidade() > camisetaVendaEncontrada.getQuantidade()) {
			    		int quantidade = camiseta.getQuantidade() - camisetaVendaEncontrada.getQuantidade();
			    		camisetaBanco.setQuantidade(camisetaBanco.getQuantidade() - quantidade);
			    	}
			    	else if(camiseta.getQuantidade() < camisetaVendaEncontrada.getQuantidade()) {
			    		int quantidade =  camisetaVendaEncontrada.getQuantidade() - camiseta.getQuantidade();
			    		camisetaBanco.setQuantidade(camisetaBanco.getQuantidade() + quantidade);
			    	}
			    	else {
			    		
			    	}
			    	camisetaVendaEncontrada.setQuantidade(camiseta.getQuantidade());
			    	camisetaRepository.save(camisetaBanco);
			    }
			    
		    	
			    
			    float valor = 0;
			    
			    for(CamisetaVenda camiseta_array: camisetas) {
			    		
			    	valor+= camiseta_array.getValor() * camiseta_array.getQuantidade();
			    	
			    }
			    venda.setValor(valor);
			    
			    
			    venda.setCamisetaVendas(camisetas);
			    
		
			    return vendaRepository.save(venda);
			}
	     
	     @Transactional
	 	public Long saveManagement(long id) {
	 	    LocalDate localDate = LocalDate.now();
	 	    Cliente clienteBanco = clienteRepository.findById(id)
	 	    	    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente não encontrado"));
	 	    
	 	    Venda venda = Venda.builder()
	 		        .dia_venda(localDate)
	 		        .valor(0)
	 		        .cliente(clienteBanco)
	 		        .build();

	 	    return vendaRepository.save(venda).getId();
	 	 

	     }
}