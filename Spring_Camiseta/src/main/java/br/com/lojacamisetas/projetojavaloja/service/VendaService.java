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
		List<Long> vendaIds = cliente.getVendaIds();
		List<Venda> arrayDeVendas = new ArrayList<>();
		for (Long vendaId : vendaIds) {
			Venda venda = this.findByIdOrThrowBadRequestException(vendaId);
			arrayDeVendas.add(venda);
		}
		return arrayDeVendas;
	}

	@Transactional
	public Venda save(VendaPostRequestBody vendaPostRequestBody) {
	    List<CamisetaVenda> camisetas = vendaPostRequestBody.getCamisetaVendas();
	    Cliente cliente = vendaPostRequestBody.getCliente();
	    
	    Cliente clienteBanco = clienteRepository.findById(cliente.getId())
	    	    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente não encontrado"));
	    
	    Venda venda = Venda.builder()
		        .dia_venda(vendaPostRequestBody.getDia_venda())
		        .valor(0)
		        .camisetaVendas(camisetas)
		        .cliente(cliente)
		        .build();

	    Venda savedVenda = vendaRepository.save(venda);

	    

	    	clienteBanco.getVendaIds().add(savedVenda.getId());
	    	savedVenda.setCliente(clienteBanco);

	    	clienteRepository.save(clienteBanco);
	    	
	    	List<CamisetaVenda> camisetasBanco = new ArrayList<>();

	    	 
	    for (CamisetaVenda camiseta : camisetas) {
	   
    	        Camiseta camisetaBanco = camisetaRepository.findById(camiseta.getCamiseta().getId())
    	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Camiseta não encontrada"));
    	        
    	       
    	        if (camisetaBanco.getQuantidade() >= camiseta.getQuantidade()) {
    	            camisetaBanco.setQuantidade(camisetaBanco.getQuantidade() - camiseta.getQuantidade());

    	            camisetaRepository.save(camisetaBanco);
    	        }
    	        else {
    	        	throw new RuntimeException("A camiseta "+camiseta.getCamiseta().getClube()+" esgotou :(");
    	            }
    	        savedVenda.setValor(savedVenda.getValor() + camisetaBanco.getValor() * camiseta.getQuantidade());
    	        
    	        CamisetaVendaPostRequestBody camisetaVenda = new CamisetaVendaPostRequestBody();
    	        camisetaVenda.setCamiseta(camisetaBanco);
    	        camisetaVenda.setQuantidade(camiseta.getQuantidade());
    	        camisetaVenda.setValor(camisetaBanco.getValor());
    	        camisetaVenda.setVenda(savedVenda);
    	        
    	        
    	        CamisetaVenda nova_camiseta_venda = camisetaVendaService.save(camisetaVenda);
    	        
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
	    	Cliente cliente_atual = clienteService.findByIdOrThrowBadRequestException(id_cliente_atual);
	    	cliente_atual.getVendaIds().removeIf(id -> id == id_venda);
	    	clienteRepository.save(cliente_atual);
	    	
	    	Cliente novo_cliente = clienteService.findByIdOrThrowBadRequestException(id_novo_cliente);
	    	novo_cliente.getVendaIds().add(id_venda);
	    	clienteRepository.save(novo_cliente);
	    	
	    	savedVenda.setCliente(novo_cliente);
	    	
	    }
	    /*
	    List<CamisetaVenda> camisetas_antes = savedVenda.getCamisetaVendas();
	    
	    
	    for (CamisetaVenda camiseta : camisetas_antes) {
		 	   
	        Camiseta camisetaBanco = camisetaRepository.findById(camiseta.getId())
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Camiseta não encontrada"));
	        
	            camisetaBanco.setQuantidade(camisetaBanco.getQuantidade() + camiseta.getQuantidade());

	            camisetaRepository.save(camisetaBanco);
	      
	    }
	    
	    
	    long id_cliente_atual = savedVenda.getCliente().getId();
	    long id_novo_cliente = vendaPutRequestBody.getCliente().getId();
	    long id_venda = savedVenda.getId();
	    
	    if(id_cliente_atual == id_novo_cliente) {
	    	
	    }
	    else {
	    	Cliente cliente_atual = clienteService.findByIdOrThrowBadRequestException(id_cliente_atual);
	    	cliente_atual.getVendaIds().removeIf(id -> id == id_venda);
	    	clienteRepository.save(cliente_atual);
	    	
	    	Cliente novo_cliente = clienteService.findByIdOrThrowBadRequestException(id_novo_cliente);
	    	novo_cliente.getVendaIds().add(id_venda);
	    	clienteRepository.save(novo_cliente);
	    	
	    	savedVenda.setCliente(novo_cliente);
	    	
	    }
	    
	    savedVenda.setValor(0);
	    
	    List<CamisetaVenda> camisetas_depois = vendaPutRequestBody.getCamisetaVendas();
	    List<CamisetaVendaPostRequestBody> camisetas_atualizadas = new ArrayList<>();
	    
	    for (CamisetaVenda camiseta : camisetas_depois) {
	 	   
	        Camiseta camisetaBanco = camisetaRepository.findById(camiseta.getId())
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Camiseta não encontrada"));
	        if (camisetaBanco.getQuantidade() >= camiseta.getQuantidade()) {
	            camisetaBanco.setQuantidade(camisetaBanco.getQuantidade() - camiseta.getQuantidade());

	            camisetaRepository.save(camisetaBanco);
	        }
	        else {
	        	throw new RuntimeException("A camiseta "+camiseta.getCamiseta().getClube()+" esgotou :(");
	            }
	        savedVenda.setValor(savedVenda.getValor() + camisetaBanco.getValor() * camiseta.getQuantidade());
	        
    }
	   
	    
	    savedVenda.setDia_venda(vendaPutRequestBody.getDia_venda());
	    savedVenda.setCamisetaVendas(vendaPutRequestBody.getCamisetaVendas());
	     */
	    
	    vendaRepository.save(savedVenda);
	}

	     @Transactional
		public Venda addCamisetasVenda(CamisetaVendaPostRequestBody camiseta) {
			Long camisetaId = camiseta.getCamiseta().getId();
		    Camiseta camisetaBanco = camisetaService.findByIdOrThrowBadRequestException(camisetaId);
		    
		    
		    
		    Long vendaId = camiseta.getVenda().getId();
		    Venda venda = findByIdOrThrowBadRequestException(vendaId);
		    
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
		    	camiseta.setValor(camisetaBanco.getValor());
			    camiseta.setCamiseta(camisetaBanco);
			    
			    CamisetaVenda camiseta_salva = camisetaVendaService.save(camiseta);
			    camisetas.add(camiseta_salva);
		    }
		    
		    
		    
		    venda.setCamisetaVendas(camisetas);
		    
		    float valor = 0;
		    for(CamisetaVenda camiseta_array : camisetas) {
		    	
		    	valor+= camiseta_array.getValor() * camiseta_array.getQuantidade();
		    	
		    }
		    
		    venda.setValor(valor);
	
		    return vendaRepository.save(venda);
		}
	     
	     @Transactional
			public Venda RemoveCamisetasVenda(CamisetaVenda camiseta) {
				Long camisetaId = camiseta.getCamiseta().getId();
			    Camiseta camisetaBanco = camisetaService.findByIdOrThrowBadRequestException(camisetaId);
			    
			    Long vendaId = camiseta.getVenda().getId();
			    Venda venda = findByIdOrThrowBadRequestException(vendaId);
			    
			    
			    List<CamisetaVenda> camisetas = venda.getCamisetaVendas();
			    
	
			    CamisetaVenda camisetaVendaEncontrada = camisetas.stream()
		                .filter(camisetaVenda -> camisetaVenda.getCamiseta().getId().equals(camisetaId))
		                .findFirst()
		                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao procurar camiseta"));
		    	
			    if(camisetaVendaEncontrada.getQuantidade() <= camiseta.getQuantidade()) {
			    	camisetaVendaService.delete(camisetaVendaEncontrada.getId());
			    	camisetas.remove(camisetaVendaEncontrada);
			    	
			    }
			    else {
			    	int quantidade = camisetaVendaEncontrada.getQuantidade() - camiseta.getQuantidade();
			    	
			    	camisetaVendaEncontrada.setQuantidade(quantidade);
			    }
			    
		    	
			    
			    float valor = 0;
			    
			    for(CamisetaVenda camiseta_array: camisetas) {
			    		
			    	valor+= camiseta_array.getValor() * camiseta_array.getQuantidade();
			    	
			    }
			    venda.setValor(valor);
			    
			    
			    venda.setCamisetaVendas(camisetas);
		
			    return vendaRepository.save(venda);
			}
}