public class Machine {
	private int[] regs;
	private long maxRAM = 3435973868L; 	// 4GB RAM in bits
	private int pageSize = 65535;		// 8KB page size
	private long clock;
	
	
	private static final Machine instance = new Machine(); //run one machine at a time
	private Machine() {
		
	}
	
	public static Machine getInstance(){
		return instance;
	}
	

	
}