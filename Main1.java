package sevenseg;

public class Main1 {

	// 空白、および0から9の数字の点灯箇所と非点灯箇所
	public static String[] POS = "3f	06	5b	4f	66	6d	7d	27	7f	6f	00".split("\t");
	public static String[] NEG = "40	79	24	30	19	12	02	58	00	10	7f".split("\t");
	
	public static void main(String[] args) {
		/*0*/ test( "06:4b:46:64:6d,79:20:10:10:02", "12345,13996" );    
		/*1*/ test( "41:00,3e:01", "-" );    
		/*2*/ test( "00:00,79:79", "1,11" );    
		/*3*/ test( "02:4b:46:64,20:20:10:10", "1234,3399" );    
		/*4*/ test( "06:2f:3f:27,40:00:00:40", "1000,7987" );    
		/*5*/ test( "00:3d:2d:26,00:00:00:00", "600,9899" );    
		/*6*/ test( "40:20:10,00:00:00", "200,998" );    
		/*7*/ test( "00:00:00,40:20:10", "1,739" );    
		/*8*/ test( "08:04:02:01,00:00:00:00", "2000,9999" );    
		/*9*/ test( "00:00:00:00,08:04:02:01", "1,7264" );    
		/*10*/ test( "08:04:02:01,01:02:04:08", "-" );    
		/*11*/ test( "04:02:01,02:04:08", "527,627" );    
		/*12*/ test( "04:02:01:40:10,02:04:08:10:20", "52732,62792" );    
		/*13*/ test( "00:30:07,00:01:10", "-" );    
		/*14*/ test( "37,00", "0,8" );    
		/*15*/ test( "3f,40", "0,0" );    
		/*16*/ test( "3f:3f,40:40", "-" );    
		/*17*/ test( "00:3f,40:40", "0,70" );    
		/*18*/ test( "00:3f,38:00", "0,18" );    
		/*19*/ test( "18,07", "-" );    
		/*20*/ test( "08,10", "3,9" );    
		/*21*/ test( "42,11", "4,4" );    
		/*22*/ test( "18,05", "-" );    
		/*23*/ test( "10:00,0b:33", "-" );    
		/*24*/ test( "14:02,00:30", "61,83" );    
		/*25*/ test( "00:1a,3d:04", "2,2" );    
		/*26*/ test( "00:28,38:40", "0,10" );    
		/*27*/ test( "20:08:12,4f:37:24", "-" );    
		/*28*/ test( "02:4c:18,00:00:04", "132,992" );    
		/*29*/ test( "4a:7a:02,10:00:30", "381,983" );    
		/*30*/ test( "00:00:06,0b:11:08", "1,47" );    
		/*31*/ test( "04:20:2c:14,39:08:50:09", "-" );    
		/*32*/ test( "02:06:02:02,00:31:18:11", "1111,9174" );    
		/*33*/ test( "00:04:48:50,03:02:20:02", "526,636" );    
		/*34*/ test( "00:58:42:40,00:20:08:12", "245,9245" );    
		/*35*/ test( "08:08:60:00:32,76:67:02:16:04", "-" );    
		/*36*/ test( "00:00:00:08:02,06:1a:3b:20:11", "21,34" );    
		/*37*/ test( "08:58:12:06:12,10:20:20:00:04", "32202,92292" );    
		/*38*/ test( "00:10:74:4e:10,10:04:02:00:24", "2632,92692" );    
		/*39*/ test( "44:76:0a:00:0c:44,39:08:11:09:02:11", "-" );    
		/*40*/ test( "00:00:44:0a:04:00,79:06:02:04:79:28", "5211,6211" );    
		/*41*/ test( "30:02:02:2c:0e:02,00:08:04:02:20:01", "612531,872634" );    
		/*42*/ test( "00:00:04:10:00:60,25:19:01:02:24:00", "1624,44629" );    
		/*43*/ test( "04:18:54:38:00:14:70,10:65:09:01:6c:00:0d", "-" );    
		/*44*/ test( "18:04:26:20:04:24:1a,02:21:50:48:02:08:00", "6177540,6177678" );    
		/*45*/ test( "00:08:34:00:00:64:06,18:24:02:00:61:08:61", "260141,7269141" );    
		/*46*/ test( "00:02:0a:04:4a:00:20,18:21:24:02:04:60:19", "125214,7126214" );
	}

	public static void test(String in, String out) {
//		System.out.println(getMinMax(in));
		if (out.equals(getMinMax(in))) {
			System.out.println("OK");
		} else {
			System.out.println("NG");
		}
	}
	
