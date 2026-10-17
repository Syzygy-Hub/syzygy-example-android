[![Android](https://img.shields.io/badge/Android-Kotlin-7F77DD?style=flat)](https://developer.android.com/) [![Kotlin](https://img.shields.io/badge/Kotlin-2.0-1D9E75?logo=kotlin&logoColor=white&style=flat)](https://kotlinlang.org) [![CI](https://img.shields.io/github/actions/workflow/status/Syzygy-Hub/syzygy-example-android/ci.yml?label=ci&style=flat)](https://github.com/Syzygy-Hub/syzygy-example-android/actions/workflows/ci.yml) [![Version](https://img.shields.io/badge/version-1.0.0-D85A30?style=flat)](https://github.com/Syzygy-Hub/syzygy-example-android/releases) [![License](https://img.shields.io/badge/License-MIT-green?style=flat)](LICENSE)

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/Syzygy-Hub/.github/main/brand/assets/banners/syzygy-banner-dark-1200.png">
  <img src="https://raw.githubusercontent.com/Syzygy-Hub/.github/main/brand/assets/banners/syzygy-banner-light-1200.png" alt="Syzygy" width="600">
</picture>

# syzygy-example-android

An Android example app (Kotlin and Jetpack Compose) that composes all five Syzygy layers and Base through one Core DI Container, with a demo login.

> This is an example application. It is **not published** to any package registry. Its only release artefact is a GitHub Release.

## Purpose

This example shows how to compose the Syzygy layers in a Jetpack Compose app: one DI container, one set of screens and a demo login that lets a reader reach every screen without a backend. It is a reference for wiring Foundation, Core, Services, AI and UI together on Android.

## Layers demonstrated

| Layer | Where it is shown | Status |
|---|---|---|
| Foundation | Features screen: `SyzygyID.generate<T>()` | Implemented |
| UI | Features screen: `PrimaryButton`, `CardView`, `ProgressBar`. Theme: `SyzygyThemeProvider` | Implemented |
| Core | Features screen: `ConfigRegistry.get(ConfigKey)`, `InMemoryFeatureFlagProvider.value(FeatureFlag)`. Container: `di/AppModule.kt` | Implemented |
| Services | Features screen: `StorageProvider.set` / `get`, `StorageTypeMismatchError`. Network, auth and storage providers registered in `di/AppModule.kt` | Implemented |
| AI | Features screen: `LLMProvider.complete`, `LLMRequest`, `LLMResponse`, `AIError`, with an offline provider in the app | Implemented |
| Base | Composition root in `di/AppModule.kt` and screen switching in `MainActivity.kt` | Implemented |

## Example screens

Login, Home and Features are switched by state in `MainActivity`.

- **Login** signs in with the demo account (see Demo login). Wrong credentials show an error.
- **Home** shows the signed-in email, with **Open Features** and **Log Out**.
- **Features** demonstrates each layer with public APIs only and no API keys. The AI demo uses the offline provider at `features/features/data/OfflineLLMProvider.kt`.
- **Log Out** clears the session and returns to Login. Back navigation from Features returns to Home.

## Demo login

This example is not connected to a backend, so it signs in with an offline demo account by default.

| Field | Value |
|---|---|
| Email | `demo@syzygy.example` |
| Password | `demo-password` |

These are demo values only, not secrets. The flag is `USE_DEMO_LOGIN` in `app/src/main/java/com/syzygyhub/example/di/AppModule.kt`. Its default is `true`, which selects the demo login.

## Not included

- A backend. Login uses the demo account only. Registration, token refresh and real API calls are not implemented.
- A production AI provider. The AI demo answers from the offline provider in the app.
- UI tests. This release runs only unit tests.

## Requirements

- Android 10 (API 29) or later, the `minSdk` in `app/build.gradle.kts`
- Kotlin 2.0 or later
- Java 17 or later
- Android Studio, current stable release

## How to run

1. Clone the repository:
   ```sh
   git clone https://github.com/Syzygy-Hub/syzygy-example-android.git
   cd syzygy-example-android
   ```
2. Preview the rename. This is a dry run and changes nothing:
   ```sh
   ./setup.sh
   ```
3. Apply the rename. It sets the app name to `Syzygy Example` and the identifier to `com.syzygyhub.example`. Commit your changes before this step:
   ```sh
   ./setup.sh --apply
   ```
4. Open the project in Android Studio and sync Gradle. JitPack resolves the five Syzygy layers at 3.0.0.
5. Run the unit tests and build the debug APK:
   ```sh
   ./gradlew testDebugUnitTest assembleDebug
   ```

## Library compatibility

This release is built against Syzygy 3.0.0. The five layers are pinned to exactly 3.0.0 in `app/build.gradle.kts`. The `depends_on` constraints in `syzygy.yml` are `>=3.0.0`. The compatibility table is in [example-apps-standard.md](https://github.com/Syzygy-Hub/.github/blob/main/engineering/standards/example-apps-standard.md#4-compatibility-table).

## Versioning and release

This example versions independently of the Syzygy libraries. Releases are tagged with bare strict semver, for example `1.0.0`, with no `v` prefix. Each tag creates a GitHub Release from the matching CHANGELOG section. Nothing is published to a registry.

## Contributing

Follow the [Syzygy contribution guide](https://github.com/Syzygy-Hub/.github/blob/main/CONTRIBUTING.md). Open a pull request against `main`.

## Security

Report security issues through the [Syzygy security policy](https://github.com/Syzygy-Hub/.github/blob/main/SECURITY.md).

## License

MIT. Copyright (c) 2026 Ayush Kumar Sethi. See [LICENSE](LICENSE).
