-- Atualiza a tabela usuarios para permitir valores nulos
ALTER TABLE usuarios
MODIFY COLUMN estado_emocional VARCHAR(255) NULL,
MODIFY COLUMN atividade_realizada VARCHAR(255) NULL;
