# AGENT.md

This file provides minimal, repo-level guidance for AI agents working on this workspace.

## Repository overview

- **Build system**: Gradle (Kotlin DSL)
- **Repo type**: multi-module Scala playground / spike repo
- **Included modules** (see `settings.gradle.kts`):
  - `prover-commons` (project dir: `prover-commons/module`)
    - `prover-commons:infra`
    - `prover-commons:core`
    - `prover-commons:meta2`
    - `prover-commons:spark`
  - `common`, `common:generic`, `lecture`, `cats3`, `zio`, `kyo`, `scraper`, `spark`, `serde`, `deeplearning`

If you are working inside `prover-commons`, also consult `prover-commons/AGENTS.md`.

## Common commands

Run everything from the repo root.

```bash
./gradlew build
./gradlew test
./gradlew check
```

Project provides a convenience wrapper for tests:

```bash
./dev/test.sh
```

Module-specific examples:

```bash
./gradlew :zio:test
./gradlew :serde:test
./gradlew :prover-commons:core:test
```

## Change guidelines

- Keep edits minimal and targeted.
- Do not delete existing comments.
- Prefer repo-local conventions (Gradle Kotlin DSL, existing Scala style).
- Keep imports at the top of files.
- After changes, prefer running the narrowest relevant test task, then `./gradlew test` if needed.
