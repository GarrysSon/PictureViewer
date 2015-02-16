import java.util.ArrayList;
import java.io.File;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;

import javax.imageio.ImageIO;
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
	public GridBagConstraints buttonConstr;
	
	public JButton nextButton;
	public GridBagConstraints nextConstr;
	
	public JButton prevButton;
	public GridBagConstraints prevConstr;
	
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
	public GridBagConstraints imageConstr;
	
	/**
	 * Constructor for the PictureViewer.
	 */
	public PictureViewer()
	{
		setTitle("Picture Viewer");
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(700, 550));
		getContentPane().setLayout(new GridBagLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		initialize();
		openFile();
		
		// Setting menu bar and button panel after everything has been initialized.
		setJMenuBar(menuBar);
		getContentPane().add(imageLabel, imageConstr);
		getContentPane().add(buttonPanel, buttonConstr);
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
			openFile();
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
		buttonConstr = new GridBagConstraints();
		
		nextButton = new JButton("Next");
		nextConstr = new GridBagConstraints();
		
		prevButton = new JButton("Previous");
		prevConstr = new GridBagConstraints();
		initButtons();
		
		// Data Items
		imagePaths = new ArrayList<File>();
		maxImageIndex = 0;
		selectedIndex = 0;
		
		// GUI Items
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		imageLabel = new JLabel();
		imageConstr = new GridBagConstraints();
		imageConstr.fill = GridBagConstraints.HORIZONTAL;
		imageConstr.ipady = 40;
		
		menuBar = new JMenuBar();
		initMenuBar();
	}
	
	/**
	 * Initializes the buttons to move to different images.
	 */
	private void initButtons()
	{
		prevButton.addActionListener(this);
		prevConstr.fill = GridBagConstraints.HORIZONTAL;
		prevConstr.gridx = 0;
		prevConstr.gridy = 0;
		
		nextButton.addActionListener(this);
		nextConstr.fill = GridBagConstraints.HORIZONTAL;
		nextConstr.gridx = 1;
		nextConstr.gridy = 0;
		
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.add(prevButton, prevConstr);
		buttonPanel.add(nextButton, nextConstr);
		buttonConstr.fill = GridBagConstraints.HORIZONTAL;
		buttonConstr.ipadx = 100;
		buttonConstr.gridx = 0;
		buttonConstr.gridy = 1;
	}
	
	/**
	 * Initializes the menu bar for the picture viewer.
	 */
	private void initMenuBar()
	{
		// File menu in the menu bar.
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		openItem.setToolTipText("Open a file.");
		openItem.addActionListener(this);
		
		closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
		closeItem.setToolTipText("Close the picture viewer.");
		closeItem.addActionListener(this);
		
		fileMenu.add(openItem);
		fileMenu.add(new JSeparator(SwingConstants.HORIZONTAL));
		fileMenu.add(closeItem);
		
		// View menu in the menu bar.
		nextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		nextItem.setToolTipText("Move to the next image.");
		nextItem.addActionListener(this);
		
		prevItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
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
		setVisible(false);
		
		// If the user chooses a directory to open, try to get all contained file paths.
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
		    try(DirectoryStream<Path> stream = Files.newDirectoryStream(fileChooser.getSelectedFile().toPath()))
		    {
		    	selectedIndex = 0;
		    	imagePaths.clear();
		    	
		    	for(Path entry : stream)
		    	{
		    		if(entry.toFile().isFile() && PathIsImage(entry))
		    		{
		    			imagePaths.add(entry.toFile());
		    		}
		    	}
		       
		    	// Setting the maximum-index tracking variable.
		    	maxImageIndex = imagePaths.size() - 1;
		    	initImage();
		    }
		    catch(Exception ex)
		    {
		    	ex.printStackTrace();
				System.exit(1);
		    }
		}
		
		setVisible(true);
	}
	
	/**
	 * Initialize the image to be shown.
	 */
	private void initImage()
	{
		// Initializing the image in the frame if there was one.
		if(imagePaths.size() > 0)
		{	
			imageLabel.setIcon(scaleIcon());
		}
	}
	
	/**
	 * Resizes the image label icon to the appropriate size.
	 * 
	 * @return			The image label icon scaled correctly.
	 */
	private ImageIcon scaleIcon()
	{
		// TODO: Fix the image scaling issue!
		
		Image image = new ImageIcon(imagePaths.get(selectedIndex).toString()).getImage();
		int width = getScaledWidth(image.getWidth(imageLabel));
		int height = getScaledHeight(image.getHeight(imageLabel));
		Image newimg = image.getScaledInstance(width, height, Image.SCALE_FAST);
		
		return new ImageIcon(newimg);
	}
	
	/**
	 * Gets the scaled width for the image icon.
	 * 
	 * @param currentWidth	The current width of the image icon.
	 * @return				The scaled width of the image icon.
	 */
	private int getScaledWidth(int currentWidth)
	{
		int width = 700;
		return width;
	}
	
	/**
	 * Gets the scaled height for the image icon.
	 * 
	 * @param currentHeight	The current height of the image icon.
	 * @return				The scaled height of the image icon.
	 */
	private int getScaledHeight(int currentHeight)
	{
		int height = 550;
		return height;
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
		imageLabel.setIcon(scaleIcon());
	}
	
	/**
	 * Sets the image to be shown to the previous image in the file.
	 */
	private void prevImage()
	{
		// Moves to the previous index if within the accessible range, else move to the last image.
		selectedIndex = (selectedIndex > 0) ? (selectedIndex - 1) : maxImageIndex;
		imageLabel.setIcon(scaleIcon());
	}
}
