# Week 13-01: Template Method Pattern

**Branch**: `13-01-template-method`

**Compilation**: `javac -d bin/13-01-template-method src/*.java src/**/*.java`

**Run**: `java -cp bin/13-01-template-method Main`

---

## Overview

Template Method Pattern mendefinisikan **skeleton (kerangka) algoritma** dalam method di superclass, membiarkan subclass **mengisi langkah-langkah spesifik** tanpa mengubah struktur algoritma.

---

## Motivasi

### Masalah: Code Duplication dalam Algoritma

Bayangkan kita memiliki beberapa jenis level loader dengan algoritma serupa:

```java
// DungeonLoader
public void load() {
    System.out.println("Loading dungeon textures...");
    System.out.println("Building dungeon walls...");
    System.out.println("Spawning skeletons and goblins...");
}

// ForestLoader
public void load() {
    System.out.println("Loading forest textures...");
    System.out.println("Building trees and paths...");
    System.out.println("Spawning wolves and bandits...");
}

// CastleLoader
public void load() {
    System.out.println("Loading castle textures...");
    System.out.println("Building throne room...");
    System.out.println("Spawning guards and the boss...");
}
```

**Masalah**:
- Algoritma sama (load → build → spawn) tapi diulang di setiap class
- Jika ingin menambah langkah baru, harus ubah semua class
- Tidak ada jaminan urutan langkah konsisten

---

## Solusi: Template Method Pattern

### Struktur Class

```
┌────────────────────────────────────┐
│        LevelLoader (abstract)       │
│ ────────────────────────────────── │
│ + loadLevel() : void {final}        │  ← Template method (fixed)
│ # loadAssets() : void {abstract}    │  ← Primitive operation
│ # buildWorld() : void {abstract}    │  ← Primitive operation
│ # spawnEnemies() : void {abstract}  │  ← Primitive operation
│ # shouldPlayMusic() : boolean       │  ← Hook method (optional)
│ # playBackgroundMusic() : void      │
└────────────────────────────────────┘
                  △
                  │
     ┌────────────┼────────────┐
     │            │            │
┌─────────┐ ┌─────────┐ ┌─────────┐
│ Dungeon │ │ Forest  │ │ Castle  │
│ Loader  │ │ Loader  │ │ Loader  │
└─────────┘ └─────────┘ └─────────┘
```

---

## Implementasi

### 1. Abstract Class dengan Template Method

**`src/level/LevelLoader.java`**:
```java
package level;

/**
 * Template Method Pattern: Abstract class defining algorithm skeleton
 *
 * The loadLevel() method is the TEMPLATE METHOD - it defines the
 * steps of the algorithm. Subclasses implement the specific steps.
 */
public abstract class LevelLoader {

    /**
     * TEMPLATE METHOD - defines algorithm skeleton
     * Marked as 'final' to prevent subclasses from changing the algorithm
     */
    public final void loadLevel() {
        System.out.println("\n=== Loading Level: " + getLevelName() + " ===\n");

        // Step 1: Load assets (textures, sounds, etc.)
        loadAssets();

        // Step 2: Build the world (terrain, structures)
        buildWorld();

        // Step 3: Spawn enemies
        spawnEnemies();

        // Step 4: Optional hook for background music
        if (shouldPlayMusic()) {
            playBackgroundMusic();
        }

        System.out.println("\n=== " + getLevelName() + " loaded successfully! ===\n");
    }

    // ==========================================
    // PRIMITIVE OPERATIONS (abstract - must implement)
    // ==========================================

    /**
     * Load level-specific assets
     */
    protected abstract void loadAssets();

    /**
     * Build the world/environment
     */
    protected abstract void buildWorld();

    /**
     * Spawn enemies for this level
     */
    protected abstract void spawnEnemies();

    /**
     * Get the level name for display
     */
    protected abstract String getLevelName();

    // ==========================================
    // HOOK METHODS (optional override)
    // ==========================================

    /**
     * Hook method - subclasses can override to disable music
     * @return true to play background music (default)
     */
    protected boolean shouldPlayMusic() {
        return true;  // Default: play music
    }

    /**
     * Play background music - can be overridden for custom music
     */
    protected void playBackgroundMusic() {
        System.out.println("♪ Playing default background music...");
    }
}
```

### 2. Concrete Implementations

