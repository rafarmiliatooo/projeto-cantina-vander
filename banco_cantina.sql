CREATE DATABASE cantina_escolar_vanders;
USE cantina_escolar_vanders;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    status_perfil INT NOT NULL, -- 1: Funcionário, 2: Aluno, 3: Responsável
    turma VARCHAR(20) DEFAULT NULL,           -- NOVO: Ex: '6º Ano A', '1º EM B' (Apenas para Alunos)
    saldo DECIMAL(10,2) DEFAULT 0.00,        -- Saldo/Crédito atual
    limite_fiado DECIMAL(10,2) DEFAULT 250.00, -- Teto do fiado (máx R$ 250,00)
    limite_diario DECIMAL(10,2) DEFAULT NULL   -- Limite diário configurado pelo pai
);

-- 2. VÍNCULO (Responsável x Aluno)
CREATE TABLE aluno_responsavel (
    id_responsavel INT NOT NULL,
    id_aluno INT NOT NULL,
    PRIMARY KEY (id_responsavel, id_aluno),
    FOREIGN KEY (id_responsavel) REFERENCES usuarios(id),
    FOREIGN KEY (id_aluno) REFERENCES usuarios(id)
);

-- 3. CARDÁPIO / PRODUTOS
CREATE TABLE produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(50) DEFAULT 'Lanche',
    esgotado BOOLEAN DEFAULT FALSE -- Ativa/desativa produto no app do aluno
);

-- 4. PEDIDOS ANTECIPADOS (Fluxo Aluno -> Cozinha)
CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_aluno INT NOT NULL,
    codigo_retirada VARCHAR(10) NOT NULL,    -- QR Code / Código gerado na tela final
    intervalo VARCHAR(10) NOT NULL,          -- "09:00" ou "15:30"
    data_retirada DATE NOT NULL,             -- Data do intervalo
    valor_total DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'pendente',   -- pendente, em_preparacao, entregue, cancelado
    alergia VARCHAR(255) DEFAULT NULL,       -- Campo de observação de restrição alimentar
    criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_aluno) REFERENCES usuarios(id)
);

-- 5. ITENS DO PEDIDO ANTECIPADO
CREATE TABLE itens_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_produto INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id),
    FOREIGN KEY (id_produto) REFERENCES produtos(id)
);

-- 6. VENDAS DIRETAS NO BALCÃO (Poucos toques na fila)
CREATE TABLE vendas_balcao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_aluno INT DEFAULT NULL,              -- Opcional (NULL se pagar na hora sem se identificar)
    id_funcionario INT NOT NULL,            -- Quem atendeu
    tipo_pagamento VARCHAR(20) NOT NULL,    -- 'conta_aluno', 'dinheiro', 'pix', 'cartao'
    valor_total DECIMAL(10,2) NOT NULL,
    data_venda DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_aluno) REFERENCES usuarios(id),
    FOREIGN KEY (id_funcionario) REFERENCES usuarios(id)
);

-- 7. ITENS DA VENDA NO BALCÃO
CREATE TABLE itens_venda_balcao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_venda INT NOT NULL,
    id_produto INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venda) REFERENCES vendas_balcao(id),
    FOREIGN KEY (id_produto) REFERENCES produtos(id)
);
drop table usuarios;
drop table aluno_responsavel;
drop table produtos;
drop table pedidos;
drop table itens_pedido;
drop table vendas_balcao;
drop table itens_venda_balcao; 