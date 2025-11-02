@echo off
echo ========================================
echo   DUNGEON ESCAPE - Extended Gameplay Test
echo ========================================
echo.
echo This will run the game for 30 seconds.
echo Watch for:
echo   - Wolf (W) chasing NPC (N)
echo   - Collision with Spike (^) - damages NPC
echo   - Collision with Goblin (G) - damages NPC
echo   - Collision with Wolf (W) - damages NPC
echo   - Coins (C) collected - increases score
echo   - HP decreases when hit
echo.
echo Press Ctrl+C to stop early
echo.
pause

cd bin\10-01-hardcoded-spawning
timeout /t 30 java Main
cd ..\..

echo.
echo ========================================
echo Test completed!
echo ========================================
pause
