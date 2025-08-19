#!/bin/bash
GREEN='\033[0;32m'; YELLOW='\033[1;33m'; RED='\033[0;31m'; NC='\033[0m'
if ! command -v adb &> /dev/null; then echo -e "${RED}Fehler: 'adb' nicht gefunden.${NC}"; exit 1; fi
if [ $(adb devices | grep -w "device" | wc -l) -eq 0 ]; then echo -e "${RED}Fehler: Kein Gerät gefunden.${NC}"; exit 1; fi
OUTPUT_DIR="screenshots"
mkdir -p "$OUTPUT_DIR"
FILENAME="screenshot_$(date +%Y-%m-%d_%H-%M-%S).png"
DEVICE_PATH="/sdcard/$FILENAME"
LOCAL_PATH="$OUTPUT_DIR/$FILENAME"
echo "Nehme Screenshot..."
adb shell screencap -p "$DEVICE_PATH"
echo "Kopiere Screenshot..."
adb pull "$DEVICE_PATH" "$LOCAL_PATH" > /dev/null
adb shell rm "$DEVICE_PATH"
if [ -f "$LOCAL_PATH" ]; then
    echo -e "\n${GREEN}Screenshot erfolgreich!${NC}\nGespeichert unter: ${YELLOW}$LOCAL_PATH${NC}"
else
    echo -e "\n${RED}Fehler beim Erstellen des Screenshots.${NC}"
fi
