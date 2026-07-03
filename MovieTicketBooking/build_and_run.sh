#!/bin/bash

echo "============================================"
echo "    CinePlex Pro - Movie Ticket Booking"
echo "============================================"
echo

# Check Java
if ! command -v javac &> /dev/null; then
    echo "ERROR: javac not found. Please install JDK 11+ first."
    echo "  Ubuntu/Debian: sudo apt install default-jdk"
    echo "  macOS:         brew install openjdk"
    echo "  Download:      https://adoptium.net/"
    exit 1
fi

echo "[1/3] Compiling source files..."
mkdir -p out/classes
javac -d out/classes src/moviebooking/*.java
if [ $? -ne 0 ]; then
    echo "COMPILATION FAILED."
    exit 1
fi

echo "[2/3] Creating JAR..."
jar --create --file CineplexPro.jar --main-class moviebooking.Main -C out/classes .
if [ $? -ne 0 ]; then
    echo "JAR creation failed."
    exit 1
fi

echo "[3/3] Launching CinePlex Pro..."
echo
java -jar CineplexPro.jar
