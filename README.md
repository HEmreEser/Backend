# Kreisel Backend

Ein Spring Boot Backend für ein Kleidungs- und Equipment-Verleihsystem für die Hochschule München.

## 📋 Projektübersicht

Das Kreisel Backend ermöglicht es HM-Studenten, Kleidung und Equipment (Sportausrüstung, Accessoires, etc.) an verschiedenen Campus-Standorten auszuleihen. Das System verwaltet Benutzer, Items und Ausleihvorgänge mit einem umfassenden Filtersystem.

## 🚀 Features

### Authentifizierung
- Registrierung nur mit HM-E-Mail-Adressen (`@hm.edu`)
- Login/Logout-Funktionalität
- Automatische Admin-Rolle für E-Mails die mit "admin" beginnen
- Passwort-Verschlüsselung mit BCrypt

### Item-Management
- **Standortbasierte Filterung**: Pasing, Lothstraße, Karlstraße
- **Kategorien**: Kleidung, Schuhe, Accessoires, Taschen, Equipment
- **Unterkategorien**: Hosen, Jacken, Stiefel, Wanderschuhe, Mützen, etc.
- **Gender-Filter**: Damen, Herren, Unisex
- **Textsuche**: Name, Beschreibung, Marke
- **Größenfilter**: Flexible Größenangaben (L, XL, 120cm, 1.5L, etc.)
- **Verfügbarkeitsstatus**: Automatische Verwaltung bei Ausleihe/Rückgabe

### Rental-System
- **Ausleihlimits**: Maximal 5 aktive Ausleihen pro Benutzer
- **Ausleihzeitraum**: Maximal 60 Tage
- **Verlängerung**: Einmalige Verlängerung um 30 Tage möglich
- **Rückgabeverfolgung**: Automatische Status-Updates
- **Überfällige Ausleihen**: Tracking und Verwaltung

## 🛠 Technologie-Stack

- **Framework**: Spring Boot 3.x
- **Sicherheit**: Spring Security
- **Datenbank**: H2 (In-Memory für Development)
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven/Gradle
- **Java Version**: 17+

## 📁 Projektstruktur

```
src/main/java/edu/hm/cs/kreisel_backend/
├── config/
│   └── SecurityConfig.java          # CORS & Security-Konfiguration
├── controller/
│   ├── AuthController.java          # Login/Registrierung
│   ├── ItemController.java          # Item-Verwaltung & Filterung
│   ├── RentalController.java        # Ausleih-Management
│   └── UserController.java          # Benutzerverwaltung
├── dto/
│   ├── AuthResponse.java            # Login/Register-Response
│   ├── LoginRequest.java            # Login-Daten
│   └── RegisterRequest.java         # Registrierungs-Daten
├── model/
│   ├── Item.java                    # Item-Entität mit Enums
│   ├── Rental.java                  # Ausleih-Entität
│   └── User.java                    # Benutzer-Entität
├── repository/
│   ├── ItemRepository.java          # Item-Datenzugriff
│   ├── RentalRepository.java        # Rental-Datenzugriff
│   └── UserRepository.java          # User-Datenzugriff
├── service/
│   ├── AuthService.java             # Authentifizierungs-Logik
│   ├── ItemService.java             # Item-Geschäftslogik
│   ├── RentalService.java           # Rental-Geschäftslogik
│   └── UserService.java             # User-Geschäftslogik
└── KreiselBackendApplication.java   # Main-Klasse
```

## 🚀 Setup & Installation

### Voraussetzungen
- Java 17+
- Maven oder Gradle

### Installation
1. Repository klonen
2. Dependencies installieren:
   ```bash
   mvn clean install
   ```
3. Anwendung starten:
   ```bash
   mvn spring-boot:run
   ```

Die Anwendung läuft standardmäßig auf `http://localhost:8080`

### H2-Datenbank-Konsole
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leer)

## 📚 API-Endpunkte

### Authentifizierung (`/api/auth`)
- `POST /register` - Benutzer registrieren
- `POST /login` - Benutzer anmelden
- `POST /logout` - Benutzer abmelden

