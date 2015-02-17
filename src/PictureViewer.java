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
	public static int IMAGE_WIDTH = 700;
	
	public static int IMAGE_HEIGHT = 550;
	
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
	//*								CommandItems						*//
	//*******************************************************************//
	public JPanel commandPanel;
	public GridBagConstraints commandConstr;
	
	public JLabel commandLabel;
	public GridBagConstraints labelConstr;
	
	public JTextField commandField;
	public GridBagConstraints fieldConstr;
	
	public JButton commandSubmit;
	public GridBagConstraints submitConstr;
	
	public JLabel commandError;
	public GridBagConstraints errorConstr;
	
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
	
	public Image defaultImage;
	
	public Image dogImage;
	
	/**
	 * Constructor for the PictureViewer.
	 */
	public PictureViewer()
	{
		setTitle("Inu Viewer");
		getContentPane().setLayout(new GridBagLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		defaultImage = new ImageIcon("InuViewer.jpg").getImage();
		dogImage = new ImageIcon("happyInu.gif").getImage();
		
		initialize();
		openFile();
		
		// Setting menu bar and button panel after everything has been initialized.
		setJMenuBar(menuBar);
		getContentPane().add(imageLabel, imageConstr);
		getContentPane().add(buttonPanel, buttonConstr);
		getContentPane().add(commandPanel, commandConstr);
	}
	
	/**
	 * The main method.
	 * 
	 * @param args			The passed in arguments.
	 */
	public static void main(String[] args)
	{
		PictureViewer picViewer = new PictureViewer();
		picViewer.setMinimumSize(new Dimension(800, 800));
		picViewer.pack();
		picViewer.setLocationRelativeTo(null);
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
		else if(e.getSource() == commandField || e.getSource() == commandSubmit)
		{
			String temp = commandField.getText();
			commandError.setText("");
			
			if(temp.equals("Next") || temp.equals("next"))
			{
				nextImage();
			}
			else if(temp.equals("Previous") || temp.equals("previous"))
			{
				prevImage();
			}
			else if(temp.equals("Default") || temp.equals("default"))
			{
				imageLabel.setIcon(defaultIcon());
			}
			else if(temp.equals("dog"))
			{
				imageLabel.setIcon(dogIcon());
			}
			else
			{
				commandField.setText("");
				commandError.setText("The entered text is invalid. Please enter a valid command.");
			}
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
		
		// Command
		commandPanel = new JPanel();
		commandConstr = new GridBagConstraints();
		
		commandLabel = new JLabel();
		labelConstr = new GridBagConstraints();
		
		commandField = new JTextField();
		fieldConstr = new GridBagConstraints();
		
		commandSubmit = new JButton("Submit");
		submitConstr = new GridBagConstraints();
		
		commandError = new JLabel();
		errorConstr = new GridBagConstraints();
		initCommands();
		
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
		imageConstr.ipadx = 10;
		imageConstr.ipady = 40;
		
		menuBar = new JMenuBar();
		initMenuBar();
	}
	
	/**
	 * Initializes the command section to move to different images.
	 */
	private void initCommands()
	{
		commandLabel.setText("Command: ");
		labelConstr.fill = GridBagConstraints.HORIZONTAL;
		labelConstr.gridx = 0;
		
		commandField.addActionListener(this);
		commandField.setColumns(20);
		fieldConstr.fill = GridBagConstraints.HORIZONTAL;
		fieldConstr.gridx = 1;
		
		commandSubmit.addActionListener(this);
		submitConstr.fill = GridBagConstraints.HORIZONTAL;
		submitConstr.gridx = 2;
		
		errorConstr.fill = GridBagConstraints.HORIZONTAL;
		errorConstr.gridy = 1;
		errorConstr.gridwidth = 3;
		errorConstr.ipady = 10;
		
		commandPanel.setLayout(new GridBagLayout());
		commandPanel.add(commandLabel, labelConstr);
		commandPanel.add(commandField, fieldConstr);
		commandPanel.add(commandSubmit, submitConstr);
		commandPanel.add(commandError, errorConstr);
		
		commandConstr.ipadx = 25;
		commandConstr.gridx = 0;
		commandConstr.gridy = 2;
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
		selectedIndex = 0;
		maxImageIndex = 0;
    	imagePaths.clear();
		
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
		
		initImage();
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
		else
		{
			imageLabel.setIcon(defaultIcon());
		}
	}
	
	/**
	 * Resizes the dog image icon to the appropriate size.
	 * 
	 * @return			The image label icon scaled correctly.
	 */
	private ImageIcon dogIcon()
	{
		Dimension temp = getScaledDimension(dogImage.getWidth(imageLabel), dogImage.getHeight(imageLabel));
		Image newimg = dogImage.getScaledInstance(temp.width, temp.height, Image.SCALE_FAST);
		
		return new ImageIcon(newimg);
	}
	
	/**
	 * Resizes the image label icon to the appropriate size.
	 * 
	 * @return			The image label icon scaled correctly.
	 */
	private ImageIcon scaleIcon()
	{
		Image image = new ImageIcon(imagePaths.get(selectedIndex).toString()).getImage();
		Dimension temp = getScaledDimension(image.getWidth(imageLabel), image.getHeight(imageLabel));
		Image newimg = image.getScaledInstance(temp.width, temp.height, Image.SCALE_FAST);
		
		return new ImageIcon(newimg);
	}
	
	/**
	 * Resizes the default image icon to the appropriate size.
	 * 
	 * @return			The image label icon scaled correctly.
	 */
	public ImageIcon defaultIcon()
	{
		Dimension temp = getScaledDimension(defaultImage.getWidth(imageLabel), defaultImage.getHeight(imageLabel));
		Image newimg = defaultImage.getScaledInstance(temp.width, temp.height, Image.SCALE_FAST);
		
		return new ImageIcon(newimg);
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
		if(maxImageIndex > 0)
		{
			// Moves to the next index if within the accessible range, else move to the first image.
			selectedIndex = (selectedIndex < maxImageIndex) ? (selectedIndex + 1) : 0;
			imageLabel.setIcon(scaleIcon());
		}
		else
		{
			// Sets the icon to the default image icon.
			imageLabel.setIcon(defaultIcon());
		}
	}
	
	/**
	 * Sets the image to be shown to the previous image in the file.
	 */
	private void prevImage()
	{
		if(maxImageIndex > 0)
		{
			// Moves to the previous index if within the accessible range, else move to the last image.
			selectedIndex = (selectedIndex > 0) ? (selectedIndex - 1) : maxImageIndex;
			imageLabel.setIcon(scaleIcon());
		}
		else
		{
			// Sets the icon to the default image icon.
			imageLabel.setIcon(defaultIcon());
		}
	}
	
	/**
	 * Gets the dimension for the newly scaled image.
	 * 
	 * @param origWidth		The width of the original image.
	 * @param origHeight	The height of the original image.
	 * 
	 * @return				The new dimension of which to scale the image.
	 */
	public Dimension getScaledDimension(int origWidth, int origHeight)
	{
	    int boundWidth = IMAGE_WIDTH;
	    int boundHeight = IMAGE_HEIGHT;
	    int newWidth = origWidth;
	    int newHeight = origHeight;
	    
	    // first check if we need to scale width
	    if(origWidth > boundWidth)
	    {
	        //scale width to fit
	    	newWidth = boundWidth;
	        //scale height to maintain aspect ratio
	    	newHeight = (newWidth * origHeight) / origWidth;
	    }
	    
	    // then check if we need to scale even with the new height
	    if(newHeight > boundHeight)
	    {
	        //scale height to fit instead
	    	newHeight = boundHeight;
	        //scale width to maintain aspect ratio
	        newWidth = (newHeight * origWidth) / origHeight;
	    }
	    
	    return new Dimension(newWidth, newHeight);
	}
}
