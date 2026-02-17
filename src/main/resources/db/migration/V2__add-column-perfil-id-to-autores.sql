ALTER TABLE autores ADD COLUMN perfil_id BIGINT;
UPDATE autores SET perfil_id = 1; -- Define um valor padrão se houver registros
ALTER TABLE autores MODIFY COLUMN perfil_id BIGINT NOT NULL;
ALTER TABLE autores ADD CONSTRAINT fk_autores_perfil_id FOREIGN KEY (perfil_id) REFERENCES perfis (id);