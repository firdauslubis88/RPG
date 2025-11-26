@echo off
echo ========================================
echo   DUNGEON ESCAPE - Build Script
echo ========================================
echo.

echo Cleaning bin folder...
if exist bin\*.class del /q bin\*.class
if exist bin\engine rd /s /q bin\engine
if exist bin\gamestate rd /s /q bin\gamestate
if exist bin\battle rd /s /q bin\battle
if exist bin\commands rd /s /q bin\commands
if exist bin\difficulty rd /s /q bin\difficulty
if exist bin\entities rd /s /q bin\entities
if exist bin\events rd /s /q bin\events
if exist bin\factories rd /s /q bin\factories
if exist bin\input rd /s /q bin\input
if exist bin\level rd /s /q bin\level
if exist bin\obstacles rd /s /q bin\obstacles
if exist bin\pools rd /s /q bin\pools
if exist bin\systems rd /s /q bin\systems
if exist bin\ui rd /s /q bin\ui
if exist bin\utils rd /s /q bin\utils
if exist bin\world rd /s /q bin\world

echo.
echo Compiling Java sources...
javac -d bin -sourcepath src src/Main.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo   Build successful!
    echo ========================================
    echo.
    echo To run the game:
    echo   cd bin
    echo   java Main
    echo.
) else (
    echo.
    echo ========================================
    echo   Build FAILED!
    echo ========================================
)
