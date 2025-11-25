# Week 13-01: Hardcoded Level Loading (ANTI-PATTERN)

**Branch**: `13-01-hardcoded-level`

**Compilation**: `javac -d bin/13-01-hardcoded-level src/*.java src/**/*.java`

**Run**: `java -cp bin/13-01-hardcoded-level LevelDemo`

---

## Overview

Branch ini mendemonstrasikan **masalah** yang terjadi ketika level loading diimplementasikan tanpa pattern yang tepat. Setiap level loader memiliki algoritma yang **sama** tetapi di-copy-paste, menyebabkan **code duplication**.

---

## Masalah: Code Duplication

### Setiap Level Loader Punya Algoritma Sama

```java
// DungeonLevelLoader.java
public class DungeonLevelLoader {
    public void loadLevel() {
        System.out.println("=== Loading Dungeon ===");

        // Step 1: Load assets
        System.out.println("Loading dungeon textures...");
        System.out.println("Loading torch effects...");

        // Step 2: Build world
        System.out.println("Building dungeon corridors...");
        System.out.println("Placing torches...");

        // Step 3: Spawn enemies
        System.out.println("Spawning skeletons...");
        System.out.println("Spawning goblins...");

        // Step 4: Play music
        System.out.println("Playing dungeon music...");

        System.out.println("=== Dungeon Loaded ===");
    }
}
```

```java
// ForestLevelLoader.java
public class ForestLevelLoader {
    public void loadLevel() {
        System.out.println("=== Loading Forest ===");

        // Step 1: Load assets (DUPLICATED STRUCTURE!)
        System.out.println("Loading tree textures...");
        System.out.println("Loading nature sounds...");

        // Step 2: Build world (DUPLICATED STRUCTURE!)
        System.out.println("Generating forest terrain...");
        System.out.println("Planting trees...");

        // Step 3: Spawn enemies (DUPLICATED STRUCTURE!)
        System.out.println("Spawning wolves...");
        System.out.println("Spawning bandits...");

        // Step 4: Play music (DUPLICATED STRUCTURE!)
        System.out.println("Playing forest ambience...");

        System.out.println("=== Forest Loaded ===");
    }
}
```

```java
// BossArenaLoader.java - INCONSISTENT!
public class BossArenaLoader {
    public void loadLevel() {
        System.out.println("=== Loading Boss Arena ===");

        // DIFFERENT ORDER! Bug risk!
        System.out.println("Spawning boss first...");  // Step 3 first??
        System.out.println("Loading boss textures..."); // Step 1 second??
        System.out.println("Building arena...");        // Step 2 third??
        // No music! (forgot step 4)

        System.out.println("=== Boss Arena Loaded ===");
    }
}
```

---

## Masalah yang Teridentifikasi

### 1. Code Duplication
```
DungeonLevelLoader.loadLevel()  ─┐
ForestLevelLoader.loadLevel()   ─┼── Algoritma sama, copy-paste!
CastleLevelLoader.loadLevel()   ─┘
```

Setiap kali ada level baru, harus copy-paste seluruh algoritma.

### 2. Inconsistent Algorithm Order
```java
// DungeonLoader: assets → world → enemies → music ✓
// ForestLoader:  assets → world → enemies → music ✓
// BossArena:     enemies → assets → world → ???   ✗ INCONSISTENT!
```

Tidak ada jaminan urutan langkah konsisten.

### 3. Missing Steps
```java
// BossArenaLoader lupa step music!
// Tidak ada compile-time check untuk memastikan semua langkah ada
```

### 4. Difficult to Modify Algorithm
Jika ingin menambah langkah baru (misalnya "validate level"), harus ubah **SEMUA** loader:
```java
// Harus edit setiap file satu per satu!
DungeonLevelLoader.java   - tambah validateLevel()
ForestLevelLoader.java    - tambah validateLevel()
CastleLevelLoader.java    - tambah validateLevel()
BossArenaLoader.java      - tambah validateLevel()
// ... dan seterusnya
```

