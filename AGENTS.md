# AGENTS.md

Guidance for AI coding agents working in this repository.

## Project purpose

AnalyticsKit-Android is an Android multi-module library that wraps analytics SDKs
(Firebase Analytics first; more providers later) behind a single API.

Apps depend on Kit modules and call `Analytics.*`. They must not talk to
Firebase / other SDKs directly for event tracking.

## Module map

| Module | Role |
|--------|------|
| `:analytics` | Core API, facade, provider contract, NoOp / Logging / Composite |
| `:analytics-firebase` | `FirebaseAnalyticsProvider` + Firebase BOM |
| `:sample` | Demo app; Logging + Firebase |

Do **not** add Firebase (or other vendor SDKs) to `:analytics`. New vendors get
their own module, e.g. `:analytics-appsflyer`.

## Architecture rules

```
App code → Analytics (facade) → AnalyticsProvider(s) → Vendor SDK
```

- Public app-facing API lives in `com.kit.analytics` (`Analytics`, `AnalyticsConfig`,
  `AnalyticsEvent`, `AnalyticsValue`, param DSL).
- Vendor adapters implement `com.kit.analytics.provider.AnalyticsProvider`.
- Multiple providers are wrapped by `CompositeAnalyticsProvider` inside
  `Analytics.initialize` when more than one is configured.
- Typed params use `AnalyticsValue` (String / Long / Double / Boolean). Do not
  introduce `Map<String, Any>` for event params.
- Boolean maps to Firebase as `"true"` / `"false"` strings (Bundle has no bool).

## AGP / Kotlin conventions

- AGP 9+ has **built-in Kotlin**. Do **not** apply `org.jetbrains.kotlin.android`
  (causes duplicate `kotlin` extension).
- Compose modules still use `org.jetbrains.kotlin.plugin.compose`.
- Prefer version catalog (`gradle/libs.versions.toml`) for deps and plugins.
- Library modules that expose Firebase artifacts must use `api(platform(firebase-bom))`
  so consumers resolve versions (empty version = missing BOM on consumer classpath).

## Firebase sample constraints

- Host apps need a **real** `google-services.json` from Firebase Console plus the
  `google-services` plugin. The old Quickstart mock (`mockproject-1234`) yields
  `API_KEY_INVALID` from Firebase Installations.
- `applicationId` must match a `package_name` in that json.
- Sample defaults to `LoggingAnalyticsProvider` only until a real Firebase
  config is added; see `sample/google-services.json.README`.

## Coding guidelines

- Kotlin only for library and sample sources.
- Match existing style: concise KDoc on public types, fail soft in Composite /
  facade when `debug` is on (log), do not crash the host app for one provider.
- Prefer small, focused changes; no drive-by refactors or unsolicited docs.
- New provider checklist:
  1. New Gradle module depending on `:analytics`
  2. Implement `AnalyticsProvider`
  3. Wire sample optionally
  4. Document in README module table

## Publishing (JitPack)

- Shared script: `gradle/android-library-publish.gradle.kts`
- Coordinates: `com.github.e-hai.AnalyticsKit:<module>:<git-tag>`
- Modules published: `:analytics`, `:analytics-firebase` (not `:sample`)
- CI config: `jitpack.yml` (JDK 21)
- Do not change `GROUP_ID` casually; it must stay aligned with the GitHub
  `owner/repo` used by JitPack consumers.

## Out of scope (unless asked)

- Publishing to Maven Central (JitPack is configured)
- iOS / KMP
- Changing core API shape without a clear migration path
