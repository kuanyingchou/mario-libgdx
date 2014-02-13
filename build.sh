#!/bin/bash
cd src
javac -cp .:../libs/gdx.jar:../libs/gdx-backend-lwjgl.jar -d ../bin kuanying/mario/*.java
cd ..
