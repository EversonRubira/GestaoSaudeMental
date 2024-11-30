# GestaoSaudeMental

Descrição
O Gestão Saúde Mental é uma aplicação projetada para auxiliar os usuários a melhorar seu bem-estar mental. O sistema permite que os usuários registrem diariamente seu estado emocional e sugere ações baseadas nesses registros. Além disso, oferece um histórico emocional para que os usuários possam acompanhar sua evolução ao longo do tempo.

Funcionalidades Atuais
Cadastro de Usuários: Permite o registro de novos usuários com informações pessoais básicas.
Registro Diário de Estado Emocional: Os usuários podem registrar seu estado emocional atual e a atividade realizada.
Histórico Emocional Pessoal: Exibição do histórico emocional do usuário, permitindo a análise de tendências ao longo do tempo.
Sugestões Motivacionais Personalizadas: Com base no estado emocional e na atividade realizada, o sistema fornece mensagens motivacionais.
Exclusão Lógica de Usuários: Usuários podem ser desativados sem perder seus dados históricos.
Endpoints da API
Usuários
POST /usuarios: Cadastra um novo usuário.
PUT /usuarios: Atualiza as informações pessoais do usuário.
DELETE /usuarios/{id}: Realiza a exclusão lógica de um usuário.
Histórico Emocional
POST /usuarios/{id}/historico: Registra o estado emocional e a atividade realizada por um usuário.
GET /usuarios/{id}/historico_por_periodo: Exibe o histórico emocional de um usuário em um intervalo de tempo.
GET /usuarios/{id}/historico-cronologico: Lista o histórico emocional completo do usuário em ordem cronológica.
Tecnologias Utilizadas
Java 17
Spring Boot 3.3.2
JPA/Hibernate
MySQL (gerenciado com Flyway)
Maven para gerenciamento de dependências
Ajustes e Melhorias Planejados
Análise de Tendências Emocionais: Implementação de gráficos dinâmicos para visualização de tendências (usando Python/Matplotlib).
Notificações e Lembretes: Lembretes configuráveis para registro diário de estado emocional.
Autenticação e Autorização: Implementação de autenticação JWT para proteger os endpoints.
Sugestões Mais Avançadas: Integração com APIs externas para enriquecer as sugestões motivacionais.
Interface Gráfica: Desenvolvimento de um frontend intuitivo.
Testes Automatizados: Testes unitários e de integração para maior confiabilidade.
Deploy: Configuração de containers Docker para facilitar o deploy.
Como Contribuir
Contribuições são bem-vindas! Para colaborar:

Faça um fork deste repositório.
Crie uma branch para sua funcionalidade ou correção (git checkout -b minha-feature).
Envie suas alterações por meio de um pull request.
