#!/bin/bash
cd bin
java -cp .:../libs/gdx.jar:../libs/gdx-backend-lwjgl.jar:../libs/gdx-natives.jar:../libs/gdx-backend-lwjgl-natives.jar:../assets DesktopGame
cd ..
