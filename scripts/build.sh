#!/bin/bash

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${YELLOW}--- Überprüfe ob wir uns Root-Verzeichnis befinden. ---${NC}"
if [ ! -f "gradlew" ]; then
    echo -e "${RED}--- Fehler: Muss vom Root-Verzeichnis ausgeführt werden. ---${NC}"
	exit 1
fi

echo -e "${YELLOW}--- Mache gradlew Ausführbar (chmod +x ./gradlew). ---${NC}"
chmod +x ./gradlew

BUILD_TYPE="debug"

if [ "$1" == "release" ]; then
    BUILD_TYPE="release"
fi

TASK_NAME="assemble${BUILD_TYPE^}"

echo -e "${YELLOW}--- Starte Task: '$TASK_NAME'... ---${NC}"

./gradlew "$TASK_NAME"

if [ $? -eq 0 ]; then
    APK_PATH="app/build/outputs/apk/$BUILD_TYPE/app-$BUILD_TYPE.apk"
    echo -e "\n${GREEN}-- Build erfolgreich! ---${NC}\n--- APK: ${YELLOW}$APK_PATH ---${NC}"
else
    echo -e "\n${RED}--- Build fehlgeschlagen. ---${NC}"
fi
