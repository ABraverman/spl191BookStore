package bgu.spl.mics.application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import com.google.gson.Gson;

import bgu.spl.mics.application.passiveObjects.*;

/** This is the Main class of the application. You should parse the input file, 
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) {
    	try{
    	Reader reader = new FileReader("c:/Users/guytd/Desktop/shit.json");
    	Gson json = new Gson();
    	InputObj b = json.fromJson(reader, InputObj.class);
    	System.out.println(b);
    	}
    	catch (FileNotFoundException e){
    		e.printStackTrace();
    	}
    	
    }
}
