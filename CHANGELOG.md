# Changelog

### 3.1.8 (unreleased) - Kaizen
- Corrigido bug na contagem de linhas do cabeçalho quando existiam linhas em branco. 

A contagem de linhas do cabeçalho não estava sendo executada corretamente quando arquivos .xls/.xlsx eram utilizados com linhas em branco no cabecalho.
Essa contagem era realizada contando a quantidade de linhas lidas dos arquivos, ao invés de ser contadas pelo seu índice. Essas linhas em branco não eram 
recuperadas na leitura o que causava um erro na contagem da primeira linha dos dados que deveriam serem lidos.    

Ex: 
```
1|Coluna A   |coluna B   |...
2|                          <- Linha em branco (ignorada)
3|                          <- Linha em branco (ignorada)
1|Cabecalho A|Cabecalho B|...
```

### 3.1.7