import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.JOptionPane;

public class Client extends Host
{

	public Client()
	{
		super();
		
		// Initialise the datagram socket
		try
		{
			socket = new DatagramSocket();
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
		while(true)
		{
			// Prompt user for inputs
			getUserInputs();

			if(!directory.exists())
			{
				print("Creating directory " + directory.getPath());
				try
				{
					directory.mkdirs();
				}
				catch(SecurityException e)
				{
					System.err.println("SecurityException " + e.getMessage());
					System.exit(0);
				}
			}
		}
		
	}
	
	/**
	 * Updates all relevant class fields initialises sendArray depending on the type of request.
	 */
	@SuppressWarnings("unused")
	private void generateRequest(TransferType transferType, String fileName, TransferMode transferMode)
	{
		// Update fields
		this.transferType = transferType;
		this.fileName = fileName;
		this.transferMode = transferMode;
		
		try
		{
			this.file = new File(directory, fileName);
		}
		catch (NullPointerException e)
		{
			System.err.println("File name is not valid " + e.getMessage());
			System.exit(0);
		}
		
		// Opcode, block number, and null byte
		sendArray[0] = 0;
		if(transferType == TransferType.READ)
		{
			sendArray[1] = 1;
		}
		else
		{
			sendArray[1] = 2;
		}
		
		// Insert filename to send array
		for(int i = 0; i < fileName.length(); i++)
		{
			sendArray[i + 2] = fileName.getBytes()[i];
		}
		
		// Zero byte
		sendArray[2 + fileName.length()] = 0;
		
		// Insert transfer mode to send array
		if(transferMode == TransferMode.NETASCII)
		{
			for(int i = 0; i < 8; i++)
			{
				sendArray[i + 3 + fileName.length()] = "netascii".getBytes()[i];
			}
			// Zero byte
			sendArray[11 + fileName.length()] = 0;
		}
		else // OCTET
		{
			for(int i = 0; i < 5; i++)
			{
				sendArray[i + 3 + fileName.length()] = "octet".getBytes()[i];
			}
			// Zero byte
			sendArray[8 + fileName.length()] = 0;
		}
	}
	
	private void getUserInputs()
	{
		String[] qORv = {"Quiet", "Verbose"};
		String[] tORn = {"Test", "Normal"};
		String[] rORw = {"Read", "Write"};
		
		// Directory
		if(JOptionPane.showConfirmDialog(jFrame, "Do you want to use the default directory?", "Directory", JOptionPane.YES_NO_OPTION) == 0)
		{
			directory = new File("Client");
		}
		else
		{
			directory = new File(JOptionPane.showInputDialog(null,"Directory:", "Directory", JOptionPane.QUESTION_MESSAGE));
		}
		
		// Output mode
		if(((String) JOptionPane.showInputDialog(jFrame, "Which output mode would you like to use?", "Output mode", JOptionPane.QUESTION_MESSAGE, null, qORv, qORv[0])).equals(qORv[0]))
		{
			outputMode = OutputMode.QUIET;
		}
		else
		{
			outputMode = OutputMode.VERBOSE;
		}
		
		// File name
		fileName = JOptionPane.showInputDialog(null, "Which file would you like to transfer?", "File name", JOptionPane.QUESTION_MESSAGE);
		file = new File(fileName);
		
		// 
		
		
	}
	
	public static void main(String args[])
	{
		Client client = new Client();
		Thread thread = new Thread(client, "Client");
		thread.start();
	}
}
