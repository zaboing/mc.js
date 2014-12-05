package mcjs;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util
{
	public static void save(Serializable object, String destination)
	{
		try (OutputStream outputStream = Files.newOutputStream(Paths.get(destination)))
		{
			try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream))
			{
				objectStream.writeObject(object);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static Object load(String destination)
	{
		try (InputStream inputStream = Files.newInputStream(Paths.get(destination)))
		{
			try (ObjectInputStream objectStream = new ObjectInputStream(inputStream))
			{
				return objectStream.readObject();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T load(String destination, Class<? extends T> type)
	{
		try (InputStream inputStream = Files.newInputStream(Paths.get(destination)))
		{
			try (ObjectInputStream objectStream = new ObjectInputStream(inputStream))
			{
				Object object = objectStream.readObject();
				if (object.getClass() == type)
				{
					return (T) object;
				}
				else
				{
					return null;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
