#! /bin/sh


gnome-terminal -- sh -c "cd bin; rmiregistry 50011; bash"
gnome-terminal -- sh -c "java -Djava.security.policy=settings/MUD.policy -cp bin/ server.MUDMainline 50011 50018; bash"
java -Djava.security.policy=settings/MUD.policy -cp bin/ MUDClient localhost 50011

