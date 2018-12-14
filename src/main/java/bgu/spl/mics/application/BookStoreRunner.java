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
    	if (args.length != 5){
    		throw new IllegalArgumentException("Program was given " + args.length + " arguments, 5 are needed");
    	}
    	try{
	    	Reader reader = new FileReader(args[0]);
	    	Gson json = new Gson();
	    	InputObj input = json.fromJson(reader, InputObj.class);
	    	System.out.println(input);
	    	Inventory i = Inventory.getInstance();
	    	i.load(input.getInitialInventory());
	    	ResourcesHolder rh = ResourcesHolder.getInstance();
	    	rh.load(input.getInitialResources());
	    	
    	}
    	catch (FileNotFoundException e){
    		e.printStackTrace();
    	}
    	
    	
    }
}