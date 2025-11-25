ALTER TABLE historico_emocional
ADD COLUMN nivel_energia ENUM('ESGOTADO', 'BAIXO', 'MODERADO', 'BOM', 'PLENO'),
ADD COLUMN sugestao_id BIGINT,
ADD COLUMN feedback_sugestao ENUM('ACEITA', 'RECUSADA', 'ALTERNATIVA'),
ADD CONSTRAINT fk_historico_sugestao FOREIGN KEY (sugestao_id) REFERENCES sugestoes(id);

CREATE TABLE historico_contextos (
    historico_id BIGINT NOT NULL,
    contexto ENUM('TRABALHO', 'RELACIONAMENTO', 'SONO', 'SAUDE', 'NOTICIAS', 'NADA_ESPECIFICO') NOT NULL,
    CONSTRAINT fk_historico_contextos FOREIGN KEY (historico_id) REFERENCES historico_emocional(id) ON DELETE CASCADE
);
