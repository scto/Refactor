#!/bin/bash

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

VERSION="latest"

echo -e "${YELLOW}--- Mache gradlew Ausführbar (chmod +x ./gradlew). ---${NC}"
chmod +x ./gradlew

echo -e "${YELLOW}--- Starte Update: '$VERSION'... ---${NC}"
./gradlew wrapper --gradle-version $VERSION

