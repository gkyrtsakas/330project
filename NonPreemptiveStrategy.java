package process.scheduler;

import java.util.PriorityQueue;

public abstract class NonPreemptiveStrategy
{
	private static int count=0;
	
	public void addProcess (Process job, PriorityQueue<Process> queue)
	{
		job.setOrder(count);
		count++;
		
		queue.add(job);
	}
}
