# Kreisel Backend

Dies ist das Backend eines Equipment-Verleihsystems (z.B. für Hochschulen oder Vereine). Es stellt eine REST-API für die Verwaltung von Nutzern, Ausleihen, Bewertungen, Standorten, Kategorien und Equipment bereit.

## Features

- **User Management**
  - Registrierung und Login für Nutzer, inkl. Admin-Erkennung
  - Abruf von Profilinformationen
- **Equipment Management**
  - Equipment anlegen, auflisten und nach Verfügbarkeit/Kategorie/Standort filtern
- **Rental Management**
  - Ausleihen und Rückgabe von Equipment
  - Eigene und alle Ausleihen einsehen (für Admins)
  - Verlängerung von Ausleihen
- **Bewertungen**
  - Equipment bewerten und Kommentare hinterlassen
  - Bewertungen zu Equipment und von Nutzern anzeigen
- **Kategorien & Standorte**
  - Kategorien und Standorte verwalten
- **Security**
  - Passwort-Hashing, Rollenverwaltung
  - (Hinweis: Aktuell Dummy-Token, JWT kann integriert werden)
- **Fehlerbehandlung**
  - Einheitliche Validierungs- und Fehlerantworten

## Technologie-Stack

- Java, Spring Boot
- Spring Data JPA (Hibernate)
- H2 (In-Memory-DB, konfigurierbar)
- Maven

## Setup & Installation

1. Repository klonen:
   ```bash
   git clone https://github.com/HEmreEser/Backend.git
   ```
2. In das Projektverzeichnis wechseln und starten:
   ```bash
   cd Backend
   ./mvnw spring-boot:run
   ```
3. Die API ist standardmäßig erreichbar unter: `http://localhost:8080`

## API-Übersicht (Auszug)

### Auth
- `POST /api/auth/register` – Registrierung
- `POST /api/auth/login` – Login

### Nutzer
- `GET /api/users/me` – Eigene Profilinfos

### Equipment
- `GET /api/equipment` – Verfügbare Geräte
- `GET /api/equipment/all` – Alle Geräte
- `POST /api/equipment` – Gerät anlegen
- `GET /api/equipment/category/{id}` – Geräte nach Kategorie
- `GET /api/equipment/location/{id}` – Geräte nach Standort
- `GET /api/equipment/filter?...` – Flexibles Filtern

### Ausleihen
- `POST /api/rentals` – Ausleihe anlegen
- `GET /api/rentals/user` – Eigene Ausleihen
- `GET /api/rentals/all` – Alle Ausleihen (Admin)
- `POST /api/rentals/{rentalId}/return` – Rückgabe

### Bewertungen
- `POST /api/ratings` – Bewertung abgeben/aktualisieren
- `GET /api/ratings/equipment/{id}` – Bewertungen zu Equipment

### Kategorien & Standorte
- `GET /api/categories` – Alle Kategorien
- `POST /api/categories` – Kategorie anlegen
- `GET /api/locations` – Alle Standorte
- `POST /api/locations` – Standort anlegen

## Hinweise

- Die Authentifizierung ist derzeit noch vereinfacht (Dummy-Token). Für produktive Nutzung JWT oder OAuth2 integrieren!
- Für Entwicklung/Testing wird eine In-Memory-Datenbank genutzt.
- Für mehr Details siehe die Controller-Klassen im Source-Code.

---

**Viel Spaß beim Entwickeln!**
