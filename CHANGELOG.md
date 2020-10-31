# Changelog

### 3.1.9 (preview)

Issue #50: Usar github actions para publicar no Nexus
Melhorias de DevOps/Publicação de pacotes
 
Issue #46: Analisar alerta de segurança guava 
Remoção de dependência di Guava

Issue #47 :Revisar itens sonar
Melhorias de códigos apontados pelo sonar


### 3.1.8
- Corrigido bug na contagem de linhas do cabeçalho (headerRows) em arquivos com linhas em branco. 

A contagem de linhas do cabeçalho não estava conseguindo identificar que arquivos .xls/.xlsx possuiam linhas em branco no cabecalho, fazendo com que a identificação da primeira linha dos dados fosse incorreta.       

Ex: Como a contagem era realizada: 

Nesse exemplo do funcionamento antes da correção, seria setado pelo método setHeaderRos() o valor 4, informando que queremos ignorar as 4 primeiras linhas do arquivo (pois são apenas o cabeçalho e não possuem dados relevantes). A contagem correta deveria ignorar as 4 primeiras linhas e iniciar a leitura dos dados na linha 5.
Porém, como o método SpreadsheetParser#parseCurrentSheet() não conseguia identificar que existiam as duas linhas em branco, essas linhas eram ignoradas e a leitura dos dados iniciaria na linha 7 (pois como as linhas 2 e 3 eram ignoradas, as 4 do linhas do cabecalho eram consideradas as linhas 1, 4, 5 e 6).
Repare que nesse caso as linhas 5 e 6 eram consideradas como cabeçalho e não eram lidas como dados do arquivo. 
```
1|Coluna A   |coluna B   |...
2|           |           |    <- Linha em branco (ignorada)
3|           |           |    <- Linha em branco (ignorada)
4|Cabecalho A|Cabecalho B|...
5|Dados A    |Dados B    |...
6|Dados A    |Dados B    |...
7|Dados A    |Dados B    |...
```

Ex: Como a contagem é realizada agora:

Utilizando o mesmo exemplo anterior, agora se informarmos o valor 4 para o método setHeaderRos(), a contagem irá considerar as duas linhas em branco do cabeçalho (linhas 2 e 3) iniciando a leitura dos dados na linha 5. Ou seja, nenhum dado seria ignorado na leitura do arquivo.

```
1|Coluna A   |coluna B   |...
2|           |           |    <- Considerada uma linha do cabecalho
3|           |           |    <- Considerada uma linha do cabecalho
4|Cabecalho A|Cabecalho B|...
5|Dados A    |Dados B    |...
6|Dados A    |Dados B    |...
7|Dados A    |Dados B    |...
```

### 3.1.7