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
import org.springframework.web.bind.annotation.RestController;
import br.com.lojacamisetas.projetojavaloja.model.CamisetaVenda;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.service.CamisetaVendaService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import javax.validation.Valid;

@Component
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/camiseta_venda")
public class CamisetaVendaController {
    private final CamisetaVendaService camisetaVendaService;
    
    @GetMapping
    public ResponseEntity<List<CamisetaVenda>> list(){
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




