CREATE TABLE usuario(
    usuario_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_completo VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL, 
    status VARCHAR2(20) NOT NULL, 
    data_nascimento DATE NOT NULL,
    cargo VARCHAR2(35) NOT NULL,
    funcao VARCHAR2(35) NOT NULL
);

CREATE TABLE contrato (
    id_contrato NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_empresa VARCHAR2(100) NOT NULL,
    quantidade_plantas NUMBER NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    quantidade_ativos NUMBER,
    descricao VARCHAR2(255)
);

CREATE TABLE contrato_usuario (
    id_contrato NUMBER NOT NULL,
    id_usuario NUMBER NOT NULL,

    CONSTRAINT pk_contrato_usuario 
        PRIMARY KEY (id_contrato, id_usuario),

    CONSTRAINT fk_contrato_usuario_contrato 
        FOREIGN KEY (id_contrato) 
        REFERENCES contrato(id_contrato),

    CONSTRAINT fk_contrato_usuario_usuario 
        FOREIGN KEY (id_usuario) 
        REFERENCES usuario(usuario_id)
);