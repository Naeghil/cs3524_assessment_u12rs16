package utils;

import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;


public class GameFile {
	protected String path;
	public GameFile(String pathIn) {
		path = pathIn;
	}
	
	public void rewrite(String content) throws IOException {
		try(FileWriter writer = new FileWriter(path);
			BufferedWriter fileWriter = new BufferedWriter(writer);) {
			fileWriter.write(content);
		}
	}
	public void addLine(String line) throws IOException {
		try(FileWriter writer = new FileWriter(path, true);
			BufferedWriter fileWriter = new BufferedWriter(writer);) {
			//TODO: dirty trick, computationally expensive, do something:
			if(readAll().size()!=0) 
				writer.write(System.getProperty("line.separator"));
			writer.write(line);
		}
	}
	public void removeLine(String toRemove) throws IOException {
		ArrayList<String> lines = readAll();
		String nl = System.getProperty("line.separator");
		String content = "";
		if(lines.size()>0) content = lines.get(0);
		String line;
		for(int i=1; i<lines.size(); i++) if(!(line = lines.get(i)).equals(toRemove)) content+=nl+line;
		rewrite(content);
	}
	
	public ArrayList<String> readAll() throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
		try(FileReader reader = new FileReader(path);
			BufferedReader fileReader = new BufferedReader(reader);){
			String line;
			while((line = fileReader.readLine())!=null) lines.add(line);
		}
		return lines;
	}

//Static methods:
	//Initializes the directories and the files necessary for a new world
	public static void initWorldDirs(String name) throws IOException {
		Path dir = Paths.get("worlds/"+name+"/characters");
		Path cList = Paths.get("worlds/"+name+"/"+name+".cli");
		Files.createDirectories(dir);
		Files.createFile(cList);
	}
	//Creates or updates a world file; this is only meant to use with .edg, .msg and .thg files
	public static void updateWorldFile(String wName, String ext, String content) throws IOException {
		GameFile file = new GameFile("worlds/"+wName+"/"+wName+"."+ext);
		file.rewrite(content);
	}
	//Creates or updates a character file
	public static void updateCharacter(String name, String wName, String content) throws IOException {
		GameFile file = new GameFile("worlds/"+wName+"/characters/"+name);
		file.rewrite(content);
	}
	//Deletes the files of a world
	public static void deleteWorld(String name) {
		try {
			//Remove from the list
			GameFile worldList = new GameFile("worlds/worldslist");
			worldList.removeLine(name);
			//Deletes files
			Path dir = Paths.get("worlds/"+name);
			Files.delete(dir);
		} catch (IOException e) {
			//Well, what can I do here...
		}
		
	}
	public static void deleteCharacter(String name, String wName) {
		try {
			//Remove from the list
			GameFile charList = new GameFile("worlds/"+wName+"/"+wName+".cli");
			charList.removeLine(name);
			//Delete file
			Path file = Paths.get("worlds/"+wName+"/characters/"+name);
			Files.delete(file);
		} catch (IOException e) {
			//Again...
		}
	}


}