JFLAGS = -sourcepath ./src/ -classpath ./bin/ -d ./bin/ -Xlint:unchecked -g
JC = javac

all: mud utils interfaces server client

interfaces:
	$(JC) $(JFLAGS) src/interfaces/MainlineInterface.java
	$(JC) $(JFLAGS) src/interfaces/ClientInterface.java
	$(JC) $(JFLAGS) src/interfaces/ServerInterface.java	

server:
	$(JC) $(JFLAGS) src/server/Mainline.java
	$(JC) $(JFLAGS) src/server/Server.java
	$(JC) $(JFLAGS) src/server/PlayingCharacter.java

mud:
	$(JC) $(JFLAGS) src/mud/Vertex.java
	$(JC) $(JFLAGS) src/mud/Edge.java
	$(JC) $(JFLAGS) src/mud/MUD.java

client:
	$(JC) $(JFLAGS) src/client/Client.java
	$(JC) $(JFLAGS) src/client/Main.java
	
utils:
	$(JC) $(JFLAGS) src/utils/text.java
	$(JC) $(JFLAGS) src/utils/GameFile.java

uninst:
	rm -rf bin/*
