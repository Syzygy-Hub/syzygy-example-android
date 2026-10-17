# Changelog

All notable changes to `syzygy-example-android` are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.0.0] - 2026-10-18

### Added
- Release workflow `.github/workflows/release.yml` from the Hub's app release template. It fires on strict bare-semver tags and calls `app-release.yml@main`.
- Library compatibility table in the README.
- The demo login is documented as shipping in all build types, including release builds.

### Changed
- CI caller aligned with the Hub's Android app CI template: `project_type: app`, `module: ':app'`, Java 21. Triggers are push-only with tags excluded.
- Lint: one ktlint setup, the ktlint-cli 1.0.1 task in `app/build.gradle.kts`, now checks Kotlin sources. The ktlint Gradle plugin is removed.
- Removed the placeholder instrumented test template.
- README rewritten to the v3.0.0 facts. The one-time rename steps are removed.
- `versionName` 3.0.0, `versionCode` 2.

## [1.0.0] - 2026-10-16

### Added
- Home screen showing the signed-in email, with buttons to open Features and to log out
- Features screen demonstrating each layer with public APIs and no keys: Foundation (`SyzygyID`), Core (`ConfigRegistry`, `FeatureFlag`), Services (`StorageProvider`, `StorageTypeMismatchError`), AI (`LLMProvider` with an offline provider written in the app) and UI (`PrimaryButton`, `CardView`, `ProgressBar`)
- Offline `LLMProvider` implementation in the app (`OfflineLLMProvider`)

### Changed
- App renamed from SyzygyBase to Syzygy Example (applicationId `com.syzygyhub.example`, project `SyzygyExample`)
- Syzygy layers remain declared at 3.0.0 on JitPack (Foundation, Core, Services, AI, UI)

