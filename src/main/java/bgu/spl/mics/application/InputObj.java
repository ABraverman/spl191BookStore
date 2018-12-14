package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;


public class InputObj {
	private BookInventoryInfo[] initialInventory;
	private vehicles[] initialResources;
	private Services services;
	
	public BookInventoryInfo[] getInitialInventory(){
		return initialInventory;
	}
	
	public DeliveryVehicle[] getInitialResources(){
		return initialResources[0].getVehicles();
	}
	
	public int getSpeed(){
		return services.getSpeed();
	}
	
	public int getDuration(){
		return services.getDuration();
	}
	
	public int getSelling(){
		return services.getSelling();
	}
	
	public int getInventoryService(){
		return services.getInventoryService();
	}
	
	public int getLogistics(){
		return services.getLogistics();
	}
	
	public Customer[] getCustomers(){
		return services.getCustomers();
	}
		
	public String toString(){
		String str = "";
		for (int i=0;i<initialInventory.length;i++)
			str += initialInventory[i] + "\n";
		str += "\n";
		
		for (int i=0;i<initialResources.length;i++)
			str += initialResources[i] + "\n";
		
		str+= services.toString();
		return str;
	}
}

class vehicles {
	private DeliveryVehicle[] vehicles;
	
	public DeliveryVehicle[] getVehicles(){
		return vehicles;
	}
	
	public String toString(){
		String str = "";
		for (int i=0;i<vehicles.length;i++)
			str += vehicles[i] + "\n";
		return str;
	}
}

class Services{
	private initTime time;
	private int selling;
	private int inventoryService;
	private int logistics;
	private Customer[] customers;
	
	public int getSpeed(){
		return time.getSpeed();
	}
	
	public int getDuration(){
		return time.getDuration();
	}
	
	public int getSelling(){
		return selling;
	}
	
	public int getInventoryService(){
		return inventoryService;
	}
	
	public int getLogistics(){
		return logistics;
	}
	
	public Customer[] getCustomers(){
		return customers;
	}
	
	public String toString(){
		String str = "";
		str += time.toString();
		str += "\nSelling: " + selling;
		str += ", InventoryService: " + inventoryService;
		str += ", Logistics: " + logistics;
		str += "\n";
		for (int i=0;i<customers.length;i++)
			str += customers[i].toString() + "\n";
		return str;
	}
}

class initTime{
	private int speed;
	private int duration;
	
	public int getSpeed(){
		return speed;
	}
	
	public int getDuration(){
		return duration;
	}
	
	public String toString(){
		return "Speed: " + speed + ", Duration: " + duration;
	}
}