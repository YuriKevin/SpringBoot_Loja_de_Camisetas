package br.com.lojacamisetas.projetojavaloja.service;

import org.springframework.web.server.ResponseStatusException;

import br.com.lojacamisetas.projetojavaloja.model.Camiseta;
import br.com.lojacamisetas.projetojavaloja.model.CamisetaVenda;
import br.com.lojacamisetas.projetojavaloja.model.Cliente;
import br.com.lojacamisetas.projetojavaloja.model.Venda;
import br.com.lojacamisetas.projetojavaloja.repository.CamisetaRepository;
import br.com.lojacamisetas.projetojavaloja.repository.CamisetaVendaRepository;
import br.com.lojacamisetas.projetojavaloja.repository.ClienteRepository;
import br.com.lojacamisetas.projetojavaloja.repository.VendaRepository;
import br.com.lojacamisetas.projetojavaloja.requests.VendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.VendaPutRequestBody;
import lombok.RequiredArgsConstructor;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPutRequestBody;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class VendaService {
	private final VendaRepository vendaRepository;
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

	public List<Venda> findVendasByClienteId(long clienteId) {
		Cliente cliente = clienteService.findByIdOrThrowBadRequestException(clienteId);
		List<Venda> arrayDeVendas = cliente.getVendas();
		
		return arrayDeVendas;
	}

	@Transactional
	public Venda save(VendaPostRequestBody vendaPostRequestBody) {
	    Cliente clienteBanco = clienteService.findByIdOrThrowBadRequestException(vendaPostRequestBody.getCliente().getId());
	    
	    Venda venda = Venda.builder()
		        .dia_venda(LocalDate.now())
		        .valor(0)
		        .cliente(clienteBanco)
		        .build();

	    Venda savedVenda = vendaRepository.save(venda);
	    	
		List<CamisetaVenda> camisetasBanco = new ArrayList<>();
		List<CamisetaVenda> camisetasVenda = vendaPostRequestBody.getCamisetaVendas();
	    	 
	    for (CamisetaVenda camisetaVenda : camisetasVenda) {
    	        Camiseta camisetaBanco = camisetaService.findByIdOrThrowBadRequestException(camisetaVenda.getCamiseta().getId());
    	        
    	        if (camisetaBanco.getQuantidade() >= camisetaVenda.getQuantidade()) {
    	            camisetaBanco.setQuantidade(camisetaBanco.getQuantidade() - camisetaVenda.getQuantidade());
    	            camisetaService.saveExistingCamiseta(camisetaBanco);
    	        }
    	        else {
    	        	throw new RuntimeException("A camiseta "+camisetaVenda.getCamiseta().getClube()+" possui "+camisetaBanco.getQuantidade()+" disponíveis.");
				}
    	        CamisetaVenda nova_camiseta_venda = new CamisetaVenda();
    	        nova_camiseta_venda.setCamiseta(camisetaBanco);
    	        nova_camiseta_venda.setQuantidade(camisetaVenda.getQuantidade());
    	        nova_camiseta_venda.setValor(camisetaBanco.getValor());
    	        nova_camiseta_venda.setVenda(savedVenda);
    	        
    	        camisetasBanco.add(nova_camiseta_venda);
	    }
	    savedVenda.setCamisetas(camisetasBanco);
	    savedVenda.setValor(calcularValorVenda(savedVenda.getCamisetas()));
    	    
	    return vendaRepository.save(savedVenda);
	}

	public void delete(long id) {
		vendaRepository.delete(findByIdOrThrowBadRequestException(id));	
	}

	@Transactional
	public void replace(VendaPutRequestBody vendaPutRequestBody) {
	    Venda vendaSalva = findByIdOrThrowBadRequestException(vendaPutRequestBody.getId());
	    vendaSalva.setDia_venda(vendaPutRequestBody.getDia_venda());
	    
	    long id_cliente_atual = vendaSalva.getCliente().getId();
	    long id_novo_cliente = vendaPutRequestBody.getCliente().getId();
	    
	    if(id_cliente_atual != id_novo_cliente) {
	    	Cliente cliente_novo = clienteService.findByIdOrThrowBadRequestException(id_novo_cliente);
	    	vendaSalva.setCliente(cliente_novo);
	    }
	    
	    vendaRepository.save(vendaSalva);
	}

	     @Transactional
		public Venda adicionarCamisetasParaUmaVenda(CamisetaVendaPostRequestBody camiseta) {
			Long camisetaId = camiseta.getCamiseta().getId();
		    Camiseta camisetaBanco = camisetaService.findByIdOrThrowBadRequestException(camisetaId);

		    if(camisetaBanco.getQuantidade() < camiseta.getQuantidade()) {
		    	throw new RuntimeException("A camiseta "+camiseta.getCamiseta().getClube()+" possui "+camisetaBanco.getQuantidade()+" disponíveis.");
		    }
		    
		    Venda venda = findByIdOrThrowBadRequestException(camiseta.getVendaId());
		    
		    List<CamisetaVenda> camisetas = venda.getCamisetas();
		    
		    boolean camisetaJaExistente = camisetas.stream()
		            .anyMatch(camisetaVenda -> camisetaVenda.getCamiseta().getId().equals(camisetaId));

		    if (camisetaJaExistente) {
		    	CamisetaVenda camisetaVendaEncontrada = camisetas.stream()
		                .filter(camisetaVenda -> camisetaVenda.getCamiseta().getId().equals(camisetaId))
		                .findFirst()
		                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao procurar a camiseta"));
		    	
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
		    camisetaService.saveExistingCamiseta(camisetaBanco);
		    
		    venda.setCamisetas(camisetas);
		    
		    venda.setValor(calcularValorVenda(venda.getCamisetas()));
	
		    return vendaRepository.save(venda);
		}
	     
	     @Transactional
			public Venda atualizarCamisetasDeUmaVenda(CamisetaVendaPutRequestBody camiseta) {
				Long camisetaId = camiseta.getCamiseta().getId();
			    CamisetaVenda camisetaVendaBanco = camisetaVendaService.findByIdOrThrowBadRequestException(camiseta.getId());
			    Camiseta camisetaBanco = camisetaService.findByIdOrThrowBadRequestException(camisetaId);
			    Venda venda = findByIdOrThrowBadRequestException(camisetaVendaBanco.getVenda().getId());
    
			    List<CamisetaVenda> camisetas = venda.getCamisetas();
			    
	
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
			    	camisetaVendaEncontrada.setQuantidade(camiseta.getQuantidade());
			    	camisetaService.saveExistingCamiseta(camisetaBanco);
			    }
			    venda.setValor(calcularValorVenda(venda.getCamisetas()));
			    venda.setCamisetas(camisetas);
			    
			    return vendaRepository.save(venda);
			}
	     
	     @Transactional
	 	public Long inserirVendaManualmente(long id) {
	 	    Cliente clienteBanco = clienteService.findByIdOrThrowBadRequestException(id);
	 	    
	 	    Venda venda = Venda.builder()
	 		        .dia_venda(LocalDate.now())
	 		        .valor(0)
	 		        .cliente(clienteBanco)
	 		        .build();

	 	    return vendaRepository.save(venda).getId();
	     }

		 public static float calcularValorVenda(List<CamisetaVenda> camisetas){
			float valor = 0;
			for(CamisetaVenda camiseta : camisetas){
				valor += camiseta.getValor() * camiseta.getQuantidade();
			}
			return valor;
		 }
}