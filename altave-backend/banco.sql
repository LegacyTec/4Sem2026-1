-- =============================================================
-- Script de criacao do banco de dados
-- Sistema de Gestao de Manutencoes - Altave
-- LegacyTech / FATEC SJC / API 2025-2
-- =============================================================

-- Sequences (Oracle nao tem AUTO_INCREMENT)
CREATE SEQUENCE seq_cliente       START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_contrato      START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_certificacao  START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_localizacao   START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_tipo_equip    START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_regra_manut   START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_ativo         START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_tecnico       START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_disp_tecnico  START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_manutencao    START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_hist_status   START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_ordem_servico START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_item_estoque  START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_retirada_item START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_ck_template   START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_ck_item       START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_ck_execucao   START WITH 1 INCREMENT BY 1;

-- =============================================================
-- MODULO 1 - Ativos e Contratos
-- =============================================================

CREATE TABLE cliente (
    id           NUMBER PRIMARY KEY,
    razao_social VARCHAR2(200) NOT NULL,
    pais         VARCHAR2(100) NOT NULL,
    criticidade  VARCHAR2(10)  NOT NULL CHECK (criticidade IN ('alta', 'media', 'baixa')),
    ativo        CHAR(1)       NOT NULL DEFAULT 'S' CHECK (ativo IN ('S', 'N'))
);

