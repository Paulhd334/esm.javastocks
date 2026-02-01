-- Script de création de la base de données JavaStocks
--  a Exécuter dans PostgreSQL

-- 1. Créer la base de données
CREATE DATABASE javastocks
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

-- 2. Se connecter à la base
\c javastocks;

-- 3. Créer les tables (le code Java les créera automatiquement si elles n'existent pas)

-- Pour insérer des données de test manuellement :
INSERT INTO coureur (nom, prenom) VALUES
('Berthollo', 'Pierre'),
('Brillat', 'Savarin'),
('Dupont', 'Jean'),
('Martin', 'Sophie');

INSERT INTO type_epreuve (libelle) VALUES
('Course folle junior'),
('Marathon de Paris'),
('Trail des montagnes'),
('10 km urbain');

INSERT INTO article (libelle, categorie, quantite, taille, couleur) VALUES
('T-shirt sport', 'T', 50, 'L', 'Bleu'),
('Short running', 'T', 30, 'M', 'Noir'),
('Bouteille 50cl', 'B', 200, NULL, NULL),
('Bouteille 1L', 'B', 150, NULL, NULL),
('Barre énergétique 50g', 'DS', 300, NULL, NULL),
('Gel énergétique', 'DS', 250, NULL, NULL);