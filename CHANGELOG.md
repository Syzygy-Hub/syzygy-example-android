# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [1.0.0] - 2026-10-16

### Added
- Home screen showing the signed-in email, with buttons to open Features and to log out
- Features screen demonstrating each layer with public APIs and no keys: Foundation (`SyzygyID`), Core (`ConfigRegistry`, `FeatureFlag`), Services (`StorageProvider`, `StorageTypeMismatchError`), AI (`LLMProvider` with an offline provider written in the app) and UI (`PrimaryButton`, `CardView`, `ProgressBar`)
- Offline `LLMProvider` implementation in the app (`OfflineLLMProvider`)

### Changed
- App renamed from SyzygyBase to Syzygy Example (applicationId `com.syzygyhub.example`, project `SyzygyExample`)
- Syzygy layers remain declared at 3.0.0 on JitPack (Foundation, Core, Services, AI, UI)

## [3.0.0] - 2026-10-06

### Added
- All 5 Syzygy layers declared as dependencies at v3.0.0 (Foundation, Core, Services, AI, UI)
- Core DI Container wiring for Logger, NetworkClient, AuthProvider, StorageProvider, StateStore, EventBus, Router, Scheduler, FeatureFlagProvider, ConfigRegistry, AppLifecycleTracker
- SyzygyThemeProvider wrapping the app root
- Hub reusable CI workflow (android-ci.yml@main)
- EditorConfig lint configuration
- JitPack repository for layer dependencies
- syzygy.yml layer manifest

### Changed
- applicationId renamed from com.aks.boilerplate to com.syzygyhub.base
- App name renamed from Boilerplate to SyzygyBase
- Version set to 3.0.0

### Removed
- Inline CI workflow (android.yml) replaced by Hub reusable workflow
- Android Studio template theme files (Color.kt, Theme.kt, Type.kt, Spacing.kt)
- Local shadow copies of NetworkClient, ApiError, SecureStorage
- Hand-rolled DI container replaced by Core DI Container
