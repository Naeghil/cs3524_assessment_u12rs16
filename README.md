# cs3524_assessment_u12rs16
This is the assessment for course Distributed Systems

It's a simple client-server system allowing the user to play/host a simple text-based MUD.

### Prerequisites
Because of the use of cerain language constructs (e.g. try-with-resources), the program requires Java7 or higher to run properly.

Additionally, as the scripts and Makefile work with relative paths, while it's possible to move the project folder, it's better not to move or rename the files and subdirectories within the folder, as this may result in errors in the execution.

### Installing
Installing the system can be easily done by:
1. Open a terminal
2. Navigate to the folder of the project (i.e. the folder where this file was first found)
3. Ensure the file 'Makefile' and the folder 'src' are in the folder
4. Type "make all" and wait for the compilation to finish

### Running
To run the MUD (including the server):
1. Open a terminal
2. Navigate to the folder of the project (i.e. the folder where this file was first found)
3. Give execution permission to the "start.sh" script with: "chmod +x start.sh"
4. Execute the script with: "./start.sh"
5. Return to the original terminal, as that's where the client is running

To run the client (e.g. if an additional client needs to be connected to the server):
1. Open a terminal
2. Navigate to the folder of the project (i.e. the folder where this file was first found)
3. Give execution permission to the "startClient.sh" script with: "chmod +x startClient.sh"
4. Execute the script with: "./startClient.sh"

The script uses by default some ports that should be free. If this is not the case, please modify the script accordingly.

## Authors
* **Roberto Sautto**    - [Naeghil](https://github.com/Naeghil/)
