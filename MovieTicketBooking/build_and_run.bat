@echo off
echo ============================================
echo     CinePlex Pro - Movie Ticket Booking
echo ============================================
echo.

REM Check Java
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH.
    echo Please install JDK 11 or higher from https://adoptium.net/
    pause
    exit /b 1
)

echo [1/3] Compiling source files...
if not exist out\classes mkdir out\classes
javac -d out\classes src\moviebooking\*.java
if errorlevel 1 (
    echo COMPILATION FAILED. Check error messages above.
    pause
    exit /b 1
)

echo [2/3] Creating JAR...
jar --create --file CineplexPro.jar --main-class moviebooking.Main -C out/classes .
if errorlevel 1 (
    echo JAR creation failed.
    pause
    exit /b 1
)

echo [3/3] Launching CinePlex Pro...
echo.
java -jar CineplexPro.jar

pause
