CREATE TABLE CADASTRO_USUARIO (
	CODIGO SERIAL NOT NULL,
	USUARIO VARCHAR( 30 ) NOT NULL,
	SENHA VARCHAR( 255 ) NOT NULL,
	NOME VARCHAR( 50 ) NOT NULL,
	CONSTRAINT CADASTRO_USUARIO_PKEY PRIMARY KEY( CODIGO ),
	CONSTRAINT CADASTRO_USUARIO_USUARIO_UK UNIQUE( USUARIO )
);