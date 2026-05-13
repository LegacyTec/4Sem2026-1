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
    descricao VARCHAR2(255),
    criador VARCHAR2(100) NOT NULL,
    quantidade_supervisoes NUMBER NOT NULL,
    status VARCHAR2(35) NOT NULL CHECK(status IN('ATIVO', 'INATIVO'))
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

CREATE TABLE ativo (
    id_ativo                 NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_ativo VARCHAR2(50) NOT NULL,
    status                   VARCHAR2(20)    NOT NULL
        CONSTRAINT ck_ativo_status CHECK (status IN ('OPERACIONAL', 'EM_MANUTENCAO', 'INATIVO', 'REMOVIDO')),
    fabricante               VARCHAR2(100),
    tipo                     VARCHAR2(50)    NOT NULL,
    periodicidade_manutencao NUMBER          NOT NULL,
    descricao                VARCHAR2(500),
    data_instalacao          DATE            NOT NULL,
    data_remocao             DATE,
    predio                   VARCHAR2(100),
    planta                   VARCHAR2(100),
    id_contrato              NUMBER          NOT NULL,
    CONSTRAINT fk_ativo_contrato
        FOREIGN KEY (id_contrato) REFERENCES contrato(id_contrato)
);

CREATE TABLE ordem_manutencao (
    id_ordem        NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_ordem      VARCHAR2(100)   NOT NULL,
    data_inicio     DATE            NOT NULL,
    data_fim        DATE,
    tipo_manutencao VARCHAR2(20)    NOT NULL
        CONSTRAINT ck_om_tipo CHECK (tipo_manutencao IN ('PREVENTIVA', 'CORRETIVA')),
    id_ativo        NUMBER          NOT NULL,
    descricao       VARCHAR2(500),
    status          VARCHAR2(20)    NOT NULL
        CONSTRAINT ck_om_status CHECK (status IN ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA')),
    comentario      VARCHAR2(500),
    CONSTRAINT fk_om_ativo
        FOREIGN KEY (id_ativo) REFERENCES ativo(id_ativo)
);

CREATE TABLE ordem_usuario (
    id_ordem_manutencao  NUMBER NOT NULL,
    id_usuario           NUMBER NOT NULL,
    CONSTRAINT pk_ordem_usuario
        PRIMARY KEY (id_ordem_manutencao, id_usuario),
    CONSTRAINT fk_ou_ordem
        FOREIGN KEY (id_ordem_manutencao) REFERENCES ordem_manutencao(id_ordem),
    CONSTRAINT fk_ou_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(usuario_id)
);


--- Usuario
INSERT INTO usuario (nome_completo, email, status, data_nascimento, cargo, funcao)
VALUES ('João Silva', 'joao.silva@email.com', 'ATIVO', DATE '1995-03-10', 'Técnico', 'Manutenção');

INSERT INTO usuario (nome_completo, email, status, data_nascimento, cargo, funcao)
VALUES ('Maria Oliveira', 'maria.oliveira@email.com', 'ATIVO', DATE '1990-07-21', 'Supervisor', 'Gestão');

INSERT INTO usuario (nome_completo, email, status, data_nascimento, cargo, funcao)
VALUES ('Carlos Souza', 'carlos.souza@email.com', 'INATIVO', DATE '1988-11-05', 'Analista', 'Planejamento');

-- Contratos
INSERT INTO contrato (nome_empresa, quantidade_plantas, data_inicio, data_fim, quantidade_ativos, descricao, criador, quantidade_supervisoes, status)
VALUES ('Empresa Alpha', 3, DATE '2024-01-01', DATE '2026-01-01', 10, 'Contrato de manutenção industrial', 'Maria Oliveira', 2, 'ATIVO');

INSERT INTO contrato (nome_empresa, quantidade_plantas, data_inicio, data_fim, quantidade_ativos, descricao, criador, quantidade_supervisoes, status)
VALUES ('Empresa Beta', 2, DATE '2023-06-01', DATE '2025-06-01', 5, 'Contrato de inspeção técnica', 'João Silva', 1, 'INATIVO');

--- Contrato_Usuario
-- João e Maria no contrato 1
INSERT INTO contrato_usuario (id_contrato, id_usuario) VALUES (5, 4);
INSERT INTO contrato_usuario (id_contrato, id_usuario) VALUES (5, 5);
SELECT * FROM usuario;
SELECT * FROM contrato;
SELECT * FROM contrato_usuario;
SELECT * FROM contrato;


-- Carlos no contrato 2
INSERT INTO contrato_usuario (id_contrato, id_usuario) VALUES (6, 6);

--- Ativos
INSERT INTO ativo (nome_ativo, status, fabricante, tipo, periodicidade_manutencao, descricao, data_instalacao, predio, planta, id_contrato)
VALUES ('Câmera de segurança', 'OPERACIONAL', 'Intelbras', 'CAMERA', 30, 'Câmera de segurança - entrada principal', DATE '2024-02-10', 'Prédio A', 'Planta 1', 6);

SELECT *FROM ORDEM_MANUTENCAO;
-- Ordem de manutenção
INSERT INTO ordem_manutencao 
(nome_ordem, data_inicio, tipo_manutencao, id_ativo, descricao, status)
VALUES 
('Inspeção câmera entrada', DATE '2025-01-10', 'PREVENTIVA', 17, 
 'Verificação de lente, foco e conexão', 'PENDENTE');

-- Ordem_usuario
 INSERT INTO ordem_usuario (id_ordem_manutencao, id_usuario) VALUES (10, 5);

SELECT * FROM ordem_manutencao;

SELECT * FROM ativo;

commit;