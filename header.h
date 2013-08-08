typedef struct {
	int pid;			
	int memlength;		//Length of process in RAM
	int priority;		
	int submitTime;		//what time does the process enter the ready queue
	int completionTime;
	int IOrequest;		//True if process requires IO. If false, all below are false.
	int HDDreq;
	int ODDreq;
	int USBreq;
} HEADER_CLASS;