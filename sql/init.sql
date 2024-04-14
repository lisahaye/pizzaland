DROP TABLE IF EXISTS panier, commandes, recettes, ingredients, pizzas, pates, tokens;

-- PATES

CREATE TABLE pates(
    dno SERIAL PRIMARY KEY,
    d_nom VARCHAR(20) UNIQUE NOT NULL
);

-- INGREDIENTS

CREATE TABLE ingredients(
    ino SERIAL PRIMARY KEY,
    i_nom VARCHAR(20) UNIQUE NOT NULL,
    i_prix FLOAT  NOT NULL
);

-- PIZZAS

CREATE TABLE pizzas(
    pno SERIAL PRIMARY KEY,
    p_nom VARCHAR(20) UNIQUE NOT NULL,
    dno INT DEFAULT 1,
    p_prix FLOAT NOT NULL,

    FOREIGN KEY (dno) references pates(dno) ON DELETE CASCADE
);

-- PIZZAS / INGREDIENTS
CREATE TABLE recettes(
    pno INT,
    ino INT,

    PRIMARY KEY (pno, ino),
    FOREIGN KEY (pno) references pizzas(pno) ON DELETE CASCADE,
    FOREIGN KEY (ino) references ingredients(ino) ON DELETE CASCADE
);

-- COMMANDES
CREATE TABLE commandes (
    cno SERIAL PRIMARY KEY,
    c_name VARCHAR(255),
    c_orderDate DATE DEFAULT NOW()
);

-- COMMANDES / PIZZA
CREATE TABLE panier (
    cno INT,
    pno INT,
    PRIMARY KEY (cno, pno),
    FOREIGN KEY (cno) REFERENCES commandes(cno) ON DELETE CASCADE,
    FOREIGN KEY (pno) REFERENCES pizzas(pno) ON DELETE CASCADE
);


CREATE TABLE tokens (
    user_name VARCHAR(25),
    password VARCHAR(25),

    PRIMARY KEY (user_name, password)
);

INSERT INTO pates (d_nom) VALUES 
('Classique'),
('Fine'),
('Épaisse'),
('Blé entier'),
('Sans gluten');

INSERT INTO ingredients(i_nom, i_prix) VALUES
('Sauce tomate', 1.50),
('Mozzarella', 2.00),
('Jambon', 2.50),
('Champignons', 1.75),
('Olives', 1.25),
('Cœurs d''artichaut', 2.00),
('Ricotta', 2.50),
('Poivrons', 1.50),
('Oignons', 1.25),
('Aubergine', 2.00),
('Ananas', 1.50),
('Pepperoni', 2.50),
('Anchois', 2.00),
('Câpres', 1.50),
('Origan', 1.00),
('Fromage de chèvre', 3.00),
('Miel', 2.00),
('Gorgonzola', 2.50),
('Chèvre', 2.50),
('Parmesan', 2.00);

INSERT INTO pizzas(p_nom, dno, p_prix) VALUES 
('Margherita', 1, 8.99),        
('Quattro Stagioni', 2, 10.99), 
('Calzone', 3, 9.99),           
('Végétarienne', 4, 11.99),     
('Sans Gluten', 5, 12.99),      
('Pepperoni', 1, 9.49),         
('Napolitaine', 2, 11.49),      
('Hawaïenne', 3, 10.99),        
('Fromage de chèvre', 4, 12.99),
('Quatre fromages', 5, 13.49);


INSERT INTO recettes(pno, ino) VALUES 
-- Margherita
(1, 1), -- Sauce tomate
(1, 2), -- Mozzarella
-- Quattro Stagioni
(2, 1), -- Sauce tomate
(2, 2), -- Mozzarella
(2, 3), -- Jambon
(2, 4), -- Champignons
(2, 5), -- Olives
(2, 6), -- Cœurs d'artichaut
-- Calzone 
(3, 1), -- Sauce tomate
(3, 2), -- Mozzarella
(3, 7), -- Ricotta
(3, 3), -- Jambon
-- Végétarienne
(4, 1), -- Sauce tomate
(4, 2), -- Mozzarella
(4, 4), -- Champignons
(4, 8), -- Poivrons
(4, 9), -- Oignons
(4, 5), -- Olives
(4, 10), -- Aubergine
-- Sans Gluten
(5, 1), -- Sauce tomate
(5, 2), -- Mozzarella
(5, 3), -- Jambon
(5, 4), -- Champignons
(5, 5), -- Olives
(5, 6), -- Cœurs d'artichaut
(5, 11), -- Ananas
-- Pepperoni
(6, 1), -- Sauce tomate
(6, 2), -- Mozzarella
(6, 12), -- Pepperoni
-- Napolitaine
(7, 1), -- Sauce tomate
(7, 2), -- Mozzarella
(7, 13), -- Anchois
(7, 5), -- Olives
(7, 14), -- Câpres
(7, 15), -- Origan
-- Hawaïenne
(8, 1), -- Sauce tomate
(8, 2), -- Mozzarella
(8, 3), -- Jambon
(8, 11), -- Ananas
-- Fromage de chèvre
(9, 1), -- Sauce tomate
(9, 2), -- Mozzarella
(9, 16), -- Fromage de chèvre
(9, 17), -- Miel
-- Quatre fromages 
(10, 1), -- Sauce tomate
(10, 2), -- Mozzarella
(10, 18), -- Gorgonzola
(10, 19), -- Chèvre
(10, 20); -- Parmesan

INSERT INTO commandes (c_name, c_orderDate) 
VALUES
    ('Commande 1', '2023-06-01'),
    ('Commande 2', '2023-06-02'),
    ('Commande 3', '2023-06-03'),
    ('Commande 4', '2023-06-04'),
    ('Commande 5', '2023-06-05'),
    ('Commande 6', '2023-06-06'),
    ('Commande 7', '2023-06-07'),
    ('Commande 8', '2023-06-08');

INSERT INTO panier (cno, pno) VALUES 
    -- Commande 1
    (1, 1),
    (1, 2),
    -- Commande 2
    (2, 2),
    -- Commande 3
    (3, 3),
    -- Commande 4
    (4, 4),
    -- Commande 5
    (5, 5),
    -- Commande 6
    (6, 6),
    -- Commande 7
    (7, 7),
    -- Commande 8
    (8, 8);

INSERT INTO tokens (user_name, password) VALUES 
('admin', 'admin');

