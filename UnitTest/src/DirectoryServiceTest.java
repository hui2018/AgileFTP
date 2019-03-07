import org.apache.commons.net.ftp.FTPClient;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;


public class DirectoryServiceTest {

	private FTPClient ftp;
	
	String serverAddress = "10.200.157.251";
    int port = 2121;
    String userId = "james";
    String password = "james";
	
	@Test
	public void ShouldCreateNewDirectory() {
		
		boolean create = false;

		//Given
		String name = "newDirectory4";
		
		//When
		try
		{
			ftp = new FTPClient();
            port = 2121;
            ftp.connect(serverAddress, port);
            ftp.login(userId, password);
			create = ftp.makeDirectory(name);
			ftp.logout();
            ftp.disconnect();
		}
		catch(IOException error)
		{
    		System.out.println("IO operation failed!");
    		error.printStackTrace();
		}
    	//Then
    	Assert.assertTrue(create == true);
	
	
    }
	@Test
	public void ShouldRemoveDirectory() {
		//Given
		boolean remove = false;
		String name = "newDirectory1";
				
		//When
		try
		{
			ftp = new FTPClient();
		    port = 2121;
		    ftp.connect(serverAddress, port);
		    ftp.login(userId, password);
			remove = ftp.removeDirectory(name);
			ftp.logout();
		    ftp.disconnect();
		}
		catch(IOException error)
		{
		    System.out.println("IO operation failed!");
		    error.printStackTrace();
		}
		//Then
		Assert.assertTrue(remove == true);
		
	}
	
	@Test
	public void ShouldChangeDirectory() {
		//Given
		boolean change = false;
		String name = "newDirectory3";
				
		//When
		try
		{
			ftp = new FTPClient();
		    port = 2121;
		    ftp.connect(serverAddress, port);
		    ftp.login(userId, password);
			change = ftp.changeWorkingDirectory(name);
			ftp.logout();
		    ftp.disconnect();
		}
		catch(IOException error)
		{
		    System.out.println("IO operation failed!");
		    error.printStackTrace();
		}
		//Then
		Assert.assertTrue(change == true);
		
	}
	
	@Test
	public void ShouldChangeDirectoryPermission() {
		//Given
		boolean permission = false;
		String name = "newDirectory3";
				
		//When
		try
		{
			ftp = new FTPClient();
		    port = 2121;
		    ftp.connect(serverAddress, port);
		    ftp.login(userId, password);
			permission = ftp.sendSiteCommand("chmod " + "777 " + name);
			ftp.logout();
		    ftp.disconnect();
		}
		catch(IOException error)
		{
		    System.out.println("IO operation failed!");
		    error.printStackTrace();
		}
		//Then
		Assert.assertTrue(permission == true);
		
	}
	
	
}
