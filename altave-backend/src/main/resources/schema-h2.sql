-- Schema para H2 Database
-- Este arquivo é executado automaticamente quando o profile 'h2' é ativado

CREATE TABLE IF NOT EXISTS usuario(
    usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_nascimento DATE NOT NULL,
    cargo VARCHAR(35) NOT NULL,
    funcao VARCHAR(35) NOT NULL
);

CREATE TABLE IF NOT EXISTS contrato (
    id_contrato INT AUTO_INCREMENT PRIMARY KEY,
    nome_empresa VARCHAR(100) NOT NULL,
    quantidade_plantas INT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    quantidade_ativos INT,
    descricao VARCHAR(255),
    criador VARCHAR(100) NOT NULL,
    quantidade_supervisoes INT NOT NULL,
    status VARCHAR(35) NOT NULL
);

CREATE TABLE IF NOT EXISTS contrato_usuario (
    id_contrato INT NOT NULL,
    id_usuario INT NOT NULL,
    PRIMARY KEY (id_contrato, id_usuario),
    FOREIGN KEY (id_contrato) REFERENCES contrato(id_contrato),
    FOREIGN KEY (id_usuario) REFERENCES usuario(usuario_id)
);

CREATE TABLE IF NOT EXISTS ativo (
    id_ativo INT AUTO_INCREMENT PRIMARY KEY,
    nome_ativo VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    fabricante VARCHAR(100),
    tipo VARCHAR(50) NOT NULL,
    periodicidade_manutencao INT NOT NULL,
    descricao VARCHAR(500),
    data_instalacao DATE NOT NULL,
    data_remocao DATE,
    predio VARCHAR(100),
    planta VARCHAR(100),
    id_contrato INT NOT NULL,
    FOREIGN KEY (id_contrato) REFERENCES contrato(id_contrato)
);

CREATE TABLE IF NOT EXISTS ordem_manutencao (
    id_ordem INT AUTO_INCREMENT PRIMARY KEY,
    nome_ordem VARCHAR(100) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE,
    tipo_manutencao VARCHAR(20) NOT NULL,
    id_ativo INT NOT NULL,
    descricao VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    comentario VARCHAR(500),
    FOREIGN KEY (id_ativo) REFERENCES ativo(id_ativo)
);

CREATE TABLE IF NOT EXISTS ordem_usuario (
    id_ordem_manutencao INT NOT NULL,
    id_usuario INT NOT NULL,
    PRIMARY KEY (id_ordem_manutencao, id_usuario),
    FOREIGN KEY (id_ordem_manutencao) REFERENCES ordem_manutencao(id_ordem),
    FOREIGN KEY (id_usuario) REFERENCES usuario(usuario_id)
);
