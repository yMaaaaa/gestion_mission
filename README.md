# GestionMission

Application de bureau Java Swing pour gérer des missions, des agents et leur participation aux missions. Le projet utilise MySQL pour stocker les données et est organisé comme un projet Java Ant créé avec NetBeans.

## Fonctionnalités

- Créer, consulter et modifier des missions.
- Ajouter et consulter des agents. Dans la liste des agents, un double-clic ouvre la fenêtre de modification ou de suppression.
- Affecter un agent actif à une mission, puis consulter les participations. Un double-clic sur une participation permet de modifier son rôle et sa date ou de la supprimer.
- Naviguer entre les fonctions depuis le menu général.

## Prérequis

- JDK 26 ou version ultérieure compatible avec la source Java 26 du projet.
- MySQL Server démarré localement, par défaut sur le port `3306`.
- MySQL Connector/J sur le classpath du projet.
- NetBeans avec le support des projets Java Ant, ou Apache Ant pour compiler en ligne de commande.

## Installation

1. Récupérez le dépôt et ouvrez le dossier du projet — celui qui contient `build.xml` et `nbproject/project.xml` — dans NetBeans.
2. Dans les propriétés du projet, sélectionnez un JDK 26 ou supérieur.
3. Configurez MySQL Connector/J :
   - Le projet référence par défaut `mysql-connector-j-9.4.0.jar` sur un lecteur réseau de l’établissement.
   - Si ce chemin n’est pas accessible, installez/téléchargez le pilote Connector/J et ajoutez son fichier `.jar` au classpath du projet dans **Propriétés du projet > Libraries**.
   - Vous pouvez aussi définir la propriété locale `file.reference.mysql-connector-j-9.4.0.jar` dans `nbproject/private/private.properties` pour pointer vers votre fichier `.jar`. Ne versionnez pas un chemin propre à votre ordinateur.
4. Créez la base et les tables avec le script ci-dessous, ou adaptez votre schéma existant aux colonnes attendues par les DAO.

## Configuration de la base de données

La connexion est définie dans `src/gestionmission/Connexion/ConnexionBDD.java` :

- URL : `jdbc:mysql://localhost:3306/gestion_missions`
- Utilisateur par défaut : `root`
- Mot de passe par défaut : vide

Ces valeurs correspondent à une configuration locale de développement. Si votre serveur utilise un autre hôte, port, utilisateur ou mot de passe, modifiez les constantes de connexion dans `ConnexionBDD.java`. N’enregistrez pas de véritable mot de passe dans le dépôt.

### Schéma MySQL attendu

Les DAO utilisent les tables `mission`, `agent` et `participation`. Pour une nouvelle base, ce schéma est compatible avec les requêtes du projet :

```sql
CREATE DATABASE IF NOT EXISTS gestion_missions
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gestion_missions;

CREATE TABLE IF NOT EXISTS mission (
    id INT NOT NULL AUTO_INCREMENT,
    titre VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    statut VARCHAR(20) NOT NULL,
    priorite VARCHAR(20) NOT NULL,
    budget DECIMAL(12, 2) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS agent (
    id_agent INT NOT NULL AUTO_INCREMENT,
    pseudo VARCHAR(100) NOT NULL,
    nom VARCHAR(150) NOT NULL,
    specialite VARCHAR(150) NOT NULL,
    niveau INT NOT NULL,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_agent)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS participation (
    id_agent INT NOT NULL,
    id_mission INT NOT NULL,
    `role` VARCHAR(150) NOT NULL,
    date_affectation DATE NOT NULL,
    PRIMARY KEY (id_agent, id_mission),
    CONSTRAINT fk_participation_agent
        FOREIGN KEY (id_agent) REFERENCES agent (id_agent)
        ON DELETE CASCADE,
    CONSTRAINT fk_participation_mission
        FOREIGN KEY (id_mission) REFERENCES mission (id)
        ON DELETE CASCADE
) ENGINE=InnoDB;
```

Les valeurs de statut acceptées sont `A_PREPARER`, `EN_COURS`, `TERMINEE` et `ANNULEE`. Les valeurs de priorité sont `BASSE`, `NORMALE`, `HAUTE` et `CRITIQUE`. Elles doivent être enregistrées exactement ainsi dans la base.

