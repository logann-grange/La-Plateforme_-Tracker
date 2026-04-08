# projet-tracker

Projet Java (Maven) pour creer automatiquement la base PostgreSQL `tracker` et les tables:
- `student`
- `note`
- `users`

## Prerequis

- Java 17 ou plus
- PostgreSQL lance localement
- Un utilisateur PostgreSQL avec les droits de creation de base

## Structure Maven

Le code Java principal est dans:
- `src/main/java/creation_table.java`

## Commandes Maven

### Avec Maven installe globalement

```bash
mvn clean compile
mvn exec:java
```

### Avec Maven Wrapper (recommande)

Sous Windows (PowerShell):

```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd exec:java
```

Sous Linux/macOS:

```bash
./mvnw clean compile
./mvnw exec:java
```

## Lancer l'interface JavaFX

Sous Windows (PowerShell):

```powershell
.\mvnw.cmd javafx:run
```

Sous Linux/macOS:

```bash
./mvnw javafx:run
```

## Configuration base de donnees

Les parametres de connexion sont dans `creation_table.java`:
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `ADMIN_DB_URL`

Adapte ces valeurs a ton environnement PostgreSQL avant execution.
