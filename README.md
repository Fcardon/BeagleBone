Mini-projet BeagleBone
===
Le projet a pour but de piloter un pointeur laser avec deux servomoteurs grâce à une BeagleBone. La BeagleBone devra fonctionner avec un noyau Linux personnalisé généré avec Yocto.

# Carte Utilisée
La carte utilisée pour le projet est une BeagleBone white. Elle possède un cortex A8. Pour plus d'informations sur cette carte RDV ici: [BeagleBone white](http://beagleboard.org/bone-original).

<div align="center"><img src="/Resources README/beaglebone.jpg"/></div>

# Noyau Linux personnalisé
Partie non réalisée. La BeagleBone fonctionne actuellement sur une Debian 7.9.

# Interface Homme-Machine
## Présentation de l'IHM
L'interface homme-machine est programmée en Java avec l'IDE "Eclipse". Elle utilise la librairie "RXTX 2.2pre2" (pour Linux) pour dialoguer sur le port série relié à la BeagleBone.
<div align="center"><img src="/Resources README/IHM1.png" width="500"/></div>
Elle permet d'afficher la position du laser ainsi que son état (allumé ou éteint). Il y a même un rendu graphique de ces informations sur la droite de l'IHM. Un déplacement d'une graduation de ce graphique correspond à 10° pour le servomoteur et l'état éteint du laser est représenté par un cercle rouge tandis qu'un point rouge est utilisé pour indiquer que le laser est allumé.
## Dépendances
La librarie "RXTX 2.2pre2" disponible sur le ![Git](/rxtx-2.2pre2-bins) ou par ce [site](http://rxtx.qbang.org/wiki/index.php/Download).

## Fonctionnement
La première chose à faire est de se connecter à l'UART de la BeagleBone. Pour cela il suffit d'aller dans le menu "File" et cliquez sur "Connection". Il suffit maintenant de sélectionner le port associé à l'UART de la BeagleBone. 

Une fois connecté, l'IHM va attendre de recevoir des données de la part de la BeagleBone et afficheront les données reçues.

### Mode "Auto"
Ce mode éxecute des formes en boucle pré-programmées dans la BeagleBone.
Formes réalisées dans l'ordre:
- Carré (2 fois)
- Losange (3 fois)
- Cercle (3 fois)
- Infini (3 fois)

### Mode "Manuel"
Ce mode permet de piloter le pointeur laser avec son clavier.
Touches de commandes:
- 8: Haut
- 2: Bas
- 4: Gauche
- 6: Droite
- 5: Revient au centre
- ENTER: Allume ou éteint le laser

On peut envoyer l'ordre de dessiner une forme pré-programmée en la choisissant dans la liste en bas à gauche et cliquer sur "Start".

Si l'on choisit "Perso" dans la liste, cela nous permet de dessiner des formes personnalisées. Il suffit de dessiner avec sa souris sur le graphique la forme souhaitée. Clic gauche pour dessiner avec le laser allumé et clic droit avec le laser éteint. Comme indiqué ci-dessous.
<div align="center"><img src="/Resources README/IHM2.png" width="500"/></div>
Cliquez sur "Start" et le tour est joué !

Pour effacer les points, changez de mode ou appuyez sur le bouton "Reset".
### Mode "Wii"
Ce mode permet de piloter le pointeur laser avec une manette de Wii "Nunchuk".
<div align="center"><img src="/Resources README/manette-nunchuk.jpg" width="400"/></div>
Par défaut le pilotage du pointeur laser se fait par le joystick. Si on appuie sur le bouton "C" le pilotage se fait par l'accéléromètre de la manette. Pour allumer le laser il suffit d'appuyer sur le bouton "Z".