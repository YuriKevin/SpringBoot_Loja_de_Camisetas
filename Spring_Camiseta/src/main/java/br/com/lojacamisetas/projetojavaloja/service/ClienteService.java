package br.com.lojacamisetas.projetojavaloja.service;

import org.springframework.web.server.ResponseStatusException;

import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import br.com.lojacamisetas.projetojavaloja.classe.Cliente;
import br.com.lojacamisetas.projetojavaloja.repository.CamisetaRepository;
import br.com.lojacamisetas.projetojavaloja.repository.ClienteRepository;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.ClientePostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.ClientePutRequestBody;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClienteService {
	private final ClienteRepository clienteRepository;

	public List<Cliente> listAll() {
    	return clienteRepository.findAll();
    }
	
	public Cliente findByIdOrThrowBadRequestException(long id) {
        return clienteRepository.findById(id)
        		.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente Não encontrado"));
 	    }
	
	public Cliente findByCpf(String cpf) {
    	return clienteRepository.findByCpf(cpf);
    }
    
	public Cliente save(ClientePostRequestBody clientePostRequestBody) {
		
		Cliente clienteExistente = clienteRepository.findByCpf(clientePostRequestBody.getCpf());
		if(clienteExistente != null) {
			new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado");
		}
    	return clienteRepository.save(Cliente.builder()
    			.nome(clientePostRequestBody.getNome())
    			.cpf(clientePostRequestBody.getCpf())
    			.build());
    }

	public void delete(long id) {
		clienteRepository.delete(findByIdOrThrowBadRequestException(id));
		
	}

	public void replace(ClientePutRequestBody clientePutRequestBody) {
        Cliente savedCliente = findByIdOrThrowBadRequestException(clientePutRequestBody.getId());
        Cliente clienteExistente = clienteRepository.findByCpf(clientePutRequestBody.getCpf());
		if(clienteExistente != null) {
			new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado");
		}else {
			
        Cliente cliente = Cliente.builder()
                .id(savedCliente.getId())
                .nome(clientePutRequestBody.getNome())
                .cpf(clientePutRequestBody.getCpf()) 
                .build();

        clienteRepository.save(cliente);
		}
    }
	     
	    
}
