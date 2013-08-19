public class Machine {
	private int[] regs;
	
	private int currentLogicalPage = 0;
	private int[] pages = new int[524288];	// this number x 8KB = 4GB RAM
	
	
	private static final Machine instance = new Machine(); //run one machine at a time
	private Machine() {
		
	}
	
	public static Machine getInstance(){
		return instance;
	}
	
	public int requestRAMPage (){
		if (pages[currentLogicalPage] == 0){
			pages[currentLogicalPage] = 1;
			return currentLogicalPage;
		}
		else {
			currentLogicalPage++;
			if (currentLogicalPage >= pages.length)
				currentLogicalPage = 0;
			return requestRAMPage();
			
		}
	}
	
	public void freeRAMPage (int logicalPage){
		if (logicalPage <= pages.length - 1 && logicalPage >= 0)
			pages[logicalPage] = 0;
	}
	
}