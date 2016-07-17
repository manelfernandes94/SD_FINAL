# Projeto de Sistemas Distribuídos 2015-2016 #

SD Alameda



Manuel Fernandes 76002 majo_fernandes@hotmail.com




Repositório:
https://github.com/manelfernandes94/SD_FINAL

-------------------------------------------------------------------------------

## Instruções de instalação


### Ambiente

[0] Iniciar sistema operativo

Linux

[1] Iniciar servidores de apoio

[JUDDI](http://disciplinas.tecnico.ulisboa.pt/leic-sod/2015-2016/download/juddi-3.3.2_tomcat-7.0.64_9090.zip) (assumindo que existe o JUDDI na maquina):

```
cd $JUDDI_HOME/bin
startup.sh
```


[2] Criar pasta temporária

```
mkdir project
cd project
```


[3] Obter código fonte do projeto (versão entregue)

```
git clone https://github.com/manelfernandes94/SD_FINAL.git

cd project
```

[4] Instalar módulos de bibliotecas auxiliares

```
cd uddi-naming
mvn clean install
```

[5] Instalar serviço de handlers

```
cd ws-handlers
mvn clean install
```

-------------------------------------------------------------------------------

### Serviço Certificate Authority (CA)

[1] Construir e executar **servidor**

```
cd ca-ws
mvn package exec:java
```

[2] Construir **cliente** e executar testes

```
cd ca-ws-cli
mvn clean install
```

-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor**

```
cd transporter-ws
mvn clean install
mvn exec:java -Dws.i=1
mvn exec:java -Dws.i=2
```

[2] Construir **cliente** e executar testes

```
cd transporter-ws-cli
mvn clean install
```

...



-------------------------------------------------------------------------------
**FIM**
