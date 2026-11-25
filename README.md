# 🌳 Arboris - Écosystème de Gestion d'Arbres et d'Événements

Arboris est une plateforme complète comprenant un site web, une API robuste et une application mobile compagnon. L'écosystème est conçu pour faciliter la gestion et le suivi d'arbres, de personnes et d'événements liés.

## 📱 L'Application Mobile (Compagnon)

L'application mobile est conçue comme un outil de saisie rapide ("on-the-go") :

- **Création Rapide** : Ajouter instantanément des personnes, des documents et des événements depuis le terrain.
- **Saisie de Données** : Un compagnon pratique pour alimenter la base de données principale sans avoir à utiliser une interface complexe.
- **Interface Moderne** : Design soigné avec Jetpack Compose pour une navigation fluide et une expérience utilisateur optimisée.

*Note : La visualisation complète des données se fait via la plateforme Web.*

## ✨ Fonctionnalités Clés de l'Écosystème

- **Gestion des Arbres** : Ajout d'arbres avec suivi.
- **Gestion des Personnes** : Gestion de personnes associées aux activités.
- **Calendrier d'Événements** : Création et organisation d'événements spécifiques.
- **Authentification Sécurisée** : Système de connexion pour une expérience utilisateur personnalisée.

## 🛠 Technologies Utilisées (Mobile)

- **Langage** : [Kotlin](https://kotlinlang.org/)
- **UI Framework** : [Jetpack Compose](https://developer.android.com/jetpack/compose) (Déclaratif)
- **Architecture** : MVVM (Model-View-ViewModel)
- **Navigation** : Compose Navigation
- **Réseau** : Retrofit & OkHttp
- **Gestion de Données** : Repository Pattern, DTOs, et gestion d'état par observation.

## 🚀 Installation et Compilation

Pour tester l'application mobile localement, suivez ces étapes :

### Prérequis
- **Android Studio** (dernière version recommandée)
- **Java JDK 17+**
- Un appareil physique avec le débogage USB activé ou un émulateur Android.

### Compilation
1. Clonez le dépôt :
   ```bash
   git clone https://github.com/Mangin-Allan/Arboris_ClientMobile.git
   cd Arboris_ClientMobile
   ```
2. Générez l'APK de production :
   ```bash
   ./gradlew assembleRelease
   ```
   *L'APK sera disponible dans le dossier `app/build/outputs/apk/release/`.*

## 📂 Structure du Projet
Le projet est organisé de manière modulaire pour séparer les responsabilités :
- `core/` : Logique réseau, pagination et stockage de tokens.
- `data/` : Modèles de données (DTO), modèles métier et dépôts (Repositories).
- `ui/` : Écrans, composants réutilisables et thèmes graphiques.
- `Navigation/` : Gestion des flux de navigation de l'application.

## 👥 Auteurs

- **Leloup Malo**
- **Nury Louis**
- **Girot Matis**
- **Ledemazel Dorian**
- **Coudure-tuquet Louis**
- **Halilou Sami**
- **Mangin Allan**
