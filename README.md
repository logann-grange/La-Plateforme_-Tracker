# projet-tracker

Application de gestion scolaire développée en **JavaFX + Maven**, avec persistance PostgreSQL.

---

## Fonctionnalités

- **Élèves** : ajout, modification, suppression, tri et filtrage
- **Notes** : saisie et consultation par élève
- **Authentification** : connexion et inscription utilisateur
- **Import / Export** : formats CSV, XML et JSON
- **Tests** : tests unitaires avec rapport de couverture JaCoCo

---

## Prérequis

| Outil | Version minimale |
|---|---|
| JDK | 26 |
| Maven | 3.9 |
| PostgreSQL | en cours d'exécution en local |

La base de données doit s'appeler `tracker`.

---

## Installation des dépendances

Dépendances utilisées :

- `org.openjfx:javafx-controls` / `javafx-fxml`
- `org.postgresql:postgresql`
- `org.springframework.security:spring-security-crypto`
- `commons-logging:commons-logging`


---

## Configuration de la base de données

Les paramètres de connexion sont définis dans deux fichiers :

- `src/main/java/Model/DatabaseConnection.java`
- `src/main/java/creation_table.java`

Avant le premier lancement, vérifier :

- L'URL PostgreSQL
- Le nom d'utilisateur et le mot de passe
- Les droits suffisants pour créer tables et bases

### Créer la base et les tables

```powershell
.\mvnw.cmd exec:java
```

Ce script crée automatiquement (si absents) :

- la base `tracker`
- les tables `student`, `grade` et `users`

---

## Lancer l'application

```powershell
.\mvnw.cmd javafx:run
```

Ou avec Maven système :

```powershell
mvn javafx:run
```

---



## Structure du projet

```
src/
├── main/java/
│   ├── MainApp.java              # Point d'entrée JavaFX
│   ├── creation_table.java       # Initialisation de la base
│   ├── Controller/               # Contrôleurs MVC
│   ├── Model/                    # Accès aux données et logique métier
│   └── Vue/                      # Vues JavaFX et composants
```

---

## Commandes récapitulatives

```powershell
.\mvnw.cmd clean compile     # Compiler
.\mvnw.cmd javafx:run        # Démarrer l'application
```
