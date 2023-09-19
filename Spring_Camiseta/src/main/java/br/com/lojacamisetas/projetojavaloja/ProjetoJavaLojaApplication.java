package br.com.lojacamisetas.projetojavaloja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


//@EntityScan(basePackages = "package br.com.lojacamisetas.projetojavaloja.classe")
@SpringBootApplication
public class ProjetoJavaLojaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoJavaLojaApplication.class, args);
	}

}
