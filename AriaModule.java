package Encode;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

public class AriaModule {
	ARIAEngine instance;
	ArrayList<byte[]> plainResult;
	ArrayList<byte[]> cipherResult;
	byte[] mk;
	byte[] plain;
	byte[] cipher;

	public AriaModule() {
		instance = null;
		plainResult = new ArrayList<byte[]>();
		cipherResult = new ArrayList<byte[]>();
		mk = new byte[32];
		plain = null;
		cipher = null;
	}

//��ȣȭ �۾� Methods/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//�Է��� ���ڿ� ���� 16����Ʈ ��ŭ �ڸ��ڿ� ��ȣȭ �۾� ������ �ڿ� ArrayList�� ���� 
	public String startEncrypt(String inputStr) {
		String returnCipherResult = ""; //���������� ��ȣȭ �� ���� �����ϴ� ����
		int inStrLen = inputStr.length(); //��ȣȭ ��ų plain ���ڿ� ���� ���� ����ȭ

		if (inStrLen <= 0)
			System.out.println("�Է°��� �����ϴ�.");

		while (inStrLen > 0) { // inputStr�� ���̰� 16���� ���� inputStr�� 16����Ʈ ���̸�ŭ ������ �� ��ȣȭ �۾��� inputStr�� �տ��� ������ 16���̸�ŭ �ڸ��� �ٽ� inputStr�� �־��ش�.
			cipher = new byte[16]; // ��ȣȭ �� ���� ����� �迭
			String cutStr = byteCuter(inputStr, 16); // inputStr���� 16����Ʈ ���̸�ŭ �ڸ� ���ڿ��� cutStr ������ �־��ش�.

			plain = make16Byte(cutStr.getBytes()); //16����Ʈ ������ ���ڿ� cutStr�� ����Ʈ �迭�� ����� 16byte ��ȭ �����ִ� �޼ҵ� make16Byte�� �Ѱ��ش�. ��ȭ �� �迭���� plain�� ����. 
			encrypt(mk, plain, cipher); //������ Ű mk�� ��ȣȭ �� �� plain, ��ȣȭ ���� ����� �� cipher�� �Ű������� �Ѱ��ش�. 
			cipherResult.add(cipher); // ��ȣȭ �� cipher�� cipherResult����Ʈ�� �߰�
			if (inStrLen > 16) { //inputStr�� ���̰� 16����Ʈ ���� ū���
				inputStr = inputStr.substring(16); // ������ inputStr���� 16����Ʈ �ڸ��� inputStr�� �ֱ�
				inStrLen = inputStr.length(); //���� ���� �ڸ� ���ڿ��� ���̷� ����
			} else
				inStrLen = 0; //inputStr�� ���̰� 16����Ʈ���� ������쿡�� 0���� ���������ν� while���� �ѹ��� ���� ���ش�. 
		}

		returnCipherResult = showCipherResult(); //16������ ����� ��ȣȭ ���� ���Ϲ޾� returnCipherResult�� �����Ѵ�.

		return returnCipherResult;
	}

//��ȣȭ �޼ҵ�(KISA����)/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void encrypt(byte[] mk, byte[] p, byte[] c) {
		PrintStream out = System.out;
		try {
			instance = new ARIAEngine(256);

			for (int i = 0; i < 32; i++)
				mk[i] = 0;

			instance.setKey(mk); // ������ Ű �Ҵ�
			instance.setupRoundKeys();
			// out.print("plaintext : ");
			// ARIAEngine.printBlock(out, p); // ��ȣȭ �� ��� ���
			// out.println();
			instance.encrypt(p, 0, c, 0); // ��ȣȭ �۾�
			// out.print("ciphertext: ");
			// ARIAEngine.printBlock(out, c); // ��ȣȭ �� �� ���
			// out.println();

		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//��ȣȭ �� ���� 16������ ǥ���Ͽ� ���//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String showCipherResult() {
		String returnCipherResult = "";
		System.out.println("===============================================================================");
		System.out.println("=============================== cipherResult ��� ===============================");
		System.out.println("===============================================================================");
		for (byte[] resultArray : cipherResult) { //��ȣȭ �� ���� ����Ǿ��ִ� ����Ʈ cipherResult���� �ϳ��� ���� 16������ ������ ���ڿ� returnCipherResult�� �������ش�.
			for (byte result : resultArray) {
				System.out.print(Integer.toString(((byte) result & 0xff) + 0x100, 16).substring(1) + " ");
				returnCipherResult += Integer.toString(((byte) result & 0xff) + 0x100, 16).substring(1);
			}
		}
		System.out.println("\n");

		return returnCipherResult; //16������ ����� ��ȣȭ ���� �������ش�.
	}

//��ȣȭ �۾� Methods/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
//��ȣȭ �۾� Methods/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public String startDecrypt(String inputStr) { //16������ ǥ���� ��ȣȭ ���� ���´�(1����Ʈ�� = 16���� 2�ڸ����� ǥ�������� ----> 16����Ʈ�� = 32�ڸ���)
		String returnPlainResult = ""; //���������� ��ȣȭ �� ����� �����ϴ� ����  
		int inStrLen = inputStr.length(); //��ȣȭ �� cipher ���ڿ��� ���̸� ����ȭ
		String cutStr = null; //cutStr���� ��ȣȭ ���ڿ��� 16����Ʈ�� �ʰ��ɶ� 16����Ʈ�� �߶� ������ �����ϴ� �����̴�.
		
