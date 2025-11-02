# Branch: 09-00-without-game-loop

## 🎯 Learning Objective
Memahami MENGAPA game loop paradigm diperlukan dengan mendemonstrasikan problem nyata dari approach monolithic.

---

## 📖 Skenario: Game Tanpa Separation of Concerns

### Context
Game sederhana dengan:
- **NPC** (Non-Player Character) yang bergerak otomatis dari kiri ke kanan
- **Coin** yang spawn random dan jatuh ke bawah
- **Collision detection**: NPC nabrak coin → score++
- **Display**: Terminal-based (print grid)

### Architecture Problem
Semua logic (update, render) ada dalam **SATU METHOD BESAR** yang loop selamanya.

```
main() {
    while(true) {
        // Update NPC position
        // Draw NPC
        // Update Coin position  
        // Draw Coin
        // Check collision
        // Draw score
        // ❌ SEMUA TERCAMPUR!
    }
}
```

---

## 🔴 Problems yang Akan Muncul

### Problem 1: Frame Rate Coupling
**Issue**: Game logic speed terikat dengan rendering speed.

**Real-World Scenario**: 
Bayangkan game kompleks dengan:
- 100 entities di layar
- Particle effects
- Shadow rendering

Jika rendering lambat (GPU bottleneck):
- Physics simulation ikut lambat
- Movement jadi sluggish
- Unplayable di hardware berbeda

**Real-World Justification**:
- **GPU bottleneck**: Complex 3D scenes dengan lighting
- **Network lag**: Multiplayer games rendering remote state
- **Low-end devices**: Mobile games di perangkat murah
- **VSync waiting**: Monitor refresh rate sync

### Problem 2: Cannot Test Logic
**Issue**: Tidak bisa unit test tanpa rendering.

**Real-World Scenario**:
Developer ingin test:
- Collision detection bekerja?
- Scoring logic benar?
- Movement speed konsisten?

Dengan approach ini, test HARUS render → slow, flaky.

**Real-World Justification**:
- **CI/CD pipeline**: Automated testing tanpa display
- **Parallel execution**: Multiple tests simultaneously
- **Fast feedback**: Test suite < 5 minutes
- **Reliability**: Deterministic, no GUI flakiness

### Problem 3: Hard to Maintain
**Issue**: Code spaghetti - sulit trace.

**Real-World Scenario**:
Bug: "NPC movement tidak smooth"
Developer baca 500 baris untuk cari:
- Update position logic?
- Render position code?
- Collision check?

Semua tercampur!

---

## 🧪 Demonstration Points

### Demo 1: Artificial Rendering Delay
**Purpose**: Membuktikan logic terikat render speed.

**Why This Matters in Real World**:
Dalam production, rendering delay terjadi karena:
- GPU processing heavy scenes
- Network synchronization delays
- Display refresh rate variations
- Resource contention (CPU/GPU)

**Implementation**:
1. Tambahkan `Thread.sleep(100)` di setiap render
2. Observe: NPC movement jadi SANGAT lambat
3. Calculate: Should 2 px/sec → actually 0.5 px/sec

**Expected Learning**: 
"Update logic shouldn't wait for render!"

### Demo 2: Try to Unit Test
**Purpose**: Membuktikan untestability.

**Why This Matters in Real World**:
Modern development requires:
- Automated CI/CD pipelines (no display)
- Regression testing (catch bugs early)
- Refactoring confidence (tests as safety net)

**Implementation**:
1. Coba buat test collision detection
2. Realize: cannot separate logic from rendering
3. Test requires terminal → impossible for CI

**Expected Learning**:
"We need separation for testability!"

---

## 📊 Metrics to Track

| Aspect | Without Separation |
|--------|-------------------|
| Lines in main | 150+ |
| Testability | ❌ Requires display |
| Frame consistency | ❌ Varies |
| Maintainability | ❌ Spaghetti |
| CI/CD compatible | ❌ No |

---

## 🎓 Teaching Notes

### Key Points
1. **Paradigm Shift**: Web = passive, Game = active loop
2. **Separation**: update() = logic, draw() = view
3. **Industry Standard**: All professional games separate

### Common Questions

**Q**: "Kenapa tidak render di dalam update?"
**A**: Render speed unpredictable. Logic harus constant 60 FPS regardless.

**Q**: "Bukankah lebih simple di satu tempat?"
**A**: Ya untuk toy. Tidak untuk real. Seperti MVC di Spring Boot.

**Q**: "Game mobile lag tapi masih playable, kenapa?"
**A**: Mereka pisahkan! Render skip frames (choppy), logic tetap (responsive).
