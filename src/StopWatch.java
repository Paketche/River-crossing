
public class StopWatch{

	private long start;
	private long end;

	public StopWatch(){
	}

	public void start(){
		start = System.currentTimeMillis();
	}

	public int getCurrentTime(){
		end = System.currentTimeMillis();
		return (int) ((end - start) / 1000.0);
	}
	
	public static void main(String[] args){
		StopWatch aw = new StopWatch();
		aw.start();
		try{
			Thread.sleep(30000);
		} catch (InterruptedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(aw.getCurrentTime());
	}
	
}
