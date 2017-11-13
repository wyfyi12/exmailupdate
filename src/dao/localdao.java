package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import bean.user;
import configure.config;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

//用于本地数据库操作
public class localdao {
	private static Logger logger = Logger.getLogger(localdao.class);
	public static Connection conn;
	public static PreparedStatement ps;
	public static ResultSet rs;
	public static Statement st;
	public static HashMap<String, user> umap = new HashMap<String, user>();

	public static void getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(config.getLocaldburl(), config.getLocaldbroot(), config.getLocaldbpw());

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("错误故障为：" + e);
		}
	}

	public static void create() {
		try {
			String sql = "CREATE database localuser;";
			st = (Statement) conn.createStatement();
			st.executeUpdate(sql);
			st.close();
			conn.close();
		} catch (Exception e) {
		}

	}

	public static void createtable() {
		try {
			String sql = "CREATE TABLE localuser (alias varchar(50) NOT NULL ,"+ "mobile varchar(50),  " + "password varchar(50) NOT NULL,  "
					+ " name varchar(50) NOT NULL,  " + "ExtId varchar(50) NOT NULL,  "
					+  "partylist  varchar(50) NOT NULL, "  + " PRIMARY KEY (alias)  "
					+ ") ENGINE=MyISAM  DEFAULT CHARSET=utf8;";
			st = (Statement) conn.createStatement();
			st.executeUpdate(sql);
			st.close();
			conn.close();
		} catch (Exception e) {
			logger.error("错误故障为：" + e);
			e.printStackTrace();
		}
	}

	public static HashMap<String, Integer> searchtable() {
		HashMap<String, Integer> tablename = new HashMap<String, Integer>();
		tablename.put("localuser", 0);
		try {
			String sql = "show tables;";
			st = (Statement) conn.createStatement();

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				String ename = rs.getString("Tables_in_sqxy");
				tablename.put(ename, 1);
			}
			st.close();
		} catch (Exception e) {
			logger.error("错误故障为：" + e);
		}
		return tablename;
	}

	public static ArrayList<Integer> insert(HashMap<String, user> usermap) throws SQLException {
		ArrayList<Integer> re = new ArrayList<Integer>();
		try {
			st = (Statement) conn.createStatement();
			for (String alias : usermap.keySet()) {
				String sql = "insert into localuser(alias,password,name,ExtId,partylist) values('" + alias
						+ "','" + usermap.get(alias).getPassword() + "','" + usermap.get(alias).getName() + "','"
						+ usermap.get(alias).getExtId()  + "','"
						+ usermap.get(alias).getPartylist() + "')";
				int result = st.executeUpdate(sql);
				if (result == -1) {
					System.out.println(usermap.get(alias).getName() + "添加失败");
				} else {
					System.out.println(usermap.get(alias).getName() + "添加成功");
				}
			}

		} catch (SQLException e) {
			logger.error("错误故障为：" + e);
		}
		st.close();
		return re;
	}

	public static ArrayList<Integer> insertuser(JSONObject usermap) throws SQLException {
		ArrayList<Integer> re = new ArrayList<Integer>();
		try {
			st = (Statement) conn.createStatement();

			String sql = "insert into localuser(alias,password,name,ExtId,partylist,mobile) values('"
					+ usermap.getString("userid") + "','" + usermap.getString("password") + "','" + usermap.getString("name") + "','"
					+ usermap.getString("extid") + "','"  + usermap.get("party") + "','"  + usermap.get("mobile")+  "')";
			int result = st.executeUpdate(sql);
			if (result == -1) {
				System.out.println(usermap.get("userid") + "添加失败");
			} else {
				System.out.println(usermap.get("userid") + "添加成功");
			}

		} catch (SQLException e) {
			logger.error("错误故障为：" + e);
			e.printStackTrace();
		}
		st.close();
		return re;
	}

	public static ArrayList<Integer> updateuser(JSONObject usermap) throws SQLException {
		ArrayList<Integer> re = new ArrayList<Integer>();
		try {
			st = (Statement) conn.createStatement();
			String sql = "update localuser set partylist='" + usermap.get("party") + "' where ExtId='"
					+ usermap.get("extid") + "'";
			System.out.println(sql);
			int result = st.executeUpdate(sql);
			if (result == -1) {
				System.out.println(usermap.get("userid") + "修改失败");
			} else {
				System.out.println(usermap.get("userid") + "修改成功");
			}

		} catch (SQLException e) {
			logger.error("错误故障为：" + e);
		}
		st.close();
		return re;
	}
	

	public static HashMap<String, user> query() throws SQLException {

		try {
			String sql = "select * from	localuser";
			st = (Statement) conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String alias = rs.getString("alias");
				String password = rs.getString("password");
				String name = rs.getString("name");
				String ExtId = rs.getString("ExtId");
				String Gender = rs.getString("gender");
				String partylist = rs.getString("partylist");
				String status="";
				String mobile="";
				user u = new user(alias, password, name, ExtId, Gender, partylist,status,mobile,"","");
				umap.put(ExtId, u);
			}
		} catch (SQLException e) {
			logger.error("错误故障为：" + e);
		}
		st.close();
		return umap;
	}

	public static HashMap<String, String> queryById(String id) throws SQLException {
		HashMap<String, String> userinfo = new HashMap<String, String>();
		try {
			String sql = "select * from	localuser where ExtId='" + id + "'";
			st = (Statement) conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String alias = rs.getString("alias");
				String password = rs.getString("password");
				String name = rs.getString("name");
				String ExtId = rs.getString("ExtId");
				String partylist = rs.getString("partylist");
				userinfo.put("alias", alias);
				userinfo.put("password", password);
				userinfo.put("name", name);
				userinfo.put("ExtId", ExtId);
				userinfo.put("partylist", partylist);
			}

		} catch (SQLException e) {
			logger.error("错误故障为：" + e);
		}
		st.close();
		return userinfo;
	}

}