La clé primaire composite de `participation` empêche d’affecter deux fois le même agent à la même mission. Les clés étrangères en cascade permettent de supprimer un agent ou une mission avec ses participations associées.

### Jeu de données de test

Après la création du schéma, exécutez ce script sur des tables vides pour ajouter cinq agents, cinq missions et cinq participations. Les identifiants sont précisés afin que les participations référencent les bonnes lignes. Ne relancez pas le script tel quel : les identifiants existent déjà après la première exécution.

```sql
USE gestion_missions;

INSERT INTO agent (id_agent, pseudo, nom, specialite, niveau, actif) VALUES
    (1, 'amartin', 'Anne Martin', 'Infrastructure', 3, TRUE),
    (2, 'mbernard', 'Maxime Bernard', 'Securite', 4, TRUE),
    (3, 'ldupont', 'Lea Dupont', 'Developpement', 2, TRUE),
    (4, 'srobert', 'Sami Robert', 'Reseaux', 3, TRUE),
    (5, 'npetit', 'Noah Petit', 'Support', 1, FALSE);

INSERT INTO mission
    (id, titre, description, date_debut, date_fin, statut, priorite, budget)
VALUES
    (1, 'Audit du systeme informatique', 'Analyse du parc et des services informatiques.', '2026-01-05', '2026-02-20', 'TERMINEE', 'HAUTE', 4500.00),
    (2, 'Migration des postes', 'Migration des postes de travail vers un nouvel environnement.', '2026-11-20', '2026-12-15', 'A_PREPARER', 'NORMALE', 8500.00),
    (3, 'Deploiement reseau', 'Mise a niveau du reseau des locaux.', '2026-11-01', '2026-12-01', 'EN_COURS', 'CRITIQUE', 12000.00),
    (4, 'Mise a jour du site web', 'Refonte de plusieurs pages du site web.', '2026-10-01', '2026-10-30', 'TERMINEE', 'BASSE', 3200.00),
    (5, 'Formation a la securite', 'Sensibilisation des equipes aux bonnes pratiques de securite.', '2026-12-01', '2026-12-20', 'A_PREPARER', 'HAUTE', 5600.00);

INSERT INTO participation (id_agent, id_mission, `role`, date_affectation) VALUES
    (1, 1, 'Cheffe de projet', '2026-01-05'),
    (2, 1, 'Analyste securite', '2026-01-07'),
    (3, 4, 'Developpeuse', '2026-10-01'),
    (4, 3, 'Responsable reseau', '2026-11-01'),
    (1, 2, 'Coordinatrice', '2026-11-16');
```

Le cinquième agent est inactif pour vérifier qu’il n’est pas proposé dans le formulaire d’affectation. Les autres agents permettent de tester les affectations.

## Lancement

### Avec NetBeans

1. Vérifiez que MySQL est démarré, que la base `gestion_missions` existe et que le pilote JDBC est configuré.
2. Faites **Clean and Build** si le projet vient d’être récupéré ou si une classe compilée manque.
3. Cliquez sur **Run**. La classe principale configurée est `gestionmission.IHM.MenuGeneral`.

### En ligne de commande

Depuis la racine du projet, après installation d’Apache Ant et configuration du pilote JDBC :

```text
ant clean
ant run
```

Les opérations qui lisent ou modifient les données nécessitent une connexion MySQL valide. `gestionmission.GestionMission` est une ancienne classe de test console : elle n’est pas le point d’entrée de l’interface et ajoute une mission de test lorsqu’elle est exécutée.

## Dépannage

- **`ClassNotFoundException: gestionmission.IHM.MenuGeneral`** : lancez **Clean and Build** dans NetBeans, puis réessayez. Vérifiez aussi que `MenuGeneral.java` se trouve sous `src/gestionmission/IHM`.
- **Erreur de connexion ou `Communications link failure`** : vérifiez que le service MySQL fonctionne, que l’hôte et le port correspondent à l’URL, et que la base existe.
- **Pilote JDBC introuvable ou `No suitable driver`** : vérifiez que le `.jar` de MySQL Connector/J est présent dans les bibliothèques de compilation et d’exécution.
- **Erreur SQL sur une table ou une colonne** : vérifiez que les tables et colonnes correspondent au schéma ci-dessus.
