import java.util.LinkedList;

/* This class handles the moving of processes between different queues.  based on the scheduling method selected,
 * it will place the process into the correct location of each queue. * 
 */
public class NonPreemptiveScheduler
{
	private int scheduleType = 1;		//Will set the scheduling method, FCFS by default to avoid errors.
	
	/* Allows the user to set the type of scheduling they want.  The decision is made in the
	 * the client's interface, and passed and stored in this class.
	 */
	public NonPreemptiveScheduler(int type)
	{
		if (type >= 1 && type <= 3)
			scheduleType = type;
	}
	
	public void setScheduler(int scheduleType)
	{
		if (scheduleType >=1 && scheduleType <= 3)
			this.scheduleType = scheduleType;
	}
	
	//Function to return the type of scheduling currently in use.
	public int getScheduler()
	{
		return scheduleType;
	}
	
	//This function adds the passed process to the NEW queue, sorting it based on the time that the job will be submitted to the ready queue.
	public LinkedList moveToNew(Process process, LinkedList<Process> queue)
	{
		//Flag checks to see if item has been added to the queue yet.
		boolean flag = false;
		
		//Parse through the linked list,
		for (int k=0; k<queue.size(); k++)
		{
			//and when the submitTime is less than the submit time of the next item in the list
			if (process.getSubmitTime() < queue.get(k).getSubmitTime())
			{
				//add the item to the queue
				queue.add(k, process);

				//and flag that it was added.
				flag = true;
				break;
			}
		}
		
		//If the item was never added to the list,
		if (flag==false)
		{
			//add it to the end.
			queue.add(process);
		}
		
		//Finally, update the process object to signify it is in the right state.
		process.setState("new");
		
		return queue;
	}
	
	//Function to move processes from the New Queue to the Ready Queue
	public LinkedList moveFromNewToReady(Process process, LinkedList<Process> queue)
	{
		//FCFS Algorithm
		if (scheduleType == 1)
		{
			//Add to end of the queue
			queue.add(process);
		}
		//SJF Algorithm
		else if (scheduleType == 2)
		{
			//temp flag
			boolean flag = false;
			
			//scan the entire array contents...
			for (int k=0; k<=queue.size(); k++)
			{
				//...and place the process into the queue, so that it is sorted based on size
				if (process.getBurstsCycle() < queue.get(k).getBurstsCycle())
				{
					queue.add(k, process);
				}
				//flag turns on when process has been added to the queue
				flag = true;
			}
			
			//if process has not been added yet...
			if (flag==false)
			{
				//add to the end
				queue.add(process);
			}
		}
		//Priority Algorithm
		else if (scheduleType == 3)
		{
			boolean flag = false;
			
			//Scan through the entire array...
			for (int k=0; k<=queue.size(); k++)
			{
				//and add to the queue based on priority (in event of a tie, put after the processes that were in queue
				if (process.getPriority() < queue.get(k).getPriority())
				{
					queue.add(k, process);
				}
				//flag on when process has been added
				flag = true;
			}
			
			//if process has not been added yet...
			if (flag==false)
			{
				//add to the end
				queue.add(process);
			}
		}
		
		//set state of the process to be in ready queue
		process.setState("ready");
		
		return queue;
	}
	
	//Function moves process from the ready queue to the running queue
	public LinkedList moveFromReadyToRunning(Process process, LinkedList<Process> queue)
	{
		//Only move process to the running queue if the queue is emmpty.
		if (queue.isEmpty())
		{
			queue.add(process);
			process.setState("running");
		}
		
		return queue;
	}
	
	//Function to move processes out of running queue
	public LinkedList moveFromRunning(Process process, LinkedList<Process> queue)
	{
		queue.add(process);
		return queue;
	}
	
	public LinkedList moveFromWaitingToReady(Process process, LinkedList<Process> queue)
	{
		//FCFS Algorithm (adds to front of queue, due to reader/writer problem)
		if (scheduleType == 1)
		{
			queue.addFirst(process);
		}
		//SJF Algorithm
		else if (scheduleType == 2)
		{
			boolean flag = false;
			
			for (int k=0; k<=queue.size(); k++)
			{
				if (process.getBurstsCycle() < queue.get(k).getBurstsCycle())
				{
					queue.add(k, process);
				}
				flag = true;
			}
			
			if (flag==false)
			{
				queue.add(process);
			}
		}
		//Priority Algorithm
		else if (scheduleType == 3)
		{
			boolean flag = false;
			
			for (int k=0; k<=queue.size(); k++)
			{
				if (process.getPriority() < queue.get(k).getPriority())
				{
					queue.add(k, process);
				}
				flag = true;
			}
			
			if (flag==false)
			{
				queue.add(process);
			}
		}
		return queue;
	}
}