**`src/level/DungeonLoader.java`**:
```java
package level;

/**
 * Concrete implementation for Dungeon level
 */
public class DungeonLoader extends LevelLoader {

    @Override
    protected void loadAssets() {
        System.out.println("Loading dungeon stone textures...");
        System.out.println("Loading torch and shadow effects...");
        System.out.println("Loading skeleton and goblin sprites...");
    }

    @Override
    protected void buildWorld() {
        System.out.println("Generating dungeon corridors...");
        System.out.println("Placing torches on walls...");
        System.out.println("Adding treasure chests...");
    }

    @Override
    protected void spawnEnemies() {
        System.out.println("Spawning 5 Skeletons...");
        System.out.println("Spawning 3 Goblins...");
        System.out.println("Placing Dungeon Boss at end...");
    }

    @Override
    protected String getLevelName() {
        return "Dark Dungeon";
    }

    @Override
    protected void playBackgroundMusic() {
        System.out.println("♪ Playing eerie dungeon ambience...");
    }
}
```

**`src/level/ForestLoader.java`**:
```java
package level;

/**
 * Concrete implementation for Forest level
 */
public class ForestLoader extends LevelLoader {

    @Override
    protected void loadAssets() {
        System.out.println("Loading tree and grass textures...");
        System.out.println("Loading nature sounds...");
        System.out.println("Loading wolf and bandit sprites...");
    }

    @Override
    protected void buildWorld() {
        System.out.println("Generating forest terrain...");
        System.out.println("Planting trees and bushes...");
        System.out.println("Creating winding paths...");
    }

    @Override
    protected void spawnEnemies() {
        System.out.println("Spawning wolf packs...");
        System.out.println("Placing bandits near paths...");
    }

    @Override
    protected String getLevelName() {
        return "Enchanted Forest";
    }

    @Override
    protected void playBackgroundMusic() {
        System.out.println("♪ Playing forest ambient sounds with birds...");
    }
}
```

**`src/level/BossArenaLoader.java`**:
```java
package level;

/**
 * Concrete implementation for Boss Arena
 * Demonstrates hook method by disabling music until boss appears
 */
public class BossArenaLoader extends LevelLoader {

    @Override
    protected void loadAssets() {
        System.out.println("Loading boss arena textures...");
        System.out.println("Loading epic boss music...");
        System.out.println("Loading boss sprite and animations...");
    }

    @Override
    protected void buildWorld() {
        System.out.println("Creating circular arena...");
        System.out.println("Adding pillars for cover...");
        System.out.println("Setting up dramatic lighting...");
    }

    @Override
    protected void spawnEnemies() {
        System.out.println(">>> SPAWNING BOSS: Ancient Dragon <<<");
    }

    @Override
    protected String getLevelName() {
        return "Boss Arena";
    }

    /**
     * Override hook: No music during loading
     * Music will start when boss battle begins
     */
    @Override
    protected boolean shouldPlayMusic() {
        System.out.println("(Music disabled - will play when boss appears)");
        return false;
    }
}
```

---

## Penggunaan

```java
public class Main {
    public static void main(String[] args) {
        // Client code only knows about LevelLoader
        LevelLoader loader;

        // Load different levels using same interface
        loader = new DungeonLoader();
        loader.loadLevel();  // Template method handles everything

        loader = new ForestLoader();
        loader.loadLevel();

        loader = new BossArenaLoader();
        loader.loadLevel();
    }
}
```

### Output:
```
=== Loading Level: Dark Dungeon ===

Loading dungeon stone textures...
Loading torch and shadow effects...
Loading skeleton and goblin sprites...
Generating dungeon corridors...
Placing torches on walls...
Adding treasure chests...
Spawning 5 Skeletons...
Spawning 3 Goblins...
Placing Dungeon Boss at end...
♪ Playing eerie dungeon ambience...

=== Dark Dungeon loaded successfully! ===

=== Loading Level: Enchanted Forest ===

Loading tree and grass textures...
Loading nature sounds...
Loading wolf and bandit sprites...
Generating forest terrain...
Planting trees and bushes...
Creating winding paths...
Spawning wolf packs...
Placing bandits near paths...
♪ Playing forest ambient sounds with birds...

=== Enchanted Forest loaded successfully! ===

=== Loading Level: Boss Arena ===

Loading boss arena textures...
Loading epic boss music...
Loading boss sprite and animations...
Creating circular arena...
Adding pillars for cover...
Setting up dramatic lighting...
>>> SPAWNING BOSS: Ancient Dragon <<<
(Music disabled - will play when boss appears)

=== Boss Arena loaded successfully! ===
```

---

## Hollywood Principle: "Don't Call Us, We'll Call You"

Template Method Pattern menerapkan **Hollywood Principle**:

