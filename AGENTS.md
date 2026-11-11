# AGENTS.md

Guidance for AI coding agents and human contributors working in this repository.

## Project Purpose

**AnalyticsKit-Android** is a production-ready Android multi-module library that wraps analytics SDKs
(Firebase Analytics first; more providers later) behind a unified, strongly-typed API.

Applications depend on Kit modules and call `Analytics.*`. They must not talk to
Firebase or other third-party SDKs directly for event tracking.

## Module Map

| Module | Role | Dependencies |
|:---|:---|:---|
| `:analytics` | Core API, facade, SPI contract, NoOp / Logging / Composite | Zero 3rd-party dependencies |
| `:analytics-firebase` | `FirebaseAnalyticsProvider` adapter | `:analytics`, Firebase BOM |
| `:sample` | Demo application (Jetpack Compose) | `:analytics-firebase` |

> **Rule**: Do **not** add Firebase (or any vendor SDK) to `:analytics`. New vendors must be isolated in their own adapter modules (e.g. `:analytics-appsflyer`).

## Architecture & Code Guidelines

```
App code → Analytics (facade) → AnalyticsProvider(s) → Vendor SDK
```

1. **Facade & Thread Safety**:
   - Public entry point is `com.kit.analytics.Analytics` singleton.
   - Internal states (`initialized`, `enabled`, `debug`) must be thread-safe.
   - Guard against duplicate initialization gracefully with a debug warning.
2. **Strongly-Typed Parameters**:
   - Event parameters are strictly constrained to `AnalyticsValue` (`String`, `Long`, `Double`, `Boolean`).
   - Do **not** introduce `Map<String, Any>` or raw `Object` into public event APIs.
3. **Fail-Soft Principle**:
   - `CompositeAnalyticsProvider` must catch any provider exception/error to ensure host applications never crash due to analytics tracking.
4. **Boolean Mapping**:
   - In `:analytics-firebase`, Boolean parameters must map to `"true"` / `"false"` strings because Android `Bundle` has no primitive boolean in standard analytics protocols.

## Gradle & Build Conventions

- Use **AGP 9+ with built-in Kotlin**. Do **not** apply `org.jetbrains.kotlin.android`.
- Version catalog (`gradle/libs.versions.toml`) is the single source of truth for dependencies.
- Library modules exposing Firebase artifacts must use `api(platform(libs.firebase.bom))` so consumers resolve versions correctly.
- JDK requirement: **JDK 21**.

## Publishing & Dependency Rules

- **GitHub Packages Only**: Releases and artifacts are published exclusively to **GitHub Packages** (`https://maven.pkg.github.com/e-hai/AnalyticsKit-Android`).
- **JitPack Deprecation**: Do NOT re-introduce JitPack repositories (`jitpack.io`) or `jitpack.yml` configuration.
- Artifact coordinates:
  - `com.github.e-hai.AnalyticsKit-Android:analytics:<version>`
  - `com.github.e-hai.AnalyticsKit-Android:analytics-firebase:<version>`
