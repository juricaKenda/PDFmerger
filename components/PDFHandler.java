import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFHandler {

	private PDDocument outputDoc,inputDoc;
	private PDPage storagePage;
	private String saveLocation;
	
	
	public PDFHandler(String destination) {
		this.outputDoc = new PDDocument();
		this.storagePage = new PDPage();
		this.saveLocation = destination;

	}
	
	public void copyFromTo(String source , int From , int To) {
		try {
			this.outputDoc = new PDDocument();
			this.inputDoc = new PDDocument();
			this.inputDoc = PDDocument.load(new File(source));
			
			if((From <= 0) || (To > this.inputDoc.getNumberOfPages()) || (From > To)) {
				return;
			}
			
			for(int each=From; each <= To; each++) {
				this.storagePage = this.inputDoc.getPage(each);
				this.outputDoc.addPage(storagePage);
			}
			
			this.outputDoc.save(this.saveLocation);
			this.outputDoc.close();
			this.inputDoc.close();
			
		} catch (InvalidPasswordException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void copyPDF(String source) {
		
		try {
			this.inputDoc = new PDDocument();
			this.inputDoc = PDDocument.load(new File(source));
			
			for(int i=0; i< this.inputDoc.getNumberOfPages();i++) {

				this.storagePage = this.inputDoc.getPage(i);
				this.outputDoc.addPage(storagePage);
			}
			
			this.outputDoc.save(new File(this.saveLocation));
			this.inputDoc.close();
		} catch (InvalidPasswordException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	public  String PDFtoString(PDDocument doc) {
		StringBuilder collector = new StringBuilder();
		
		try {
			PDFTextStripper tStripper = new PDFTextStripper();
			String allText = tStripper.getText(doc);
			String[] lines = allText.split("\\n");
			
			
			for(String line : lines) {
				collector.append(line + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return collector.toString();
	}

	
	
}
