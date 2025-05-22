-- Tabellen erstellen (mit ENUM-Typen für PostgreSQL)
CREATE TYPE item_size AS ENUM ('XS', 'S', 'M', 'L', 'XL');
CREATE TYPE item_gender AS ENUM ('Damen', 'Herren');
CREATE TYPE item_condition AS ENUM ('Neu', 'Gebraucht');
CREATE TYPE item_status AS ENUM ('Verfugbar', 'NichtVerfugbar');
CREATE TYPE item_location AS ENUM ('Lothstraße', 'Pasing', 'Karlstraße');

-- Categories table
CREATE TABLE categories (
                            id UUID PRIMARY KEY,
                            name VARCHAR(255) NOT NULL
);

-- Subcategories table
CREATE TABLE subcategories (
                               id UUID PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               category_id UUID,
                               FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Users table
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(10) NOT NULL CHECK (role IN ('ADMIN', 'USER'))
);

-- Items table mit ENUMs
CREATE TABLE items (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description VARCHAR(255),
                       brand VARCHAR(255),
                       available_from DATE,
                       image_url VARCHAR(512),
                       size item_size,
                       gender item_gender,
                       condition item_condition,
                       status item_status,
                       location item_location,
                       category_id UUID,
                       subcategory_id UUID,
                       FOREIGN KEY (category_id) REFERENCES categories(id),
                       FOREIGN KEY (subcategory_id) REFERENCES subcategories(id)
);

-- Rentals table
CREATE TABLE rentals (
                         id UUID PRIMARY KEY,
                         user_id UUID,
                         item_id UUID,
                         rental_date DATE NOT NULL,
                         return_date DATE,
                         FOREIGN KEY (user_id) REFERENCES users(id),
                         FOREIGN KEY (item_id) REFERENCES items(id)
);

-- Insert categories
INSERT INTO categories (id, name) VALUES
                                      ('11111111-1111-1111-1111-111111111111', 'Kleidung'),
                                      ('22222222-2222-2222-2222-222222222222', 'Schuhe'),
                                      ('33333333-3333-3333-3333-333333333333', 'Equipment'),
                                      ('44444444-4444-4444-4444-444444444444', 'Taschen'),
                                      ('55555555-5555-5555-5555-555555555555', 'Accesoires');

-- Insert subcategories
INSERT INTO subcategories (id, name, category_id) VALUES
                                                      -- Kleidung
                                                      ('11111111-1111-1111-1111-111111111111', 'Jacken', '11111111-1111-1111-1111-111111111111'),
                                                      ('22222222-2222-2222-2222-222222222222', 'Hosen', '11111111-1111-1111-1111-111111111111'),

                                                      -- Schuhe
                                                      ('33333333-3333-3333-3333-333333333333', 'Wanderschuhe', '22222222-2222-2222-2222-222222222222'),
                                                      ('44444444-4444-4444-4444-444444444444', 'Stiefel', '22222222-2222-2222-2222-222222222222'),

                                                      -- Equipment
                                                      ('55555555-5555-5555-5555-555555555555', 'Ski', '33333333-3333-3333-3333-333333333333'),
                                                      ('66666666-6666-6666-6666-666666666666', 'Snowboard', '33333333-3333-3333-3333-333333333333'),
                                                      ('77777777-7777-7777-7777-777777777777', 'Helme', '33333333-3333-3333-3333-333333333333'),

                                                      -- Accesoires
                                                      ('88888888-8888-8888-8888-888888888888', 'Schals', '55555555-5555-5555-5555-555555555555'),
                                                      ('99999999-9999-9999-9999-999999999999', 'Mützen', '55555555-5555-5555-5555-555555555555'),
                                                      ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Handschuhe', '55555555-5555-5555-5555-555555555555'),
                                                      ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Brillen', '55555555-5555-5555-5555-555555555555');

-- Insert users
INSERT INTO users (id, email, password, role) VALUES
                                                  ('aaaaaaaa-1111-1111-1111-111111111111', 'admin@example.com', 'adminpass', 'ADMIN'),
                                                  ('bbbbbbbb-2222-2222-2222-222222222222', 'user@example.com', 'userpass', 'USER'),
                                                  ('cccccccc-3333-3333-3333-333333333333', 'max.mustermann@example.com', 'max123', 'USER'),
                                                  ('dddddddd-4444-4444-4444-444444444444', 'lisa.schmidt@example.com', 'lisa456', 'USER');

