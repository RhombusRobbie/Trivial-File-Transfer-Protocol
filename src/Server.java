import java.net.DatagramSocket;
import java.net.SocketException;

public class Server extends Host
{
	// Well known port to which the server will be bound
	public static final int SERVER_PORT = 69;
	// Timeout of the datagram socket in milliseconds
	public static final int SOCKET_TIMEOUT = 1000;
	
	public Server()
	{
		super();

		// Initialise the datagram socket and bind it to the servers well known port
		try
		{
			socket = new DatagramSocket(SERVER_PORT);
		}
		catch(SocketException e)
		{
			System.err.println("SocketException " + e.getMessage());
			System.exit(0);
		}
		catch(SecurityException e)
		{
			System.err.println("SecuityException " + e.getMessage());
			System.exit(0);
		}
	}
	
	@Override
	public void run()
	{
		
	}
	
//	static public void main(String[] args)
//	{
//		Server server = new Server();
//		Thread serverThread = new Thread(server);
//		serverThread.start();
//	}
}
