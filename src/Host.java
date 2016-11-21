import java.net.*;
import java.io.*;
import javax.swing.*;

enum OutputMode {QUIET, VERBOSE};
enum TransferType {READ, WRITE};
enum TransferMode {NETASCII, OCTET};
enum SimulationMode {TEST, NORMAL};

/**
 * A host able to transfer data via datagram sockets and packets using TFTP.
 * @author Robert Graham
 * @since 12.11.16
 */
public abstract class Host implements Runnable
{
	public static final int SEND_RECEIVE_ARRAY_SIZE = 516;
	
	protected JFrame jFrame;
	protected FileWriter fileWriter;
	protected BufferedWriter bufferedWriter;
	protected BufferedReader bufferedReader;
	// Directory and file name as a string
	protected String directoryName, fileName;
	// Directory and file to/from where the data will be transferred
	protected File directory, file;
	// The current block number of the current file transfer
	protected int currentBlock;
	// Simulation mode, determines whether the error simulator will be used
	protected SimulationMode simulationMode;
	// File transfer mode, either netascii or octet
	protected TransferMode transferMode;
	// Verbose prints all messages while quiet prints only essential information
	protected OutputMode outputMode;
	// Type of transfer being performed, either read or write
	protected TransferType transferType;
	// Packets will be sent from the local port to the target port
	protected int localPort, targetPort;
	// Datagram socket used to send and receive packets
	protected DatagramSocket socket;
	// Packets which will be sent and received
	protected DatagramPacket sendPacket, receivePacket;
	// Byte arrays for each of data, acknowledge, request, and error.
	protected byte[] sendArray, receiveArray;
	
	/**
	 * 
	 */
	public Host()
	{
		this.currentBlock = 0;
		
/*		File directory = new File(directoryName);
		File file = new File(fileName);
		
		// Create the host directory if necessary
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
*/		
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
/*		try
		{
			bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));
		}
		catch (IOException e)
		{
			System.err.println("IOException " + e.getMessage());
			System.exit(0);
		}
*/		
		// Max size of data will be opcode (4) + data/message/request details (512 max)
		sendArray = new byte[SEND_RECEIVE_ARRAY_SIZE];
		receiveArray = new byte[SEND_RECEIVE_ARRAY_SIZE];
		
		sendPacket = new DatagramPacket(sendArray, SEND_RECEIVE_ARRAY_SIZE);
		receivePacket = new DatagramPacket(receiveArray, SEND_RECEIVE_ARRAY_SIZE);
	}

	protected void sendDatagramPacket()
	{
		sendPacket.setData(sendArray);
		try
		{
			socket.send(sendPacket);
		}
		catch (IOException e)
		{
			System.err.println("IOException " + e.getMessage());
			System.exit(0);
		}
		print("Sent packet containing\n" + new String(sendArray) + "\n" + sendArray.toString());
	}
	
	protected void receiveDatagramPacket()
	{
		try
		{
			socket.receive(receivePacket);
		}
		catch (IOException e)
		{
			System.err.println("IOException " + e.getMessage());
			System.exit(0);
		}
		print("Received packet containing\n" + new String(receiveArray) + "\n" + receiveArray.toString());
	}
	
	/**
	 * Extracts data from the file beginning at whichever block number we are currently at and puts it into sendArray.
	 */
	protected void readFromFile()
	{
		
	}
	
	/**
	 * Writes the specified array to the file.
	 * @param byteArray Data to be written to the global file.
	 */
	protected void writeToFile(byte[] byteArray)
	{
		try
		{
			bufferedWriter.write(new String(byteArray));
		}
		catch (IOException e)
		{
			System.err.println("IOException " + e.getMessage());
			System.exit(0);
		}
	}
	
//	protected byte[] truncateNullCharacters(byte[] byteArray)
//	{
//		
//	}
	
	protected void clearReceiveArray()
	{
		for(int i = 0; i < sendArray.length; i++)
		{
			receiveArray[i] = 0;
		}
	}
	
	protected void clearSendArray()
	{
		for(int i = 0; i < sendArray.length; i++)
		{
			sendArray[i] = 0;
		}
	}
	
	/**
	 * Clean up any objects used by the Host.
	 */
	protected void cleanUp()
	{
		socket.close();
		try
		{
			bufferedReader.close();
		}
		catch (IOException e)
		{
			System.err.println("IOException " + e.getMessage());
			System.exit(0);
		}
	}
	
	@Override
	public void run()
	{
		
//		System.out.println("Specify the directory where the files will be transfered");
//		try
//		{
//			directoryName = bufferedReader.readLine();
//		}
//		catch (IOException e)
//		{
//			System.err.println("IOException " + e.getMessage());
//			System.exit(0);
//		}
	}
	
	/**
	 * Prints the specified string if the output mode is verbose.
	 * @param str The string to be printed.
	 */
	protected void print(String str)
	{
		if(outputMode == OutputMode.VERBOSE)
		{
			System.out.println(str);
		}
	}
}