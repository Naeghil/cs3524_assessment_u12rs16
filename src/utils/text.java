package utils;

import java.util.ArrayList;

public class text {
	public static String nl = System.getProperty("line.separator");
	
	public static String INITIAL_MESSAGE = "Welcome to this MUD!"+nl
										+"The self-explanatory 'exit' and 'help' commands are available at any time."+nl
										+"'chworld' is used to change the current world.";
	
	public static String GETWORLD_MESSAGE = "Choose one of the existing worlds or type 'makeworld <worldname>' to start creating your own";
	
	public static String ENTER_MESSAGE =  "Login with your character or create a new one:"+nl
								+"<command> <name> <password>"+nl
								+"\t<command> is 'reg' or 'log', respectively to register or log in a character"+nl
								+"\tCharacters are world-specific";

	public static String HELP =  "\tmove <dir> - <dir> is one of the four cardinal directions, provided there is a path in that direction"+nl
								+"\tpickup <thing> - <thing> is one of the things found at this location"+nl
								+"\tsee - shows a description of your current location"+nl
								+"\tinventory - shows what you're carrying with you"+nl
								+"\thelp - shows this text"+nl
								+"\tlogout - if you want to use another character"+nl
								+"\tchworld - allows you to choose a different world"+nl
								+"\texit - if you have other things to do";

	public static String GET_EDGES = nl+"Let's start with the basic structure of the world."+nl
								+"Write a series of sentences in the form: <place1> <dir> <place2> <message>"+nl
								+"<place1> of the first of these messages will be the starting location"+nl
								+"<place1> and <place2> must be single words and are the names of two adjacent locations that exist in your world"+nl
								+"<dir> must be one of 'north', 'south', 'east', 'west', that is the direction to go towards to get from <place1> to <place2>"+nl
								+"<message> is what a character in <place1> sees when they look towards <dir>"+nl
								+"to finish edge building just press enter on an empty line."+nl
								+"Please, keep in mind it won't be possible to modify the world later on, so be sure of what you write";

	public static String GET_MESSAGES = "Good! Let's move on to how these places look like!"+nl
								+"Please write a series of sentences in the form:"+nl
								+"\t<place> <message>"+nl
								+"<place> is the name of a place in your world, while <message> is what a player will see while being in <place>"+nl
								+"to finish world description just press enter on an empty line."+nl
								+"Please, keep in mind it won't be possible to modify the world later on, so be sure of what you write";

	public static String GET_THINGS = "Well, then. Now it's time to add the things that the characters can interact with."+nl
								+"Please write a series of sentences in the form:"+nl
								+"\t<place> <thing>"+nl
								+"where <place> is a place in your world and <thing> is a thing that is there. They must both be single worlds."+nl
								+"to finish adding things just press enter on an empty line."+nl
								+"Please, keep in mind it won't be possible to modify the world later on, so be sure of what you write";

	public static String BADINPUT = "I'm sorry, what?";
	
//Generated messages:
	public static String getWorldMessage(String wList) {
		return "So, where do you want to go?"+nl+wList+GETWORLD_MESSAGE;
	}
	public static String fatal(String msg) {
		return "The program has encountered an unexpected exception and will be terminated"+nl+msg;
	}
	public static String noWorldCreation(String name) {
		return "I'm sorry, "+name+" cannot be created."+nl+"Either a world by this name already exists, or no more worlds can be created.";
	}
	public static String nonUniqueChar(String cName, String wName) {
		return "There is already a "+cName+" in "+wName;
	}
//String manipulation
	public static String indented(ArrayList<String> array) {
		String list = "";
		for(String line : array) list+="\t"+line+nl;
		return list;
	}
	public static String oneString(ArrayList<String> array) {
		String str = "";
		for(String line : array) str+=line+nl;
		return str;
	}
	public static String oneLine(ArrayList<String> array) {
		String line = "";
		for(String item : array) line+=item+" ";
		if(array.size()>0) line = line.substring(0, line.length()-1);
		return line;
	}
}