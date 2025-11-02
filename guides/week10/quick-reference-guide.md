# Quick Reference Guide - Week 9-10 Files

**Purpose**: Fast navigation untuk semua files yang sudah dibuat  
**Total Files**: 31 files

---

## 📁 Week 9: Foundation & Singleton (13 files)

### Branch 09-00: without-game-loop (Problem)
| File | Purpose | Key Content |
|------|---------|-------------|
| **scenario** | Learning objectives | Monolithic loop problems, frame rate coupling, untestable code |
| **guideline** | Implementation details | Intentionally bad code, mixed update/render, anti-patterns |
| **prompt** | Claude Code kickstart | Copy-paste ready prompt dengan specs lengkap |

### Branch 09-01: with-game-loop (Solution)
| File | Purpose | Key Content |
|------|---------|-------------|
| **scenario** | Solution explanation | Separated concerns, delta time, testability |
| **guideline** | Clean implementation | GameEngine class, update/draw separation, tests |
| **prompt** | Claude Code kickstart | Refactoring guide dari 09-00 |

### Branch 09-02: without-singleton (Problem)
| File | Purpose | Key Content |
|------|---------|-------------|
| **scenario** | Multiple instances bug | State inconsistency, object drilling hell |
| **guideline** | Buggy implementation | Public constructor, HUD creates own instance |
| **prompt** | Claude Code kickstart | Intentional bug demonstration |

### Branch 09-03: with-singleton (Solution)
| File | Purpose | Key Content |
|------|---------|-------------|
| **scenario** | Singleton solution | Single instance guarantee, no drilling |
| **guideline** | Proper implementation | Private constructor, getInstance(), reset() |
| **prompt** | Claude Code kickstart | Singleton pattern complete |

### Branch 09-analysis
| File | Purpose | Key Content |
|------|---------|-------------|
| **analysis** | Comprehensive comparison | Metrics, benchmarks, migration guides, when-to-use |

---

## 📁 Week 10: World Building & Object Management (15 files)

### Branch 10-01: hardcoded-spawning (Problem)
| File | Purpose | Key Content |
|------|---------|-------------|
| **scenario** | Tight coupling problem | Switch-case hell, OCP violation, merge conflicts |
| **guideline** | Hard-coded implementation | WorldController with switch, imports all types |
| **prompt** | Claude Code kickstart | Demonstrate tight coupling |

### Branch 10-02: with-factory (Solution)
| File | Purpose | Key Content |
|------|---------|-------------|
| **scenario** | Factory Method solution | Decoupling, OCP compliance, extensibility |
| **guideline** | Factory implementation | Abstract factory, concrete factories, clean WorldController |
| **prompt** | Claude Code kickstart | Factory pattern complete |

### Branch 10-03: gc-performance-issue (Problem)
| File | Purpose | Key Content |
|------|---------|-------------|
| **scenario** | GC lag problem | 1200+ allocations/min, 180ms pauses, frame drops |
| **guideline** | High spawn rate | Performance monitoring, stress test, metrics |
| **prompt** | Claude Code kickstart | Demonstrate GC lag |

### Branch 10-04: with-pool (Solution)
| File | Purpose | Key Content |
|------|---------|-------------|
| **scenario** | Object Pool solution | 99% reduction, no GC lag, stable FPS |
| **guideline** | Pool implementation | Generic ObjectPool, Poolable interface, acquire/release |
| **prompt** | Claude Code kickstart | Pool pattern complete |

### Branch 10-analysis
| File | Purpose | Key Content |
|------|---------|-------------|
| **analysis** | Comprehensive comparison | Factory + Pool synergy, performance benchmarks, migration |

---

## 📁 Supporting Documents (3 files)

| File | Purpose | Key Content |
|------|---------|-------------|
| **progress-report** | Status summary | What's done, what's pending, statistics |
| **continuation-prompt** | Week 11-12 starter | Complete context for new conversation |
| **final-summary** | Overall achievement | Metrics, quality checks, next steps |

---

## 🔍 How to Use This Reference

### For Instructors:
1. **Teaching prep**: Read SCENARIO for learning objectives
2. **Demo prep**: Use GUIDELINE demonstration scripts
3. **Answer questions**: Check analysis docs for comparisons

### For Claude Code:
1. **Start branch**: Copy PROMPT.md
2. **Implementation**: Follow GUIDELINE.md
3. **Verification**: Check success criteria

### For Students:
1. **Understand WHY**: Read SCENARIO.md
2. **Learn HOW**: Study GUIDELINE.md
3. **Practice**: Use PROMPT for own implementation

---

## 📊 Content Statistics by File Type

### SCENARIO Files (9 total)
- Average length: ~800 lines
- Key sections: Learning objectives, problems, demonstrations, teaching notes
- Focus: WHY and WHAT

### GUIDELINE Files (9 total)
- Average length: ~600 lines
- Key sections: File structure, implementation, tests, demos, critical notes
- Focus: HOW (technical details)

### PROMPT Files (9 total)
- Average length: ~400 lines
- Key sections: Context, specs, code structure, success criteria
- Focus: Copy-paste ready kickstart