		if (inputStr.length() <= 0)
			System.out.println("�Է°��� �����ϴ�.");
		
		while(inStrLen > 0) {
			cipher = new byte[16]; //16������ ǥ���� ��ȣȭ�� ���� 16����Ʈ�� ��ȯ�� ������ cipher�迭
			plain = new byte[16]; //��ȣȭ �� ���� �����ϴ� plain�迭
			cutStr = byteCuter(inputStr, 32); //16������ ǥ���� ��ȣȭ ���ڿ��� 32����Ʈ�� �߶� cutStr�� ����
			cipher = hexStringToByteArray(cutStr); //���ڿ� 16���� cutStr�� ����Ʈ �迭�� ��ȯ�ؼ� cipher�� ����  
			
			decrypt(mk, cipher, plain); //��ȣȭ �۾�
			plainResult.add(plain); //��ȭȭ �� plain�� plainResult����Ʈ�� ����
			
			if (inStrLen > 32) {
				inputStr = inputStr.substring(32); // ������ inputStr���� 32����Ʈ �ڸ��� inputStr�� �ֱ�
				inStrLen = inputStr.length();
			} else
				inStrLen = 0;
		}

	////////////////////////////////////////////////////////////////////////////////////////////////

	returnPlainResult = showPlainResult();

	return returnPlainResult;

	}

	public void decrypt(byte[] mk, byte[] c, byte[] p) {
		PrintStream out = System.out;
		try {
			instance = new ARIAEngine(256);

			for (int i = 0; i < 32; i++)
				mk[i] = 0;

			instance.setKey(mk); // ������ Ű �Ҵ�
			instance.setupRoundKeys();

			instance.decrypt(c, 0, p, 0); // ��ȣȭ �۾�
			// out.print("decrypted : ");
			// ARIAEngine.printBlock(out, p); //��ȣȭ �� �� ���
			// out.println();

		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String showPlainResult() {
		String returnPlainResult = "";

		System.out.println("===============================================================================");
		System.out.println("================================ plainResult ��� ===============================");
		System.out.println("===============================================================================");
		String result = null;

		try {
			for (byte[] p : plainResult) {
				result = new String(p, "UTF-8");
				System.out.print(result);
				returnPlainResult += result;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnPlainResult;
	}
//��ȣȭ �۾� Methods/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

// 16����Ʈ���ȭ(16byte)�ϴ�  �۾�//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public byte[] make16Byte(byte[] p) {
		byte[] chngByte = new byte[16]; //��ȣȭ �� �ϳ��� ��
		for (int i = 0; i < 16; i++) //�� ������ ���� 0���� �ʱ�ȭ
			chngByte[i] = 0;

		int nSize = Math.min(p.length, 16); //�Ű������� �Ѿ�� �迭p�� ���̰� 16�� �� ������츦 

		for (int i = 0; i < nSize; i++) { //�Ѿ�� ���� chngByte�迭�� �־� �ְ�, ���̰� 16���� ��������� ���� 0���� ���� �ȴ�.
			chngByte[i] = p[i];
		}

		return chngByte; //16����Ʈ�� ��ȭ �� ���� ��ȯ 
	}

// ����Ʈ ������ ����� �ٽ� ���ڿ��� ��ȯ���ִ� �޼ҵ�//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String byteCuter(String st, int cutLeng) {
		if (st.getBytes().length > cutLeng) {
			StringBuilder stringBuilder = new StringBuilder(cutLeng);
			int nCnt = 0;
			for (char ch : st.toString().toCharArray()) {
				nCnt += String.valueOf(ch).getBytes().length;
				if (nCnt > cutLeng)
					break;
				stringBuilder.append(ch);
			}
			return stringBuilder.toString();
		} else {
			return st;
		}
	}

//16������ ǥ���� ���ڿ��� ����Ʈ �迭�� ��ȯ ���ִ� �޼ҵ�
	public byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
}