### 5. No Polymorphism
```java
// Client code tidak bisa menggunakan polymorphism
public void startLevel(String levelType) {
    if (levelType.equals("dungeon")) {
        new DungeonLevelLoader().loadLevel();
    } else if (levelType.equals("forest")) {
        new ForestLevelLoader().loadLevel();
    } else if (levelType.equals("boss")) {
        new BossArenaLoader().loadLevel();
    }
    // Harus tambah if-else untuk setiap level baru!
}
```

---

## File Structure

```
src/
└── level/
    ├── DungeonLevelLoader.java   # Copy-paste algoritma
    ├── ForestLevelLoader.java    # Copy-paste algoritma
    ├── CastleLevelLoader.java    # Copy-paste algoritma
    └── BossArenaLoader.java      # Inconsistent algoritma!
```

---

## Implementasi

### DungeonLevelLoader.java
```java
package level;

/**
 * ANTI-PATTERN: Hardcoded level loading
 *
 * Problem: Entire algorithm is copy-pasted in each loader
 */
public class DungeonLevelLoader {

    public void loadLevel() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║     LOADING: DARK DUNGEON          ║");
        System.out.println("╚════════════════════════════════════╝\n");

        // Step 1: Load assets
        System.out.println("[Step 1] Loading assets...");
        System.out.println("  - Loading stone wall textures");
        System.out.println("  - Loading torch flame sprites");
        System.out.println("  - Loading skeleton animations");
        System.out.println("  - Loading goblin animations");

        // Step 2: Build world
        System.out.println("\n[Step 2] Building world...");
        System.out.println("  - Generating dungeon layout");
        System.out.println("  - Placing wall tiles");
        System.out.println("  - Adding torch lighting");
        System.out.println("  - Placing treasure chests");

        // Step 3: Spawn enemies
        System.out.println("\n[Step 3] Spawning enemies...");
        System.out.println("  - Spawning 5 Skeletons");
        System.out.println("  - Spawning 3 Goblins");
        System.out.println("  - Placing Dungeon Boss");

        // Step 4: Play music
        System.out.println("\n[Step 4] Starting music...");
        System.out.println("  ♪ Playing: eerie_dungeon.mp3");

        System.out.println("\n✓ Dark Dungeon loaded successfully!\n");
    }
}
```

### ForestLevelLoader.java
```java
package level;

/**
 * ANTI-PATTERN: Hardcoded level loading
 *
 * Problem: Same algorithm structure as DungeonLevelLoader (DUPLICATED!)
 */
public class ForestLevelLoader {

    public void loadLevel() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║     LOADING: ENCHANTED FOREST      ║");
        System.out.println("╚════════════════════════════════════╝\n");

        // Step 1: Load assets (SAME STRUCTURE AS DUNGEON!)
        System.out.println("[Step 1] Loading assets...");
        System.out.println("  - Loading tree textures");
        System.out.println("  - Loading grass sprites");
        System.out.println("  - Loading wolf animations");
        System.out.println("  - Loading bandit animations");

        // Step 2: Build world (SAME STRUCTURE!)
        System.out.println("\n[Step 2] Building world...");
        System.out.println("  - Generating forest terrain");
        System.out.println("  - Planting trees and bushes");
        System.out.println("  - Creating winding paths");
        System.out.println("  - Adding hidden clearings");

        // Step 3: Spawn enemies (SAME STRUCTURE!)
        System.out.println("\n[Step 3] Spawning enemies...");
        System.out.println("  - Spawning wolf packs");
        System.out.println("  - Placing bandits near paths");

        // Step 4: Play music (SAME STRUCTURE!)
        System.out.println("\n[Step 4] Starting music...");
        System.out.println("  ♪ Playing: forest_ambience.mp3");

        System.out.println("\n✓ Enchanted Forest loaded successfully!\n");
    }
}
```

