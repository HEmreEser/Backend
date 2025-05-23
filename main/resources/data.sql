-- 👤 Beispiel-User
INSERT INTO app_user (id, name, email, password) VALUES
                                                 (1, 'Anna Admin', 'admin@hm.edu', 'admin123'),
                                                 (2, 'Ben Benutzer', 'ben@hm.edu', 'benpass');

-- 🎽 Beispiel-Items (mit korrekten Locations: PASING, LOTHSTRASSE, KARLSTRASSE)
INSERT INTO app_item (id, name, size, available, description, brand, location, gender, category, subcategory, zustand)
VALUES
    (1, 'Winterjacke', 'L', TRUE, 'Warme Winterjacke für Damen', 'North Face', 'LOTHSTRASSE', 'DAMEN', 'KLEIDUNG', 'JACKEN', 'Neu'),
    (2, 'Skihose', 'M', TRUE, 'Wasserdicht und bequem', 'Burton', 'LOTHSTRASSE', 'HERREN', 'KLEIDUNG', 'HOSEN', 'Gebraucht'),
    (3, 'Snowboard', '120cm', FALSE, 'Perfekt für Anfänger', 'Nitro', 'KARLSTRASSE', 'HERREN', 'EQUIPMENT', 'Snowboards', 'Gebraucht'),
    (4, 'Flasche', '1.5L', TRUE, 'BPA-frei', 'Nalgene', 'PASING', 'DAMEN', 'EQUIPMENT', 'Flaschen', 'Neu'),
    (5, 'Handschuhe', 'S', TRUE, 'Winddicht', 'Reusch', 'LOTHSTRASSE', 'DAMEN', 'ACCESSOIRES', 'HANDSCHUHE', 'Neu');

-- 📦 Beispiel-Rental (Ben leiht das Snowboard)
INSERT INTO app_rental (id, start_date, end_date, return_date, user_id, item_id)
VALUES (1, CURRENT_DATE, DATEADD('MONTH', 2, CURRENT_DATE), NULL, 2, 3);
