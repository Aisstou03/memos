Gestion des Mémoires et Thèses de l'université Assane Seck de Ziguinchor

Description du Projet

Ce projet est une application web permettant de faciliter la gestion des mémoires et thèses au sein de l'UASZ. Il offre des fonctionnalités du GRUB pour la gestion des memoires, thèses et des utilisateurs telles que l'ajout, modification, recherche et  suppression.

Fonctionnalités Principales

Gestion des mémoires et thèses (ajout, modification, suppression)

Assignation de roles en fonction des utilisateurs

Système de corbeille pour les mémoires et thèses supprimés pour pour restauration ou la suppression définitive

Authentification et gestion des rôles avec Spring Security

Interface utilisateur en fonction des roles avec Thymeleaf

Systèmes de notification des modification apportées

Systeme de messagerie pour une fluidité de communication entre utilisateurs

Téléchargement de memoire ou thèse en fonction d'une tranche de periode (année), type, ufr, département et filière (ces champs sont optionels)

Option de filtrage de l'affichage par ufr, departement et filiere ou par ecole doctorale

Statistique des nombres de type de memoires, thèses et utilisateurs en ligne

Graphes de statistiques de nombre de memoire et thèses en fonction des ufr

Systemes de logs

Listes des memoires, thèsesavec une possibilité de recherche incrémentale en fonction des données possédées

Listes de tous les utilisateurs et des utilisateurs connectés


Technologies Utilisées

Backend : Spring Boot, Spring Security, JPA/Hibernate

Frontend : Thymeleaf, Bootstrap

Base de données : PostgreSQL/MySQL

Outils : Git, GitHub, Maven

Environnement : Ubuntu 22.10

Installation et Exécution

Prérequis

JDK 17+

Maven

PostgreSQL/MySQL

Un IDE comme IntelliJ IDEA ou VS Code

Instructions

Cloner le répertoire :

git clone https://github.com/votre-utilisateur/nom-du-projet.git
cd nom-du-projet

Configurer la base de données dans application.properties

Compiler et exécuter le projet :

mvn spring-boot:run

Accéder à l'application via http://localhost:8080

Défis Rencontrés

Gestion des relations entre entités complexes

Mise en place de Spring Security pour les rôles

Dynamisation de l'ajout d'encadrants avec Thymeleaf

Implémentation de la corbeille pour la suppression de mémoires

Succès et Améliorations Futures

Fonctionnalité de corbeille fonctionnelle

Ajout d'encadrants dynamiquement via Thymeleaf

Possibilité d'améliorer l'interface utilisateur et d'ajouter une API REST

Intégration d'un système de notifications

Contribution

Les contributions sont les bienvenues !

Forker le projet

Créer une branche (feature/amélioration)

Soumettre une pull request

Licence

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus d'informations.
