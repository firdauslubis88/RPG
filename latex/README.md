# LaTeX Materials for Week 09

This directory contains TikZ diagrams and Beamer presentation for Week 09: Game Loop Pattern & Singleton Pattern.

## Directory Structure

```
latex/
├── diagrams/               # Standalone TikZ diagrams
│   ├── uml-comparison.tex
│   ├── singleton-pattern.tex
│   ├── game-loop-pattern.tex
│   ├── object-drilling.tex
│   └── performance-comparison.tex
├── beamer/                 # Beamer presentations
│   └── week09-presentation.tex
└── README.md              # This file
```

## Standalone Diagrams

Each diagram can be compiled independently:

### 1. UML Comparison (`uml-comparison.tex`)
**Purpose**: Visual comparison of architecture across all 4 branches

**Compile**:
```bash
cd diagrams
pdflatex uml-comparison.tex
```

**Shows**:
- 09-00: Monolithic structure
- 09-01: Game loop separation
- 09-02: Object drilling problem
- 09-03: Singleton solution

### 2. Singleton Pattern (`singleton-pattern.tex`)
**Purpose**: Detailed visualization of Singleton pattern structure

**Compile**:
```bash
cd diagrams
pdflatex singleton-pattern.tex
```

**Shows**:
- Three components (static instance, private constructor, getInstance())
- Code implementation
- Usage examples

### 3. Game Loop Pattern (`game-loop-pattern.tex`)
**Purpose**: Flow diagram of game loop execution

**Compile**:
```bash
cd diagrams
pdflatex game-loop-pattern.tex
```

**Shows**:
- Initialize → Loop → Update → Draw → Sync
- Delta time calculation
- FPS control
- Benefits annotation

### 4. Object Drilling (`object-drilling.tex`)
**Purpose**: Before/after comparison of object drilling problem

**Compile**:
```bash
cd diagrams
pdflatex object-drilling.tex
```

**Shows**:
- 09-02: Parameter passing through 4 levels (problem)
- 09-03: Direct getInstance() access (solution)
- Level annotations and problem/solution boxes

### 5. Performance Comparison (`performance-comparison.tex`)
**Purpose**: Charts comparing metrics across branches

**Compile**:
```bash
cd diagrams
pdflatex performance-comparison.tex
```

**Shows**:
- FPS comparison (2 → 60)
- Lines of code (150 → 3)
- Constructor parameters (6 → 0)
- GameManager instances (2 → 1)
- Test coverage (0% → 100%)

## Complete Presentation

### Week 09 Presentation (`week09-presentation.tex`)

**Purpose**: Complete Beamer presentation integrating ALL Week 09 content

**Compile**:
```bash
cd beamer
pdflatex week09-presentation.tex
pdflatex week09-presentation.tex  # Run twice for TOC
```

**Content** (90+ slides):
1. **Introduction**: Learning journey and progressive approach
2. **Branch 09-00**: Monolithic anti-pattern problems
3. **Branch 09-01**: Game loop pattern solution
4. **Branch 09-02**: New problems with features
5. **Branch 09-03**: Singleton pattern solution
6. **Performance Comparison**: Metrics and charts
7. **Design Analysis**: Trade-offs and alternatives
8. **Summary**: Complete journey overview
9. **Discussion**: Questions for classroom
10. **Assessment**: Rubric and learning outcomes

### Features:
- **All diagrams embedded** (TikZ code included)
- **Code listings** with syntax highlighting
- **Performance charts** with pgfplots
- **Discussion questions** for classroom
- **Assessment rubric** for grading
- **Animations** (use `\pause` for step-by-step reveal)

## Using in Overleaf

### Option 1: Upload Individual Diagrams
1. Upload `.tex` files to Overleaf project
2. Compile each separately
3. Include generated PDFs in your main document:
   ```latex
   \includegraphics{uml-comparison.pdf}
   ```

### Option 2: Include TikZ Directly
1. Copy TikZ code from standalone files
2. Paste into your Beamer/article document
3. Remove `\documentclass` and `\begin{document}` wrappers
4. Keep only the `\begin{tikzpicture}...\end{tikzpicture}` part

### Option 3: Use Complete Presentation
1. Upload `week09-presentation.tex` to Overleaf
2. Compile with `pdflatex`
3. Customize as needed (colors, theme, content)

