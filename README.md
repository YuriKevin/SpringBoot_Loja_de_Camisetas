# Spring Boot Loja de Camisas de futebol

##
Back-end em Java com o framework Spring Boot de um chat em tempo real utilizando WebSocket. Esta aplicação é complementada por um front-end em Angular que está em outro repositório do perfil: [clique aqui!](https://github.com/YuriKevin/Angular_Loja_de_Camisetas)  <br>

## Funcionamento do sistema: 
[Assista ao vídeo](https://youtu.be/EZUzDujXRBU)


## O que o back-end traz?
- É uma API RESTful organizada em camadas: modelo, serviço e controlador;
- Se comunica e modela o banco de dados (MySQL) a partir das entidades da camada de modelo;
- Recebe requisições HTTP GET, POST PUT e DELETE através de endpoints nos controladores;
- Guarda as informações das camisetas, dos clientes e das vendas realizadas;
- Impede a criação de usuários duplicados com o mesmo CPF;
- Impede a compra de camisetas caso a quantidade desejada seja inferior a disponível no estoque;
- Atualiza o valor de uma venda automaticamente sempre que há alteração nas camisetas da venda.
- Salva imagens na base 64 string no atributo imagem de camiseta;



###
Caso deseje realizar um test com o back-end, não esqueça de inserir o ip e a porta para o banco de dados no arquivo application.yml
1. Acesse o arquivo: o caminho é o seguinte: Spring_Camiseta/src/main/resources/application.yml;
2. Em: "url: jdbc:mysql://000.000.0.000:0000/bd_camiseta?createDatabaseIfNotExist=true"
troque os zeros pelos números de ip : porta do seu banco de dados;
3. Adicione username e password do seu banco de dados se necessário;
4. Rode a aplicação na porta 8080 (não precisa especificar a porta, é a padrão do spring).
<br>
Confira também o front-end desenvolvido em Angular para este projeto no perfil e execute na porta 4200 para ter uma experiência completa com o sistema.
