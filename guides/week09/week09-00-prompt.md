# Kickstart Prompt for Claude Code

Copy-paste ini untuk memulai branch `09-00-without-game-loop`:

---

```
Saya ingin membuat demo game untuk mengajarkan MENGAPA game loop paradigm diperlukan. Game ini SENGAJA bad design untuk tujuan pembelajaran.

## Context
Branch pertama project "Dungeon Escape" untuk kuliah OOP. Mahasiswa sudah paham Java, OOP, SOLID. Branch ini SENGAJA anti-pattern untuk show pain points.

## Game Specs
- Terminal-based, auto-scrolling
- NPC: Bergerak otomatis kiri-kanan (belum ada player control)
- Coin: Spawn random, jatuh ke bawah
- Mechanics: NPC nabrak coin → score++
- Display: Grid 10x10, refresh 100ms

## Architecture (INTENTIONALLY BAD!)
- Main.java: SATU METHOD BESAR (main)
- Update dan render TERCAMPUR
- While(true) loop, semua di dalam
- No delta time, fixed sleep
- No methods, semua di main

## File Structure
```
src/
├── Main.java           # Monolithic 150+ lines
├── entities/
│   ├── NPC.java       # x, y, speed
│   └── Coin.java      # x, y, fallSpeed
└── utils/
    └── GridRenderer.java  # Static print utils
```

## Main.java Structure
```java
public static void main(String[] args) {
    NPC npc = new NPC(0, 5);
    Coin coin = new Coin(random(), 0);
    int score = 0;
    
    while (true) {
        // ❌ Update NPC
        npc.moveRight();
        // ❌ IMMEDIATELY draw
        GridRenderer.drawEntity('N', npc.x, npc.y);
        
        // ❌ Update coin
        coin.fall();
        // ❌ IMMEDIATELY draw
        GridRenderer.drawEntity('C', coin.x, coin.y);
        
        // ❌ Collision + score + draw mixed
        if (collision) {
            score += 10;
            coin.respawn(random());
            System.out.println("Score: " + score);
        }
        
        Thread.sleep(100);
    }
}
```

## Entity Classes
**NPC**: int x,y,speed | moveRight()
**Coin**: int x,y,fallSpeed | fall(), respawn()

## GridRenderer
Static methods:
- clearScreen()
- drawEntity(char, x, y)
- Grid 10x10, '░' empty

## Demonstrations
1. Add Thread.sleep(100) after draws → show lag
2. Create 10 coins → show massive slowdown

## Anti-Patterns Required
1. ❌ No separation
2. ❌ No delta time
3. ❌ Everything in main
4. ❌ Side effects everywhere

## Comments to Add
`// ❌ PROBLEM: Update and draw mixed`
`// ❌ PROBLEM: Frame-rate dependent`

## Testing
Create MainTest.java:
```java
@Test
void test() {
    fail("Cannot test this!");
}
```

## Output
```
░░░░░░░░░░
░░░░C░░░░░
░N░░░░░░░░
Score: 0
```

Include PROBLEM.md listing issues.

Implementasikan dengan fokus anti-pattern demonstration!
```
