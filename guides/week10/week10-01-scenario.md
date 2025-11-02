# Branch: 10-01-hardcoded-spawning

## 🎯 Learning Objective
Memahami MASALAH dari hard-coded object creation: **tight coupling** dan **violates Open/Closed Principle**.

---

## 📖 Skenario: Hard-Coded Spawning System

### Context
Game sudah punya game loop + singleton (Week 9). Sekarang perlu spawn obstacles:
- **Spike**: Static obstacle (damage on touch)
- **Goblin**: Patrol pattern (moves left-right)
- **Wolf**: Chase NPC if in range

**Problem**: WorldController hard-codes ALL obstacle creation.

### Architecture Problem
```java
// WorldController.java
switch(randomType) {
    case 0: obstacle = new Spike(x, y); break;
    case 1: obstacle = new Goblin(x, y); break;
    case 2: obstacle = new Wolf(x, y); break;
    // Want to add Missile? MODIFY THIS FILE!
}
```

---

## 🔴 Problems

### Problem 1: Tight Coupling
**Issue**: WorldController KNOWS all concrete obstacle classes.

**Pain Points**:
- Import 10 obstacle classes
- Can't compile without all implementations
- Adding Boss = modify WorldController

**Real-World**: In teams, WorldController = merge conflict hotspot. Every developer adding obstacles touches same file.

### Problem 2: Violates Open/Closed Principle
**Issue**: Cannot add new obstacle without modifying existing code.

**OCP**: "Open for extension, closed for modification"

**Before**: Adding Missile obstacle
- Modify: WorldController (switch-case)
- Risk: Break existing Spike/Goblin/Wolf
- Testing: Re-test entire spawning system

### Problem 3: Cannot Unit Test Easily
**Issue**: WorldController tightly coupled to concrete classes.

```java
@Test
void testSpawning() {
    // Must have Spike, Goblin, Wolf compiled!
    // Cannot mock obstacle creation
}
```

---

## 🧪 Demonstration Points

### Demo 1: Add Boss Obstacle
**Task**: Add new Boss obstacle type.

**Files Modified**:
1. Boss.java (new)
2. WorldController.java (modify switch-case)

**Risk**: Typo in switch → breaks existing spawning!

**Expected Learning**: "Adding feature = modifying existing code = risk!"

### Demo 2: Merge Conflict Scenario
**Setup**: 2 developers add obstacles simultaneously.

Developer A adds Missile:
```java
case 3: return new Missile();
```

Developer B adds Trap:
```java
case 3: return new Trap();  // Conflict!
```

**Result**: Merge conflict in WorldController!

---

## 📊 Metrics

| Aspect | Hard-Coded |
|--------|------------|
| Coupling | ❌ High (knows all types) |
| OCP compliance | ❌ No (must modify) |
| Merge conflicts | ❌ High risk |
| Testing | ❌ Difficult (mocking hard) |
| Files modified (add type) | 2+ |

---

## 🎓 Teaching Notes

### Key Point: Open/Closed Principle
"Classes should be open for extension, but closed for modification."

**Example**:
- ✅ Good: Add Boss.java (extension, no modification)
- ❌ Bad: Modify WorldController switch-case (modification)

### Common Questions

**Q**: "Kenapa tidak langsung buat semua obstacle types dari awal?"
**A**: Requirements change! Client mungkin tambah obstacle baru. Code harus flexible.

**Q**: "Switch-case kan readable?"
**A**: Ya untuk 3 types. Bagaimana dengan 20 types? 50? Code smell!

---

## ✅ Success Criteria

Students should observe:
- [ ] WorldController imports ALL obstacle classes
- [ ] Switch-case for obstacle creation
- [ ] Adding new type requires modification
- [ ] Merge conflict risk demonstrated

**Next**: Branch 10-02 solves this with Factory Method! 🏭
