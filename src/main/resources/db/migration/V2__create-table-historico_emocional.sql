CREATE TABLE historico_emocional (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_registro DATE NOT NULL,
    estado_emocional ENUM('FELIZ', 'TRISTE', 'ANSIOSO', 'CANSADO', 'IRRITADO', 'OUTROS') NOT NULL,
    atividade_realizada ENUM('EXERCICIO', 'LEITURA', 'TRABALHO', 'SOCIALIZAR', 'ASSISTIR_TV', 'MEDITAR', 'OUTROS') NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
