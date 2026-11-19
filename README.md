# Controle de Documentos de Frota (API REST)

API em Spring Boot para controlar a validade dos documentos de motoristas e caminhões de uma transportadora de produtos perigosos (CNH, curso MOPP, exame médico, CRLV, inspeções do veículo, tacógrafo).

A ideia veio do meu trabalho com controle de documentos: documento vencido nesse ramo pode deixar o caminhão parado ou o carregamento bloqueado. O objetivo da API é responder **"esse motorista pode sair com esse caminhão?"**.

**Em desenvolvimento.** Por enquanto tem as entidades (motorista, veículo, documento) e os repositórios com Spring Data JPA. Próximos passos: regras de vencimento, liberação de viagem e os endpoints REST.

## Tecnologias

- Java 21 e Spring Boot 3 (Web, Data JPA, Validation)
- H2
