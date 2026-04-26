 Sistema de Ordem de Serviço (OS Fácil)

Sistema backend desenvolvido em Java com Spring Boot para gerenciamento de ordens de serviço, permitindo cadastro de clientes, abertura e controle de chamados técnicos.

 Funcionalidades
 Cadastro de clientes
 Listagem de clientes
 Exclusão de clientes
 Abertura de chamados (OS)
 Listagem de chamados
 Finalização de chamados
 Controle de status (ABERTO / FINALIZADO)
 Integração com banco de dados PostgreSQL

 Tecnologias utilizadas
Java 17+
Spring Boot
Spring Web
Spring Data JPA
PostgreSQL
Maven
Lombok
 Estrutura do projeto
src/main/java/com/sistema_os/OsFacil
 controller   # Endpoints da API
 service      # Regras de negócio
 repository   # Acesso ao banco (JPA)
 model        # Entidades

 Como rodar o projeto
1. Clonar repositório git clone https://github.com/tharles07/os-facil.git
2. Configurar banco PostgreSQL
Crie um banco, por exemplo: CREATE DATABASE osfacil;

3. Configurar application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/osfacil
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

4. Rodar aplicação

No IntelliJ ou terminal: mvn spring-boot:run

 Endpoints principais
Clientes
Criar cliente
POST /clientes
{
  "nome": "João",
  "telefone": "11999999999"
}

Listar clientes
GET /clientes
DELETE /clientes/{id}

Chamados (OS)
Criar chamado 
POST /chamados
{
  "descricao": "Instalação de câmera",
  "valor": 150.0,
  "pago": false,
  "cliente": {
    "id": 1
  }
}
Listar chamados
GET /chamados
PUT /chamados/{id}/finalizar

Regras de negócio
Um chamado precisa estar vinculado a um cliente existente
Status inicial do chamado: ABERTO
Ao finalizar: status muda para FINALIZADO
Não é permitido finalizar um chamado já finalizado

Próximas melhorias
 Autenticação com Spring Security
 Dashboard de chamados
 Controle de pagamento
 Deploy em nuvem (Render / Railway)
 Multiempresa (SaaS)
 
Autor
Tharles Santos
GitHub: https://github.com/tharles07

Observação
Este projeto foi desenvolvido como prática de backend com foco em criação de APIs REST e regras de negócio reais.

