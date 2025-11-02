# Movement Demonstration - Branch 09-00

## Game Mechanics

### NPC (Non-Player Character)
- **Symbol**: `N`
- **Movement**: Horizontal (left to right)
- **Starting position**: (X=0, Y=5) - left side, middle height
- **Behavior**:
  - Moves right by 1 pixel per frame
  - X coordinate increases: 0 → 1 → 2 → 3 ... → 9
  - When X reaches 10 (edge), wraps back to X=0
  - Y coordinate stays constant at 5

### Coin
- **Symbol**: `C`
- **Movement**: Vertical (top to bottom / falling)
- **Starting position**: (X=random, Y=0) - random column, top row
- **Behavior**:
  - Falls down by 1 pixel per frame
  - Y coordinate increases: 0 → 1 → 2 → 3 ... → 9
  - X coordinate stays constant
  - When Y reaches 10 (bottom edge), respawns at top with new random X

## Visual Example

### Frame 1
```
??????????
????????C?  <- Coin at (8, 1)
??????????
??????????
??????????
?N????????  <- NPC at (1, 5)
??????????
```

### Frame 2
```
??????????
??????????
????????C?  <- Coin fell to (8, 2)
??????????
??????????
??N???????  <- NPC moved to (2, 5)
??????????
```

### Frame 3
```
??????????
??????????
??????????
????????C?  <- Coin fell to (8, 3)
??????????
???N??????  <- NPC moved to (3, 5)
??????????
```

## Coordinate System

```
       X →
  ┌──────────────────
Y │  (0,0)  (1,0)  (2,0) ...
↓ │  (0,1)  (1,1)  (2,1) ...
  │  (0,2)  (1,2)  (2,2) ...
  │   ...
```

- **X**: Column (horizontal position)
- **Y**: Row (vertical position)
- Grid is 10x10 (columns 0-9, rows 0-9)

## Movement Summary

| Entity | Direction | Axis | Changes |
|--------|-----------|------|---------|
| NPC    | Horizontal (→) | X | X increases, Y constant |
| Coin   | Vertical (↓)   | Y | Y increases, X constant |

## Why This Design?

For Week 09, we use simple 1D movements:
- **NPC horizontal**: Easy to demonstrate game loop without complex AI
- **Coin falling**: Gravity simulation (like Flappy Bird obstacles)
- **No walls**: Clean grid, no collision obstacles yet (comes in Week 10)

This is similar to:
- Flappy Bird: obstacles fall, bird "moves" horizontally
- Endless Runner: player fixed position, obstacles come from right
- Galaga: enemies move in patterns, player moves horizontally

Next weeks will add:
- Week 10: Walls, obstacles with different behaviors
- Week 11: Player control (WASD input)
- Week 12: Full dungeon with boss battle