## Required Packages

All files use these LaTeX packages:
```latex
\usepackage{tikz}
\usepackage{pgfplots}
\usepackage{listings}  % For code
\usepackage{xcolor}    % For colors
\usepackage{fontawesome5}  % For icons (presentation only)
```

TikZ libraries used:
```latex
\usetikzlibrary{positioning,shapes,arrows,calc,decorations.pathreplacing,fit}
```

## Customization

### Colors
Change colors in presentation preamble:
```latex
\definecolor{problemred}{RGB}{220,50,50}
\definecolor{solutiongreen}{RGB}{50,150,50}
\definecolor{codeblue}{RGB}{0,100,200}
```

### Theme
Change Beamer theme:
```latex
\usetheme{Madrid}  % Or: Berlin, Copenhagen, Warsaw, etc.
\usecolortheme{default}  % Or: dolphin, crane, beetle, etc.
```

### Aspect Ratio
Change slide dimensions:
```latex
\documentclass[aspectratio=169]{beamer}  % 16:9 (widescreen)
\documentclass[aspectratio=43]{beamer}   % 4:3 (classic)
```

## Best Practices

### For Teaching
1. **Use animation**: Add `\pause` between items for step-by-step reveal
2. **Highlight code**: Use `\alert{...}` for important code lines
3. **Add notes**: Use `\note{...}` for speaker notes
4. **Interactive**: Ask questions, use `\only<2>{...}` for reveals

### For Diagrams
1. **Export to PDF**: Compile standalone diagrams to PDF first
2. **Reuse**: Include PDFs in multiple documents
3. **Scale**: Use `\scalebox{0.8}{...}` to resize if needed
4. **Crop**: Use `[trim=...]` option for fine-tuning

### For Overleaf
1. **Single file**: Upload complete presentation as one `.tex` file
2. **Fast compile**: Use `pdflatex` (not `xelatex` unless needed)
3. **Cache**: Overleaf caches compiled diagrams for speed
4. **Version**: Set `\pgfplotsset{compat=1.18}` for latest features

## Examples of Use

### In Article/Report
```latex
\documentclass{article}
\usepackage{tikz}
\usetikzlibrary{positioning,shapes,arrows}

\begin{document}
\section{Architecture Comparison}

\input{diagrams/uml-comparison}  % Include TikZ code

\end{document}
```

### In Beamer Slide
```latex
\begin{frame}{Singleton Pattern}
    \begin{center}
        \scalebox{0.7}{
            \input{diagrams/singleton-pattern}
        }
    \end{center}
\end{frame}
```

### As Standalone PDF
```bash
pdflatex uml-comparison.tex
# Generates: uml-comparison.pdf
```

## Troubleshooting

### "Undefined control sequence"
- **Cause**: Missing package or library
- **Fix**: Add required `\usepackage{...}` or `\usetikzlibrary{...}`

### "Dimension too large"
- **Cause**: Coordinates out of range
- **Fix**: Scale down with `\scalebox{0.5}{...}`

### "Missing $ inserted"
- **Cause**: Math mode issue in text
- **Fix**: Escape special characters: `\$`, `\_`, `\&`

### Slow compilation
- **Cause**: Complex TikZ diagrams
- **Fix**: Use `\includegraphics` instead of re-compiling TikZ

## Tips for Presentations

1. **Practice**: Run through slides before lecture
2. **Backup**: Keep PDF backup in case of LaTeX errors
3. **Handouts**: Generate handout mode with `\documentclass[handout]{beamer}`
4. **Notes**: Use `\setbeameroption{show notes on second screen}` for notes
5. **Timing**: Estimate ~2 minutes per slide for teaching

## Additional Resources

- **TikZ Manual**: [CTAN](https://ctan.org/pkg/pgf)
- **Beamer User Guide**: [CTAN](https://ctan.org/pkg/beamer)
- **PGFPlots Gallery**: [SourceForge](http://pgfplots.sourceforge.net/gallery.html)
- **Overleaf Docs**: [overleaf.com/learn](https://www.overleaf.com/learn)

## License

These materials are created for educational purposes for the OOP course. Feel free to adapt and reuse for teaching.

---

**Created with**: Claude Code 🤖
**For**: Week 09 - Game Loop Pattern & Singleton Pattern
**Date**: 2025
