# Sistema de Gestão de Estacionamento

## Descrição
Este projeto é um sistema de gestão de estacionamento para um shopping, desenvolvido em Java e utilizando MySQL como banco de dados. O sistema tem como objetivo gerenciar o número de vagas disponíveis, registrar a entrada e saída de veículos, calcular o valor a ser pago pelo tempo de permanência e garantir que as regras específicas de entrada e saída sejam respeitadas.


### Cadastro de Veículos e Categorias
O sistema suporta o cadastro de veículos nas seguintes categorias:
- **Mensalista:** Veículos com um plano de estacionamento recorrente. As placas dos veículos mensalistas são registradas previamente para abrir automaticamente as cancelas.
- **Caminhões de Entrega:** Veículos de carga, como caminhões de entrega e vans, que também precisam ser registrados previamente.
- **Serviço Público:** Veículos de serviço público, como ambulâncias e viaturas, que têm acesso livre e são isentos de cobrança.
- **Avulso:** Veículos que não precisam de cadastro prévio, recebendo um ticket na entrada.
  
### Gestão das Cancelas
- **Cancelas de Entrada:** Numeradas de 1 a 5.
- **Cancelas de Saída:** Numeradas de 6 a 10.
- **Regras de Entrada e Saída:**
  - Mensalistas e Avulsos podem entrar por qualquer cancela de entrada e sair por qualquer cancela de saída.
  - Caminhões de Entrega só podem entrar pela cancela 1.
  - Motos só podem entrar pela cancela 5 e sair pela cancela 10.
  - Veículos de Serviço Público têm acesso livre em todas as cancelas.

### Gestão de Vagas
- O sistema controla o número total de vagas disponíveis, com um limite máximo de 500 vagas.
- 200 vagas são reservadas para mensalistas.
- **Vagas Ocupadas:**
  - Motos: 1 vaga
  - Carros: 2 vagas
  - Caminhões de Entrega: 4 vagas

### Registro de Entrada e Saída através de Tickets
- **Mensalistas e Caminhões de Entrega:** A entrada e saída são registradas automaticamente.
- **Avulsos:** Recebem um ticket que registra:
  - Hora de entrada
  - Hora de saída
  - Cancela de entrada e saída
  - Vaga ocupada
  - Valor a ser pago
- Os dados do ticket são persistidos no banco de dados MySQL.
- Veículos de Serviço Público não recebem ticket.

### Cobrança
- O sistema calcula o valor baseado no tempo que o veículo permaneceu no estacionamento:
  - Valor por minuto: R$ 0,10
  - Cobrança mínima para veículos avulsos: R$ 5,00
  - Mensalistas pagam uma taxa fixa mensal de R$ 250,00.
  - Veículos de Serviço Público são isentos de cobrança.
 

## Requisitos do Sistema

Certifique-se de ter as seguintes ferramentas instaladas:

- Java Development Kit (JDK) 21
- MySQL versão 8.0.39


## Instalação
Siga os passos abaixo para configurar o projeto no seu ambiente:
1. **Clone o repositório**
   ```bash
   git clone https://github.com/GuilhermeFrnc/Sistema-de-Gestao-de-Estacionamento.git
   ```

2. **Compile o projeto**
   ```bash
   mvn clean install
   ```
   Isso irá compilar o projeto, baixar as dependências e criar o pacote final.


3. **Crie o banco de dados**

  Abra o terminal do MySQL e execute os seguintes comandos:

  ```sql
  CREATE DATABASE gestao_estacionamento;
  
  CREATE TABLE Vaga (
      id_vaga INT PRIMARY KEY,
      categoria VARCHAR(50),
      ocupada BOOLEAN
  );
  
  DELIMITER //
  
  CREATE PROCEDURE PopulaVagas()
  BEGIN
      DECLARE i INT DEFAULT 1;
  
      WHILE i <= 200 DO
          INSERT INTO Vaga (id_vaga, categoria, ocupada) 
          VALUES (i, 'Mensalista', FALSE);
          SET i = i + 1;
      END WHILE;
  
      WHILE i <= 500 DO
          INSERT INTO Vaga (id_vaga, categoria, ocupada) 
          VALUES (i, 'Avulso', FALSE);
          SET i = i + 1;
      END WHILE;
  END //
  
  DELIMITER ;
  
  CALL PopulaVagas(); 
  
  CREATE TABLE Veiculo (
      id_veiculo INT PRIMARY KEY AUTO_INCREMENT, 
      placa VARCHAR(10),
      tipo ENUM('MOTO', 'CARRO', 'CAMINHAO', 'VAN', 'VEICULO_PUBLICO'),
      categoria ENUM('MENSALISTA', 'AVULSO', 'CAMINHAO_ENTREGA', 'PUBLICO')
  );
  
  CREATE TABLE Estacionamento (
      id_estacionamento INT PRIMARY KEY AUTO_INCREMENT,
      id_veiculo INT,
      data_entrada DATETIME,
      data_saida DATETIME,
      valor_pago DECIMAL(10, 2),
      id_cancela_entrada VARCHAR(5),
      id_cancela_saida VARCHAR(5),
      FOREIGN KEY (id_veiculo) REFERENCES Veiculo(id_veiculo)
  );
  
  CREATE TABLE Vaga_Ocupacao (
      id_vaga INT,
      id_estacionamento INT,
      PRIMARY KEY (id_vaga, id_estacionamento),
      FOREIGN KEY (id_vaga) REFERENCES Vaga(id_vaga),
      FOREIGN KEY (id_estacionamento) REFERENCES Estacionamento(id_estacionamento)
  );
  
  CREATE TABLE Mensalidade (
      id_mensalidade INT PRIMARY KEY AUTO_INCREMENT,
      id_veiculo INT,
      valor DECIMAL(10, 2),
      data_pagamento DATETIME,
      FOREIGN KEY (id_veiculo) REFERENCES Veiculo(id_veiculo)
  );
  
  CREATE TABLE Caminhao_Entrega (
      id_caminhao INT PRIMARY KEY AUTO_INCREMENT,
      id_veiculo INT,
      valor DECIMAL(10, 2),
      FOREIGN KEY (id_veiculo) REFERENCES Veiculo(id_veiculo)
  );
  ```

4. **Modifique o arquivo db.properties**

  para fazer a conexao com o banco de dados insira as credenciais do seu banco
  
  ```properties
  user=root
  password=1234567
  dburl=jdbc:mysql://localhost:3306/gestao_estacionamento
  useSSl=false
  ```
5. **Execute o projeto**
  ```bash
     mvn exec:java -Dexec.mainClass="src/main/java/application/Program.java"
  ```
