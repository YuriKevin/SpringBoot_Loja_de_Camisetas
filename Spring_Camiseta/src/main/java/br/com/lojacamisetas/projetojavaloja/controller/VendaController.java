package br.com.lojacamisetas.projetojavaloja.controller;


import java.util.List;
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
import br.com.lojacamisetas.projetojavaloja.model.Venda;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaVendaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.VendaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.VendaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.service.VendaService;
import lombok.RequiredArgsConstructor;

@Component
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/vendas") 
public class VendaController {
    private final VendaService vendaService;
    
    @GetMapping
    public ResponseEntity<List<Venda>> list(){
        return ResponseEntity.ok(vendaService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Venda> findById(@PathVariable long id){
        return ResponseEntity.ok(vendaService.findByIdOrThrowBadRequestException(id));
    }
    
    @GetMapping(path = "cliente/{id}")
    public ResponseEntity<List<Venda>> findVendasByClienteId(@PathVariable long id){
        return ResponseEntity.ok(vendaService.findVendasByClienteId(id));
    }
    
    @PostMapping
    public ResponseEntity<Venda> save(@RequestBody VendaPostRequestBody vendaPostRequestBody){
        return new ResponseEntity<>(vendaService.save(vendaPostRequestBody), HttpStatus.CREATED);
    }
    @PostMapping(path = "cliente/{id}")
    public ResponseEntity<Long> inserirVendaManualmente(@PathVariable long id){
        return new ResponseEntity<>(vendaService.inserirVendaManualmente(id), HttpStatus.CREATED);
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
    public ResponseEntity<Void> adicionarCamisetasParaUmaVenda(@RequestBody CamisetaVendaPostRequestBody camisetaVenda){
    	vendaService.adicionarCamisetasParaUmaVenda(camisetaVenda);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(path = "/atualiza_camiseta")
    public ResponseEntity<Void> atualizarCamisetasDeUmaVenda(@RequestBody CamisetaVendaPutRequestBody camisetaVenda){
    	vendaService.atualizarCamisetasDeUmaVenda(camisetaVenda);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}

