import java.util.ArrayList;
import java.io.File;
import java.awt.event.*;
import java.nio.file.*;
import javax.swing.*;
import java.awt.*;

/**
 * A picture viewer.
 * 
 * @author BruceLee
 */
@SuppressWarnings("serial")
public class PictureViewer extends JFrame implements ActionListener
{
	//*******************************************************************//
	//*								GUIItems							*//
	//*******************************************************************//
	public JFileChooser fileChooser;
	
	public JMenuBar menuBar;
	
	public JLabel imageLabel;
	
	//*******************************************************************//
	//*								MenuItems							*//
	//*******************************************************************//
	public JMenu fileMenu;
	
	public JMenu viewMenu;
	
	public JMenuItem openItem;
	
	public JMenuItem closeItem;
	
	public JMenuItem prevItem;
	
	public JMenuItem nextItem;
	
	//*******************************************************************//
	//*								Buttons								*//
	//*******************************************************************//
	public JButton nextButton;
	
	public JButton prevButton;
	
	//*******************************************************************//
	//*								DataItems							*//
	//*******************************************************************//
	public ArrayList<File> imagePaths;
	
	public int maxImageIndex;
	
	public int selectedIndex;
	
	/**
	 * Constructor for the PictureViewer.
	 */
	public PictureViewer()
	{
		//************************Initializing this frame.
		getContentPane().setLayout(new GridLayout(2, 1));
		setMinimumSize(new Dimension(700, 550));
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Picture Viewer");
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		imagePaths = new ArrayList<File>();
		
		//************************Opening the file.
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
		    try(DirectoryStream<Path> stream = Files.newDirectoryStream(fileChooser.getSelectedFile().toPath()))
		    {
		       for(Path entry : stream)
		       {
		    	   if(entry.toFile().isFile() && PathIsImage(entry))
		    	   {
		    		   imagePaths.add(entry.toFile());
		    	   }
		       }
		    }
		    catch(Exception ex)
		    {
		    	ex.printStackTrace();
				System.exit(1);
		    }
		}
		
		// Showing the image in the frame if there was one.
		if(imagePaths.size() > 0)
		{
			for(File path : imagePaths)
			{
				System.out.println(path.toString());
			}
		}
	}
	
	public static void main(String[] args)
	{
		PictureViewer picViewer = new PictureViewer();
		picViewer.pack();
		picViewer.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Tells whether the given path is an image.
	 * 
	 * @param entry		The path to the image.
	 * @return			Whether the path is an image.
	 */
	private static Boolean PathIsImage(Path entry)
	{
		// Check if file is a hidden file.
		if(entry.toFile().getName().startsWith("."))
		{
			return false;
		}
		
		// Check if the file of the correct type.
		Boolean isImage = 
		(
			entry.toFile().getName().endsWith("jpg")
			|| entry.toFile().getName().endsWith("JPG")
			|| entry.toFile().getName().endsWith("png")
			|| entry.toFile().getName().endsWith("PNG")
			|| entry.toFile().getName().endsWith("bmp")
			|| entry.toFile().getName().endsWith("BMP")
			|| entry.toFile().getName().endsWith("wbmp")
			|| entry.toFile().getName().endsWith("WBMP")
			|| entry.toFile().getName().endsWith("gif")
			|| entry.toFile().getName().endsWith("GIF")
		);	
		
		return isImage;
	}
}
