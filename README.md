# SoundWave 🎵

SoundWave is an Android music streaming app built with **Kotlin** and **Jetpack Compose**. It provides a modern UI for browsing, searching, and playing songs, backed by a REST API.

## Features

- 🔐 **Authentication** — Login screen backed by a token-based auth flow
- 🏠 **Home** — Browse songs on the home feed
- 🔍 **Search** — Search for songs by title/artist
- 📚 **Library** — View your saved/library songs
- 👤 **Profile** — User profile screen
- 🎨 Built entirely with Jetpack Compose and Material 3

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Navigation:** Navigation Compose
- **Networking:** Retrofit + OkHttp (with logging interceptor)
- **Serialization:** Gson
- **Async:** Kotlin Coroutines
- **Local Storage:** Jetpack DataStore (Preferences)
- **Image Loading:** Coil
- **Architecture:** MVVM (ViewModel + UI State)

## Project Structure

```
app/src/main/java/com/ashvinprajapati/soundwave/
├── data/
│   ├── local/           # TokenManager (DataStore-backed local storage)
│   └── remote/          # ApiService, Retrofit setup, Auth interceptor/manager, DTOs
├── domain/
│   └── model/           # Song, SongPageResponse
├── navigation/           # Routes, NavGraph, BottomNavItem
├── ui/
│   ├── login/           # Login screen + ViewModel
│   ├── home/            # Home screen + ViewModel
│   ├── song/            # Song search screen, song item, ViewModel
│   ├── library/         # Library screen
│   ├── profile/         # Profile screen
│   ├── main/            # Main screen (hosts bottom navigation)
│   ├── customComponents/# Reusable composables (e.g. LogoIcon)
│   └── theme/           # Color, Type, Theme
├── MainActivity.kt
└── SoundWaveApp.kt
```

## Requirements

- Android Studio (latest stable release recommended)
- JDK 17
- Android SDK with:
  - `minSdk` = 24
  - `targetSdk` / `compileSdk` = 36

## Getting Started

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd SoundWave-Frontend-master
   ```

2. **Open in Android Studio**
   Open the project folder and let Gradle sync.

3. **Configure the backend URL**

   The app talks to a backend API. The base URL is currently set in:
   ```
   app/src/main/java/com/ashvinprajapati/soundwave/data/remote/RetrofitInstance.kt
   ```
   ```kotlin
   private val url = "http://10.241.0.140:8080/"
   ```
   Update this to point to your own backend server (e.g. `http://<your-local-ip>:8080/` if testing on a physical device, or `http://10.0.2.2:8080/` if the backend runs on your host machine and you're using the Android emulator).

   > ⚠️ Note: The app uses `usesCleartextTraffic="true"` to allow plain HTTP during development. Use HTTPS for production.

4. **Build & run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or just click **Run** in Android Studio.
   
## Authentication Flow

- On login, a token is received and persisted via `TokenManager` (DataStore).
- `AuthInterceptor` attaches the stored token to outgoing API requests.
- `AuthManager` exposes login state (`isLoggedIn`) used by navigation to decide whether to show the **Login** screen or the **Main** app.

## Building a Release APK

```bash
./gradlew assembleRelease
```

Update `app/proguard-rules.pro` and enable minification in `app/build.gradle.kts` before shipping a production build.

## License

Add your license of choice here (e.g. MIT).
