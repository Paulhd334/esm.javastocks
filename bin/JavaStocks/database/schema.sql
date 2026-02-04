-- Script de création de la base de données Java Stocks
-- Version 3.0 - Avec réapprovisionnement et réservations en attente

-- Table des fournisseurs
CREATE TABLE IF NOT EXISTS fournisseur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    rue VARCHAR(200) NOT NULL,
    code_postal VARCHAR(10) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    email VARCHAR(100)
);

-- Table des points de livraison
CREATE TABLE IF NOT EXISTS point_livraison (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    rue VARCHAR(200) NOT NULL,
    code_postal VARCHAR(10) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    email VARCHAR(100)
);

-- Table des demandes de réapprovisionnement
CREATE TABLE IF NOT EXISTS demande_reappro (
    id SERIAL PRIMARY KEY,
    date_demande DATE NOT NULL,
    motif VARCHAR(2) NOT NULL, -- R, NP, UR
    fournisseur_id INTEGER REFERENCES fournisseur(id),
    point_livraison_id INTEGER REFERENCES point_livraison(id)
);

-- Table des lignes de demande de réapprovisionnement
CREATE TABLE IF NOT EXISTS ligne_demande (
    id SERIAL PRIMARY KEY,
    demande_id INTEGER REFERENCES demande_reappro(id),
    article_id INTEGER REFERENCES article(id),
    quantite INTEGER NOT NULL
);

-- Table des réservations en attente
CREATE TABLE IF NOT EXISTS reservation_en_attente (
    id SERIAL PRIMARY KEY,
    date_reservation DATE NOT NULL,
    coureur_id INTEGER REFERENCES coureur(id),
    type_epreuve_id INTEGER REFERENCES type_epreuve(id),
    date_creation DATE NOT NULL,
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE'
);

-- Table des lignes de réservation en attente
CREATE TABLE IF NOT EXISTS ligne_reservation_attente (
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER REFERENCES reservation_en_attente(id),
    article_id INTEGER REFERENCES article(id),
    quantite INTEGER NOT NULL
);

-- Ajouter la colonne seuil_reappro à la table article
ALTER TABLE article ADD COLUMN IF NOT EXISTS seuil_reappro INTEGER DEFAULT 50;