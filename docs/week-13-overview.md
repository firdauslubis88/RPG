# Week 13: Template Method dan Facade Pattern

## Overview

Week 13 memperkenalkan dua design pattern penting:
1. **Template Method Pattern** - Mendefinisikan skeleton algoritma, subclass mengisi detail
2. **Facade Pattern** - Menyederhanakan interaksi dengan subsystem kompleks

Seperti minggu-minggu sebelumnya, kita akan mendemonstrasikan **masalah** terlebih dahulu (anti-pattern), kemudian **solusi** dengan design pattern.

---

## Branch Structure

| Branch | Tipe | Deskripsi |
|--------|------|-----------|
| `13-01-hardcoded-level` | PROBLEM | Level loading dengan code duplication |
| `13-02-template-method` | SOLUTION | Template Method Pattern |
| `13-03-tightly-coupled` | PROBLEM | Client tightly coupled ke subsystems |
| `13-04-facade-pattern` | SOLUTION | Facade Pattern |

---

## Learning Flow

```
Template Method:
┌─────────────────────┐         ┌─────────────────────┐
│  13-01: PROBLEM     │         │  13-02: SOLUTION    │
│  Hardcoded Level    │ ──────► │  Template Method    │
│  (Code Duplication) │         │  (Code Reuse)       │
└─────────────────────┘         └─────────────────────┘

Facade:
┌─────────────────────┐         ┌─────────────────────┐
│  13-03: PROBLEM     │         │  13-04: SOLUTION    │
│  Tightly Coupled    │ ──────► │  Facade Pattern     │
│  (Complex Client)   │         │  (Simple Interface) │
└─────────────────────┘         └─────────────────────┘
```

---

## Template Method Pattern

### Konsep
Template Method mendefinisikan **kerangka algoritma** di superclass, membiarkan subclass mengimplementasikan langkah-langkah spesifik **tanpa mengubah struktur algoritma**.

### Hollywood Principle
"Don't call us, we'll call you!"
- Superclass memanggil method subclass, bukan sebaliknya
- Inversion of Control: kontrol ada di parent class

### Struktur
```
┌─────────────────────────────┐
│    AbstractClass            │
│ ───────────────────────────│
│ + templateMethod()          │  ← Final method (skeleton)
│ # primitiveOperation1()     │  ← Abstract (wajib implement)
│ # primitiveOperation2()     │  ← Abstract (wajib implement)
│ # hookMethod()              │  ← Hook (optional override)
└─────────────────────────────┘
            △
            │
    ┌───────┴───────┐
    │               │
┌───────────┐ ┌───────────┐
│ ConcreteA │ │ ConcreteB │
└───────────┘ └───────────┘
```

### Contoh: Level Loading
```java
public abstract class LevelLoader {
    // Template method - fixed algorithm
    public final void loadLevel() {
        loadAssets();        // Step 1
        buildWorld();        // Step 2
        spawnEnemies();      // Step 3
        if (shouldPlayMusic()) {  // Hook
            playBackgroundMusic();
        }
    }

    // Primitive operations - must be implemented
    protected abstract void loadAssets();
    protected abstract void buildWorld();
    protected abstract void spawnEnemies();

    // Hook method - optional override
    protected boolean shouldPlayMusic() {
        return true;  // Default behavior
    }
}
```

---

## Facade Pattern

### Konsep
Facade menyediakan **interface sederhana** untuk kumpulan interface dalam subsystem. Facade mendefinisikan interface higher-level yang membuat subsystem lebih mudah digunakan.

### Struktur
```
┌─────────────────┐
│     Client      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│     Facade      │  ← Interface sederhana
└────────┬────────┘
         │
    ┌────┼────┐
    │    │    │
    ▼    ▼    ▼
┌─────┐┌─────┐┌─────┐
│Sub1 ││Sub2 ││Sub3 │  ← Subsystem kompleks
└─────┘└─────┘└─────┘
```

### Contoh: Game Engine Facade
```java
public class GameEngineFacade {
    private AudioSystem audio;
    private PhysicsEngine physics;
    private VideoSystem video;

    // Simple interface to complex subsystems
    public void startGame() {
        audio.init();
        physics.init();
        video.init();
        audio.playBackgroundMusic();
    }

    public void updateFrame(float deltaTime) {
        physics.step(deltaTime);
        video.render();
    }

    public void shutdown() {
        audio.cleanup();
        physics.cleanup();
        video.cleanup();
    }
}
```

---

## Kapan Menggunakan Pattern Ini?

### Template Method
- Ketika beberapa class memiliki **algoritma serupa** dengan langkah-langkah berbeda
- Untuk **menghindari code duplication** dalam algoritma
- Ketika ingin mengontrol **extensibility** melalui hook methods

### Facade
- Ketika sistem memiliki **banyak subsystem** yang kompleks
- Untuk menyediakan **interface sederhana** ke library/framework
- Ketika ingin **decoupling** client dari implementasi subsystem

---

## Design Patterns Summary (Week 13)

| Pattern | Intent | Key Benefit |
|---------|--------|-------------|
| Template Method | Define algorithm skeleton | Code reuse + controlled extensibility |
| Facade | Simplify complex subsystems | Easy-to-use interface |

---

## Prinsip yang Diterapkan

1. **Hollywood Principle** (Template Method)
   - "Don't call us, we'll call you"
   - Superclass mengontrol alur, subclass menyediakan implementasi

2. **Principle of Least Knowledge** (Facade)
   - "Talk only to your immediate friends"
   - Client hanya berinteraksi dengan Facade, bukan subsystem

3. **Open/Closed Principle**
   - Algoritma template tertutup untuk modifikasi
   - Terbuka untuk extension melalui subclassing

---

## Next Steps

Lihat dokumentasi detail untuk setiap branch:
- [Week 13-01: Hardcoded Level Loading (PROBLEM)](./week-13-01-hardcoded-level.md)
- [Week 13-02: Template Method Pattern (SOLUTION)](./week-13-02-template-method.md)
- [Week 13-03: Tightly Coupled Subsystems (PROBLEM)](./week-13-03-tightly-coupled.md)
- [Week 13-04: Facade Pattern (SOLUTION)](./week-13-04-facade-pattern.md)

---

## References

- **Design Patterns: Elements of Reusable Object-Oriented Software** (GoF)
- **Head First Design Patterns** (O'Reilly)
