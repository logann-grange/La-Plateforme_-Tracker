# projet-tracker

Application JavaFX + Maven pour gerer des eleves, leurs notes et les comptes utilisateurs avec PostgreSQL.

## Fonctionnalites

- Gestion des eleves: ajout, modification, suppression, tri et filtrage.
- Gestion des notes: ajout et affichage des notes d'un eleve.
- Authentification: connexion et inscription utilisateur.
- Export et import des eleves au format CSV, XML et JSON.
- Tests unitaires et rapport de couverture JaCoCo.


## Dependances a installer

Les dependances sont gerees par Maven dans [pom.xml](pom.xml). Tu n'as pas besoin de les installer a la main si Maven est disponible, mais voici la liste utilisee par le projet:

- `org.openjfx:javafx`
- `org.postgresql:postgresql`
- `org.springframework.security:spring-security-crypto`
- `commons-logging:commons-logging`
- `org.junit.jupiter:junit-jupiter-api`
- `org.junit.jupiter:junit-jupiter-engine`
- `org.jacoco:jacoco-maven-plugin`


## Arborescence utile

- `src/main/java/MainApp.java` : point d'entree de l'interface.
- `src/main/java/creation_table.java` : creation de la base et des tables.
# projet-tracker

Application JavaFX + Maven pour gerer des eleves, leurs notes et les comptes utilisateurs avec PostgreSQL.

## Fonctionnalites

- Gestion des eleves: ajout, modification, suppression, tri et filtrage.
- Gestion des notes: ajout et affichage des notes d'un eleve.
- Authentification: connexion et inscription utilisateur.
- Export et import des eleves au format CSV, XML et JSON.
- Tests unitaires et rapport de couverture JaCoCo.

## Dependances a installer

Les dependances sont gerees par Maven dans [pom.xml](pom.xml). Tu n'as rien a installer a la main si Maven est dispo, mais voici ce que le projet utilise:

- `org.openjfx:javafx-controls`
- `org.openjfx:javafx-fxml`
- `org.postgresql:postgresql`
- `org.springframework.security:spring-security-crypto`
- `commons-logging:commons-logging`
- `org.junit.jupiter:junit-jupiter-api`
- `org.junit.jupiter:junit-jupiter-engine`
- `org.jacoco:jacoco-maven-plugin`

Pour telecharger automatiquement les dependances:

```powershell
.\mvnw.cmd clean test
```

## Arborescence utile

- `src/main/java/MainApp.java` : point d'entree de l'interface.
- `src/main/java/creation_table.java` : creation de la base et des tables.
- `src/main/java/Controller/` : controleurs MVC.
- `src/main/java/Model/` : acces aux donnees et logique metier.
- `src/main/java/Vue/` : vues JavaFX et composants reutilisables.
- `src/test/java/` : tests unitaires.

## Prerequis

- JDK 26
- Maven 3.9 ou plus
- PostgreSQL lance localement
- Une base PostgreSQL nommee `tracker`

## Configuration base de donnees

Les parametres de connexion sont actuellement definis dans le code:

- `src/main/java/Model/DatabaseConnection.java`
- `src/main/java/creation_table.java`

Points a verifier avant lancement:

- URL PostgreSQL correcte
- utilisateur PostgreSQL valide
- mot de passe PostgreSQL valide
- droits suffisants pour creer la base et les tables

## Creation de la base et des tables

Lance le script de creation une fois la base PostgreSQL disponible:

```powershell
.\mvnw.cmd exec:java
```

Ce script cree si besoin:

- la base `tracker`
- la table `student`
- la table `grade`
- la table `users`

## Lancer l'application JavaFX

Sous Windows PowerShell:

```powershell
.\mvnw.cmd javafx:run
```

Avec Maven systeme:

```powershell
mvn javafx:run
```

## Lancer les tests

```powershell
.\mvnw.cmd test
```

Ou avec Maven systeme:

```powershell
mvn test
```

## Couverture JaCoCo

Le rapport HTML de couverture est genere ici:

- `target/site/jacoco/index.html`

Commande utilisee pour generer le rapport dans ce projet:

```powershell
mvn -Dmaven.compiler.release=21 clean test jacoco:report
```

Pourquoi: la version de bytecode Java 26 peut poser probleme avec la version actuelle de JaCoCo.

## Creer un exe Windows

Pour obtenir un executable Windows avec les dependances integrees, la methode la plus simple est:

1. Compiler le projet.
2. Copier les dependances Maven dans un dossier de distribution.
3. Utiliser `jpackage` pour creer l'installeur `.exe`.

Commandes PowerShell:

```powershell
.\mvnw.cmd clean package dependency:copy-dependencies
```

Ensuite, cree un dossier comme `target\dist` et copie dedans:

- `target\projet-tracker-1.0-SNAPSHOT.jar`
- tous les JARs presents dans `target\dependency\`

Puis lance `jpackage`:

```powershell
jpackage `
	--type exe `
	--name projet-tracker `
	--input target\dist `
	--main-jar projet-tracker-1.0-SNAPSHOT.jar `
	--main-class MainApp `
	--win-menu `
	--win-shortcut
```

Si tu veux seulement un dossier portable, remplace `--type exe` par `--type app-image`.

Notes:

- `jpackage` fait partie du JDK, il faut donc l'executer avec un JDK qui contient cet outil.
- PostgreSQL n'est pas embarque: l'application se connecte a une base locale ou distante existante.
- Si tu veux un vrai installateur avec icone et raccourci, on peut ensuite faire un script PowerShell dedie.

## Commandes utiles

```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd test
.\mvnw.cmd javafx:run
```

## Notes

- Les tests unitaires couvrent surtout les modeles et quelques vues JavaFX.
- Les tests qui touchent directement la base utilisent la base locale `tracker`.
- Pour une vraie isolation de test, il faudra idealement une base de test dediee.
