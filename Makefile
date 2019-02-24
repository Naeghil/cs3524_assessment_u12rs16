JFLAGS = -sourcepath ./src/ -classpath ./bin/ -d ./bin/ -Xlint:unchecked -g
JC = javac

all: server mud client

server:
	$(JC) $(JFLAGS) src/server/MUDServerInterface.java
	$(JC) $(JFLAGS) src/server/MUDServerImpl.java
	$(JC) $(JFLAGS) src/server/MUDMainline.java

mud:
	$(JC) $(JFLAGS) src/mud/Vertex.java
	$(JC) $(JFLAGS) src/mud/Edge.java
	$(JC) $(JFLAGS) src/mud/MUD.java

client:
	$(JC) $(JFLAGS) src/MUDClient.java

uninst:
	rm -rf bin/*