	public static String getMinMax(String s) {
		String[] inputs = s.split(",", 0);
		String[] on = inputs[0].split(":", 0);
		String[] off = inputs[1].split(":", 0);
		
		if (on.length != off.length) {
			throw new IllegalArgumentException("明と暗の桁数が異なります。");
		}
		int numDigits = on.length;
		int[] maybeNums = new int[numDigits];
		for (int i = 0; i < numDigits; i++) {
			int maybeNum = judge(on[i], off[i]);
			// 空白も数字も入らない場合、または右端に空白しか入らない場合
			if (maybeNum == 0 || (i == numDigits - 1 && maybeNum == (1 << 10))) {
				return "-";
			}			
			maybeNums[i] = maybeNum;
		}
		
		// 空白確定桁より左に数字確定桁がある場合
		boolean checkFlg = false;
		// 右端が空白でないことはチェック済なので、右から2番目の桁から順にチェックする
		for (int i = numDigits - 2; i >= 0 ; i--) {
			if (checkFlg && (maybeNums[i] & (1 << 10)) == 0) {
				return "-";
			}
			if (!checkFlg && maybeNums[i] == (1 << 10)) {
				checkFlg = true;
			}
		}
		
		// 右端以外の0確定桁より左に1〜9が存在しえない場合
		for (int i = 0; i < numDigits - 1; i++) {
			// 1〜9が入り得る桁が見つかった場合
			if ((maybeNums[i] & ((1 << 10) - 2)) > 0) {
				break;
			}
			// 0確定桁が見つかった場合
			if (maybeNums[i] == 1) {
				return "-";
			}
		}
		
		// 最大値を求める
		StringBuilder max = new StringBuilder();
		boolean allowsZero = false;
		for (int i = 0; i < numDigits; i++) {
			if (i == numDigits - 1) {
				allowsZero = true;
			}
			int d = getMaxDigit(maybeNums[i]);
			if (d < 0) {
				max = new StringBuilder();
			} else if (d > 0 || (allowsZero && d == 0)) {
				max.append(d);
			}
		}
		
		// 最小値を求める
		StringBuilder min = new StringBuilder();
		allowsZero = false;
		boolean allowsSpace = true;
		for (int i = 0; i < numDigits; i++) {
			if (i == numDigits - 1) {
				allowsZero = true;
			}
			int d = getMinDigit(maybeNums[i], allowsZero);
//			System.out.println("d: " + d + ", i: " + i);
//			System.out.println("canBeSpace(maybeNums[i]): " + canBeSpace(maybeNums[i]));
			if (allowsSpace && i != numDigits - 1 && canBeSpace(maybeNums[i])) {
				min = new StringBuilder();
			} else if (d > 0 || (allowsZero && d == 0)) {
				min.append(d);
				allowsZero = true;
				allowsSpace = false;
			}
		}
		
		return min.toString() + "," + max.toString();
	}

	/**
	 * 空白が入り得るか判定する
	 * @param i
	 * @return
	 */
	private static boolean canBeSpace(int i) {
		if ((i & (1 << 10)) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 明と暗から可能性のある数字を割り出す
	 * @param on
	 * @param off
	 * @return 0から9、および空白をビットで表したもの
	 */
	private static int judge(String on, String off) {
		int result = (1 << 11) - 1;
		for (int i = 0; i < 11; i++) {
			if ((Integer.parseInt(on, 16) & Integer.parseInt(NEG[i], 16)) > 0
					|| (Integer.parseInt(off, 16) & Integer.parseInt(POS[i], 16)) > 0) {
				result -= 1 << i;
			}
		}
		return result;
	}

	/**
	 * 可能性のある1桁の数字のうち、最大値を返す
	 * 空白しか入らない場合は-1を、何も入らない場合は-2を返す
	 * @param maybeNum
	 * @return
	 */
	private static int getMaxDigit(int maybeNum) {
		if (maybeNum == 0) {
			return -2;
		}
		if (maybeNum == (1 << 10)) {
			return -1;
		}
		for (int i = 9; i >= 0; i--) {
			if ((maybeNum & (1 << i)) > 0) {
				return i;
			}
		}
		// 想定外
		throw new IllegalStateException("最大値取得時に想定外のエラーが発生しました。");
	}

	/**
	 * 可能性のある1桁の数字のうち、最小値を返す
	 * 空白しか入らない場合は-1を、何も入らない場合は-2を返す
	 * @param maybeNum
	 * @return
	 */
	private static int getMinDigit(int maybeNum, boolean allowsZero) {
		if (maybeNum == 0) {
			return -2;
		}
		if (maybeNum == (1 << 10)) {
			return -1;
		}
		for (int i = 0; i <= 9; i++) {
			if (i == 0 && !allowsZero) {
				continue;
			}
			if ((maybeNum & (1 << i)) > 0) {
				return i;
			}
		}
		// 想定外
		throw new IllegalStateException("最小値取得時に想定外のエラーが発生しました。");
	}
}
