# Mini RPG Top-Down (LibGDX Java)

Projet LibGDX full-code (modules `core`, `desktop`, `android`) pour un mini RPG 2D top-down avec pêche, culture, inventaire et sauvegarde JSON. Aucun éditeur de scène nécessaire : toute la map, la logique et l'UI sont codées.

## Stack
- Java 11
- LibGDX 1.12.0
- Gradle multi-modules (`core`, `desktop`, `android`)

## Arborescence
- `core/src/com/myrpg/game/` : logique de jeu
  - `MyRpgGame.java` : bootstrap LibGDX
  - `GameScreen.java` : écran principal, HUD, contrôles tactiles, inventaire
  - `Player.java`, `Entity.java` : entités et animation
  - `MapManager.java` : génération et rendu d'une map 40x40, collisions
  - `FishingSystem.java` : pêche contextuelle au bord de l'eau
  - `FarmingSystem.java` : plantation, croissance (3 stades), récolte
  - `Inventory.java`, `InventoryItem.java` : gestion d'items
  - `Assets.java` : chargement des textures
  - `SaveManager.java` : sauvegarde/chargement JSON (position, inventaire, cultures)
- `desktop/` : launcher PC (`DesktopLauncher.java`)
- `android/` : launcher Android (`AndroidLauncher.java`)
- `core/assets/` : placez ici les PNG indiqués ci-dessous

## Assets (à fournir)
Placez les fichiers suivants dans `core/assets/` (16x16 ou 32x32 pixel art, libres de droits). Vous pouvez utiliser n'importe quels PNG respectant ces noms :
- `player.atlas` + PNG associés : atlas LibGDX avec régions `down`, `up`, `left`, `right` (4 frames chacun). Vous pouvez générer l'atlas avec TexturePacker (JSON ou fichier .atlas classique).
- `tiles/grass.png`
- `tiles/dirt.png`
- `tiles/water.png`
- `tiles/tree.png`
- `tiles/soil.png` (terre labourée)
- `tiles/crop_seed.png` (stade planté)
- `tiles/crop_grow.png` (stade croissance)
- `tiles/crop_ready.png` (stade récolte)
- `items/fish_common.png`
- `items/fish_rare.png`
- `items/carrot.png`
- `items/seeds.png`
- `ui/inventory_bg.png` (un simple panneau 300x300 ou plus)

## Commandes
### Desktop (test rapide)
```bash
./gradlew :desktop:run
```

### APK Android
```bash
./gradlew :android:assembleDebug
# APK généré dans android/build/outputs/apk/debug/
```

## Gameplay & commandes
- Déplacement : flèches clavier (desktop) ou pavé virtuel à l'écran.
- Bouton Action : pêche si le joueur touche l'eau, sinon plante/récolte sur une case de champ.
- Inventaire : bouton "Inv" (affiche liste et icônes).
- Sauvegarde : automatique lors de la mise en pause (Android) / fermeture (desktop via pause appelé par LibGDX).

## Notes design
- Map 40x40 inclut une zone d'eau pour la pêche, une zone de champ (tiles `soil`) et des arbres/bordures collisionnables.
- Pêche : lancer + délai 2s, tirage aléatoire (poisson commun/rare ou rien). Messages contextuels affichés en haut à gauche.
- Culture : 3 stades (planté, croissance, prêt). Croissance toutes les 5 secondes de jeu. Récolte ajoute une carotte à l'inventaire.
- Inventaire : liste simple, quantité par item, avec icône.
