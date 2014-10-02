package scripts;

public class DelayedRunnable implements Runnable
{
	private final Runnable runnable;
	private final int delay;

	public DelayedRunnable(Runnable runnable, int delay)
	{
		this.runnable = runnable;
		this.delay = delay;
	}

	public void start()
	{
		Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.setName("Delayed runnable " + hashCode());
		thread.start();
	}

	@Override
	public void run()
	{
		try
		{
			Thread.sleep(delay);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			return;
		}
		runnable.run();
	}
}
