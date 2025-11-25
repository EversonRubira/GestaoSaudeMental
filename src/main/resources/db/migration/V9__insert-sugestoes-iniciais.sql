-- Sugestões para TRISTEZA + ENERGIA BAIXA/ESGOTADO
INSERT INTO sugestoes (titulo, descricao, categoria, duracao, icone, razao, alternativas, estado_emocional, nivel_energia, ativa)
VALUES
('Banho Quente', 'Tome um banho quente relaxante de 15 minutos', 'Descompressão Suave', '15 min', '🛁',
 'Você está mentalmente esgotado. Priorize descanso passivo agora.',
 'Chá de camomila|Deitar sem tela por 20 minutos|Massagem facial',
 'TRISTE', 'BAIXO', TRUE),

('Assistir Comédia Leve', 'Assista um episódio de uma série de comédia', 'Distração Acolhedora', '30 min', '📺',
 'Rir libera endorfinas mesmo quando você está cansado.',
 'Ouvir podcast engraçado|Ver fotos de momentos felizes|Acariciar um pet',
 'TRISTE', 'ESGOTADO', TRUE);

-- Sugestões para TRISTEZA + ENERGIA MODERADA
INSERT INTO sugestoes (titulo, descricao, categoria, duracao, icone, razao, alternativas, estado_emocional, nivel_energia, ativa)
VALUES
('Caminhada Leve', 'Faça uma caminhada tranquila de 10 minutos', 'Movimento Suave', '10 min', '🚶',
 'Movimento leve ajuda a processar emoções sem esgotar você.',
 'Alongamento suave|Respiração 4-7-8|Yoga restaurativa',
 'TRISTE', 'MODERADO', TRUE);

-- Sugestões para TRISTEZA + ENERGIA ALTA
INSERT INTO sugestoes (titulo, descricao, categoria, duracao, icone, razao, alternativas, estado_emocional, nivel_energia, ativa)
VALUES
('Corrida ou Exercício Intenso', 'Pratique exercício físico intenso por 30 minutos', 'Descarga Emocional', '30 min', '🏃',
 'Energia alta permite transformar tristeza em movimento.',
 'Boxe|Dança intensa|Limpeza energética da casa',
 'TRISTE', 'BOM', TRUE),

('Corrida ao Ar Livre', 'Corra ao ar livre aproveitando a natureza', 'Ação Transformadora', '30 min', '🏃',
 'Você tem energia para transformar essa tristeza em ação.',
 'Natação|Escalada|Spinning',
 'TRISTE', 'PLENO', TRUE);

-- Sugestões para ANSIEDADE + ENERGIA BAIXA
INSERT INTO sugestoes (titulo, descricao, categoria, duracao, icone, razao, alternativas, estado_emocional, nivel_energia, ativa)
VALUES
('Respiração 4-7-8', 'Pratique 3 ciclos da respiração 4-7-8', 'Regulação', '5 min', '🧘',
 'Seu sistema nervoso está sobrecarregado. Foco em regulação rápida.',
 'Progressive muscle relaxation|ASMR|Grounding 5-4-3-2-1',
 'ANSIOSO', 'BAIXO', TRUE),

('Grounding 5-4-3-2-1', 'Técnica de ancoragem: 5 coisas que vê, 4 que ouve, 3 que toca, 2 que cheira, 1 que prova', 'Ancoragem', '5 min', '🌱',
 'Ansiedade com esgotamento precisa de ancoragem no presente.',
 'Segurar objeto frio|Água gelada no rosto|Nomear emoções em voz alta',
 'ANSIOSO', 'ESGOTADO', TRUE);

-- Sugestões para ANSIEDADE + ENERGIA MODERADA
INSERT INTO sugestoes (titulo, descricao, categoria, duracao, icone, razao, alternativas, estado_emocional, nivel_energia, ativa)
VALUES
('Lista Urgente vs Importante', 'Organize suas tarefas em matriz de prioridades', 'Organização', '10 min', '📋',
 'Ansiedade com energia moderada se beneficia de estrutura e organização.',
 'Pomodoro 25 min|Time blocking do dia|Brain dump em papel',
 'ANSIOSO', 'MODERADO', TRUE);

-- Sugestões para ANSIEDADE + ENERGIA ALTA
INSERT INTO sugestoes (titulo, descricao, categoria, duracao, icone, razao, alternativas, estado_emocional, nivel_energia, ativa)
VALUES
('HIIT ou Corrida Intensa', 'Pratique treino intervalado de alta intensidade', 'Descarga Física', '20 min', '💪',
 'Energia alta com ansiedade precisa descarregar adrenalina em movimento.',
 'Pular corda|Boxe|Natação intensa',
 'ANSIOSO', 'BOM', TRUE),

