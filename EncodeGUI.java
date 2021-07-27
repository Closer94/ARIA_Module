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
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 17));
		lblNewLabel.setBounds(45, 25, 111, 36);
		frame.getContentPane().add(lblNewLabel);
		
		inputPlainTxtBox = new JTextField();
		inputPlainTxtBox.setBounds(110, 31, 277, 27);
		frame.getContentPane().add(inputPlainTxtBox);
		inputPlainTxtBox.setColumns(10);
		
		//암호화 된 결과값을 보여주는 resultBox01
		JTextArea resultBox01 = new JTextArea();
		resultBox01.setBounds(110, 71, 384, 127);
		resultBox01.setLineWrap(true);
		frame.getContentPane().add(resultBox01);
		
		//암호화 작업 실행 버튼
		JButton encryptionBtn = new JButton("\uC554\uD638\uD654");
		encryptionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aria = new AriaModule(); //암,복호화 모듈 클래스 선언 및 생성
				inputStr = inputPlainTxtBox.getText(); //텍스트 박스에 입력값 inputStr에 저장
				resultCipher = aria.startEncrypt(inputStr); //inputStr에 대한 암호화 메소드 실행, 암호화 된 결과값 리턴
				resultBox01.setText(resultCipher); //암호화 된 결과값 resultBox01에 출력
				inputPlainTxtBox.setText("");
			}
		});
		encryptionBtn.setBounds(397, 33, 97, 23);
		frame.getContentPane().add(encryptionBtn);
		
		JLabel lblNewLabel_1 = new JLabel("\uC785\uB825\uAC12:");
		lblNewLabel_1.setFont(new Font("굴림", Font.PLAIN, 17));
		lblNewLabel_1.setBounds(45, 208, 111, 36);
		frame.getContentPane().add(lblNewLabel_1);
		
		inputCipherTxtBox = new JTextField();
		inputCipherTxtBox.setColumns(10);
		inputCipherTxtBox.setBounds(110, 214, 277, 27);
		frame.getContentPane().add(inputCipherTxtBox);

		//복호화 된 결과값을 보여주는 resultBox02
		JTextArea resultBox02 = new JTextArea();
		resultBox02.setBounds(110, 262, 384, 127);
		frame.getContentPane().add(resultBox02);
		frame.getContentPane().add(resultBox02);
		
		//복호화 작업 실행 버튼
		JButton decryptionBtn = new JButton("\uBCF5\uD638\uD654");
		decryptionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aria = new AriaModule(); //암,복호화 모듈 클래스 선언 및 생성 
				inputStr = inputCipherTxtBox.getText(); //텍스트 박스에 입력값 inputStr에 저장
				resultPlain = aria.startDecrypt(inputStr); //복호화 메소드 실행, 복호화 된 결과값 리턴
				resultBox02.setText(resultPlain); //복호화 된 결과값 resultBox02에 출력
				inputCipherTxtBox.setText("");
			}
		});
		decryptionBtn.setBounds(397, 216, 97, 23);
		frame.getContentPane().add(decryptionBtn);
		
	
		
	}
}
