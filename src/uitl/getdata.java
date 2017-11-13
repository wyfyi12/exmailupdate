package uitl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import configure.config;


/**
 * 读取配置文件中的信息
 * @author -_-
 *
 */
public class getdata {
	static Properties prop = new Properties();
	private static Logger logger = Logger.getLogger(getdata.class);
	public static String dbuser = "";
	public static String mysqlpassword = "";
	public static String dbname = "";
	public static String namezd = "";
	public static String ExtIdzd = "";
	public static String domain = "";
	public static String Genderzd = "";
	public static String partyzd = "";
	public static String corpid = "";
	public static String userkey = "";
	public static String mobilezd = "";
	public static String passwordzd = "";
	public static String statuszd = "";
	public static String sqlname = "";
	public static void getconn() {
		try{
    	 InputStream ip = new FileInputStream(config.getConfigpropurl());
 		 prop.load(ip);
		dbuser = prop.getProperty("username");
		sqlname=prop.getProperty("sqlname");
		mysqlpassword = prop.getProperty("userpassword");
		domain = "@" + prop.getProperty("domain");
		corpid = prop.getProperty("alias");
		userkey = prop.getProperty("key");
		dbname = prop.getProperty("dbname");
		ExtIdzd = prop.getProperty("ExtIdzd");
		Genderzd = prop.getProperty("Genderzd");
		namezd = prop.getProperty("namezd");
		partyzd = prop.getProperty("partyzd");
		mobilezd=prop.getProperty("mobilezd");
		passwordzd=prop.getProperty("passwordzd");
		statuszd=prop.getProperty("statuszd");
 		 ip.close();}catch(Exception e){
 			 logger.info(e);
 		 }
	}
}