```
┌────────────────────────────────┐
│      LevelLoader (Parent)       │
│                                 │
│  loadLevel() {                  │
│      loadAssets();    ←────────── "I'll call you"
│      buildWorld();    ←────────── "I'll call you"
│      spawnEnemies();  ←────────── "I'll call you"
│  }                              │
└────────────────────────────────┘
              │
              │ calls down to
              ▼
┌────────────────────────────────┐
│    DungeonLoader (Child)        │
│                                 │
│  loadAssets() {                 │
│      // Implementation          │  "Don't call us"
│  }                              │  (wait to be called)
└────────────────────────────────┘
```

**Inversion of Control**:
- Subclass **TIDAK** memanggil superclass
- Superclass yang **memanggil** method di subclass
- Kontrol alur algoritma ada di superclass

---

## Primitive Operations vs Hook Methods

### Primitive Operations (Abstract)
- **Harus** diimplementasikan oleh subclass
- Mewakili langkah-langkah **wajib** dalam algoritma
- Contoh: `loadAssets()`, `buildWorld()`, `spawnEnemies()`

### Hook Methods (Optional)
- Default implementation di superclass
- Subclass **boleh** override jika diperlukan
- Memberikan flexibility tanpa memaksa
- Contoh: `shouldPlayMusic()`, `playBackgroundMusic()`

```java
// Hook dengan default behavior
protected boolean shouldPlayMusic() {
    return true;  // Default: play music
}

// Subclass override untuk customize
@Override
protected boolean shouldPlayMusic() {
    return false;  // Boss arena: no music at start
}
```

---

## Aplikasi dalam RPG Game

### Contoh: Enemy AI Behavior

```java
public abstract class EnemyAI {
    // Template method
    public final void takeTurn() {
        decideAction();
        performAction();
        afterAction();
    }

    protected abstract void decideAction();
    protected abstract void performAction();

    // Hook: default do nothing
    protected void afterAction() { }
}

public class AggressiveEnemy extends EnemyAI {
    @Override
    protected void decideAction() {
        System.out.println("Choosing: ATTACK!");
    }

    @Override
    protected void performAction() {
        System.out.println("Attacking player!");
    }
}
```

### Contoh: Battle Phase

```java
public abstract class BattlePhase {
    // Template method
    public final void execute() {
        showPhaseStart();
        runPhaseLogic();
        showPhaseEnd();
    }

    protected abstract void showPhaseStart();
    protected abstract void runPhaseLogic();

    // Hook
    protected void showPhaseEnd() {
        System.out.println("Phase complete.");
    }
}
```

---

## Keuntungan Template Method Pattern

1. **Code Reuse**: Algoritma ditulis sekali di superclass
2. **Controlled Extensibility**: Subclass hanya bisa customize langkah tertentu
3. **Consistency**: Urutan langkah dijamin sama untuk semua subclass
4. **Separation of Concerns**: Skeleton di parent, details di children
5. **Easy to Add New Variants**: Buat subclass baru tanpa mengubah yang lain

---

## Kapan Menggunakan?

✅ **Gunakan Template Method** ketika:
- Beberapa class memiliki algoritma dengan langkah-langkah serupa
- Ingin mengontrol titik ekstensi dengan jelas
- Ingin menghindari code duplication dalam algoritma
- Urutan langkah penting dan harus konsisten

❌ **Jangan gunakan** ketika:
- Algoritma sangat sederhana (1-2 langkah)
- Setiap subclass perlu algoritma yang sama sekali berbeda
- Tidak ada code sharing yang meaningful

---

## Pattern Comparison

| Aspect | Template Method | Strategy |
|--------|-----------------|----------|
| **Intent** | Define algorithm skeleton | Define family of algorithms |
| **Structure** | Inheritance | Composition |
| **Vary** | Steps within algorithm | Entire algorithm |
| **Control** | Parent controls flow | Client controls which strategy |

---

## File Structure

```
src/
└── level/
    ├── LevelLoader.java       # Abstract class with template method
    ├── DungeonLoader.java     # Concrete: dungeon level
    ├── ForestLoader.java      # Concrete: forest level
    └── BossArenaLoader.java   # Concrete: boss arena level
```

---

## Testing

```bash
# Compile
javac -d bin/13-01-template-method src/*.java src/**/*.java

# Run demo
java -cp bin/13-01-template-method Main
```

---

## References

- **GoF**: Template Method Pattern
- **Head First Design Patterns**: Chapter 8
- **Hollywood Principle**: Inversion of Control