-- Insert items mit ENUM-Werten
INSERT INTO items (
    id, name, description, brand, available_from, image_url, size, gender, condition, status, location,
    category_id, subcategory_id
) VALUES
      -- Kleidung - Jacken
      ('11111111-1111-1111-1111-111111111111', 'Winterjacke', 'Warme Jacke für den Winter', 'North Face', '2025-05-17', 'https://example.com/winterjacke.jpg', 'L', 'Herren', 'Gebraucht', 'Verfugbar', 'Lothstraße', '11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111'),
      ('22222222-2222-2222-2222-222222222222', 'Regenjacke', 'Wasserdichte Jacke für alle Wetterlagen', 'Jack Wolfskin', '2025-05-20', 'https://example.com/regenjacke.jpg', 'M', 'Herren', 'Neu', 'Verfugbar', 'Pasing', '11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111'),

      -- Kleidung - Hosen
      ('33333333-3333-3333-3333-333333333333', 'Skihose', 'Wasserdichte Hose für den Skiausflug', 'Salomon', '2025-05-10', 'https://example.com/skihose.jpg', 'XL', 'Herren', 'Neu', 'Verfugbar', 'Karlstraße', '11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222'),
      ('44444444-4444-4444-4444-444444444444', 'Wanderhose', 'Leichte, atmungsaktive Hose', 'Fjällräven', '2025-05-18', 'https://example.com/wanderhose.jpg', 'M', 'Damen', 'Gebraucht', 'Verfugbar', 'Lothstraße', '11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222'),

      -- Schuhe - Wanderschuhe
      ('55555555-5555-5555-5555-555555555555', 'Trekking-Schuhe', 'Robuste Schuhe für lange Wanderungen', 'Meindl', '2025-05-12', 'https://example.com/trekkingschuhe.jpg', 'L', 'Herren', 'Neu', 'Verfugbar', 'Pasing', '22222222-2222-2222-2222-222222222222', '33333333-3333-3333-3333-333333333333'),
      ('66666666-6666-6666-6666-666666666666', 'Bergstiefel', 'Hochwertige Bergstiefel für anspruchsvolle Touren', 'Lowa', '2025-05-19', 'https://example.com/bergstiefel.jpg', 'S', 'Damen', 'Gebraucht', 'Verfugbar', 'Karlstraße', '22222222-2222-2222-2222-222222222222', '33333333-3333-3333-3333-333333333333'),

      -- Schuhe - Stiefel
      ('77777777-7777-7777-7777-777777777777', 'Winterstiefel', 'Warme Stiefel für kalte Tage', 'Timberland', '2025-05-14', 'https://example.com/winterstiefel.jpg', 'XL', 'Herren', 'Neu', 'Verfugbar', 'Lothstraße', '22222222-2222-2222-2222-222222222222', '44444444-4444-4444-4444-444444444444'),

      -- Equipment - Ski
      ('88888888-8888-8888-8888-888888888888', 'Alpinski', 'Hochwertige Alpinski für alle Pisten', 'Atomic', '2025-05-16', 'https://example.com/alpinski.jpg', 'M', 'Herren', 'Neu', 'Verfugbar', 'Pasing', '33333333-3333-3333-3333-333333333333', '55555555-5555-5555-5555-555555555555'),

      -- Equipment - Helme
      ('99999999-9999-9999-9999-999999999999', 'Skihelm', 'Leichter Helm mit Belüftung', 'Uvex', '2025-05-17', 'https://example.com/skihelm.jpg', 'M', 'Damen', 'Gebraucht', 'NichtVerfugbar', 'Karlstraße', '33333333-3333-3333-3333-333333333333', '77777777-7777-7777-7777-777777777777'),

      -- Taschen
      ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Rucksack', 'Großer Rucksack für Wanderungen', 'Deuter', '2025-05-15', 'https://example.com/rucksack.jpg', 'L', 'Herren', 'Neu', 'Verfugbar', 'Lothstraße', '44444444-4444-4444-4444-444444444444', NULL),

      -- Accesoires - Mützen
      ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Strickmütze', 'Warme Mütze für kalte Tage', 'The North Face', '2025-05-14', 'https://example.com/strickmuetze.jpg', 'S', 'Damen', 'Gebraucht', 'Verfugbar', 'Pasing', '55555555-5555-5555-5555-555555555555', '99999999-9999-9999-9999-999999999999');

-- Insert rentals
INSERT INTO rentals (id, user_id, item_id, rental_date, return_date) VALUES
                                                                         ('11111111-1111-1111-1111-111111111111', 'bbbbbbbb-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', '2025-05-18', NULL),
                                                                         ('22222222-2222-2222-2222-222222222222', 'cccccccc-3333-3333-3333-333333333333', '55555555-5555-5555-5555-555555555555', '2025-05-15', '2025-05-20'),
                                                                         ('33333333-3333-3333-3333-333333333333', 'dddddddd-4444-4444-4444-444444444444', '88888888-8888-8888-8888-888888888888', '2025-05-17', NULL);