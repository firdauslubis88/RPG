# Master Prompt for Claude Code - Dungeon Escape Project

Copy prompt ini ke Claude Code saat memulai development project:

---

## 🎯 Project Context

Saya sedang membuat educational project untuk mahasiswa semester 3 belajar Design Patterns melalui game development. Project ini bernama **"Dungeon Escape"** - text-based RPG game yang dibangun secara progressive selama 4 minggu.

**PENTING**: Baca PROJECT BRIEF lengkap terlebih dahulu di file `PROJECT-BRIEF.md` untuk memahami big picture.

---

## 📚 Quick Summary

### What We're Building
Text-based roguelike game dengan ASCII art di terminal. Game berkembang dari simple (NPC auto-move) ke complex (full player control + boss battle) sambil menerapkan 8 design patterns.

### Learning Progression
- **Week 9**: Foundation (Game Loop + Singleton)
- **Week 10**: World Building (Factory + Pool)
- **Week 11**: Interactivity (Command + Observer)  
- **Week 12**: Complexity (Strategy + State)

### Tech Stack
- Pure Java (JDK 11+)
- No external dependencies
- Terminal-based (no GUI)
- Standard library only

---

## 🌳 Git Structure

```
main
├── week-09-foundation/
│   ├── 09-00-without-game-loop    ❌ Problem demo
│   ├── 09-01-with-game-loop       ✅ Solution
│   ├── 09-02-without-singleton    ❌ Problem demo
│   ├── 09-03-with-singleton       ✅ Solution
│   └── 09-analysis/               📊 Comparison
│
├── week-10-world-building/
│   └── (similar structure)
│
├── week-11-interactivity/
│   └── (similar structure)
│
└── week-12-complexity/
    └── (similar structure)
```

---

## 📋 What I Need From You

### For Each Branch
Saya akan memberikan 3 files:
1. **SCENARIO.md** - Context, problems, learning objectives
2. **GUIDELINE.md** - Technical implementation details
3. **PROMPT.md** - Specific instructions for that branch

### Your Task
1. Read all 3 files carefully
2. Implement according to guidelines
3. For "problem branches": Make intentionally bad code
4. For "solution branches": Make clean, professional code
5. Include comprehensive tests
6. Add clear comments explaining patterns

---

## 🎯 Key Principles

### 1. Problem-First Approach
Every pattern starts with demonstrating the problem:
- Show bad design first (intentionally!)
- Make pain points obvious
- Include bugs if needed for demonstration
- Add `// ❌ PROBLEM:` comments

### 2. Real-World Justification
Every demonstration must explain WHY:
- How does this happen in production?
- What are the consequences?
- Why does this matter?

### 3. Progressive Features
Unlock game features based on patterns learned:
- Week 9-10: NPC auto-moves (no player control)
- Week 11: Player control introduced
- Week 12: Full game with boss battle

### 4. Clean Code Standards
Even though it's educational:
- Professional quality
- Well-tested (70%+ coverage)
- Clear documentation
- Maintainable structure

---

## ⚠️ Critical Guidelines

### For Problem Branches (XX-XX-without-, hardcoded-)
**DO**:
- ✅ Make code obviously problematic
- ✅ Include intentional anti-patterns
- ✅ Add comments marking problems
- ✅ Make students "feel the pain"

**DON'T**:
- ❌ Make it clean or optimized
- ❌ Use best practices
- ❌ Make it testable
- ❌ Apply patterns (that's next branch!)

### For Solution Branches (XX-XX-with-)
**DO**:
- ✅ Clean, professional implementation
- ✅ Apply design pattern correctly
- ✅ Include comprehensive tests
- ✅ Add clear documentation
- ✅ Note trade-offs in comments

**DON'T**:
- ❌ Over-engineer beyond pattern demo
- ❌ Add unnecessary features
- ❌ Use external libraries
- ❌ Make it too complex

### For Analysis Branches
**DO**:
- ✅ Create comparison tables
- ✅ Include metrics (LOC, performance)
- ✅ Provide when-to-use guidelines
- ✅ Document migration steps

---

## 📊 Success Criteria

### Code Quality
- [ ] All tests passing
- [ ] No compiler warnings
- [ ] Clean structure (max 30 lines per method)
- [ ] Clear naming conventions

### Pattern Implementation
- [ ] Pattern correctly applied
- [ ] Trade-offs documented
- [ ] Real-world justification clear
- [ ] Comparison with previous approach

### Educational Value
- [ ] Problem clearly demonstrated
- [ ] Solution obviously better
- [ ] Students can run and observe
- [ ] Teaching notes included

---

## 🚀 Getting Started

### Step 1: Read Context
1. Read `PROJECT-BRIEF.md` (full context)
2. Read week overview (e.g., `WEEK-09-OVERVIEW.md`)
3. Understand learning objectives

### Step 2: Read Branch Docs
For each branch I ask you to implement:
1. Read `SCENARIO.md` (what & why)
2. Read `GUIDELINE.md` (how)
3. Read `PROMPT.md` (specific instructions)

### Step 3: Implement
1. Create file structure as specified
2. Implement according to guidelines
3. Add tests
4. Add documentation
5. Test demonstrations work

### Step 4: Verify
1. Run all tests
2. Test demonstration scenarios
3. Check code quality
4. Verify learning objectives met

---

## 📝 File Structure Template

Every branch should have:
```
branch-XX-YY-name/
├── src/
│   ├── Main.java
│   ├── [other source files]
│   └── [package structure]
├── test/
│   └── [test files]
├── docs/
│   ├── SCENARIO.md
│   ├── GUIDELINE.md
│   └── PROMPT.md
└── [optional: PROBLEM.md or SOLUTION.md]
```

---

## 🎓 Teaching Philosophy

Remember: The goal is NOT to build the best game, but the best LEARNING EXPERIENCE.

Every line of code should serve one purpose:
**Help students understand WHY patterns matter.**

Questions to ask yourself:
- Does this demonstrate the problem clearly?
- Will students understand why this is better?
- Is the real-world relevance obvious?
- Can they apply this knowledge elsewhere?

---

## 🤝 How We'll Work Together

### I Will Provide
- Clear learning objectives
- Detailed implementation guidelines
- Real-world context and justification
- Success criteria

### You Will Provide
- Clean, working implementation
- Comprehensive tests
- Clear documentation
- Demonstration code that works

### We Will Iterate
- I may ask for clarifications
- I may request modifications
- I may ask you to add demonstrations
- We'll ensure teaching quality is high

---

## 🎯 Current Task

[I will specify which branch to work on here when I give you the actual task]

**Branch**: [To be specified]
**Week**: [To be specified]
**Pattern**: [To be specified]

Please confirm you've read:
- [ ] PROJECT-BRIEF.md
- [ ] Current branch SCENARIO.md
- [ ] Current branch GUIDELINE.md
- [ ] Current branch PROMPT.md

Then proceed with implementation following all guidelines above.

Let's build something educational! 🚀