CREATE TABLE contrato (
    id          NUMBER PRIMARY KEY,
    cliente_id  NUMBER        NOT NULL,
    numero      VARCHAR2(50)  NOT NULL UNIQUE,
    criticidade VARCHAR2(10)  NOT NULL CHECK (criticidade IN ('alta', 'media', 'baixa')),
    sla_dias    NUMBER        NOT NULL,
    dt_inicio   DATE          NOT NULL,
    dt_fim      DATE          NOT NULL,
    CONSTRAINT fk_contrato_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE certificacao (
    id              NUMBER PRIMARY KEY,
    nome            VARCHAR2(50)  NOT NULL UNIQUE,
    descricao       VARCHAR2(500),
    validade_meses  NUMBER        NOT NULL
);

CREATE TABLE contrato_certificacao (
    contrato_id     NUMBER NOT NULL,
    certificacao_id NUMBER NOT NULL,
    obrigatoria     CHAR(1) NOT NULL DEFAULT 'S' CHECK (obrigatoria IN ('S', 'N')),
    PRIMARY KEY (contrato_id, certificacao_id),
    CONSTRAINT fk_cc_contrato     FOREIGN KEY (contrato_id)     REFERENCES contrato(id),
    CONSTRAINT fk_cc_certificacao FOREIGN KEY (certificacao_id) REFERENCES certificacao(id)
);

-- Localizacao sem SDO_GEOMETRY para facilitar o ambiente local de desenvolvimento.
-- Em producao (OCI), substituir lat/lng por SDO_GEOMETRY conforme modulo-ativos-contratos.md
CREATE TABLE localizacao (
    id    NUMBER PRIMARY KEY,
    nome  VARCHAR2(200) NOT NULL,
    pais  VARCHAR2(100) NOT NULL,
    tipo  VARCHAR2(20)  NOT NULL CHECK (tipo IN ('terra', 'embarcacao')),
    lat   NUMBER(10, 7),
    lng   NUMBER(10, 7)
);

CREATE TABLE tipo_equipamento (
    id         NUMBER PRIMARY KEY,
    nome       VARCHAR2(200) NOT NULL,
    fabricante VARCHAR2(200),
    modelo     VARCHAR2(200)
);

CREATE TABLE regra_manutencao (
    id                 NUMBER PRIMARY KEY,
    contrato_id        NUMBER       NOT NULL,
    tipo_manutencao    VARCHAR2(20) NOT NULL CHECK (tipo_manutencao IN ('preventiva', 'corretiva')),
    periodicidade_dias NUMBER,
    limite_horas_uso   NUMBER,
    antecedencia_alerta NUMBER      NOT NULL DEFAULT 7,
    CONSTRAINT fk_regra_contrato FOREIGN KEY (contrato_id) REFERENCES contrato(id)
);

CREATE TABLE ativo (
    id                    NUMBER PRIMARY KEY,
    contrato_id           NUMBER        NOT NULL,
    tipo_equipamento_id   NUMBER        NOT NULL,
    localizacao_id        NUMBER        NOT NULL,
    numero_serie          VARCHAR2(100) NOT NULL UNIQUE,
    dt_instalacao         DATE          NOT NULL,
    horas_uso_acumuladas  NUMBER        DEFAULT 0,
    status                VARCHAR2(20)  NOT NULL DEFAULT 'operacional'
        CHECK (status IN ('operacional', 'em_manutencao', 'inativo')),
    CONSTRAINT fk_ativo_contrato   FOREIGN KEY (contrato_id)         REFERENCES contrato(id),
    CONSTRAINT fk_ativo_tipo_equip FOREIGN KEY (tipo_equipamento_id) REFERENCES tipo_equipamento(id),
    CONSTRAINT fk_ativo_local      FOREIGN KEY (localizacao_id)      REFERENCES localizacao(id)
);

-- =============================================================
-- MODULO 3 - Tecnicos e Competencias
-- =============================================================

CREATE TABLE tecnico (
    id                   NUMBER PRIMARY KEY,
    nome                 VARCHAR2(200) NOT NULL,
    email                VARCHAR2(200) NOT NULL UNIQUE,
    nivel                VARCHAR2(10)  NOT NULL CHECK (nivel IN ('junior', 'pleno', 'senior')),
    status               VARCHAR2(20)  NOT NULL DEFAULT 'disponivel'
        CHECK (status IN ('disponivel', 'em_campo', 'embarcado', 'de_folga')),
    localizacao_base_id  NUMBER,
    localizacao_atual_id NUMBER,
    lat_atual            NUMBER(10, 7),
    lng_atual            NUMBER(10, 7),
    dt_ultimo_embarque   DATE,
    dt_previsto_retorno  DATE,
    CONSTRAINT fk_tecnico_base  FOREIGN KEY (localizacao_base_id)  REFERENCES localizacao(id),
    CONSTRAINT fk_tecnico_atual FOREIGN KEY (localizacao_atual_id) REFERENCES localizacao(id)
);

CREATE TABLE tecnico_certificacao (
    tecnico_id      NUMBER        NOT NULL,
    certificacao_id NUMBER        NOT NULL,
    dt_obtencao     DATE          NOT NULL,
    dt_vencimento   DATE          NOT NULL,
    documento_ref   VARCHAR2(200),
    PRIMARY KEY (tecnico_id, certificacao_id),
    CONSTRAINT fk_tc_tecnico      FOREIGN KEY (tecnico_id)      REFERENCES tecnico(id),
    CONSTRAINT fk_tc_certificacao FOREIGN KEY (certificacao_id) REFERENCES certificacao(id)
);

CREATE TABLE disponibilidade_tecnico (
    id                 NUMBER PRIMARY KEY,
    tecnico_id         NUMBER       NOT NULL,
    dt_inicio_bloqueio DATE         NOT NULL,
    dt_fim_bloqueio    DATE         NOT NULL,
    motivo             VARCHAR2(20) NOT NULL CHECK (motivo IN ('ferias', 'embarque', 'manutencao')),
    manutencao_id      NUMBER,
    CONSTRAINT fk_disp_tecnico FOREIGN KEY (tecnico_id) REFERENCES tecnico(id)
);

-- =============================================================
-- MODULO 2 - Manutencoes
-- =============================================================

CREATE TABLE manutencao (
    id                      NUMBER PRIMARY KEY,
    ativo_id                NUMBER        NOT NULL,
    tecnico_id              NUMBER,
    tipo                    VARCHAR2(20)  NOT NULL CHECK (tipo IN ('preventiva', 'corretiva')),
    status                  VARCHAR2(20)  NOT NULL DEFAULT 'pendente'
        CHECK (status IN ('pendente', 'alocada', 'em_execucao', 'concluida', 'cancelada', 'vencida')),
    criticidade             VARCHAR2(10)  NOT NULL CHECK (criticidade IN ('alta', 'media', 'baixa')),
    dt_prevista             DATE          NOT NULL,
    dt_limite               DATE          NOT NULL,
    dt_inicio_execucao      TIMESTAMP,
    dt_conclusao            TIMESTAMP,
    horas_uso_no_momento    NUMBER        DEFAULT 0,
    observacoes             CLOB,
    gerado_automaticamente  CHAR(1)       NOT NULL DEFAULT 'N' CHECK (gerado_automaticamente IN ('S', 'N')),
    CONSTRAINT fk_manut_ativo   FOREIGN KEY (ativo_id)   REFERENCES ativo(id),
    CONSTRAINT fk_manut_tecnico FOREIGN KEY (tecnico_id) REFERENCES tecnico(id)
);

CREATE TABLE equipe_manutencao (
    manutencao_id NUMBER       NOT NULL,
    tecnico_id    NUMBER       NOT NULL,
    papel         VARCHAR2(20) NOT NULL CHECK (papel IN ('lider', 'auxiliar')),
    dt_alocacao   DATE         NOT NULL,
    PRIMARY KEY (manutencao_id, tecnico_id),
    CONSTRAINT fk_equipe_manut   FOREIGN KEY (manutencao_id) REFERENCES manutencao(id),
    CONSTRAINT fk_equipe_tecnico FOREIGN KEY (tecnico_id)    REFERENCES tecnico(id)
);

CREATE TABLE historico_status (
    id              NUMBER PRIMARY KEY,
    manutencao_id   NUMBER        NOT NULL,
    status_anterior VARCHAR2(20),
    status_novo     VARCHAR2(20)  NOT NULL,
    dt_transicao    TIMESTAMP     NOT NULL,
    usuario_id      NUMBER,
    motivo          VARCHAR2(500),
    CONSTRAINT fk_hist_manut FOREIGN KEY (manutencao_id) REFERENCES manutencao(id)
);

-- =============================================================
-- MODULO 4 - Logistica e Checklist
-- =============================================================

CREATE TABLE ordem_servico (
    id             NUMBER PRIMARY KEY,
    manutencao_id  NUMBER       NOT NULL,
    tecnico_id     NUMBER       NOT NULL,
    status         VARCHAR2(20) NOT NULL DEFAULT 'planejada'
        CHECK (status IN ('planejada', 'em_campo', 'encerrada')),
    dt_saida       DATE,
    dt_retorno     DATE,
    tempo_viagem_h NUMBER       DEFAULT 0,
    CONSTRAINT fk_os_manutencao FOREIGN KEY (manutencao_id) REFERENCES manutencao(id),
    CONSTRAINT fk_os_tecnico    FOREIGN KEY (tecnico_id)    REFERENCES tecnico(id)
);

CREATE TABLE item_estoque (
    id              NUMBER PRIMARY KEY,
    nome            VARCHAR2(200) NOT NULL,
    categoria       VARCHAR2(20)  NOT NULL CHECK (categoria IN ('ferramenta', 'EPI', 'peca')),
    qtd_disponivel  NUMBER        NOT NULL DEFAULT 0
);

CREATE TABLE retirada_item (
    id               NUMBER PRIMARY KEY,
    ordem_servico_id NUMBER NOT NULL,
    item_estoque_id  NUMBER NOT NULL,
    qtd_retirada     NUMBER NOT NULL,
    qtd_devolvida    NUMBER DEFAULT 0,
    dt_retirada      DATE   NOT NULL,
    CONSTRAINT fk_retirada_os    FOREIGN KEY (ordem_servico_id) REFERENCES ordem_servico(id),
    CONSTRAINT fk_retirada_item  FOREIGN KEY (item_estoque_id)  REFERENCES item_estoque(id)
);

CREATE TABLE checklist_template (
    id                  NUMBER PRIMARY KEY,
    tipo_equipamento_id NUMBER        NOT NULL,
    nome                VARCHAR2(200) NOT NULL,
    CONSTRAINT fk_ck_template_tipo FOREIGN KEY (tipo_equipamento_id) REFERENCES tipo_equipamento(id)
);

CREATE TABLE checklist_item (
    id          NUMBER PRIMARY KEY,
    template_id NUMBER        NOT NULL,
    descricao   VARCHAR2(500) NOT NULL,
    obrigatorio CHAR(1)       NOT NULL DEFAULT 'N' CHECK (obrigatorio IN ('S', 'N')),
    CONSTRAINT fk_ck_item_template FOREIGN KEY (template_id) REFERENCES checklist_template(id)
);

CREATE TABLE checklist_execucao (
    id               NUMBER PRIMARY KEY,
    ordem_servico_id NUMBER    NOT NULL,
    item_id          NUMBER    NOT NULL,
    concluido        CHAR(1)   NOT NULL DEFAULT 'N' CHECK (concluido IN ('S', 'N')),
    observacao       CLOB,
    dt_registro      TIMESTAMP NOT NULL,
    CONSTRAINT fk_ck_exec_os   FOREIGN KEY (ordem_servico_id) REFERENCES ordem_servico(id),
    CONSTRAINT fk_ck_exec_item FOREIGN KEY (item_id)          REFERENCES checklist_item(id)
);

-- =============================================================
-- Triggers para atualizar status do ativo
-- =============================================================

CREATE OR REPLACE TRIGGER trg_manutencao_execucao
AFTER UPDATE OF status ON manutencao
FOR EACH ROW
WHEN (NEW.status = 'em_execucao')
BEGIN
    UPDATE ativo SET status = 'em_manutencao' WHERE id = :NEW.ativo_id;
END;
/

CREATE OR REPLACE TRIGGER trg_manutencao_concluida
AFTER UPDATE OF status ON manutencao
FOR EACH ROW
WHEN (NEW.status = 'concluida')
BEGIN
    UPDATE ativo SET status = 'operacional' WHERE id = :NEW.ativo_id;
END;
/

-- =============================================================
-- Dados iniciais para teste
-- =============================================================

INSERT INTO cliente VALUES (seq_cliente.NEXTVAL, 'Altave Monitoramento', 'Brasil', 'alta', 'S');
INSERT INTO cliente VALUES (seq_cliente.NEXTVAL, 'Cliente Asia Ltda', 'Japao', 'media', 'S');

INSERT INTO contrato VALUES (seq_contrato.NEXTVAL, 1, 'CTR-2024-001', 'alta', 30, TO_DATE('2024-01-01', 'YYYY-MM-DD'), TO_DATE('2026-12-31', 'YYYY-MM-DD'));
INSERT INTO contrato VALUES (seq_contrato.NEXTVAL, 2, 'CTR-2024-002', 'media', 60, TO_DATE('2024-06-01', 'YYYY-MM-DD'), TO_DATE('2026-06-30', 'YYYY-MM-DD'));

INSERT INTO certificacao VALUES (seq_certificacao.NEXTVAL, 'NR35', 'Trabalho em altura', 24);
INSERT INTO certificacao VALUES (seq_certificacao.NEXTVAL, 'NR10', 'Seguranca em eletricidade', 12);

INSERT INTO contrato_certificacao VALUES (1, 1, 'S');
INSERT INTO contrato_certificacao VALUES (1, 2, 'S');

INSERT INTO localizacao VALUES (seq_localizacao.NEXTVAL, 'Base Rio de Janeiro', 'Brasil', 'terra', -22.9068, -43.1729);
INSERT INTO localizacao VALUES (seq_localizacao.NEXTVAL, 'Base Toquio', 'Japao', 'terra', 35.6762, 139.6503);
INSERT INTO localizacao VALUES (seq_localizacao.NEXTVAL, 'Embarcacao Alpha', 'Internacional', 'embarcacao', -10.0, -35.0);

INSERT INTO tipo_equipamento VALUES (seq_tipo_equip.NEXTVAL, 'Drone de Monitoramento', 'Altave', 'AX-200');
INSERT INTO tipo_equipamento VALUES (seq_tipo_equip.NEXTVAL, 'Sistema de Comunicacao', 'Altave', 'COM-100');

INSERT INTO regra_manutencao VALUES (seq_regra_manut.NEXTVAL, 1, 'preventiva', 90, 500, 15);
INSERT INTO regra_manutencao VALUES (seq_regra_manut.NEXTVAL, 2, 'preventiva', 180, 1000, 30);

INSERT INTO ativo VALUES (seq_ativo.NEXTVAL, 1, 1, 1, 'SN-AX200-001', TO_DATE('2023-01-15', 'YYYY-MM-DD'), 480, 'operacional');
INSERT INTO ativo VALUES (seq_ativo.NEXTVAL, 1, 1, 1, 'SN-AX200-002', TO_DATE('2023-03-10', 'YYYY-MM-DD'), 320, 'operacional');
INSERT INTO ativo VALUES (seq_ativo.NEXTVAL, 2, 2, 2, 'SN-COM100-001', TO_DATE('2022-07-01', 'YYYY-MM-DD'), 950, 'operacional');

INSERT INTO tecnico VALUES (seq_tecnico.NEXTVAL, 'Carlos Silva', 'carlos.silva@altave.com', 'senior', 'disponivel', 1, 1, -22.9068, -43.1729, NULL, NULL);
INSERT INTO tecnico VALUES (seq_tecnico.NEXTVAL, 'Ana Souza', 'ana.souza@altave.com', 'pleno', 'disponivel', 1, 1, -22.9068, -43.1729, NULL, NULL);
INSERT INTO tecnico VALUES (seq_tecnico.NEXTVAL, 'Pedro Yamamoto', 'pedro.yamamoto@altave.com', 'junior', 'disponivel', 2, 2, 35.6762, 139.6503, NULL, NULL);

INSERT INTO tecnico_certificacao VALUES (1, 1, TO_DATE('2023-01-10', 'YYYY-MM-DD'), TO_DATE('2025-01-10', 'YYYY-MM-DD'), 'DOC-NR35-001');
INSERT INTO tecnico_certificacao VALUES (1, 2, TO_DATE('2023-01-10', 'YYYY-MM-DD'), TO_DATE('2024-12-31', 'YYYY-MM-DD'), 'DOC-NR10-001');
INSERT INTO tecnico_certificacao VALUES (2, 1, TO_DATE('2023-06-15', 'YYYY-MM-DD'), TO_DATE('2025-06-15', 'YYYY-MM-DD'), 'DOC-NR35-002');

INSERT INTO manutencao VALUES (seq_manutencao.NEXTVAL, 1, NULL, 'preventiva', 'pendente', 'alta',
    TO_DATE('2025-04-01', 'YYYY-MM-DD'), TO_DATE('2025-04-15', 'YYYY-MM-DD'), NULL, NULL, 480, NULL, 'S');
INSERT INTO manutencao VALUES (seq_manutencao.NEXTVAL, 3, NULL, 'corretiva', 'pendente', 'media',
    TO_DATE('2025-03-25', 'YYYY-MM-DD'), TO_DATE('2025-03-30', 'YYYY-MM-DD'), NULL, NULL, 950, NULL, 'N');

INSERT INTO checklist_template VALUES (seq_ck_template.NEXTVAL, 1, 'Revisao Trimestral Drone AX-200');
INSERT INTO checklist_item VALUES (seq_ck_item.NEXTVAL, 1, 'Verificar integridade das helices', 'S');
INSERT INTO checklist_item VALUES (seq_ck_item.NEXTVAL, 1, 'Calibrar sensores de altitude', 'S');
INSERT INTO checklist_item VALUES (seq_ck_item.NEXTVAL, 1, 'Testar comunicacao com estacao base', 'S');
INSERT INTO checklist_item VALUES (seq_ck_item.NEXTVAL, 1, 'Limpar lentes da camera', 'N');
INSERT INTO checklist_item VALUES (seq_ck_item.NEXTVAL, 1, 'Registrar horas de uso no log', 'S');

INSERT INTO item_estoque VALUES (seq_item_estoque.NEXTVAL, 'Chave de fenda torque', 'ferramenta', 5);
INSERT INTO item_estoque VALUES (seq_item_estoque.NEXTVAL, 'Capacete com jugular', 'EPI', 10);
INSERT INTO item_estoque VALUES (seq_item_estoque.NEXTVAL, 'Helice reserva AX-200', 'peca', 8);

COMMIT;
