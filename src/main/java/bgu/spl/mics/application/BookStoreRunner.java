package bgu.spl.mics.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
    	if (args.length != 5){
    		throw new IllegalArgumentException("Program was given " + args.length + " arguments, 5 are needed");
    	}
    	
    	try{
	    	Reader reader = new FileReader(args[0]);
	    	Gson json = new Gson();
	    	InputObj input = json.fromJson(reader, InputObj.class);
	    	
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
	    	initCdl.await();
	    	tempThreadName = "s"+countService;
	    	(new Thread(new TimeService("time",input.getSpeed(),input.getDuration()),"T time")).start();
	    	terminationCdl.await();
//	    	printCustomers(input.getCustomers(),args[1]);
//	    	inv.printInventoryToFile(args[2]);
//	    	MoneyRegister.getInstance().printOrderReceipts(args[3]);
//	    	printMoneyRegister(args[4]);
	    	System.out.println("###### Customers ######");
	    	for (int i=0;i<input.getCustomers().length;i++)
	    		System.out.println(input.getCustomers()[i]);
	    	System.out.println("###### Customers ######");
	    	System.out.println(inv);
	    	System.out.println(MoneyRegister.getInstance());
	    	int numOfTest = Integer.parseInt(args[0].replace(new File(args[0]).getParent(), "").replace("/", "").replace(".json", ""));
            String dir = new File(args[1]).getParent() + "/" + numOfTest + " - ";
            Customer[] customers1 = input.getCustomers();
            Arrays.sort(customers1, Comparator.comparing(Customer::getName));
            String str_custs = Arrays.toString(customers1);
            str_custs = str_custs.replaceAll(", ", "\n---------------------------\n").replace("[", "").replace("]", "");
            Print(str_custs, dir + "Customers");

            String str_books = Arrays.toString(inv.getBooks());
            str_books = str_books.replaceAll(", ", "\n---------------------------\n").replace("[", "").replace("]", "");
            Print(str_books, dir + "Books");

            List<OrderReceipt> receipts_lst = MoneyRegister.getInstance().getOrderReceipts();
            receipts_lst.sort(Comparator.comparing(OrderReceipt::getOrderId));
            receipts_lst.sort(Comparator.comparing(OrderReceipt::getOrderTick));
            OrderReceipt[] receipts = receipts_lst.toArray(new OrderReceipt[0]);
            String str_receipts = Arrays.toString(receipts);
            str_receipts = str_receipts.replaceAll(", ", "\n---------------------------\n").replace("[", "").replace("]", "");
            Print(str_receipts, dir + "Receipts");

            Print(MoneyRegister.getInstance().getTotalEarnings() + "", dir + "Total");
	    	
    	}
    	catch (FileNotFoundException e){
    		e.printStackTrace();
    	}
    	catch (InterruptedException e){
    		e.printStackTrace();
    	}
    	
    	
    }
    
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

    public static void Print(String str, String filename) {
        try {
            try (PrintStream out = new PrintStream(new FileOutputStream(filename))) {
                out.print(str);
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
        }
    }
}