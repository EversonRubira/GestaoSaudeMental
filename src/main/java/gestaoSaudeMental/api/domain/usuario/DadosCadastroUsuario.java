package gestaoSaudeMental.api.domain.usuario;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class DadosCadastroUsuario {


    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @Email(message = "O email deve ser válido.")
    @NotBlank(message = "O email é obrigatório.")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    @Size(min = 10, max = 15, message = "O telefone deve ter entre 10 e 15 dígitos.")
    private String telefone;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser no passado.")
    private LocalDate dataNascimento;

    @NotNull(message = "O gênero é obrigatório.")
    private GeneroEnum genero;

    private String senha;

    // Construtor
    public DadosCadastroUsuario(String nome, String email, String telefone, LocalDate dataNascimento, GeneroEnum genero, String senha) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.senha = senha;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public GeneroEnum getGenero() { return genero; }
    public void setGenero(GeneroEnum genero) { this.genero = genero; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    @Override
    public String toString() {
        return "DadosCadastroUsuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", genero=" + genero +
                ", senha='********'" +
                '}';
    }
}

