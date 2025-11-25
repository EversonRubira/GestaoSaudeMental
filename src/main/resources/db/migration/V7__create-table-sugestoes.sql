CREATE TABLE sugestoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao VARCHAR(1000) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    duracao VARCHAR(50),
    icone VARCHAR(50),
    razao VARCHAR(500),
    alternativas VARCHAR(1000),
    guia_url VARCHAR(500),
    playlist_url VARCHAR(500),
    estado_emocional ENUM('FELIZ', 'TRISTE', 'ANSIOSO', 'CANSADO', 'IRRITADO', 'OUTROS'),
    nivel_energia ENUM('ESGOTADO', 'BAIXO', 'MODERADO', 'BOM', 'PLENO'),
    ativa BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE sugestao_contextos (
    sugestao_id BIGINT NOT NULL,
    contexto ENUM('TRABALHO', 'RELACIONAMENTO', 'SONO', 'SAUDE', 'NOTICIAS', 'NADA_ESPECIFICO') NOT NULL,
    CONSTRAINT fk_sugestao_contextos FOREIGN KEY (sugestao_id) REFERENCES sugestoes(id) ON DELETE CASCADE
);
