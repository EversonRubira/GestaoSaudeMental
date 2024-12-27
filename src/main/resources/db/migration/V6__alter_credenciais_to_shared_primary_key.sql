ALTER TABLE credenciais
ADD CONSTRAINT fk_credenciais_usuario FOREIGN KEY (id) REFERENCES usuarios(id);

