package scripts;

import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DelayedRunnable implements Runnable
{
	private final Callable<? super Object> callable;
	private final int delay;
	private final Plugin plugin;

	public DelayedRunnable(Callable<? super Object> callable, int delay, Plugin plugin)
	{
		this.callable = callable;
		this.delay = delay;
		this.plugin = plugin;
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
		Bukkit.getServer().getScheduler().callSyncMethod(plugin, callable);
	}
}
