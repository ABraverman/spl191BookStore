package bgu.spl.mics.application;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import com.google.gson.Gson;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.*;

/** This is the Main class of the application. You should parse the input file, 
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
	public static CountDownLatch initCdl;
	public static CountDownLatch terminationCdl;
	
    public static void main(String[] args) {
    	if (args.length != 5){ // checking exactly 5 arguments were given
    		throw new IllegalArgumentException("Program was given " + args.length + " arguments, 5 are needed");
    	}
    	
    	try{
	    	Reader reader = new FileReader(args[0]);
	    	Gson json = new Gson();
	    	InputObj input = json.fromJson(reader, InputObj.class);
	    	
	    	// starting singeltons
	    	Inventory inv = Inventory.getInstance();
	    	inv.load(input.getInitialInventory());
	    	ResourcesHolder rh = ResourcesHolder.getInstance();
	    	rh.load(input.getInitialResources());
	    	
	    	int numOfServices = input.getNumServices(),countService;
	    	initCdl = new CountDownLatch(numOfServices);
	    	terminationCdl = new CountDownLatch(numOfServices+1);
	    	String tempThreadName;
	    	
	    	countService = 1;
	    	for (int i=0;i<input.getSelling();i++){
	    		tempThreadName = "selling " + countService;
	    		(new Thread(new SellingService(tempThreadName), "T " + tempThreadName)).start();
	    		countService++;
	    	}
	    	countService = 1;
	    	for (int i=0;i<input.getInventoryService();i++){
	    		tempThreadName = "inventory " + countService;
	    		(new Thread(new InventoryService(tempThreadName), "T " + tempThreadName)).start();
	    		countService++;
	    	}
	    	countService = 1;
	    	for (int i=0;i<input.getLogistics();i++){
	    		tempThreadName = "logistics " + countService;
	    		(new Thread(new LogisticsService(tempThreadName), "T " + tempThreadName)).start();
	    		countService++;
	    	}
	    	countService = 1;
	    	for (int i=0;i<input.getResourcesService();i++){
	    		tempThreadName = "resources " + countService;
	    		(new Thread(new ResourceService(tempThreadName), "T " + tempThreadName)).start();
	    		countService++;
	    	}
	    	countService = 1;
	    	for (int i=0;i<input.getCustomers().length;i++){
	    		tempThreadName = "api " + countService;
	    		(new Thread(new APIService(tempThreadName,input.getCustomers()[i]), "T " + tempThreadName)).start();
	    		countService++;
	    	}
	    	initCdl.await(); // waiting for all services to initiate before starting time thread
	    	tempThreadName = "s"+countService;
	    	(new Thread(new TimeService("time",input.getSpeed(),input.getDuration()),"T time")).start();
	    	terminationCdl.await(); // waiting for all services to terminate before creating output files
	    	printCustomers(input.getCustomers(),args[1]);
	    	inv.printInventoryToFile(args[2]);
	    	MoneyRegister.getInstance().printOrderReceipts(args[3]);
	    	printMoneyRegister(args[4]);
	    	
    	}
    	catch (FileNotFoundException e){
    		e.printStackTrace();
    	}
    	catch (InterruptedException e){
    		e.printStackTrace();
    	}
    	
    	
    }
    
    /**
     * create serialized file for the customers
     * @param customers array of all customers in the program
     * @param filePath path to create file
     */
    private static void printCustomers(Customer[] customers,String filePath){
    	HashMap<Integer,Customer> cushm = new HashMap<>();
		for (Customer c : customers)
			cushm.put(c.getId(), c);
		try{
			FileOutputStream outFile = new FileOutputStream(filePath);
			ObjectOutputStream mapWriter = new ObjectOutputStream(outFile);
			mapWriter.writeObject(cushm);
			mapWriter.close();
			outFile.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
    }
    /**
     * creating serialized file for the money register
     * @param filePath path to create file
     */
    private static void printMoneyRegister(String filePath){
		try{
			FileOutputStream outFile = new FileOutputStream(filePath);
			ObjectOutputStream mapWriter = new ObjectOutputStream(outFile);
			mapWriter.writeObject(MoneyRegister.getInstance());
			mapWriter.close();
			outFile.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
    }
}