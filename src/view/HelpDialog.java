package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

//TO DO : complete this Class on phase 2
public class HelpDialog extends JDialog implements Serializable {
	private JPanel originPanel;
	private JButton nextBtn;
	private JButton previusBtn;
	private int currentPanel;
	private String PATH = "./icons/";

	/**
	 * Create a new helpDialog
	 */
	public HelpDialog() {
		currentPanel = 1;
		originPanel = new JPanel();
		nextBtn = new JButton("Next");
		previusBtn = new JButton("Previus");
		initialize();
	}

	/**
	 * initialize helpDialog
	 */
	private void initialize() {
		setSize(900, 700);
		setBackground(Color.DARK_GRAY);
		setResizable(false);

		CardLayout card = new CardLayout();

		setLayout(new BorderLayout());

		originPanel.setBorder(new EmptyBorder(40, 35, 40, 35));
		originPanel.setLayout(card);
		JScrollPane scrollPane = new JScrollPane(originPanel) ;
		add(scrollPane, BorderLayout.CENTER);
//		add(originPanel, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		JPanel panel_2 = new JPanel();
		JPanel panel_3 = new JPanel();
		JPanel panel_4 = new JPanel();
		JPanel panel_5 = new JPanel();
		JPanel panel_6 = new JPanel();
		JPanel panel_7 = new JPanel();
		JPanel panel_8 = new JPanel();
//		JPanel panel_9 = new JPanel();
//		JPanel panel_10 = new JPanel();

		Image image_1 = Toolkit.getDefaultToolkit().getImage(PATH + "image_1.PNG");
		Image image_2 = Toolkit.getDefaultToolkit().getImage(PATH + "image_2.PNG");
		Image image_3 = Toolkit.getDefaultToolkit().getImage(PATH + "image_3.PNG");
		Image image_4 = Toolkit.getDefaultToolkit().getImage(PATH + "image_4.PNG");
		Image image_5 = Toolkit.getDefaultToolkit().getImage(PATH + "image_5.PNG");
		Image image_6 = Toolkit.getDefaultToolkit().getImage(PATH + "image_6.PNG");
		Image image_7 = Toolkit.getDefaultToolkit().getImage(PATH + "image_7.PNG");
		Image image_8 = Toolkit.getDefaultToolkit().getImage(PATH + "image_8.PNG");
//		Image image_9 = Toolkit.getDefaultToolkit().getImage(PATH + "image_9.PNG");
//		Image image_10 = Toolkit.getDefaultToolkit().getImage(PATH + "image_10.PNG");

		JLabel label_1 = new JLabel(new ImageIcon(image_1));
		JLabel label_2 = new JLabel(new ImageIcon(image_2));
		JLabel label_3 = new JLabel(new ImageIcon(image_3));
		JLabel label_4 = new JLabel(new ImageIcon(image_4));
		JLabel label_5 = new JLabel(new ImageIcon(image_5));
		JLabel label_6 = new JLabel(new ImageIcon(image_6));
		JLabel label_7 = new JLabel(new ImageIcon(image_7));
		JLabel label_8 = new JLabel(new ImageIcon(image_8));
//		JLabel label_9 = new JLabel(new ImageIcon(image_9));
//		JLabel label_10 = new JLabel(new ImageIcon(image_10));

		panel_1.add(label_1);
		panel_2.add(label_2);
		panel_3.add(label_3);
		panel_4.add(label_4);
		panel_5.add(label_5);
		panel_6.add(label_6);
		panel_7.add(label_7);
		panel_8.add(label_8);
//		panel_9.add(label_9);
//		panel_10.add(label_10);

		originPanel.add(panel_1, "1");
		originPanel.add(panel_2, "2");
		originPanel.add(panel_3, "3");
		originPanel.add(panel_4, "4");
		originPanel.add(panel_5, "5");
		originPanel.add(panel_6, "6");
		originPanel.add(panel_7, "7");
		originPanel.add(panel_8, "8");
//		originPanel.add(panel_9, "9");
//		originPanel.add(panel_10, "10");

		nextBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPanel < 10) {
					currentPanel += 1;
					card.show(originPanel, "" + currentPanel);
				}
			}
		});

		previusBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPanel > 1) {
					currentPanel -= 1;
					card.show(originPanel, "" + currentPanel);
				}
			}
		});
		JPanel downPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		downPanel.add(previusBtn);
		downPanel.add(nextBtn);
		add(downPanel, BorderLayout.SOUTH);
	}

}
