#L'application IRISI DELICE
<img src="https://i.ibb.co/LYwkjN3/logo.png" alt="logo" width="100%" border="0"/>

Ce projet:
* Encadré par: Mr RAKRAK SAID
* Réalisé par: BENMANSOUR MOHAMMED

## Description
Il s'agit d'une application JAVA utilisant la technologie RMI pour la mise en oeuvre des objets distribués, pour gerer les commandes des clients à distance.
Il assure les fonctionnalités suivantes:

###l'authentification des utilisateur
Les utilisateurs de l'application sont:
* Les clients: il peut accéder a son espace, ou bien il peut créer un compte ainsi que modifier son mot de passe en cas d'oublie
* L'administrateur

### Partie client

####

### Partie administrateur
en utilisant le `fichier script.sql` vous pouvez utilisé le compte suivant pour se connecter à l'espace d'admin:
* Nom d'utilisateur: AdminIRISI          
* Mot de passe: AdminIRISI2020
#### statisitique des commandes
le nombre des commande par plat et le nombre des commandes par categorie en utilisant JavaFX - Charts

#### Ajout des plats
En saisissant le nom du plat, son prix unitaire, et en sélectionnant sa categorie ainsi qu'une image descriptive.

####Recherche des plats
la recherche est faite avec plusieurs critères:
* nom du plat
* categorie du plat
* prix minimal et prix maximal

####Modification des informations des plats
en redonnant d'autres valeurs aux attributs cités dans la partie ajout des plats.

####Gestion des catégories
   Ajout, modification du nom, et suppression de la catégorie.

#### Traitement des commandes en attente
l'administrateur peut choisir une commande non livrée, voir les elements de la commande(plats et quantités) et modifié son statut: les statuts définit pour une commande sont:
* En attente : au moment de la création de la commande par le client.
* En préparation : la commande est en cours de traitement.
* EN livraison : la commande a été préparé est prêt à être livrée.
* Livrée : la commande est livrée au client.(lorsqu'une commande a ce statut, le client reçoit une facture sous forme pdf dans sa boite email, et la commande n'est plus visible dans la liste des commandes en attente).


### Partie administrateur
en utilisant le `fichier script.sql` vous pouvez utilisé le compte suivant pour se connecter à l'espace client:
* Email: benmansour1997@gmail.com         
* Mot de passe: ClientIRISI2020
NB: Pour que vous puissiez recevoir les email de la part de l'application, veuillez créer un nouveau compte avec une adresse email accessible par vous.

#### Menu du restaurant
Pour lancé une commande, il suffit de :
* Consulter le menu, choisir la quantié voulue pour chaque plat, (la valeur par defaut est 0).
* Modifier l'adresse de réception de la commande (la valeur par defaut est votre adresse saisit au moment de la création du compte).
* Confirmer la commande.


#### Liste des commandes du client
Ici, le client peut consulter ses commande, pour voir les element de la commandes et le statut de la commande.

# Architecture
<img src="https://i.ibb.co/X5XTP1t/Annotation-2020-06-21-083709.png" alt="Annotation-2020-06-21-083709" width="100%" border="0"/>

* le package `bean` represente les classe utilisées pour communiquer avec la base de données.
* le package `controller` represente controlleurs de l'application, il est composé de deux sous repertoires, la première contient les controlleurs de la partie admin, et la deuxième celles du client.
* le package `helper` contient des classes qui s'adapte avec le besoin des vues.
le package `report` contient le fichier `.jasper` et `.jrxml` representant le squelette de la facture.
le package `service` contient les interfaces et l'implementtation pour chaque partie (client et admin)
le package `util` contient des classes utilitaires . ex: Connexion avec la BD, envoi d'email, Hashage du mot de passe, generation du pdf.
le package `view` les differentes interfaces graphiques de l'application.

# Utilisation
Vous devez:
 * créer une base de donnée nommée : `cmdrestaurant` dans le SGBD `MySQL`.
 * importer le fichier `script.sql`.
 * Lancer le serveur principal `MainServer.java` pour enregistrer les objet distant dans RMI.
 * Lancer la partie que vous voulez utilisée :
    * partie client: `MainClient.java`
    * partie administrateur: `MainAdmin.java`