ALTER TABLE CADASTRO_USUARIO ADD COLUMN PERFIL INT NOT NULL DEFAULT('1');
ALTER TABLE CADASTRO_USUARIO ADD CONSTRAINT FK_CADASTRO_USUARIO_PERFIL FOREIGN KEY( PERFIL ) REFERENCES PERFIL( CODIGO );