package view;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.Serializable;

public class AboutDialog extends JDialog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2736054641339427970L;
	/**
    * Create a new AboutDialg object and then
    * initialize that
	 */
	public AboutDialog() {
		initialize() ;
	}
	/**
	 * initialize a aboutDialog object
	 */
	private void initialize() {
		setSize(200,200);
		setResizable(false);
		
		JLabel aboutLabel = new JLabel("    About            ") ;
		aboutLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JLabel emptyLabel = new JLabel("                        ") ;
		emptyLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JLabel emptyLabel2 = new JLabel("                     ") ;
		emptyLabel2.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JLabel developersLabel = new JLabel("Manufacturer   ") ;
		developersLabel.setFont(new Font("Tahoma", Font.ITALIC, 15));
		JLabel myNameIs = new JLabel("Ali Nowrouzi") ;
		JLabel contactUs = new JLabel("Contact Us: ") ;
		contactUs.setFont(new Font("Tahoma", Font.ITALIC, 14));
		JLabel myEmailIs = new JLabel("AliNowrouzi@aut.ac.ir") ;
		myEmailIs.setFont(new Font("Tahoma", Font.BOLD, 13));

		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(aboutLabel) ;
		getContentPane().add(emptyLabel) ;
		getContentPane().add(developersLabel) ;
		getContentPane().add(myNameIs) ;
		getContentPane().add(emptyLabel2) ;
		getContentPane().add(contactUs) ;
		getContentPane().add(myEmailIs) ;
	}

}
