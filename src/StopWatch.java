
public class StopWatch{

	private long start;
	private long end;
	
	/**
	 * Star the timer
	 */
	public void start(){
		start = System.currentTimeMillis();
	}
	/**
	 * get the time elapsed since the start of the timer
	 * @return
	 */
	public int getCurrentTime(){
		end = System.currentTimeMillis();
		return (int) ((end - start) / 1000.0);
	}	
}
