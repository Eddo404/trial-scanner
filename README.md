# Trial & Vault Scanner

A Fabric client-side Minecraft mod that scans loaded chunks for:

- `minecraft:trial_spawner`
- `minecraft:vault`

Each chunk is scanned once, and each discovered block is only announced once in chat.

## Minecraft version

This project is configured for **Minecraft 1.21.1** and Java 21.

## Building locally

Run:

```bash
gradle build
```

The finished JAR will be in `build/libs`.

## Building with GitHub Actions

Upload these files to a GitHub repository. The included workflow in `.github/workflows/build.yml` will build the mod and upload the JAR as a workflow artifact.
