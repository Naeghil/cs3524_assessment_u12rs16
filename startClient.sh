#! /bin/sh

java -Djava.security.policy=settings/MUD.policy -cp bin/ MUDClient localhost 50011
