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

//암호화 작업 Methods/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//입력한 문자열 값을 16바이트 만큼 자른뒤에 암호화 작업 진행한 뒤에 ArrayList에 저장 
	public String startEncrypt(String inputStr) {
		String returnCipherResult = ""; //최종적으로 암호화 된 값을 저장하는 변수
		int inStrLen = inputStr.length(); //암호화 시킬 plain 문자열 값의 길이 변수화

		if (inStrLen <= 0)
			System.out.println("입력값이 없습니다.");

		while (inStrLen > 0) { // inputStr의 길이가 16보다 긴경우 inputStr을 16바이트 길이만큼 가져온 뒤 암호화 작업후 inputStr을 앞에서 가져온 16길이만큼 자르고 다시 inputStr에 넣어준다.
			cipher = new byte[16]; // 암호화 된 블럭이 저장될 배열
			String cutStr = byteCuter(inputStr, 16); // inputStr에서 16바이트 길이만큼 자른 문자열을 cutStr 변수에 넣어준다.

			plain = make16Byte(cutStr.getBytes()); //16바이트 길이의 문자열 cutStr을 바이트 배열로 만들고 16byte 블럭화 시켜주는 메소드 make16Byte로 넘겨준다. 블럭화 된 배열값이 plain에 들어간다. 
			encrypt(mk, plain, cipher); //마스터 키 mk와 암호화 할 블럭 plain, 암호화 값이 저장될 블럭 cipher을 매개변수로 넘겨준다. 
			cipherResult.add(cipher); // 암호화 한 cipher을 cipherResult리스트에 추가
			if (inStrLen > 16) { //inputStr의 길이가 16바이트 보다 큰경우
				inputStr = inputStr.substring(16); // 기존의 inputStr에서 16바이트 자르고 inputStr에 넣기
				inStrLen = inputStr.length(); //길이 또한 자른 문자열의 길이로 변경
			} else
				inStrLen = 0; //inputStr의 길이가 16바이트보다 작은경우에는 0으로 변경함으로써 while문을 한번만 돌게 해준다. 
		}

		returnCipherResult = showCipherResult(); //16진수로 변경된 암호화 값을 리턴받아 returnCipherResult에 저장한다.

		return returnCipherResult;
	}

//암호화 메소드(KISA제공)/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void encrypt(byte[] mk, byte[] p, byte[] c) {
		PrintStream out = System.out;
		try {
			instance = new ARIAEngine(256);

			for (int i = 0; i < 32; i++)
				mk[i] = 0;

			instance.setKey(mk); // 마스터 키 할당
			instance.setupRoundKeys();
			// out.print("plaintext : ");
			// ARIAEngine.printBlock(out, p); // 암호화 전 블록 출력
			// out.println();
			instance.encrypt(p, 0, c, 0); // 암호화 작업
			// out.print("ciphertext: ");
			// ARIAEngine.printBlock(out, c); // 암호화 후 블럭 출력
			// out.println();

		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//암호화 된 값을 16진수로 표현하여 출력//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String showCipherResult() {
		String returnCipherResult = "";
		System.out.println("===============================================================================");
		System.out.println("=============================== cipherResult 출력 ===============================");
		System.out.println("===============================================================================");
		for (byte[] resultArray : cipherResult) { //암호화 된 값이 저장되어있는 리스트 cipherResult에서 하나씩 꺼내 16진수로 변경후 문자열 returnCipherResult에 저장해준다.
			for (byte result : resultArray) {
				System.out.print(Integer.toString(((byte) result & 0xff) + 0x100, 16).substring(1) + " ");
				returnCipherResult += Integer.toString(((byte) result & 0xff) + 0x100, 16).substring(1);
			}
		}
		System.out.println("\n");

		return returnCipherResult; //16진수로 변경된 암호화 값을 리턴해준다.
	}

//암호화 작업 Methods/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
//복호화 작업 Methods/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public String startDecrypt(String inputStr) { //16진수로 표현된 암호화 값이 들어온다(1바이트는 = 16진수 2자리수로 표현됨으로 ----> 16바이트는 = 32자리수)
		String returnPlainResult = ""; //최정적으로 복호화 된 결과를 저장하는 변수  
		int inStrLen = inputStr.length(); //암호화 된 cipher 문자열의 길이를 변수화
		String cutStr = null; //cutStr에는 암호화 문자열이 16바이트가 초과될때 16바이트씩 잘라서 가져와 저장하는 변수이다.
		
		if (inputStr.length() <= 0)
			System.out.println("입력값이 없습니다.");
		
		while(inStrLen > 0) {
			cipher = new byte[16]; //16진수로 표현된 암호화된 값을 16바이트로 변환시 저장할 cipher배열
			plain = new byte[16]; //복호화 된 값을 저장하는 plain배열
			cutStr = byteCuter(inputStr, 32); //16진수로 표현된 암호화 문자열을 32바이트로 잘라서 cutStr에 저장
			cipher = hexStringToByteArray(cutStr); //문자열 16진수 cutStr을 바이트 배열로 반환해서 cipher에 저장  
			
			decrypt(mk, cipher, plain); //복호화 작업
			plainResult.add(plain); //복화화 된 plain을 plainResult리스트에 저장
			
			if (inStrLen > 32) {
				inputStr = inputStr.substring(32); // 기존의 inputStr에서 32바이트 자르고 inputStr에 넣기
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

			instance.setKey(mk); // 마스터 키 할당
			instance.setupRoundKeys();

			instance.decrypt(c, 0, p, 0); // 복호화 작업
			// out.print("decrypted : ");
			// ARIAEngine.printBlock(out, p); //복호화 후 블럭 출력
			// out.println();

		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String showPlainResult() {
		String returnPlainResult = "";

		System.out.println("===============================================================================");
		System.out.println("================================ plainResult 출력 ===============================");
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
//복호화 작업 Methods/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

// 16바이트블록화(16byte)하는  작업//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public byte[] make16Byte(byte[] p) {
		byte[] chngByte = new byte[16]; //암호화 할 하나의 블럭
		for (int i = 0; i < 16; i++) //블럭 내부의 값을 0으로 초기화
			chngByte[i] = 0;

		int nSize = Math.min(p.length, 16); //매개변수로 넘어온 배열p의 길이가 16로 꽉 안찰경우를 

		for (int i = 0; i < nSize; i++) { //넘어온 값을 chngByte배열에 넣어 주고, 길이가 16보다 작은경우의 값은 0으로 차게 된다.
			chngByte[i] = p[i];
		}

		return chngByte; //16바이트로 블럭화 된 것을 반환 
	}

// 바이트 단위로 끊어내고 다시 문자열을 반환해주는 메소드//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

//16진수로 표현된 문자열을 바이트 배열로 변환 해주는 메소드
	public byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
}
