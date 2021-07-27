package Encode;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class EncodeGUI {

	private JFrame frame;
	private JTextField inputPlainTxtBox;
	private JTextField inputCipherTxtBox;
	String inputStr;
	AriaModule aria;
	String resultCipher;
	String resultPlain;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EncodeGUI window = new EncodeGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EncodeGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\uC554\uD638\uD654, \uBCF5\uD638\uD654 \uD504\uB85C\uC81D\uD2B8");
		frame.setBounds(100, 100, 585, 462);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\uC785\uB825\uAC12:");
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 17));
		lblNewLabel.setBounds(45, 25, 111, 36);
		frame.getContentPane().add(lblNewLabel);
		
		inputPlainTxtBox = new JTextField();
		inputPlainTxtBox.setBounds(110, 31, 277, 27);
		frame.getContentPane().add(inputPlainTxtBox);
		inputPlainTxtBox.setColumns(10);
		
		//��ȣȭ �� ������� �����ִ� resultBox01
		JTextArea resultBox01 = new JTextArea();
		resultBox01.setBounds(110, 71, 384, 127);
		resultBox01.setLineWrap(true);
		frame.getContentPane().add(resultBox01);
		
		//��ȣȭ �۾� ���� ��ư
		JButton encryptionBtn = new JButton("\uC554\uD638\uD654");
		encryptionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aria = new AriaModule(); //��,��ȣȭ ��� Ŭ���� ���� �� ����
				inputStr = inputPlainTxtBox.getText(); //�ؽ�Ʈ �ڽ��� �Է°� inputStr�� ����
				resultCipher = aria.startEncrypt(inputStr); //inputStr�� ���� ��ȣȭ �޼ҵ� ����, ��ȣȭ �� ����� ����
				resultBox01.setText(resultCipher); //��ȣȭ �� ����� resultBox01�� ���
				inputPlainTxtBox.setText("");
			}
		});
		encryptionBtn.setBounds(397, 33, 97, 23);
		frame.getContentPane().add(encryptionBtn);
		
		JLabel lblNewLabel_1 = new JLabel("\uC785\uB825\uAC12:");
		lblNewLabel_1.setFont(new Font("����", Font.PLAIN, 17));
		lblNewLabel_1.setBounds(45, 208, 111, 36);
		frame.getContentPane().add(lblNewLabel_1);
		
		inputCipherTxtBox = new JTextField();
		inputCipherTxtBox.setColumns(10);
		inputCipherTxtBox.setBounds(110, 214, 277, 27);
		frame.getContentPane().add(inputCipherTxtBox);

		//��ȣȭ �� ������� �����ִ� resultBox02
		JTextArea resultBox02 = new JTextArea();
		resultBox02.setBounds(110, 262, 384, 127);
		frame.getContentPane().add(resultBox02);
		frame.getContentPane().add(resultBox02);
		
		//��ȣȭ �۾� ���� ��ư
		JButton decryptionBtn = new JButton("\uBCF5\uD638\uD654");
		decryptionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aria = new AriaModule(); //��,��ȣȭ ��� Ŭ���� ���� �� ���� 
				inputStr = inputCipherTxtBox.getText(); //�ؽ�Ʈ �ڽ��� �Է°� inputStr�� ����
				resultPlain = aria.startDecrypt(inputStr); //��ȣȭ �޼ҵ� ����, ��ȣȭ �� ����� ����
				resultBox02.setText(resultPlain); //��ȣȭ �� ����� resultBox02�� ���
				inputCipherTxtBox.setText("");
			}
		});
		decryptionBtn.setBounds(397, 216, 97, 23);
		frame.getContentPane().add(decryptionBtn);
		
	
		
	}
}
