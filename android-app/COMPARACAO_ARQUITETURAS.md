# 🔍 Comparação de Arquiteturas - Caso Prático

## Cenário: Implementar tela de Login

Vamos ver como seria implementar a MESMA funcionalidade em diferentes arquiteturas.

---

## 1. Arquitetura "Tudo no Activity" (❌ Anti-padrão)

### Estrutura
```
MainActivity.kt (500 linhas)
- UI (XML parsing)
- Validação de input
- Chamada HTTP (AsyncTask/Thread)
- Parsing JSON manual
- Salvar token (SharedPreferences)
- Navegação
```

### Código
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var senhaEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        senhaEditText = findViewById(R.id.senhaEditText)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()

            // Validação na Activity 😱
            if (email.isEmpty() || !email.contains("@")) {
                Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Chamada HTTP na Activity 😱😱
            Thread {
                try {
                    val url = URL("http://localhost:8080/login")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doOutput = true

                    // JSON manual 😱😱😱
                    val json = "{\"login\":\"$email\",\"senha\":\"$senha\"}"
                    connection.outputStream.write(json.toByteArray())

                    val responseCode = connection.responseCode
                    if (responseCode == 200) {
                        val token = connection.getHeaderField("Authorization")

                        // Salvar token na Activity 😱😱😱😱
                        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        prefs.edit().putString("token", token).apply()

                        // Navegação na Thread de rede 😱😱😱😱😱
                        runOnUiThread {
                            Toast.makeText(this, "Login sucesso!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "Erro ao fazer login", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }
}
```

### ❌ Problemas

1. **Impossível testar** - Como testar sem rodar o app inteiro?
2. **Código acoplado** - UI misturada com lógica de rede
3. **Memory leaks** - Thread pode continuar após Activity ser destruída
4. **Rotação de tela** - Perde estado, requisição duplicada
5. **Código não reutilizável** - Cadastro teria que copiar tudo
6. **Difícil de manter** - Mudança em uma parte afeta tudo
7. **Sem tratamento de erros** - Catch genérico
8. **Performance** - Cria nova thread a cada clique

### Tamanho: **~150 linhas POR tela**

---

## 2. Arquitetura MVC Simples (⚠️ Melhor, mas não ideal)

### Estrutura
```
LoginActivity.kt (50 linhas)
LoginController.kt (80 linhas)
Usuario.kt (20 linhas)
ApiHelper.kt (100 linhas)
```

### Código
```kotlin
// Model
data class Usuario(val id: Long, val nome: String, val email: String)

// Controller
class LoginController {
    private val apiHelper = ApiHelper()

    fun login(email: String, senha: String, callback: (Result<String>) -> Unit) {
        Thread {
            val result = apiHelper.login(email, senha)
            callback(result)
        }.start()
    }
}

// View (Activity)
class LoginActivity : AppCompatActivity() {
    private val controller = LoginController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()

            progressBar.visibility = View.VISIBLE

            controller.login(email, senha) { result ->
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    when (result) {
                        is Result.Success -> navigateToHome()
                        is Result.Error -> showError(result.message)
                    }
                }
            }
        }
    }
}
```

### ✅ Melhorias
- Separou lógica de negócio (Controller)
- Model definido

### ❌ Ainda tem problemas
- Activity ainda precisa chamar `runOnUiThread`
- Sem gerenciamento de estado
- Memory leaks possíveis
- Difícil de testar Controller (depende de rede)

### Tamanho: **~250 linhas total**

---

## 3. Arquitetura MVVM + Repository (✅ Recomendado)

### Estrutura
```
LoginScreen.kt (30 linhas) - UI Compose
LoginViewModel.kt (40 linhas)
UsuarioRepository.kt (20 linhas) - Interface
UsuarioRepositoryImpl.kt (50 linhas)
ApiService.kt (30 linhas) - Retrofit
Models.kt (30 linhas)
```

### Código

#### **UI (Compose)**
```kotlin
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.senha,
            onValueChange = { viewModel.onSenhaChange(it) },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.onLoginClick() },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text("Entrar")
            }
        }

        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    // Observa sucesso para navegar
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToHome()
        }
    }
}
```

#### **ViewModel**
```kotlin
class LoginViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, error = null)
    }

    fun onSenhaChange(senha: String) {
        _uiState.value = _uiState.value.copy(senha = senha, error = null)
    }

    fun onLoginClick() {
        val currentState = _uiState.value

        // Validação
        if (currentState.email.isEmpty() || !currentState.email.contains("@")) {
            _uiState.value = currentState.copy(error = "Email inválido")
            return
        }

        if (currentState.senha.isEmpty()) {
            _uiState.value = currentState.copy(error = "Senha não pode estar vazia")
            return
        }

        // Login
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, error = null)

            when (val result = repository.login(currentState.email, currentState.senha)) {
                is Result.Success -> {
                    _uiState.value = LoginUiState(isSuccess = true)
                }
                is Result.Error -> {
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val senha: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
```

#### **Repository Interface**
```kotlin
interface UsuarioRepository {
    suspend fun login(email: String, senha: String): Result<String>
}
```

#### **Repository Implementação**
```kotlin
class UsuarioRepositoryImpl(
    private val api: ApiService,
    private val tokenStorage: TokenStorage
) : UsuarioRepository {

    override suspend fun login(email: String, senha: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.login(LoginRequest(email, senha))

                if (response.isSuccessful) {
                    val token = response.headers()["Authorization"]
                        ?: return@withContext Result.Error("Token não encontrado")

                    tokenStorage.saveToken(token)
                    Result.Success(token)
                } else {
                    Result.Error("Credenciais inválidas")
                }
            } catch (e: IOException) {
                Result.Error("Erro de conexão: ${e.message}")
            } catch (e: Exception) {
                Result.Error("Erro desconhecido: ${e.message}")
            }
        }
    }
}
```

#### **API Service (Retrofit)**
```kotlin
interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<Void>
}

