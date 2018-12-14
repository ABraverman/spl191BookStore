package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;


public class InputObj {
	private BookInventoryInfo[] initialInventory;
	private vehicles[] initialResources;
	private Services services;
	
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
	
	public String toString(){
		return "Speed: " + speed + ", Duration: " + duration;
	}
}