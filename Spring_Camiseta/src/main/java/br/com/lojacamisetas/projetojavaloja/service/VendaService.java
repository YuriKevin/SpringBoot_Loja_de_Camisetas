package br.com.lojacamisetas.projetojavaloja.service;

import org.springframework.web.server.ResponseStatusException;
import br.com.lojacamisetas.projetojavaloja.classe.Venda;
import br.com.lojacamisetas.projetojavaloja.repository.CamisetaRepository;
import br.com.lojacamisetas.projetojavaloja.repository.ClienteRepository;
import br.com.lojacamisetas.projetojavaloja.repository.VendaRepository;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.VendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.VendaPutRequestBody;
import lombok.RequiredArgsConstructor;
import br.com.lojacamisetas.projetojavaloja.classe.Cliente;
import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;

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
    private final ClienteService clienteService;
	//private final CamisetaPutRequestBody camisetaPutRequestBody;
	   
	public List<Venda> listAll() {
    	return vendaRepository.findAll();
    }

	public Venda findByIdOrThrowBadRequestException(long id) {
        return vendaRepository.findById(id)
        		.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Venda Não encontrada"));
 	    }

	/*public List<Venda> findVendas(long clienteId) {
		Cliente cliente = clienteService.findByIdOrThrowBadRequestException(clienteId);
		List<Long> vendaIds = cliente.getVendaIds();
		List<Venda> arrayDeVendas = new ArrayList<>();
		for (Long vendaId : vendaIds) {
			Venda venda = this.findByIdOrThrowBadRequestException(vendaId);
			arrayDeVendas.add(venda);
		}
		return arrayDeVendas;
	}*/

	@Transactional
	public Venda save(VendaPostRequestBody vendaPostRequestBody) {
	    List<Camiseta> camisetas = vendaPostRequestBody.getCamisetas();
	    Cliente cliente = vendaPostRequestBody.getCliente();

	    Venda venda = Venda.builder()
	        .dia_venda(vendaPostRequestBody.getDia_venda())
	        .valor(vendaPostRequestBody.getValor())
	        .camisetas(camisetas)
	        .cliente(cliente)
	        .build();

	    Venda savedVenda = vendaRepository.save(venda);


	    for (Camiseta camiseta : camisetas) {
	   
    	        Camiseta camisetaBanco = camisetaRepository.findById(camiseta.getId())
    	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Camiseta não encontrada"));
    	        if (camisetaBanco.getQuantidade() >= camiseta.getQuantidade()) {
    	            camisetaBanco.setQuantidade(camisetaBanco.getQuantidade() - camiseta.getQuantidade());

    	            camisetaRepository.save(camisetaBanco);
    	        }
    	        else {
    	        	throw new RuntimeException("A camiseta "+camiseta.getClube()+" esgotou :(");
    	            }
    	        
    	       
    	    }
	    return savedVenda;
	}



	public void delete(long id) {
		vendaRepository.delete(findByIdOrThrowBadRequestException(id));
		
	}

	public void replace(VendaPutRequestBody vendaPutRequestBody) {
	    Venda savedVenda = findByIdOrThrowBadRequestException(vendaPutRequestBody.getId());

	    savedVenda.setDia_venda(vendaPutRequestBody.getDia_venda());
	    savedVenda.setValor(vendaPutRequestBody.getValor());
		savedVenda.setCliente(vendaPutRequestBody.getCliente());
	    savedVenda.setCamisetas(vendaPutRequestBody.getCamisetas());

	    vendaRepository.save(savedVenda);
	}

     
	public Venda addCamisetasVenda(VendaPutRequestBody vendaPutRequestBody) {
		Long vendaId = vendaPutRequestBody.getId();
	    Venda venda = findByIdOrThrowBadRequestException(vendaId);
	    List<Camiseta> camisetas = vendaPutRequestBody.getCamisetas();

	    venda.setCamisetas(camisetas);

	    return vendaRepository.save(venda);
	}

}