data class LoginRequest(
    val login: String,
    val senha: String
)
```

### ✅ Vantagens

1. **Testável**
```kotlin
class LoginViewModelTest {
    @Test
    fun `quando email invalido, deve mostrar erro`() = runTest {
        val viewModel = LoginViewModel(FakeRepository())

        viewModel.onEmailChange("invalido")
        viewModel.onLoginClick()

        assertEquals("Email inválido", viewModel.uiState.value.error)
    }
}
```

2. **Sem memory leaks** - `viewModelScope` cancela automaticamente
3. **Rotação de tela** - Estado preservado no ViewModel
4. **Código reutilizável** - Repository usado em outras telas
5. **Fácil de manter** - Mudança em Repository não afeta UI
6. **Thread-safe** - Coroutines gerenciam threads
7. **Tratamento de erros** - Específico por tipo de erro

### Tamanho: **~200 linhas total**, mas **MUITO mais robusto**

---

## Comparação Lado a Lado

| Aspecto | Tudo no Activity | MVC | MVVM + Repository |
|---------|------------------|-----|-------------------|
| **Testabilidade** | ❌ Impossível | ⚠️ Difícil | ✅ Fácil |
| **Reutilização** | ❌ Nenhuma | ⚠️ Parcial | ✅ Total |
| **Manutenibilidade** | ❌ Pesadelo | ⚠️ Média | ✅ Excelente |
| **Memory Leaks** | ❌ Alto risco | ⚠️ Possível | ✅ Protegido |
| **Rotação de Tela** | ❌ Perde estado | ❌ Perde estado | ✅ Preserva |
| **Threads** | ❌ Manual | ❌ Manual | ✅ Automático |
| **Escalabilidade** | ❌ Não escala | ⚠️ Difícil | ✅ Fácil |
| **Curva de Aprendizado** | ✅ Simples | ⚠️ Média | ❌ Alta |
| **Linhas de Código** | ~150 | ~250 | ~200 |

---

## Por Que Clean Architecture Vence?

### Cenário Real: Adicionar Cache Local

#### **Tudo no Activity**
```
❌ Reescrever TODA a lógica de rede em TODAS as Activities
❌ Decidir quando usar cache em cada lugar
❌ Alto risco de bugs
```

#### **MVC**
```
⚠️ Adicionar lógica no Controller
⚠️ Ainda precisa modificar múltiplos lugares
```

#### **MVVM + Repository**
```
✅ Mudar APENAS o UsuarioRepositoryImpl
✅ ViewModel e UI não sabem que existe cache
✅ Zero bugs nas outras camadas
```

**Implementação:**
```kotlin
class UsuarioRepositoryImpl(
    private val api: ApiService,
    private val cache: UsuarioCache,  // ← Novo!
    private val tokenStorage: TokenStorage
) : UsuarioRepository {

    override suspend fun login(email: String, senha: String): Result<String> {
        // Tenta cache primeiro
        cache.getToken()?.let { return Result.Success(it) }

        // Se não tem cache, busca da API
        return when (val result = api.login(...)) {
            is Result.Success -> {
                cache.saveToken(result.data)  // ← Salva no cache
                result
            }
            is Result.Error -> result
        }
    }
}
```

**ViewModel não muda UMA linha!** 🎉

---

## Quando Usar Cada Uma?

### Tudo no Activity
- ✅ Protótipo de 1 dia
- ✅ App de 1 tela que nunca vai crescer
- ❌ Qualquer coisa séria

### MVC
- ✅ Migração gradual de código legado
- ✅ App pequeno (2-3 telas)
- ⚠️ Não recomendado para novos projetos

### MVVM + Repository
- ✅ Apps comerciais
- ✅ Apps que vão crescer
- ✅ Trabalho em equipe
- ✅ Aprendizado (você vai usar isso no mercado)

---

## Conclusão

**Clean Architecture tem mais código inicial**, mas:

1. ✅ **Economiza tempo** no longo prazo
2. ✅ **Menos bugs** (código isolado)
3. ✅ **Mais produtividade** (reutilização)
4. ✅ **Melhor para portfólio** (empresas esperam isso)

**Investimento vale a pena!** 🚀

---

## Exercício

Tente responder sem olhar:

1. Se eu quiser trocar Retrofit por Ktor, quais camadas mudam?
   <details>
   <summary>Resposta</summary>
   Apenas DATA LAYER (ApiService). ViewModel, UI não mudam nada!
   </details>

2. Se eu quiser adicionar validação de senha forte, onde adiciono?
   <details>
   <summary>Resposta</summary>
   No ViewModel (antes de chamar Repository) ou no Domain Layer (UseCase).
   </details>

3. Como testo a UI sem fazer chamada HTTP real?
   <details>
   <summary>Resposta</summary>
   Crio um FakeRepository que retorna dados fixos e passo para o ViewModel.
   </details>

Dúvidas? Pergunte! 😊