### BossArenaLoader.java
```java
package level;

/**
 * ANTI-PATTERN: Hardcoded level loading
 *
 * Problem: INCONSISTENT algorithm order! Missing steps!
 * This demonstrates what happens without enforced structure.
 */
public class BossArenaLoader {

    public void loadLevel() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║     LOADING: BOSS ARENA            ║");
        System.out.println("╚════════════════════════════════════╝\n");

        // BUG: Wrong order! Spawning before loading assets!
        System.out.println("[Step ???] Spawning boss first...");
        System.out.println("  - ERROR: Boss texture not loaded yet!");

        // Assets loaded too late
        System.out.println("\n[Step ???] Loading assets (too late!)...");
        System.out.println("  - Loading boss sprite");
        System.out.println("  - Loading arena textures");

        // World building
        System.out.println("\n[Step ???] Building arena...");
        System.out.println("  - Creating circular arena");

        // BUG: Forgot to play music!
        // (no Step 4!)

        System.out.println("\n✓ Boss Arena loaded... with issues!\n");
    }
}
```

### LevelDemo.java
```java
import level.*;

/**
 * Demo showing problems with hardcoded level loading
 */
public class LevelDemo {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║  WEEK 13-01: HARDCODED LEVEL LOADING       ║");
        System.out.println("║  (ANTI-PATTERN DEMONSTRATION)              ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.println("\nThis demo shows problems with hardcoded level loading:\n");
        System.out.println("1. Code duplication across loaders");
        System.out.println("2. Inconsistent algorithm order");
        System.out.println("3. Missing steps in some loaders");
        System.out.println("4. No polymorphism possible");

        // Load each level
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Loading Dungeon Level...");
        System.out.println("=".repeat(50));
        new DungeonLevelLoader().loadLevel();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Loading Forest Level...");
        System.out.println("=".repeat(50));
        new ForestLevelLoader().loadLevel();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Loading Boss Arena (BUGGY!)...");
        System.out.println("=".repeat(50));
        new BossArenaLoader().loadLevel();

        // Show the problem with no polymorphism
        System.out.println("\n" + "=".repeat(50));
        System.out.println("PROBLEM: No Polymorphism!");
        System.out.println("=".repeat(50));
        System.out.println("\nTo load a level by name, we need ugly if-else:");
        System.out.println("  if (name.equals(\"dungeon\")) new DungeonLevelLoader()...");
        System.out.println("  else if (name.equals(\"forest\")) new ForestLevelLoader()...");
        System.out.println("  else if (name.equals(\"boss\")) new BossArenaLoader()...");
        System.out.println("\nThis violates Open/Closed Principle!");

        // Summary
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  PROBLEMS IDENTIFIED                       ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║  ✗ Code duplication (same algo everywhere) ║");
        System.out.println("║  ✗ Inconsistent order (boss arena buggy)   ║");
        System.out.println("║  ✗ Missing steps (no music in boss arena)  ║");
        System.out.println("║  ✗ No polymorphism (can't use base type)   ║");
        System.out.println("║  ✗ Hard to add new steps to all loaders    ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║  SOLUTION: Template Method Pattern         ║");
        System.out.println("║  See branch: 13-02-template-method         ║");
        System.out.println("╚════════════════════════════════════════════╝");
    }
}
```

---

## Output Demo

```
╔════════════════════════════════════════════╗
║  WEEK 13-01: HARDCODED LEVEL LOADING       ║
║  (ANTI-PATTERN DEMONSTRATION)              ║
╚════════════════════════════════════════════╝

This demo shows problems with hardcoded level loading:

1. Code duplication across loaders
2. Inconsistent algorithm order
3. Missing steps in some loaders
4. No polymorphism possible

==================================================
Loading Dungeon Level...
==================================================

╔════════════════════════════════════╗
║     LOADING: DARK DUNGEON          ║
╚════════════════════════════════════╝

[Step 1] Loading assets...
  - Loading stone wall textures
  - Loading torch flame sprites
  ...

==================================================
Loading Boss Arena (BUGGY!)...
==================================================

╔════════════════════════════════════╗
║     LOADING: BOSS ARENA            ║
╚════════════════════════════════════╝

[Step ???] Spawning boss first...
  - ERROR: Boss texture not loaded yet!
  ...
```

---

## Solusi

Masalah ini diselesaikan dengan **Template Method Pattern** di branch `13-02-template-method`:
- Algoritma didefinisikan sekali di abstract class
- Subclass hanya implement langkah-langkah spesifik
- Urutan dijamin konsisten oleh template method
- Polymorphism dapat digunakan

Lihat: [Week 13-02: Template Method Pattern](./week-13-02-template-method.md)
