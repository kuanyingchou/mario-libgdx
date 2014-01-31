set lib=libs
javac -cp .;%lib%\gdx.jar;%lib%\gdx-backend-lwjgl.jar;..\assets -d bin *.java
