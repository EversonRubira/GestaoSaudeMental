#Mental Health Management

Description
Mental Health Management is an app designed to help users improve their mental well-being. The system lets users record their emotional state daily and suggests actions based on these records. It also provides an emotional history so users can track their progress over time.

Current Features
User Registration: Allows new users to sign up with basic personal information.
Daily Emotional Recording: Users can log their current emotional state and activity.
Personal Emotional History: Shows the user’s emotional history for tracking trends over time.
Motivational Messages: Offers personalized suggestions based on the user’s emotional state and activity.
Logical User Deactivation: Users can be deactivated without losing their historical data.
API Endpoints
Users
POST /usuarios: Creates a new user.
PUT /usuarios: Updates a user’s personal information.
DELETE /usuarios/{id}: Deactivates a user logically.
Emotional History
POST /usuarios/{id}/historico: Records the user’s emotional state and activity.
GET /usuarios/{id}/historico_por_periodo: Shows the emotional history of a user within a specific time range.
GET /usuarios/{id}/historico-cronologico: Displays the user’s full emotional history in chronological order.
Technologies Used
Java 17
Spring Boot 3.3.2
JPA/Hibernate
MySQL (managed with Flyway)
Maven for dependencies
Planned Improvements
Emotional Trends: Add charts to show emotional trends over time (using Python/Matplotlib).
Reminders: Configurable reminders to log emotional states daily.
User Security: Add JWT authentication for endpoint protection.
Better Suggestions: Use external APIs for advanced motivational tips.
User Interface: Build an intuitive frontend.
Testing: Add unit and integration tests for reliability.
Deployment: Use Docker containers for easy deployment.
How to Contribute
Contributions are welcome! Here’s how you can help:

Fork this repository.
Create a branch for your feature or fix (git checkout -b my-feature).
Submit a pull request.


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
