import java.util.Scanner;

public class Simulator implements Runnable {
	//This class will be used to gather and print statistics for the simulation run
	//as well as being the place where the simulation is actually run
	//It will also compare two or more different scheduling algorithms

	public void getStats (){
		
	}
	
	
	public static void main(String[] args) {
		Thread t = new Thread(new Simulator());
		t.start();
	}
	
	//create objects of every other class.
	QueueManager queue = new QueueManager(1);



	public void run() {
		int response = 1;
		String input;
		
		while (response > 0){
			System.out.println("Welcome to the Scheduling Simulator");
			System.out.println();
			System.out.println("Select an option, then press Enter");
			System.out.println("0. Exit");
			System.out.println("1. Use Default Set of Processes");
			System.out.println("2. Add Process");
			System.out.println("3. Add I/O Device");
			System.out.println("4. Run Simulator");
			System.out.println("5. Print Last Run Statistics");
			
			@SuppressWarnings("resource")
			Scanner reader = new Scanner(System.in);
			response=reader.nextInt();
			
			switch (response){
			case 1:
				System.out.println("Done...");
				break;
			case 2:
				//add process
				System.out.println("And we did something here");
				break;
			case 3:
				System.out.println("Please Enter a Name for the Device");
				input = reader.next();
				new Resource(input);
				System.out.println("Resource Added...");
				break;
			case 4:
				//run sim
				break;
			case 5:
				//print stats
				break;
			default:
				break;
			}
			try {
				Thread.sleep(1500);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			
			}
		}
		
	}

	
	
}