('CrossFit ou Treino Pesado', 'Faça um treino intenso de força', 'Gasto Energético', '40 min', '🏋️',
 'Canalize a ansiedade em esforço físico produtivo.',
 'Spinning|Escalada|Corrida de velocidade',
 'ANSIOSO', 'PLENO', TRUE);

-- Sugestões para FELIZ (qualquer energia)
INSERT INTO sugestoes (titulo, descricao, categoria, duracao, icone, razao, alternativas, estado_emocional, nivel_energia, ativa)
VALUES
('Registre Este Momento', 'Anote o que te fez feliz hoje em um diário', 'Gratidão', '5 min', '📝',
 'Memórias positivas fortalecem a resiliência emocional.',
 'Tirar foto do momento|Mensagem de gratidão para alguém|Lista de 3 coisas boas do dia',
 'FELIZ', 'MODERADO', TRUE),

('Compartilhe Sua Alegria', 'Ligue ou mande mensagem para alguém especial', 'Conexão', '15 min', '💬',
 'Compartilhar felicidade multiplica o sentimento positivo.',
 'Fazer surpresa para alguém|Elogio genuíno|Ajudar alguém',
 'FELIZ', 'BOM', TRUE);

-- Sugestões para CANSADO
INSERT INTO sugestoes (titulo, descricao, categoria, duracao, icone, razao, alternativas, estado_emocional, nivel_energia, ativa)
VALUES
('Cochilo Estratégico', 'Tire um cochilo de 20 minutos', 'Recuperação', '20 min', '😴',
 'Seu corpo precisa de descanso. Um cochilo curto recarrega as energias.',
 'Deitar sem dormir|Meditação guiada|Música relaxante deitado',
 'CANSADO', 'ESGOTADO', TRUE),

('Hidratação e Lanche Leve', 'Beba água e coma algo nutritivo', 'Auto-cuidado', '10 min', '💧',
 'Cansaço pode ser sinal de desidratação ou fome.',
 'Vitamina de frutas|Chá verde|Frutas frescas',
 'CANSADO', 'BAIXO', TRUE);

-- Agora inserir os contextos para cada sugestão
-- Tristeza + Baixo + Trabalho
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'TRABALHO' FROM sugestoes WHERE titulo = 'Banho Quente';

INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'SONO' FROM sugestoes WHERE titulo = 'Banho Quente';

-- Tristeza + Esgotado + Relacionamento
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'RELACIONAMENTO' FROM sugestoes WHERE titulo = 'Assistir Comédia Leve';

-- Tristeza + Moderado + Trabalho
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'TRABALHO' FROM sugestoes WHERE titulo = 'Caminhada Leve';

INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'SONO' FROM sugestoes WHERE titulo = 'Caminhada Leve';

-- Tristeza + Alto + Trabalho
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'TRABALHO' FROM sugestoes WHERE titulo = 'Corrida ou Exercício Intenso';

-- Tristeza + Pleno + Qualquer
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'NADA_ESPECIFICO' FROM sugestoes WHERE titulo = 'Corrida ao Ar Livre';

-- Ansiedade + Baixo + Trabalho
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'TRABALHO' FROM sugestoes WHERE titulo = 'Respiração 4-7-8';

INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'NOTICIAS' FROM sugestoes WHERE titulo = 'Respiração 4-7-8';

-- Ansiedade + Esgotado + Relacionamento
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'RELACIONAMENTO' FROM sugestoes WHERE titulo = 'Grounding 5-4-3-2-1';

INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'SAUDE' FROM sugestoes WHERE titulo = 'Grounding 5-4-3-2-1';

-- Ansiedade + Moderado + Trabalho
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'TRABALHO' FROM sugestoes WHERE titulo = 'Lista Urgente vs Importante';

-- Ansiedade + Alto + Qualquer
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'NADA_ESPECIFICO' FROM sugestoes WHERE titulo = 'HIIT ou Corrida Intensa';

-- Ansiedade + Pleno + Qualquer
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'NADA_ESPECIFICO' FROM sugestoes WHERE titulo = 'CrossFit ou Treino Pesado';

-- Feliz + Moderado
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'NADA_ESPECIFICO' FROM sugestoes WHERE titulo = 'Registre Este Momento';

-- Feliz + Alto
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'RELACIONAMENTO' FROM sugestoes WHERE titulo = 'Compartilhe Sua Alegria';

-- Cansado + Esgotado
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'SONO' FROM sugestoes WHERE titulo = 'Cochilo Estratégico';

INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'TRABALHO' FROM sugestoes WHERE titulo = 'Cochilo Estratégico';

-- Cansado + Baixo
INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'TRABALHO' FROM sugestoes WHERE titulo = 'Hidratação e Lanche Leve';

INSERT INTO sugestao_contextos (sugestao_id, contexto)
SELECT id, 'SAUDE' FROM sugestoes WHERE titulo = 'Hidratação e Lanche Leve';
