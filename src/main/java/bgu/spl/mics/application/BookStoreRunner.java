package bgu.spl.mics.application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.concurrent.CountDownLatch;
import com.google.gson.Gson;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.*;

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
	    	
	    	int numOfServices = input.getNumServices(),countService = 0;
	    	CountDownLatch cdl = new CountDownLatch(numOfServices);
	    	MicroService[] services = new MicroService[numOfServices+1];
	    	
	    	for (int i=0;i<input.getInventorySelling();i++){
	    		services[countService] = new SellingService("s"+countService,cdl);
	    		services[countService].start();
	    		countService++;
	    	}
	    	for (int i=0;i<input.getInventorySelling();i++){
	    		services[countService] = new InventoryService("s"+countService,cdl);
	    		services[countService].start();
	    		countService++;
	    	}
	    	for (int i=0;i<input.getLogistics();i++){
	    		services[countService] = new LogisticsService("s"+countService,cdl);
	    		services[countService].start();
	    		countService++;
	    	}
	    	for (int i=0;i<input.getResourcesService();i++){
	    		services[countService] = new ResourcesService("s"+countService,cdl);
	    		services[countService].start();
	    		countService++;
	    	}
	    	for (int i=0;i<input.getCustomers().length;i++){
	    		services[countService] = new APIService("s"+countService,input.getCustomers()[i],cdl);
	    		services[countService].start();
	    		countService++;
	    	}
	    	cdl.await();
	    	services[countService] = new TimeService("s"+countService,input.getSpeed(),input.getDuration());
	    	
//	    	System.out.println(input);
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