package configure;

public class config {
	private static int interval=1800000;
	private static String configpropurl="c://sql.properties";
	private static String corpid="wm48e1b0413cb61c42";
	private static String txlkey="PR-GKEcqcXjXhXPqGkarumUKHoWHPZDn90qQWokSy6UBRVI8fIMiXlQ4WZl4K9e_";
	private static String userdbdriver="oracle.jdbc.driver.OracleDriver";
	private static String userdburl="jdbc:oracle:thin:@219.219.0.70:1521:zhfwdb";
	private static String userdbroot="usr_qqmail";
	private static String userdbpw="qq1106mail";
	private static String localdburl="jdbc:mysql://127.0.0.1:3306/sqxy";
	private static String localdbroot="root";
	private static String localdbpw="Nantu123";
	
	public static int getInterval() {
		return interval;
	}

	public static String getConfigpropurl() {
		return configpropurl;
	}

	public static String getCorpid() {
		return corpid;
	}

	public static String getTxlkey() {
		return txlkey;
	}

	public static String getUserdbdriver() {
		return userdbdriver;
	}

	public static String getUserdburl() {
		return userdburl;
	}

	public static String getUserdbroot() {
		return userdbroot;
	}

	public static String getUserdbpw() {
		return userdbpw;
	}

	public static String getLocaldburl() {
		return localdburl;
	}

	public static String getLocaldbroot() {
		return localdbroot;
	}

	public static String getLocaldbpw() {
		return localdbpw;
	}

}
