# Decisões Arquiteturais

## 2026-07-19 — Camada de Service

**Decisão:** Introduzir camada de Service entre Controller e Repository
para todos os domínios (Usuario, HistoricoEmocional, Autenticacao).

**Motivo:** Controllers atualmente chamam repositories diretamente
(transaction script), o que mistura HTTP handling com lógica de negócio
e dificulta teste e reuso.

**Estrutura:** Controller → Service → Repository. Sem Use Case por
operação (Hexagonal descartado por excesso de complexidade para o
tamanho atual do domínio).

**Escopo:** aplica-se a todo código novo a partir de agora. Refactor do
código existente é incremental, não é um sprint isolado.

## Pendências identificadas

- UsuarioController acumula responsabilidade de Service: cria/salva Usuario 
  e Credenciais diretamente, sem camada Service. Identificado em 24/07/2026 
  durante estudo do curso Spring Boot 3 (Alura). 
  Candidato a PRD futuro, seguindo o mesmo padrão já decidido para a 
  arquitetura Controller → Service → Repository.