### Items (`/api/items`)
- `GET /` - Items mit Filtern abrufen
  - **Required**: `location` (PASING, LOTHSTRASSE, KARLSTRASSE)
  - **Optional**: `available`, `searchQuery`, `gender`, `category`, `subcategory`, `size`
- `GET /{id}` - Einzelnes Item abrufen
- `POST /` - Neues Item erstellen
- `PUT /{id}` - Item aktualisieren
- `DELETE /{id}` - Item löschen

### Rentals (`/api/rentals`)
- `GET /` - Alle Ausleihen
- `GET /user/{userId}` - Ausleihen eines Benutzers
- `GET /user/{userId}/active` - Aktive Ausleihen
- `GET /user/{userId}/history` - Ausleih-Historie
- `POST /user/{userId}/rent` - Item ausleihen
- `POST /{rentalId}/extend` - Ausleihe verlängern
- `POST /{rentalId}/return` - Item zurückgeben

### Users (`/api/users`)
- `GET /` - Alle Benutzer
- `GET /{id}` - Benutzer nach ID
- `GET /email/{email}` - Benutzer nach E-Mail
- `GET /{id}/rentals` - Ausleihen eines Benutzers
- `POST /` - Neuen Benutzer erstellen
- `PUT /{id}` - Benutzer aktualisieren
- `DELETE /{id}` - Benutzer löschen

## 📝 Beispiel-Requests

### Registrierung
```json
POST /api/auth/register
{
  "fullName": "Max Mustermann",
  "email": "max.mustermann@hm.edu",
  "password": "securePassword123"
}
```

### Item-Filterung
```
GET /api/items?location=LOTHSTRASSE&available=true&gender=DAMEN&category=KLEIDUNG&searchQuery=winter
```

### Item ausleihen
```json
POST /api/rentals/user/1/rent
{
  "item": {"id": 3},
  "endDate": "2025-07-01"
}
```

## 🔒 Sicherheit

- **CORS**: Konfiguriert für alle Origins (Development)
- **CSRF**: Deaktiviert für API-Nutzung
- **Authentication**: Aktuell keine JWT-Tokens (Sessions)
- **Passwörter**: BCrypt-verschlüsselt
- **E-Mail-Validierung**: Nur HM-Domains erlaubt

## 📊 Datenmodell

### Item-Enums
- **Location**: PASING, LOTHSTRASSE, KARLSTRASSE
- **Gender**: DAMEN, HERREN, UNISEX
- **Category**: KLEIDUNG, SCHUHE, ACCESSOIRES, TASCHEN, EQUIPMENT
- **Subcategory**: HOSEN, JACKEN, STIEFEL, WANDERSCHUHE, MUETZEN, HANDSCHUHE, etc.
- **Zustand**: NEU, GEBRAUCHT

### Geschäftsregeln
- Maximal 5 aktive Ausleihen pro Benutzer
- Ausleihzeitraum: 1-60 Tage
- Verlängerung: Einmalig um 30 Tage
- Automatische Verfügbarkeitsverwaltung

## 🧪 Testdaten

Das System wird mit folgenden Testdaten initialisiert:
- **Admin**: admin@hm.edu (Passwort: admin123)
- **User**: ben@hm.edu (Passwort: benpass)
- **Items**: Winterjacke, Skihose, Snowboard, Flasche, Handschuhe
- **Sample Rental**: Ben hat das Snowboard ausgeliehen

## 🚧 Bekannte Limitierungen

- Keine JWT-basierte Authentifizierung
- In-Memory-Datenbank (Daten gehen bei Neustart verloren)
- Keine Datei-Uploads für Item-Bilder
- Einfache Textsuche (keine Volltextsuche)
- Keine E-Mail-Benachrichtigungen

## 📈 Mögliche Erweiterungen

- JWT-Authentication
- Persistent Database (PostgreSQL/MySQL)
- Image Upload für Items
- E-Mail-Benachrichtigungen
- Push-Notifications
- Elasticsearch für bessere Suche
- API-Rate-Limiting
- Logging & Monitoring
