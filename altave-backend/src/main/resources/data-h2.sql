-- Script de inicialização de dados para H2 Database
-- Este arquivo é executado automaticamente quando o profile 'h2' é ativado

INSERT INTO usuario (nome_completo, email, status, data_nascimento, cargo, funcao)
VALUES ('João Silva', 'joao.silva@email.com', 'ATIVO', '1995-03-10', 'Técnico', 'Manutenção');

INSERT INTO usuario (nome_completo, email, status, data_nascimento, cargo, funcao)
VALUES ('Maria Oliveira', 'maria.oliveira@email.com', 'ATIVO', '1990-07-21', 'Supervisor', 'Gestão');

INSERT INTO usuario (nome_completo, email, status, data_nascimento, cargo, funcao)
VALUES ('Carlos Souza', 'carlos.souza@email.com', 'INATIVO', '1988-11-05', 'Analista', 'Planejamento');

-- Contratos com dados realistas
INSERT INTO contrato (nome_empresa, quantidade_plantas, data_inicio, data_fim, quantidade_ativos, descricao, criador, quantidade_supervisoes, status)
VALUES ('TechCorp Brasil', 3, '2024-01-01', '2026-12-31', 15, 'Contrato de manutenção preventiva de equipamentos industriais', 'Maria Oliveira', 2, 'ATIVO');

INSERT INTO contrato (nome_empresa, quantidade_plantas, data_inicio, data_fim, quantidade_ativos, descricao, criador, quantidade_supervisoes, status)
VALUES ('Industrial Solutions', 2, '2023-06-01', '2025-06-01', 8, 'Contrato de inspeção técnica e consultoria', 'João Silva', 1, 'INATIVO');

INSERT INTO contrato (nome_empresa, quantidade_plantas, data_inicio, data_fim, quantidade_ativos, descricao, criador, quantidade_supervisoes, status)
VALUES ('Manufacturing Plus', 5, '2024-06-15', '2025-02-14', 25, 'Contrato expirando em breve - requer renovação', 'Maria Oliveira', 3, 'ATIVO');

INSERT INTO contrato (nome_empresa, quantidade_plantas, data_inicio, data_fim, quantidade_ativos, descricao, criador, quantidade_supervisoes, status)
VALUES ('AutomationNow', 4, '2024-11-01', '2027-10-31', 20, 'Contrato de automação e controle industrial', 'João Silva', 2, 'ATIVO');

-- Relacionamento Contrato-Usuário
INSERT INTO contrato_usuario (id_contrato, id_usuario) VALUES (1, 1);
INSERT INTO contrato_usuario (id_contrato, id_usuario) VALUES (1, 2);
INSERT INTO contrato_usuario (id_contrato, id_usuario) VALUES (2, 3);
INSERT INTO contrato_usuario (id_contrato, id_usuario) VALUES (3, 2);
INSERT INTO contrato_usuario (id_contrato, id_usuario) VALUES (4, 1);

-- Ativos
INSERT INTO ativo (status, fabricante, tipo, periodicidade_manutencao, descricao, data_instalacao, predio, planta, id_contrato)
VALUES ('OPERACIONAL', 'Intelbras', 'CAMERA', 30, 'Câmera de segurança HD - entrada principal', '2024-02-10', 'Prédio A', 'Planta 1', 1);

INSERT INTO ativo (status, fabricante, tipo, periodicidade_manutencao, descricao, data_instalacao, predio, planta, id_contrato)
VALUES ('OPERACIONAL', 'Schneider', 'BOMBA', 15, 'Bomba de circulação do sistema de resfriamento', '2024-01-15', 'Prédio B', 'Planta 2', 1);

INSERT INTO ativo (status, fabricante, tipo, periodicidade_manutencao, descricao, data_instalacao, predio, planta, id_contrato)
VALUES ('EM_MANUTENCAO', 'ABB', 'TRANSFORMADOR', 60, 'Transformador de entrada principal', '2023-12-01', 'Prédio A', 'Planta 1', 2);

INSERT INTO ativo (status, fabricante, tipo, periodicidade_manutencao, descricao, data_instalacao, predio, planta, id_contrato)
VALUES ('OPERACIONAL', 'Atlas Copco', 'COMPRESSOR', 45, 'Compressor de ar de alta pressão', '2024-03-05', 'Prédio C', 'Planta 3', 1);

INSERT INTO ativo (status, fabricante, tipo, periodicidade_manutencao, descricao, data_instalacao, predio, planta, id_contrato)
VALUES ('OPERACIONAL', 'Schneider', 'QUADRO_ELETRICO', 30, 'Quadro de distribuição elétrica principal', '2024-01-10', 'Prédio A', 'Planta 1', 3);

-- Ordens de manutenção (com planta, predio e requisito)
INSERT INTO ordem_manutencao (nome_ordem, data_inicio, data_fim, tipo_manutencao, id_ativo, descricao, status, planta, predio, requisito)
VALUES ('PM mensal — Câmera P-63', '2025-04-10', '2025-04-15', 'PREVENTIVA', 1, 'Inspeção preventiva mensal da câmera', 'PENDENTE', 'Planta 1', 'Prédio A', 'NR12');

INSERT INTO ordem_manutencao (nome_ordem, data_inicio, data_fim, tipo_manutencao, id_ativo, descricao, status, planta, predio)
VALUES ('Troca de bateria no-break', '2025-04-12', '2025-04-13', 'CORRETIVA', 2, 'Substituição de bateria vencida', 'EM_ANDAMENTO', 'Planta 2', 'Prédio B');

INSERT INTO ordem_manutencao (nome_ordem, data_inicio, data_fim, tipo_manutencao, id_ativo, descricao, status, planta, predio)
VALUES ('Manutenção transformador', '2025-04-02', '2025-04-05', 'CORRETIVA', 3, 'Correção de defeito elétrico', 'CONCLUIDA', 'Planta 1', 'Prédio A');

INSERT INTO ordem_manutencao (nome_ordem, data_inicio, data_fim, tipo_manutencao, id_ativo, descricao, status, planta, predio)
VALUES ('Revisão compressor de ar', '2025-04-14', '2025-04-16', 'PREVENTIVA', 4, 'Manutenção preventiva trimestral', 'PENDENTE', 'Planta 3', 'Prédio C');

INSERT INTO ordem_manutencao (nome_ordem, data_inicio, data_fim, tipo_manutencao, id_ativo, descricao, status, planta, predio, requisito)
VALUES ('Inspeção quadro elétrico', '2025-04-16', '2025-04-18', 'PREVENTIVA', 5, 'Verificação geral do quadro', 'PENDENTE', 'Planta 1', 'Prédio A', 'NR10');

-- Relacionamento Ordem-Usuário
INSERT INTO ordem_usuario (id_ordem_manutencao, id_usuario) VALUES (1, 1);
INSERT INTO ordem_usuario (id_ordem_manutencao, id_usuario) VALUES (2, 1);
INSERT INTO ordem_usuario (id_ordem_manutencao, id_usuario) VALUES (3, 2);
INSERT INTO ordem_usuario (id_ordem_manutencao, id_usuario) VALUES (4, 1);
