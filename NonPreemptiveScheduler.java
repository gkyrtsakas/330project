import java.util.*;

public class NonPreemptiveScheduler
{
	private int scheduleType = 1;		//Will set the scheduling method, FCFS by default to avoid errors.
	
	public NonPreemptiveScheduler(int type)
	{
		if (type >= 1 && type <= 3)
			scheduleType = type;
	}
	
	public int getScheduler()
	{
		return scheduleType;
	}
	
	public LinkedList moveToNew(Process process, LinkedList<Process> queue)
	{
		boolean flag = false;
		
		for (int k=0; k<=queue.size(); k++)
		{
			if (process.getSubmitTime() < queue.get(k).getSubmitTime())
			{
				queue.add(k, process);
			}
			flag = true;
		}
		
		if (flag==false)
		{
			queue.add(process);
		}
		
		return queue;
	}
	
	public LinkedList moveFromNewToReady(Process process, LinkedList<Process> queue)
	{
		//FCFS Algorithm
		if (scheduleType == 1)
		{
			queue.add(process);
		}
		//SJF Algorithm
		else if (scheduleType == 2)
		{
			boolean flag = false;
			
			for (int k=0; k<=queue.size(); k++)
			{
				if (process.getBurstsCycle().size() < queue.get(k).getBurstsCycle().size())
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
	
	public LinkedList moveFromReadyToRunning(Process process, LinkedList<Process> queue)
	{
		if (queue.isEmpty())
		{
			queue.add(process);
		}
		
		return queue;
	}
	
	public LinkedList moveFromRunning(Process process, LinkedList<Process> queue)
	{
		queue.add(process);		
		return queue;
	}
	
	public LinkedList moveFromWaitingToReady(Process process, LinkedList<Process> queue)
	{
		//FCFS Algorithm
		if (scheduleType == 1)
		{
			queue.add(process);
		}
		//SJF Algorithm
		else if (scheduleType == 2)
		{
			boolean flag = false;
			
			for (int k=0; k<=queue.size(); k++)
			{
				if (process.getBurstsCycle().size() < queue.get(k).getBurstsCycle().size())
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