@echo off
echo ========================================
echo   DUNGEON ESCAPE - Gameplay Test
echo ========================================
echo.
echo This will run the full game with Game State Pattern.
echo.
echo Game Flow:
echo   MENU - Select difficulty and level
echo   PLAYING - Navigate dungeon (WASD to move, Q to quit)
echo   BATTLE - Fight the boss at dungeon exit
echo   VICTORY/DEFEAT - Game end state
echo.
echo Press Ctrl+C to stop early
echo.
pause

cd bin
java Main
cd ..

echo.
echo ========================================
echo Game session ended!
echo ========================================
pause
