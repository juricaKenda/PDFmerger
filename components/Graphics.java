import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar.Separator;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;


public class Graphics {

	private JFrame frame;
	private JMenuBar menu;
	private JMenu addPDF,boundPDF;
	private Font selectedFont = new Font("Consolas",Font.BOLD,16);
	private JMenuItem[] addFiles,boundFiles;
	private File[] files;
	private JFileChooser fileChooser = new JFileChooser();
	private boolean[] isChosen; 
	private JTextArea textArea;
	private JButton goButton; 
	private String header = this.createHeader(),outputLoc;
	private String[] bounds; 
	private PDFhandler handler;
	
	/**Initializing everything visible to the user, as well as internal data collections
	 * 
	 */
	public Graphics() {
		this.setFrame("PDF Merger");
		
		
		this.addPDF = new JMenu("Add PDF");
		this.addPDF.setFont(this.selectedFont);
		this.addFiles = new JMenuItem[5];
		this.addFiles = this.createMenuItems(addPDF,5);
		
		
		this.boundPDF = new JMenu("Bound PDF");
		this.boundPDF.setFont(selectedFont);
		this.boundFiles = this.bound(boundPDF,5);
		
		this.menu = new JMenuBar();
		this.menu.add(addPDF);
		this.separateMenuItems();
		this.menu.add(boundPDF);
		
		
		this.setTextArea();
		this.frame.add(menu,BorderLayout.NORTH);
		this.frame.add(textArea,BorderLayout.CENTER);
		
		this.goButton = new JButton("Merge PDF Files");
		this.goButton.setFont(selectedFont);
		this.goButton.addActionListener(new ButtonListener());
		this.frame.add(goButton,BorderLayout.SOUTH);
		
		
		this.frame.setResizable(false);
		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Handles initial setup of the text area
	 */
	private void setTextArea() {
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.textArea.setFont(selectedFont);
		this.textArea.setForeground(Color.WHITE);
		this.textArea.setBackground(Color.BLACK);
	}

	/**
	 * Adds a couple of separators at once, for the clarity of code
	 */
	private void separateMenuItems() {
		this.menu.add(new Separator());
		this.menu.add(new Separator());
		this.menu.add(new Separator());
	}
	
	/**
	 * A nested class that handles onPress events for the add PDF menus
	 *
	 */
	class AddListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem test =(JMenuItem) e.getSource();

			for(int i=0;i < addFiles.length; i++) {
				JMenuItem each = addFiles[i];
				if(test.equals(each)) {
					if(JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(frame)) {
						File innerFile = fileChooser.getSelectedFile();
						//invalid files will not be added nor handled
						if(innerFile.getName().endsWith(".pdf")) {
							files[i] = innerFile;
						}
						displayStatus();
					}
				}

			}
			
		}
		
		
	}
	/**
	 * Checks which PDF it needs to handle and calls copyFromTo function on them
	 */
	public void mergeFiles() {
		this.handler = new PDFhandler(this.outputLoc);
		for(int i=0;i<this.files.length;i++) {
			if(isChosen[i]) {
				String[] parts = bounds[i].split(" ");
				int from = Integer.parseInt(parts[0]);
				int to = Integer.parseInt(parts[1]);
				this.handler.copyFromTo(files[i].toString(),from,to);
			}
		}
		this.textArea.append("\n"+this.outputLoc);
		
	}
	/**
	 * A nested class that handles onPress events from the goButton
	 *
	 */
	class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton test = (JButton) e.getSource();
			if(test.equals(goButton)) {
				if(JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(frame)) {
					File innerFile = fileChooser.getSelectedFile();
					outputLoc = innerFile.toString();
					mergeFiles();
				}
			}
			
		}
		
	}
	/**
	 * A nested class that handles onPress events from bound menu
	 *
	 */
	class BoundListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem test = (JMenuItem) e.getSource();
			
			for(int i=0;i<boundFiles.length;i++) {
				JMenuItem each = boundFiles[i];
				
				if(test.equals(each)) {
					String input = JOptionPane.showInputDialog(boundPDF, "Enter bounds: (x  y)  format");
					bounds[i] = input;
					displayStatus();
					isChosen[i] = true;
				}
			}
			
		}
		
	}
	/**Menu items that store bounds of PDF files (user set bounds)
	 * 
	 * @param menu
	 * @param size
	 * @return
	 */
	public JMenuItem[] bound(JMenu menu,int size) {
		
		JMenuItem[] items = new JMenuItem[size];
		this.bounds = new String[size];
		
		for(int i=0; i<size; i++) {
			JMenuItem each = new JMenuItem("Bound " + (i+1) + ". pdf");
			each.addActionListener(new BoundListener());
			each.setFont(selectedFont);
			menu.add(each);
			items[i] = each;
			bounds[i] = "";
		}
		return items;
	}
	
	/**Function that displays current status, each time something is updated
	 * 
	 */
	private void displayStatus() {
		this.textArea.setText(this.header);
		for(int i=0;i<files.length;i++) {
			File each = files[i];
			if(each != null) {
				if(!bounds[i].equals("")) {
					String[] parts = bounds[i].split("\\s+");
					this.textArea.append("\n" + each.getName() +emptySpace(2)+"From: "+ parts[0] +"  To:  "+ parts[1]);
				}
				else{
					this.textArea.append("\n" + each.getName());
				}
				
			}
			else {
				this.textArea.append("\n");
			}
		}

	}
	public JMenuItem[] createMenuItems(JMenu menu,int size) {
		JMenuItem[] items = new JMenuItem[size];
		this.files = new File[size];
		this.isChosen = new boolean[size];
		for(int i=0; i<size; i++) {
			JMenuItem each = new JMenuItem("Add " + (i+1) + ". pdf");
			each.addActionListener(new AddListener());
			each.setFont(selectedFont);
			menu.add(each);
			items[i] = each;
		}
	return items;
	}
	
	/**
	 * Sets relevant frame properties
	 * @param title
	 */
	private void setFrame(String title) {
		this.frame = new JFrame(title);
		this.frame.setSize(new Dimension(800,500));
		this.frame.getContentPane().setBackground(Color.BLACK);
		this.frame.setLocationRelativeTo(null);
		this.frame.setLayout(new BorderLayout());
	}
	
	
	public String emptySpace(int n) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<n;i++) {
			builder.append("  ");
		}
		return builder.toString();
	}
	
	public String createHeader() {
		StringBuilder builder = new StringBuilder();
		builder.append("Files");
		return builder.toString(); 
	}
	
	
	class PDFhandler{
		private PDDocument outputDoc,inputDoc;
		private PDPage storagePage;
		private String saveLocation;
		

		
		public PDFhandler(String destination) {
			this.outputDoc = new PDDocument();
			this.storagePage = new PDPage();
			this.saveLocation = destination;
		}
		
		//function that handles PDF copy process
		public void copyFromTo(String source , int From , int To) {
			try {
				this.outputDoc = new PDDocument();
				this.inputDoc = new PDDocument();
				this.inputDoc = PDDocument.load(new File(source));
				
				From--;
				//if the bounds are not set properly, this input file will be ignored
				if((From < 0) || (To > this.inputDoc.getNumberOfPages()) || (From > To)) {
					System.err.println("Error with " + source + "with bounds: "+ From +" "+ To);
					return;
				}
				
				//copy each page from the input document to the output document
				for(int each=From; each < To; each++) {
					this.storagePage = this.inputDoc.getPage(each);
					this.outputDoc.addPage(storagePage);
				}
				
				//make a test file to check if a file with a given name already exists
				//(check if this is not the initial creation of a PDF file and we should append to it
				File testFile = new File(this.saveLocation);
				if(!testFile.exists()) {
					this.outputDoc.save(this.saveLocation);
					this.outputDoc.close();
				}
				else {
					//if a file already exists, we will save our pages in a temporary doc and merge two files together
					//temporary document will be deleted immediately upon transfer
					String secondLoc = grabName(this.saveLocation,"temp.pdf");
					this.outputDoc.save(secondLoc);
					this.outputDoc.close();
					
					PDFMergerUtility PDFmerger = new PDFMergerUtility();
					PDFmerger.setDestinationFileName(this.saveLocation);
					PDFmerger.addSource(testFile);
				    PDFmerger.addSource(secondLoc);
				    PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
				    
				    Files.delete(new File(secondLoc).toPath());
				}
				this.inputDoc.close();
			} catch (InvalidPasswordException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	/**
	 * Grab name and append to it,
	 * to create temporary file that will be deleted 
	 * immediately upon usage
	 */
	private String grabName(String loc,String temp) {
		StringBuilder builder = new StringBuilder();
		System.out.println(loc);
		builder.append(loc);
		builder.append(temp);
		System.out.println(builder.toString());
		return builder.toString();
	}
	
	
	public static void main(String[] args) {
		new Graphics();
	}

}
