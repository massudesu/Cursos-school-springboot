# Sistema de Gestão Escolar - Cursos Spring Boot

Aplicação web desenvolvida com Spring Boot para o gerenciamento completo de uma instituição de ensino, permitindo o controle de alunos, professores, cursos, turmas, salas, escolas e matrículas. O sistema conta com uma API REST no back-end e uma interface web integrada para interação com o usuário.

## Arquitetura e Tecnologias

O projeto segue o padrão de arquitetura em camadas (Controllers, Entities, Repositories) e utiliza as seguintes tecnologias:

* **Back-end:** Java, Spring Boot (Spring Web, Spring Data JPA)
* **Gerenciador de Dependências:** Apache Maven (com Maven Wrapper incluído)
* **Front-end:** HTML5, CSS3 e JavaScript puro (servidos estaticamente via `src/main/resources/static`)
* **Persistência:** Banco de dados relacional configurado via `application.properties`

## Estrutura do Projeto

```text
Cursos-school-springboot/
├── src/
│   ├── main/
│   │   ├── java/org/dt2/cursos/
│   │   │   ├── controllers/      # Controladores REST (Alunos, Cursos, Escolas, Matrículas, Professores, Salas, Turmas)
│   │   │   ├── entities/         # Entidades JPA representando o modelo de dados
│   │   │   ├── repositories/     # Interfaces de acesso ao banco de dados (Spring Data JPA)
│   │   │   └── CursosApplication.java
│   │   └── resources/
│   │       ├── static/           # Arquivos do front-end (index.html, css/style.css, js/app.js)
│   │       └── application.properties
│   └── test/                     # Testes automatizados da aplicação
├── mvnw / mvnw.cmd               # Executáveis do Maven Wrapper
└── pom.xml                       # Configurações e dependências do projeto
```

## Funcionalidades

O sistema oferece operações completas de CRUD (Criação, Leitura, Atualização e Exclusão) para os seguintes módulos:

1. **Escolas:** Cadastro e gerenciamento de unidades escolares.
2. **Cursos:** Controle dos cursos oferecidos pela instituição.
3. **Salas:** Gerenciamento de espaços físicos e salas de aula.
4. **Professores:** Cadastro e manutenção do corpo docente.
5. **Alunos:** Registro e acompanhamento dos estudantes.
6. **Turmas:** Organização de turmas vinculando cursos, professores e salas.
7. **Matrículas:** Gestão de vínculos entre alunos e turmas, incluindo tratamento de exclusão.

## Como Executar o Projeto

### Pré-requisitos

* Java Development Kit (JDK) 17 ou superior instalado.
* Banco de dados configurado conforme as propriedades definidas em `src/main/resources/application.properties`.

### Passo a Passo

1. Clone ou extraia o diretório do projeto em sua máquina local.
2. Abra o terminal na pasta raiz do projeto (`Cursos-school-springboot`).
3. Execute a aplicação utilizando o Maven Wrapper:

   * **No Linux/macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```
   * **No Windows:**
     ```cmd
     mvnw.cmd spring-boot:run
     ```

4. Após a inicialização do servidor, acesse a interface web no navegador através do endereço:
   ```text
   http://localhost:8080
   ```

## Demonstração de Funcionamento

Abaixo estão reservados os espaços para inserir as capturas de tela (prints) comprovando o funcionamento das principais telas e fluxos do sistema.

### 1. Tela Inicial e Navegação
<img width="1857" height="846" alt="image" src="https://github.com/user-attachments/assets/66d1892a-872d-4e12-b330-cdb149dd1efc" />


### 2. Cadastro e Listagem de Cadastros Base (Escolas, Cursos e Salas)
<img width="1745" height="803" alt="image" src="https://github.com/user-attachments/assets/9567c617-c5c6-4c22-89cf-d6cc02b6f04d" />
<img width="1846" height="858" alt="image" src="https://github.com/user-attachments/assets/84677aa3-3dfe-4d89-8e7a-b1dd01fc5509" />
<img width="1811" height="828" alt="image" src="https://github.com/user-attachments/assets/14f5a16b-7bc6-44ce-b047-bb7f016820c4" />


### 3. Gestão de Professores e Alunos
<img width="1775" height="805" alt="image" src="https://github.com/user-attachments/assets/1bad0823-78e1-4157-9f89-8797f595877f" />
<img width="1827" height="797" alt="image" src="https://github.com/user-attachments/assets/38913d41-5124-4e6b-8d51-4dd23ad1e95d" />


### 4. Criação de Turmas
<img width="1833" height="841" alt="image" src="https://github.com/user-attachments/assets/2374f43f-d6b8-40bb-ba36-2539d558a881" />
<img width="1774" height="733" alt="image" src="https://github.com/user-attachments/assets/50ddc759-1ba0-4644-999c-3d2f2fbd87df" />


### 5. Realização de Matrículas
<img width="1644" height="777" alt="image" src="https://github.com/user-attachments/assets/1facdc9c-2983-4546-b7c6-eafaf26cf965" />
<img width="1594" height="573" alt="image" src="https://github.com/user-attachments/assets/ccebfd3c-fad7-4853-ac35-22a02d26f4db" />
<img width="1518" height="577" alt="image" src="https://github.com/user-attachments/assets/5c1e4e8b-9328-41e1-a811-1a25c5f0ac5e" />


### 6. Regras de exclusao
#### 1. Caso o aluno esteja matriculado em alguma turma, não sera possivel excluir antes de excluir da matricula, assim funciona para outros dados que estejam relacionados
<img width="1166" height="234" alt="image" src="https://github.com/user-attachments/assets/e87850ad-1794-455b-af3e-0d175d05a344" />
<img width="1154" height="227" alt="image" src="https://github.com/user-attachments/assets/62c9cce4-0543-4f00-9c14-ad436f0eb86c" />

### Exlusão correta

<img width="1464" height="163" alt="image" src="https://github.com/user-attachments/assets/58f05da6-12c1-4b46-a9cc-b181c40b19c8" />
Sucesso na exclusão
<img width="1676" height="471" alt="image" src="https://github.com/user-attachments/assets/48e7d66b-1d72-4f36-bbfb-3c835a8da1e8" />

Exclusão de aluno após exclusão da matricula
<img width="1632" height="514" alt="image" src="https://github.com/user-attachments/assets/a9363615-6e4f-42ee-9ddf-6b23ad33dd15" />
<img width="1362" height="711" alt="image" src="https://github.com/user-attachments/assets/4f6bfd2f-733f-4669-894c-5386304b43f2" />


---

## Autores

* **Nicolas Massuda<@massudesu>**
