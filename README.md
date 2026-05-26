# Projet-SOA-ECommerce

## Prérequis

Avant de lancer le projet, assurez-vous d'avoir installé :
- Python
- Java JDK 17 ou supérieur
- Maven

## Procédure de lancement

Pour faire fonctionner Turtle Shop, vous devez ouvrir 3 terminaux différents afin de lancer chaque microservice en parallèle.

1. Terminal 1 : Le Service Inventaire (Java)
Naviguez dans le dossier du service d'inventaire et lancez le serveur Spring Boot : //
`cd service-inventaire //
./mvnw spring-boot:run`//
Le service sera disponible sur : `http://localhost:8080/api/inventory/catalog`

2. Terminal 2 : Le Service Paiement (Python)
Naviguez dans le dossier du service de paiement et lancez l'application :
`cd service-paiement
py app.py`
Le service simulé écoutera sur le port `5000`

3. Terminal 3 : L'Orchestrateur (Python)
Naviguez dans le dossier de l'orchestrateur, installez les dépendances requises, puis lancez le serveur web :
`cd orchestrateur
pip install -r requirements.txt
py app_web.py`
L'interface web de la boutique sera accessible sur : `http://localhost:3000`

## Scénarios de Test à valider

1. **Chargement initial :** Accédez à `http://localhost:3000`. L'interface lit le fichier `stock.json` via l'orchestrateur pour afficher dynamiquement les 9 produits avec leurs icônes respectives et leurs stocks.

2. **Recherche en direct :** Utilisez le menu déroulant. Il interroge la structure de données pour afficher le stock précis du produit sélectionné.

3. **Achat réussi :** Cliquez sur "Acheter" sur un produit disponible (ex: Casque Audio). Les logs de la console affichent la réussite de la réservation Java et de la transaction Python. Le stock diminue de 1 visuellement et dans le fichier `stock.json`.

4. **Gestion de la rupture (Bouton Rouge) :** Achetez un produit jusqu'à épuisement de son stock. Au dernier clic, le bouton bleu se transforme instantanément en bouton rouge "Épuisé" et devient incliquable.

5. **Persistance :** Éteignez le serveur Java, rallumez-le. Les stocks reprennent exactement là où vous les aviez laissés grâce à la sauvegarde en direct dans `stock.json`.