### ANALYSIS Files (2 total)
- Average length: ~1200 lines
- Key sections: Comparisons, metrics, benchmarks, migration, when-to-use
- Focus: Complete understanding

---

## 🎯 Quick Navigation by Topic

### Game Loop Topics
- **Problem**: week09-00-scenario.md
- **Solution**: week09-01-scenario.md
- **Comparison**: week09-analysis.md (Part 1)

### Singleton Topics
- **Problem**: week09-02-scenario.md
- **Solution**: week09-03-scenario.md
- **Comparison**: week09-analysis.md (Part 2)

### Factory Method Topics
- **Problem**: week10-01-scenario.md
- **Solution**: week10-02-scenario.md
- **Comparison**: week10-analysis.md (Part 1)

### Object Pool Topics
- **Problem**: week10-03-scenario.md
- **Solution**: week10-04-scenario.md
- **Comparison**: week10-analysis.md (Part 2)

---

## 🔑 Key Files for Quick Reference

### Most Important Files (Must Read):
1. **week09-analysis.md** - Complete Week 9 understanding
2. **week10-analysis.md** - Complete Week 10 understanding
3. **continuation-prompt-week11-12.md** - For continuing project

### For Implementation (Claude Code):
1. **week09-01-guideline.md** - Game loop implementation
2. **week09-03-guideline.md** - Singleton implementation
3. **week10-02-guideline.md** - Factory implementation
4. **week10-04-guideline.md** - Pool implementation

### For Teaching (Instructors):
1. All **-scenario.md** files (learning objectives)
2. All **-analysis.md** files (comprehensive comparisons)
3. **final-summary-week9-10.md** (overall status)

---

## 💡 Tips for Navigation

### By Learning Goal:
- **Understand patterns**: Read SCENARIO files
- **Implement patterns**: Read GUIDELINE files
- **Quick start**: Use PROMPT files
- **Deep dive**: Read ANALYSIS files

### By Role:
- **Student**: SCENARIO → practice → GUIDELINE
- **Instructor**: SCENARIO → GUIDELINE demos → ANALYSIS
- **Developer**: PROMPT → GUIDELINE → implementation

### By Time Available:
- **5 minutes**: Read SCENARIO
- **15 minutes**: Read SCENARIO + key sections of GUIDELINE
- **30 minutes**: Read full SCENARIO + GUIDELINE
- **1 hour**: Read SCENARIO + GUIDELINE + ANALYSIS

---

## 🎓 Learning Path Recommendation

### Week 9 Learning Path:
1. week09-00-scenario.md (understand problem)
2. week09-01-scenario.md (understand solution)
3. week09-01-guideline.md (implement)
4. week09-02-scenario.md (understand problem)
5. week09-03-scenario.md (understand solution)
6. week09-03-guideline.md (implement)
7. week09-analysis.md (complete picture)

### Week 10 Learning Path:
1. week10-01-scenario.md (understand problem)
2. week10-02-scenario.md (understand solution)
3. week10-02-guideline.md (implement)
4. week10-03-scenario.md (understand problem)
5. week10-04-scenario.md (understand solution)
6. week10-04-guideline.md (implement)
7. week10-analysis.md (complete picture)

---

## 📦 File Naming Convention

Format: `weekXX-YY-name-type.md`

Where:
- **XX**: Week number (09, 10, 11, 12)
- **YY**: Branch number (00, 01, 02, 03, 04)
- **name**: Descriptive name (without-game-loop, with-factory, etc)
- **type**: scenario | guideline | prompt | analysis

Examples:
- `week09-01-scenario.md` → Week 9, Branch 1, Scenario doc
- `week10-04-guideline.md` → Week 10, Branch 4, Guideline doc

---

## ✅ Verification Checklist

Use this to verify you have all files:

### Week 9 Files (13) ✅
```
□ week09-00-scenario.md
□ week09-00-guideline.md
□ week09-00-prompt.md
□ week09-01-scenario.md
□ week09-01-guideline.md
□ week09-01-prompt.md
□ week09-02-scenario.md
□ week09-02-guideline.md
□ week09-02-prompt.md
□ week09-03-scenario.md
□ week09-03-guideline.md
□ week09-03-prompt.md
□ week09-analysis.md
```

### Week 10 Files (15) ✅
```
□ week10-01-scenario.md
□ week10-01-guideline.md
□ week10-01-prompt.md
□ week10-02-scenario.md
□ week10-02-guideline.md
□ week10-02-prompt.md
□ week10-03-scenario.md
□ week10-03-guideline.md
□ week10-03-prompt.md
□ week10-04-scenario.md
□ week10-04-guideline.md
□ week10-04-prompt.md
□ week10-analysis.md
```

### Supporting Files (3) ✅
```
□ progress-report-week9-10.md
□ continuation-prompt-week11-12.md
□ final-summary-week9-10.md
```

**Total**: 31 files ✅

---

*This quick reference guide helps you navigate all Week 9-10 documentation efficiently!*
