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

Graphes de statistiques de nombre de memoire et thèses en fonction de l'année

Graphes de statistiques de nombre de memoire et thèses en fonction des ufr pour une année choisi

Systemes de logs pour des traçes

Listes des memoires, thèsesavec une possibilité de recherche incrémentale en fonction des données possédées

Listes de tous les utilisateurs et des utilisateurs connectés

Génération d'attestation automatique en finction du memoire 



Technologies Utilisées

-Backend : Spring Boot, Spring Security, JPA/Hibernate, Token

-Frontend : Thymeleaf, Bootstrap, css, scss, js, ajax

-Base de données : MySQL

-Outils : Git, GitHub, Maven, Vscode

-Environnement : Intellij


Installation et Exécution

Prérequis : 

-JDK 17+

-Maven

-MySQL

-Un IDE comme IntelliJ IDEA ou VS Code

Instructions

-Cloner le répertoire : git clone https://github.com/Aisstou03/memos.git

-Se déplacer dans le dossier de projet :cd Gestion_Memoire_These

-Configurer la base de données dans application.properties avec le nom de la base de donnée "gestion"

-Compiler et exécuter le projet : mvn spring-boot:run

-Accéder à l'application via http://localhost:8080


Défis Rencontrés :

Gestion des relations entre entités complexes

Mise en place de Spring Security pour les rôles

Dynamisation de recherche incrémentale

Implémentation de la fonction de modification des informations de l'étudiant et encadrant pour un memoire



Succès et Améliorations Futures :

Fonctionnalité de changement de police pour les nom scientifique par exemple

Ajout d'encadrants en cliquant sur le bouton + via Thymeleaf

Possibilité d'améliorer l'interface utilisateur et d'ajouter une API REST

autocomplétion pour la recherche



Contribution

Les contributions sont les bienvenues 



Licence

Tous droits réservés à l'université Assance Seck de Ziguinchor.
