package br.com.lojacamisetas.projetojavaloja.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import br.com.lojacamisetas.projetojavaloja.classe.CamisetaVenda;
import br.com.lojacamisetas.projetojavaloja.classe.Venda;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.VendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.VendaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.service.CamisetaService;
import br.com.lojacamisetas.projetojavaloja.service.VendaService;
import br.com.lojacamisetas.projetojavaloja.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RestController
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/vendas") //receberá as requisições feitas à URL ex :http://localhost:8080/camisas/requisicao
public class VendaController {
	private final DateUtil dateUtil;
    private final VendaService vendaService;
    private final CamisetaService camisetaService;
    
    @GetMapping
    public ResponseEntity<List<Venda>> list(){
    	log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(vendaService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Venda> findById(@PathVariable long id){
        return ResponseEntity.ok(vendaService.findByIdOrThrowBadRequestException(id));
    }
    
    @GetMapping(path = "cliente/{id}")
    public ResponseEntity<List<Venda>> findVendas(@PathVariable long id){
        return ResponseEntity.ok(vendaService.findVendas(id));
    }
    
    @PostMapping
    public ResponseEntity<Venda> save(@RequestBody VendaPostRequestBody vendaPostRequestBody){
        return new ResponseEntity<>(vendaService.save(vendaPostRequestBody), HttpStatus.CREATED);
    }
    @PostMapping(path = "cliente/{id}")
    public ResponseEntity<Long> saveManagement(@PathVariable long id){
        return new ResponseEntity<>(vendaService.saveManagement(id), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
    	vendaService.delete(id);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody VendaPutRequestBody vendaPutRequestBody){
    	vendaService.replace(vendaPutRequestBody);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping(path = "/add_camiseta")
    public ResponseEntity<Void> addCamisetasVenda(@RequestBody CamisetaVendaPostRequestBody camisetaVenda){
    	vendaService.addCamisetasVenda(camisetaVenda);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(path = "/atualiza_camiseta")
    public ResponseEntity<Void> AtualizaCamisetasVenda(@RequestBody CamisetaVendaPutRequestBody camisetaVenda){
    	vendaService.atualizaCamisetasVenda(camisetaVenda);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}

