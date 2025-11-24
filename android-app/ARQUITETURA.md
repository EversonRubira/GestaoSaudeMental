# 🏗️ Arquitetura do Sistema - Gestão Saúde Mental

## 📋 Índice
1. [Visão Geral do Sistema](#visão-geral-do-sistema)
2. [Arquitetura Backend](#arquitetura-backend)
3. [Arquitetura Mobile (Android)](#arquitetura-mobile-android)
4. [Padrões de Projeto](#padrões-de-projeto)
5. [Fluxo de Dados](#fluxo-de-dados)
6. [Decisões Arquiteturais](#decisões-arquiteturais)
7. [Escalabilidade e Manutenibilidade](#escalabilidade-e-manutenibilidade)

---

## 1. Visão Geral do Sistema

### 1.1 Arquitetura de Alto Nível

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENTE (Android)                     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │   UI     │  │ViewModel │  │Repository│              │
│  └──────────┘  └──────────┘  └──────────┘              │
└───────────────────────┬─────────────────────────────────┘
                        │
                   HTTP/HTTPS (REST API)
                   JSON (Request/Response)
                   JWT (Autenticação)
                        │
┌───────────────────────▼─────────────────────────────────┐
│                 BACKEND (Spring Boot)                    │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │Controller│  │ Service  │  │Repository│              │
│  └──────────┘  └──────────┘  └──────────┘              │
│                       │                                  │
│                       ▼                                  │
│                  ┌─────────┐                            │
│                  │  MySQL  │                            │
│                  └─────────┘                            │
└─────────────────────────────────────────────────────────┘
```

### 1.2 Tipo de Arquitetura

**Arquitetura Cliente-Servidor em Camadas (Layered Client-Server Architecture)**

**Por que essa escolha?**
- ✅ **Separação de responsabilidades**: UI separada da lógica de negócio
- ✅ **Reutilização**: Mesma API pode servir web, iOS, Android
- ✅ **Segurança**: Lógica sensível protegida no servidor
- ✅ **Escalabilidade**: Backend e cliente escalam independentemente
- ✅ **Manutenibilidade**: Mudanças no backend não afetam o app (desde que a API não mude)

---

## 2. Arquitetura Backend

### 2.1 Padrão MVC (Model-View-Controller)

```
┌──────────────────────────────────────────────┐
│              BACKEND LAYERS                   │
├──────────────────────────────────────────────┤
│  Controller Layer (Presentation)             │
│  - UsuarioController                         │
│  - AutenticacaoController                    │
│  • Recebe requisições HTTP                   │
│  • Valida entrada                            │
│  • Chama Services                            │
│  • Retorna DTOs                              │
├──────────────────────────────────────────────┤
│  Service Layer (Business Logic)              │
│  - AutenticacaoService                       │
│  • Regras de negócio                         │
│  • Validações complexas                      │
│  • Orquestração de operações                 │
├──────────────────────────────────────────────┤
│  Repository Layer (Data Access)              │
│  - UsuarioRepository                         │
│  - CredenciaisRepository                     │
│  - HistoricoEmocionalRepository              │
│  • Acesso ao banco de dados                  │
│  • Queries JPA/JPQL                          │
├──────────────────────────────────────────────┤
│  Domain Layer (Entities)                     │
│  - Usuario                                   │
│  - Credenciais                               │
│  - HistoricoEmocional                        │
│  • Modelos de dados                          │
│  • Lógica de domínio                         │
└──────────────────────────────────────────────┘
```

### 2.2 Spring Security + JWT

**Fluxo de Autenticação:**

```
1. POST /login (email + senha)
         ↓
2. Spring Security valida credenciais
         ↓
3. Se válido → Gera token JWT
         ↓
4. Retorna token para o cliente
         ↓
5. Cliente guarda token
         ↓
6. Próximas requisições: Header "Authorization: Bearer {token}"
         ↓
7. Backend valida token automaticamente
```

**Vantagens:**
- ✅ **Stateless**: Servidor não guarda sessão
- ✅ **Escalável**: Cada requisição é independente
- ✅ **Seguro**: Token expira automaticamente
- ✅ **Padrão da indústria**: OAuth2, JWT são padrões

---

## 3. Arquitetura Mobile (Android)

### 3.1 Clean Architecture + MVVM

**Princípio: Separação de Camadas com Dependências Unidirecionais**

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                    │
│  ┌────────────────┐          ┌────────────────┐         │
│  │  UI (Compose)  │ ◄───────│   ViewModel    │         │
│  │  - Screens     │          │  - UIState     │         │
│  │  - Components  │          │  - Events      │         │
│  └────────────────┘          └────────────────┘         │
│                                      │                   │
│                                      │ Observa           │
│                                      ▼                   │
├─────────────────────────────────────────────────────────┤
│                     DOMAIN LAYER                         │
│  ┌────────────────┐          ┌────────────────┐         │
│  │  Use Cases     │          │   Models       │         │
│  │  (opcional)    │          │  - Usuario     │         │
│  │                │          │  - Emocao      │         │
│  └────────────────┘          └────────────────┘         │
│                                      │                   │
│                                      │ Usa               │
│                                      ▼                   │
├─────────────────────────────────────────────────────────┤
│                      DATA LAYER                          │
│  ┌────────────────┐          ┌────────────────┐         │
│  │  Repository    │ ◄───────│  Data Sources  │         │
│  │  - Interface   │          │  - Remote (API)│         │
│  │  - Impl        │          │  - Local (Cache)│        │
│  └────────────────┘          └────────────────┘         │
│         │                            │                   │
│         │                            │                   │
│         ▼                            ▼                   │
│  ┌────────────────┐          ┌────────────────┐         │
│  │  DTOs          │          │   Retrofit     │         │
│  │  (API Models)  │          │   (Network)    │         │
│  └────────────────┘          └────────────────┘         │
└─────────────────────────────────────────────────────────┘
```

### 3.2 Responsabilidades de Cada Camada

#### **Presentation Layer (UI + ViewModel)**

**UI (Jetpack Compose):**
```kotlin
// RESPONSABILIDADES:
• Renderizar interface
• Capturar eventos do usuário (cliques, inputs)
• Observar estados do ViewModel
• NÃO contém lógica de negócio
```

**ViewModel:**
```kotlin
// RESPONSABILIDADES:
• Gerenciar estado da UI (UIState)
• Processar eventos da UI
• Chamar Repository para dados
• Expor LiveData/StateFlow para UI observar
• Sobrevive a rotações de tela
• NÃO conhece detalhes da UI (Views)
```

**Exemplo:**
```kotlin
class LoginViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    // Estado da UI
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    // Evento: Usuário clicou em "Entrar"
    fun onLoginClick(email: String, senha: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            val result = repository.login(email, senha)

            _uiState.value = when (result) {
                is Result.Success -> LoginUiState(isSuccess = true)
                is Result.Error -> LoginUiState(error = result.message)
            }
        }
    }
}

// Estado da UI
data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
```

#### **Domain Layer (Modelos de Domínio)**

```kotlin
// RESPONSABILIDADES:
• Definir modelos de negócio
• Regras de validação de domínio
• Independente de frameworks
• NÃO conhece detalhes de implementação

// Exemplo:
data class Usuario(
    val id: Long,
    val nome: String,
    val email: String
) {
    init {
        require(email.contains("@")) { "Email inválido" }
    }
}
```

#### **Data Layer (Repository + API)**

**Repository (Interface):**
```kotlin
interface UsuarioRepository {
    suspend fun login(email: String, senha: String): Result<String>
    suspend fun cadastrar(usuario: CadastroDto): Result<Usuario>
    suspend fun registrarEmocao(emocao: EmocaoDto): Result<Unit>
}
```

**Repository (Implementação):**
```kotlin
// RESPONSABILIDADES:
• Abstrair origem dos dados (API, cache, DB local)
• Decidir quando usar cache vs API
• Converter DTOs em modelos de domínio
• Tratar erros de rede

class UsuarioRepositoryImpl(
    private val api: ApiService,
    private val tokenStorage: TokenStorage
) : UsuarioRepository {

    override suspend fun login(email: String, senha: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(email, senha))
            if (response.isSuccessful && response.body() != null) {
                val token = response.headers()["Authorization"]
                tokenStorage.saveToken(token)
                Result.Success(token)
            } else {
                Result.Error("Credenciais inválidas")
            }
        } catch (e: Exception) {
            Result.Error("Erro de conexão: ${e.message}")
        }
    }
}
```

**API Service (Retrofit):**
```kotlin
// RESPONSABILIDADES:
• Definir endpoints
• Serialização/Deserialização JSON
• Configurar interceptors (logging, auth)

interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<Void>

    @POST("/usuarios")
    suspend fun cadastrar(@Body usuario: CadastroDto): Response<UsuarioDto>

    @POST("/usuarios/{id}/historico")
    suspend fun registrarEmocao(
        @Path("id") id: Long,
        @Body emocao: EmocaoDto
    ): Response<Void>
}
```

---

## 4. Padrões de Projeto

### 4.1 Repository Pattern

**Problema:** Como abstrair a origem dos dados?

**Solução:**
```
┌──────────────┐
│  ViewModel   │
└──────┬───────┘
       │ chama
       ▼
┌──────────────────┐
│   Repository     │ ◄── Interface (contrato)
│   (Interface)    │
└──────────────────┘
       △
       │ implementa
       │
┌──────────────────┐
│RepositoryImpl    │
│ - API            │
│ - Cache local    │
│ - Banco local    │
└──────────────────┘
```

**Vantagens:**
- ✅ **Testabilidade**: Fácil mockar o Repository
- ✅ **Flexibilidade**: Trocar fonte de dados sem afetar ViewModel
- ✅ **Single Source of Truth**: Repositório decide de onde vem o dado

### 4.2 MVVM (Model-View-ViewModel)

**Problema:** Como separar UI da lógica de negócio?

**Solução:**
```
View (UI)  ────observa────►  ViewModel  ────usa────►  Model (Repository)
   │                             │
   │                             │
   └────────notifica──────────────┘
         (StateFlow/LiveData)
```

**Vantagens:**
- ✅ **Separação clara**: UI não conhece lógica de negócio
- ✅ **Testável**: ViewModel pode ser testado sem UI
- ✅ **Reativo**: UI atualiza automaticamente quando estado muda

### 4.3 Dependency Injection (DI)

**Problema:** Como criar e fornecer dependências?

**Solução Manual (para este projeto):**
```kotlin
// Container de dependências
object DependencyContainer {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://localhost:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepositoryImpl(apiService, tokenStorage)
    }
}

// Usage no ViewModel
class LoginViewModel(
    private val repository: UsuarioRepository = DependencyContainer.usuarioRepository
) : ViewModel()
```

**Alternativa (Hilt/Koin):** Frameworks automatizam isso, mas adicionam complexidade.

### 4.4 Observer Pattern (StateFlow)

**Problema:** Como notificar UI quando dados mudam?

**Solução:**
```kotlin
// ViewModel expõe StateFlow
class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state  // Read-only

    fun updateData() {
        _state.value = UiState(data = "novo valor")
    }
}

// UI observa
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.state.collectAsState()

    Text(text = state.data)  // Atualiza automaticamente
}
```

---

## 5. Fluxo de Dados

### 5.1 Fluxo Completo: Login

```
┌─────────────────────────────────────────────────────────┐
│ 1. USER ACTION                                          │
│    Usuário clica em "Entrar"                            │
└────────────────────┬────────────────────────────────────┘
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 2. UI LAYER                                             │
│    LoginScreen captura email/senha                      │
│    Chama: viewModel.onLoginClick(email, senha)          │
└────────────────────┬────────────────────────────────────┘
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 3. PRESENTATION LAYER (ViewModel)                       │
│    • Atualiza UIState: isLoading = true                 │
│    • Chama: repository.login(email, senha)              │
└────────────────────┬────────────────────────────────────┘
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 4. DATA LAYER (Repository)                              │
│    • Chama: api.login(LoginRequest(email, senha))       │
└────────────────────┬────────────────────────────────────┘
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 5. NETWORK LAYER (Retrofit)                             │
│    • Envia HTTP POST /login                             │
│    • Serializa JSON                                     │
└────────────────────┬────────────────────────────────────┘
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 6. BACKEND (Spring Boot)                                │
│    • Controller recebe requisição                       │
│    • Spring Security valida credenciais                 │
│    • Gera JWT token                                     │
│    • Retorna token no header                            │
└────────────────────┬────────────────────────────────────┘
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 7. NETWORK RESPONSE                                     │
│    • Retrofit recebe resposta                           │
│    • Extrai token do header                             │
└────────────────────┬────────────────────────────────────┘
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 8. DATA LAYER (Repository)                              │
│    • Salva token: tokenStorage.save(token)              │
│    • Retorna: Result.Success(token)                     │
└────────────────────┬────────────────────────────────────┘
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 9. PRESENTATION LAYER (ViewModel)                       │
│    • Atualiza UIState: isSuccess = true                 │
└────────────────────┬────────────────────────────────────┘
                     ▼
┌─────────────────────────────────────────────────────────┐
│ 10. UI LAYER                                            │
│    • Observa mudança em UIState                         │
│    • Recompõe tela → Navega para HomeScreen             │
└─────────────────────────────────────────────────────────┘
```

### 5.2 Fluxo: Registrar Emoção

```
User → UI → ViewModel → Repository → API → Backend → DB
                                      ↓
                              Retorna sucesso
                                      ↓
       UI ← ViewModel ← Repository ← API
```

---

## 6. Decisões Arquiteturais

### 6.1 Por que Clean Architecture?

| Decisão | Alternativa | Escolha | Motivo |
|---------|-------------|---------|--------|
| Clean Architecture | Tudo no Activity | Clean Architecture | Testabilidade, manutenibilidade |
| MVVM | MVP, MVC | MVVM | Padrão oficial Android, reatividade |
| Jetpack Compose | XML Views | Compose | Moderno, menos código, declarativo |
| Retrofit | Volley, OkHttp puro | Retrofit | Simples, type-safe, coroutines |
| StateFlow | LiveData | StateFlow | Kotlin-first, mais funcionalidades |
| Manual DI | Hilt/Koin | Manual | Menos complexidade para app pequeno |

### 6.2 Trade-offs

#### **Clean Architecture**

**Prós:**
- ✅ Testável (cada camada independente)
- ✅ Escalável (adicionar features é fácil)
- ✅ Manutenível (mudanças isoladas)

**Contras:**
- ❌ Mais código inicial (boilerplate)
- ❌ Curva de aprendizado

**Quando usar:**
- ✅ Apps médios a grandes
- ✅ Equipes
- ✅ Longo prazo

#### **Arquitetura Simples (tudo em Activities)**

**Prós:**
- ✅ Rápido para prototipar
- ✅ Menos código

**Contras:**
- ❌ Difícil de testar
- ❌ Difícil de manter
- ❌ Código acoplado

**Quando usar:**
- ✅ MVPs rápidos
- ✅ Apps muito simples

---

## 7. Escalabilidade e Manutenibilidade

### 7.1 Como adicionar nova feature?

**Exemplo: Adicionar "Gráfico de Emoções"**

```
1. DATA LAYER
   • Adicionar endpoint na ApiService
   • Criar DTO (GraficoDto)
   • Adicionar método no Repository

2. DOMAIN LAYER
   • Criar modelo de domínio (GraficoEmocao)

3. PRESENTATION LAYER
   • Criar ViewModel (GraficoViewModel)
   • Criar UiState (GraficoUiState)

4. UI LAYER
   • Criar tela Compose (GraficoScreen)
   • Adicionar navegação
```

**Impacto:** ZERO nas outras features! 🎉

### 7.2 Como testar?

```kotlin
// Testar ViewModel SEM precisar de API real
class LoginViewModelTest {
    @Test
    fun `quando login for sucesso, deve atualizar uiState`() = runTest {
        // Arrange
        val fakeRepository = FakeUsuarioRepository()
        val viewModel = LoginViewModel(fakeRepository)

        // Act
        viewModel.onLoginClick("email@test.com", "senha123")

        // Assert
        assertEquals(true, viewModel.uiState.value.isSuccess)
    }
}

// Mock do Repository
class FakeUsuarioRepository : UsuarioRepository {
    override suspend fun login(email: String, senha: String): Result<String> {
        return Result.Success("fake-token")
    }
}
```

### 7.3 Como migrar para multi-plataforma (iOS)?

```
Com essa arquitetura:

1. Backend → JÁ serve iOS (API REST)
2. Data Layer → Reescrever em Swift/Kotlin Multiplatform
3. Domain Layer → Reescrever (ou KMP)
4. Presentation → Reescrever (SwiftUI)
5. UI → Reescrever (SwiftUI)

Lógica de negócio no BACKEND não muda! ✅
```

---

## 8. Conceitos Importantes

### 8.1 Inversão de Dependência (SOLID)

**Ruim:**
```kotlin
class LoginViewModel {
    private val api = Retrofit.Builder().build().create(ApiService::class.java)
    // ViewModel depende de implementação concreta!
}
```

**Bom:**
```kotlin
class LoginViewModel(
    private val repository: UsuarioRepository  // Interface!
) {
    // ViewModel depende de abstração, não implementação
}
```

### 8.2 Separation of Concerns

Cada classe tem UMA responsabilidade:

- **UI**: Renderizar
- **ViewModel**: Gerenciar estado
- **Repository**: Buscar dados
- **API**: Comunicação HTTP

### 8.3 Single Source of Truth

```
Repository decide:
• Mostrar cache ou buscar da API?
• Atualizar cache após API?
• Invalidar cache?

ViewModel SÓ pede dados ao Repository.
UI SÓ observa ViewModel.
```

---

## 9. Recursos para Estudar Mais

### Artigos/Guias Oficiais
- [Android Architecture Guide](https://developer.android.com/topic/architecture)
- [Guide to app architecture](https://developer.android.com/jetpack/guide)
- [Clean Architecture (Uncle Bob)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

### Conceitos
- SOLID Principles
- Design Patterns (Gang of Four)
- Reactive Programming
- Dependency Injection
- REST API Design

---

## 10. Resumo

### Arquitetura Escolhida
**Client-Server + Clean Architecture + MVVM**

### Vantagens
✅ Separação de responsabilidades
✅ Testável
✅ Escalável
✅ Manutenível
✅ Padrão da indústria
✅ Ótimo para aprendizado

### Quando NÃO usar
- App de uma tela só
- Protótipo descartável
- Deadline de 2 dias

### Perguntas?
Sinta-se à vontade para perguntar sobre qualquer parte da arquitetura!
