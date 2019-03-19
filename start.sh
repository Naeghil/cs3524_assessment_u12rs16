#! /bin/sh

gnome-terminal -- sh -c "java -Djava.security.policy=settings/MUD.policy -cp bin/ client.Main localhost 50011; bash"
java -Djava.security.policy=settings/MUD.policy -cp bin/ server.Mainline 50011 50018 5 10


