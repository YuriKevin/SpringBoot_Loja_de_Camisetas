package br.com.lojacamisetas.projetojavaloja.controller;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import br.com.lojacamisetas.projetojavaloja.classe.Camiseta;
import br.com.lojacamisetas.projetojavaloja.classe.CamisetaVenda;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.service.CamisetaService;
import br.com.lojacamisetas.projetojavaloja.service.CamisetaVendaService;
import br.com.lojacamisetas.projetojavaloja.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@Component
@RestController
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/camiseta_venda")
public class CamisetaVendaController {
	private final DateUtil dateUtil;
    private final CamisetaVendaService camisetaVendaService;
    
    @GetMapping
    public ResponseEntity<List<CamisetaVenda>> list(){
    	log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(camisetaVendaService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CamisetaVenda> findById(@PathVariable long id){
        return ResponseEntity.ok(camisetaVendaService.findByIdOrThrowBadRequestException(id));
    }
    
    
    @PostMapping
    public ResponseEntity<CamisetaVenda> save(@RequestBody @Valid CamisetaVendaPostRequestBody camisetaVendaPostRequestBody){
        return new ResponseEntity<>(camisetaVendaService.save(camisetaVendaPostRequestBody), HttpStatus.CREATED);
    }
    
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
    	camisetaVendaService.delete(id);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody CamisetaVendaPutRequestBody camisetaVendaPutRequestBody){
    	camisetaVendaService.replace(camisetaVendaPutRequestBody);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}




