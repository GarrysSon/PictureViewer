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
	public JPanel buttonPanel;
	
	public JButton nextButton;
	
	public JButton prevButton;
	
	//*******************************************************************//
	//*								DataItems							*//
	//*******************************************************************//
	public ArrayList<File> imagePaths;
	
	public int maxImageIndex;
	
	public int selectedIndex;
	
	//*******************************************************************//
	//*								GUIItems							*//
	//*******************************************************************//
	public JFileChooser fileChooser;
	
	public JMenuBar menuBar;
	
	public JLabel imageLabel;
	
	/**
	 * Constructor for the PictureViewer.
	 */
	public PictureViewer()
	{
		//************************Initializing this frame.
		setTitle("Picture Viewer");
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(700, 550));
		getContentPane().setLayout(new GridLayout(2, 1));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//************************Initializing variables
		initialize();
		
		//************************Opening the file.
		openFile();
		
		//************************Initializing the image.
		initImage();
		
		// Setting menu bar and button panel after everything has been initialized.
		setJMenuBar(menuBar);
		getContentPane().add(buttonPanel);
	}
	
	/**
	 * The main method.
	 * 
	 * @param args			The passed in arguments.
	 */
	public static void main(String[] args)
	{
		PictureViewer picViewer = new PictureViewer();
		picViewer.pack();
		picViewer.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == closeItem)
		{
			System.exit(0);
		}
		else if(e.getSource() == openItem)
		{
			System.out.println("Opening a new directory.");
		}
		else if(e.getSource() == nextItem || e.getSource() == nextButton)
		{
			nextImage();
		}
		else if(e.getSource() == prevItem || e.getSource() == prevButton)
		{
			prevImage();
		}
	}
	
	/**
	 * Initializes all variables.
	 */
	private void initialize()
	{
		// Menu Items
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open");
		closeItem = new JMenuItem("Close");
		viewMenu = new JMenu("View");
		prevItem = new JMenuItem("Previous Image");
		nextItem = new JMenuItem("Next Image");
		
		// Buttons
		buttonPanel = new JPanel();
		nextButton = new JButton("Previous");
		prevButton = new JButton("Next");
		initButtons();
		
		// Data Items
		imagePaths = new ArrayList<File>();
		maxImageIndex = 0;
		selectedIndex = 0;
		
		// GUI Items
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		imageLabel = new JLabel();
		menuBar = new JMenuBar();
		initMenuBar();
	}
	
	/**
	 * Initializes the buttons to move to different images.
	 */
	private void initButtons()
	{
		nextButton.addActionListener(this);
		prevButton.addActionListener(this);
		
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(nextButton);
		buttonPanel.add(prevButton);
	}
	
	/**
	 * Initializes the menu bar for the picture viewer.
	 */
	private void initMenuBar()
	{
		// File menu in the menu bar.
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK | Event.SHIFT_MASK));
		openItem.setToolTipText("Open a file.");
		openItem.addActionListener(this);
		
		closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK | Event.SHIFT_MASK));
		closeItem.setToolTipText("Close the picture viewer.");
		closeItem.addActionListener(this);
		
		fileMenu.add(openItem);
		fileMenu.add(new JSeparator(SwingConstants.HORIZONTAL));
		fileMenu.add(closeItem);
		
		// View menu in the menu bar.
		nextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK | Event.SHIFT_MASK));
		nextItem.setToolTipText("Move to the next image.");
		nextItem.addActionListener(this);
		
		prevItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK | Event.SHIFT_MASK));
		prevItem.setToolTipText("Move to the previous image.");
		prevItem.addActionListener(this);
		
		viewMenu.add(nextItem);
		viewMenu.add(prevItem);
		
		// Adding both the File and View menus to the menu bar.
		menuBar.add(fileMenu);
		menuBar.add(viewMenu);
	}
	
	/**
	 * Opens a file and populates the image paths arraylist.
	 */
	private void openFile()
	{
		// If the user chooses a directory to open, try to get all contained file paths.
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
		       
		       // Setting the maximum-index tracking variable.
		       maxImageIndex = imagePaths.size() - 1;
		    }
		    catch(Exception ex)
		    {
		    	ex.printStackTrace();
				System.exit(1);
		    }
		}
	}
	
	/**
	 * Initialize the image to be shown.
	 */
	private void initImage()
	{
		// Initializing the image in the frame if there was one.
		if(imagePaths.size() > 0)
		{
			imageLabel.setIcon(new ImageIcon(imagePaths.get(selectedIndex).toString()));
			getContentPane().add(imageLabel);
		}
	}
	
	/**
	 * Tells whether the given file path is an image.
	 * 
	 * @param entry		The path to the file.
	 * @return			Whether the file path is to an image.
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
	
	/**
	 * Sets the image to be shown to the next image in the file.
	 */
	private void nextImage()
	{
		// Moves to the next index if within the accessible range, else move to the first image.
		selectedIndex = (selectedIndex < maxImageIndex) ? (selectedIndex + 1) : 0;
		imageLabel.setIcon(new ImageIcon(imagePaths.get(selectedIndex).toString()));
	}
	
	/**
	 * Sets the image to be shown to the previous image in the file.
	 */
	private void prevImage()
	{
		// Moves to the previous index if within the accessible range, else move to the last image.
		selectedIndex = (selectedIndex > 0) ? (selectedIndex - 1) : maxImageIndex;
		imageLabel.setIcon(new ImageIcon(imagePaths.get(selectedIndex).toString()));
	}
}
