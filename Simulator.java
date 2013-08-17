import java.util.Scanner;

public class Simulator {
	//This class will be used to gather and print statistics for the simulation run
	//as well as being the place where the simulation is actually run
	//It will also compare two or more different scheduling algorithms

	public void getStats (){
		
	}
	
	
	public static void main(String[] args) {
		int response = 1;
		
		while (response > 0){
			System.out.println("Welcome to the Scheduling Simulator");
			System.out.println();
			System.out.println("Select an option, then press Enter");
			System.out.println("0. Exit");
			System.out.println("1. Do Something Here");
			System.out.println("2. And here");
			System.out.println("3. Derp");
			
			Scanner reader = new Scanner(System.in);
			response=reader.nextInt();
			
			switch (response){
			case 1:
				System.out.println("We did something here");
				break;
			case 2:
				System.out.println("And we did something here");
				break;
			case 3:
				System.out.println("Derpson");
				break;
			default:
				break;
			}
			
		}

	}

	
	
}