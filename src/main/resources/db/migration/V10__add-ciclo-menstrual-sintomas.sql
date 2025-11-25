-- Adicionar campo de ciclo menstrual ao histórico emocional
ALTER TABLE historico_emocional
ADD COLUMN ciclo_menstrual ENUM('NAO_APLICAVEL', 'SIM_MENSTRUADA', 'NAO_MENSTRUADA') DEFAULT 'NAO_APLICAVEL';

-- Criar tabela para sintomas físicos
CREATE TABLE historico_sintomas (
    historico_id BIGINT NOT NULL,
    sintoma ENUM('COLICA', 'DOR_CABECA', 'ENJOO', 'FADIGA', 'INCHACO', 'DOR_SEIOS', 'NENHUM') NOT NULL,
    CONSTRAINT fk_historico_sintomas FOREIGN KEY (historico_id) REFERENCES historico_emocional(id) ON DELETE CASCADE
);
