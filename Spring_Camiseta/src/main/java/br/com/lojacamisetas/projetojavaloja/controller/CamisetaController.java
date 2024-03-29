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
import br.com.lojacamisetas.projetojavaloja.model.Camiseta;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPostRequestBody;
import br.com.lojacamisetas.projetojavaloja.requests.CamisetaPutRequestBody;
import br.com.lojacamisetas.projetojavaloja.service.CamisetaService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import javax.validation.Valid;

@Component
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/camisetas")
public class CamisetaController {
    private final CamisetaService camisetaService;
    
    @GetMapping
    public ResponseEntity<List<Camiseta>> list(){
        return ResponseEntity.ok(camisetaService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Camiseta> findById(@PathVariable long id){
        return ResponseEntity.ok(camisetaService.findByIdOrThrowBadRequestException(id));
    }
    
    @GetMapping(path = "/find")
    public ResponseEntity<List<Camiseta>> findByClube(@RequestParam(name="nome") String clube){
        return ResponseEntity.ok(camisetaService.findByClube(clube));
    }
    
    @GetMapping(path = "/find2")
    public ResponseEntity<List<Camiseta>> findByPaisContaining(@RequestParam(name="pais") String pais){
        return ResponseEntity.ok(camisetaService.findByPaisContaining(pais));
    }
    
    @PostMapping
    public ResponseEntity<Camiseta> save(@RequestBody @Valid CamisetaPostRequestBody camisetaPostRequestBody){
        return new ResponseEntity<>(camisetaService.save(camisetaPostRequestBody), HttpStatus.CREATED);
    }
    
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
    	camisetaService.delete(id);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody CamisetaPutRequestBody camisetaPutRequestBody){
    	camisetaService.replace(camisetaPutRequestBody);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}




