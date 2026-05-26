-- =============================================================
-- DDL PostgreSQL - Altave Backend
-- Banco: postgres  |  Schema: public
-- =============================================================

CREATE TABLE usuario (
    usuario_id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_completo    VARCHAR(100) NOT NULL,
    email            VARCHAR(100) NOT NULL,
    status           VARCHAR(20)  NOT NULL,
    data_nascimento  DATE         NOT NULL,
    cargo            VARCHAR(35)  NOT NULL,
    funcao           VARCHAR(35)  NOT NULL
);

CREATE TABLE contrato (
    id_contrato           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_empresa          VARCHAR(100) NOT NULL,
    quantidade_plantas    INTEGER      NOT NULL,
    data_inicio           DATE         NOT NULL,
    data_fim              DATE         NOT NULL,
    quantidade_ativos     INTEGER,
    descricao             VARCHAR(255),
    criador               VARCHAR(100) NOT NULL,
    quantidade_supervisoes INTEGER     NOT NULL,
    status                VARCHAR(35)  NOT NULL CHECK (status IN ('ATIVO', 'INATIVO'))
);

CREATE TABLE contrato_usuario (
    id_contrato BIGINT NOT NULL,
    id_usuario  BIGINT NOT NULL,

    CONSTRAINT pk_contrato_usuario
        PRIMARY KEY (id_contrato, id_usuario),

    CONSTRAINT fk_contrato_usuario_contrato
        FOREIGN KEY (id_contrato) REFERENCES contrato (id_contrato),

    CONSTRAINT fk_contrato_usuario_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (usuario_id)
);

CREATE TABLE ativo (
    id_ativo                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_ativo               VARCHAR(50)  NOT NULL,
    status                   VARCHAR(20)  NOT NULL
        CONSTRAINT ck_ativo_status CHECK (status IN ('OPERACIONAL', 'EM_MANUTENCAO', 'INATIVO', 'REMOVIDO')),
    fabricante               VARCHAR(100),
    tipo                     VARCHAR(50)  NOT NULL,
    periodicidade_manutencao BIGINT      NOT NULL,
    descricao                VARCHAR(500),
    data_instalacao          DATE         NOT NULL,
    data_remocao             DATE,
    predio                   VARCHAR(100),
    planta                   VARCHAR(100),
    id_contrato              BIGINT       NOT NULL,
    CONSTRAINT fk_ativo_contrato
        FOREIGN KEY (id_contrato) REFERENCES contrato (id_contrato)
);

CREATE TABLE ordem_manutencao (
    id_ordem        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_ordem      VARCHAR(100) NOT NULL,
    data_inicio     DATE         NOT NULL,
    data_fim        DATE,
    tipo_manutencao VARCHAR(20)  NOT NULL
        CONSTRAINT ck_om_tipo CHECK (tipo_manutencao IN ('PREVENTIVA', 'CORRETIVA')),
    id_ativo        BIGINT       NOT NULL,
    descricao       VARCHAR(500),
    status          VARCHAR(20)  NOT NULL
        CONSTRAINT ck_om_status CHECK (status IN ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA')),
    comentario      VARCHAR(500),
    CONSTRAINT fk_om_ativo
        FOREIGN KEY (id_ativo) REFERENCES ativo (id_ativo)
);

CREATE TABLE ordem_usuario (
    id_ordem_manutencao BIGINT NOT NULL,
    id_usuario          BIGINT NOT NULL,
    CONSTRAINT pk_ordem_usuario
        PRIMARY KEY (id_ordem_manutencao, id_usuario),
    CONSTRAINT fk_ou_ordem
        FOREIGN KEY (id_ordem_manutencao) REFERENCES ordem_manutencao (id_ordem),
    CONSTRAINT fk_ou_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (usuario_id)
);

-- =============================================================
-- Dados iniciais (seeds)
-- IDs gerados automaticamente pela sequência (GENERATED ALWAYS AS IDENTITY)
-- Ordem: usuario → contrato → contrato_usuario → ativo → ordem_manutencao → ordem_usuario
-- =============================================================

-- Usuários  (IDs gerados: 1=João, 2=Maria, 3=Carlos)
INSERT INTO usuario (nome_completo, email, status, data_nascimento, cargo, funcao)
VALUES ('João Silva', 'joao.silva@email.com', 'ATIVO', DATE '1995-03-10', 'Técnico', 'Manutenção');

INSERT INTO usuario (nome_completo, email, status, data_nascimento, cargo, funcao)
VALUES ('Maria Oliveira', 'maria.oliveira@email.com', 'ATIVO', DATE '1990-07-21', 'Supervisor', 'Gestão');

INSERT INTO usuario (nome_completo, email, status, data_nascimento, cargo, funcao)
VALUES ('Carlos Souza', 'carlos.souza@email.com', 'INATIVO', DATE '1988-11-05', 'Analista', 'Planejamento');

-- Contratos  (IDs gerados: 1=Empresa Alpha, 2=Empresa Beta)
INSERT INTO contrato (nome_empresa, quantidade_plantas, data_inicio, data_fim, quantidade_ativos, descricao, criador, quantidade_supervisoes, status)
VALUES ('Empresa Alpha', 3, DATE '2024-01-01', DATE '2026-01-01', 10, 'Contrato de manutenção industrial', 'Maria Oliveira', 2, 'ATIVO');

INSERT INTO contrato (nome_empresa, quantidade_plantas, data_inicio, data_fim, quantidade_ativos, descricao, criador, quantidade_supervisoes, status)
VALUES ('Empresa Beta', 2, DATE '2023-06-01', DATE '2025-06-01', 5, 'Contrato de inspeção técnica', 'João Silva', 1, 'INATIVO');

-- Contrato_Usuario
-- João (1) e Maria (2) no contrato 1 (Empresa Alpha)
INSERT INTO contrato_usuario (id_contrato, id_usuario) VALUES (1, 1);
INSERT INTO contrato_usuario (id_contrato, id_usuario) VALUES (1, 2);

-- Carlos (3) no contrato 2 (Empresa Beta)
INSERT INTO contrato_usuario (id_contrato, id_usuario) VALUES (2, 3);

-- Ativo  (ID gerado: 1)
INSERT INTO ativo (nome_ativo, status, fabricante, tipo, periodicidade_manutencao, descricao, data_instalacao, predio, planta, id_contrato)
VALUES ('Câmera de segurança', 'OPERACIONAL', 'Intelbras', 'CAMERA', 30, 'Câmera de segurança - entrada principal', DATE '2024-02-10', 'Prédio A', 'Planta 1', 1);

-- Ordem de manutenção  (ID gerado: 1)
INSERT INTO ordem_manutencao (nome_ordem, data_inicio, tipo_manutencao, id_ativo, descricao, status)
VALUES ('Inspeção câmera entrada', DATE '2025-01-10', 'PREVENTIVA', 1, 'Verificação de lente, foco e conexão', 'PENDENTE');

-- Ordem_usuario: João (1) responsável pela ordem 1
INSERT INTO ordem_usuario (id_ordem_manutencao, id_usuario) VALUES (1, 1);
