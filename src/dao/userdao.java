package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uitl.getdata;
import bean.user;
import configure.config;
import uitl.douser;
import uitl.newtxmail;
import net.sf.json.JSONObject;
import newmail.NewMailMod;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;

//用于学校服务器的资料读取与邮箱数据同步的操作
public class userdao {

	private static newtxmail ntm = new newtxmail();
	private static douser du = new douser();
	private static Logger logger = Logger.getLogger(userdao.class);
	private static int state = 0;
	private static int custate = 0;
	public static Connection conn;
	public static PreparedStatement ps;
	public static ResultSet rs;
	public static Statement st;
	private static String result = "";
	public static List<HashMap<String, String>> userinfo = new ArrayList<HashMap<String, String>>();
	public static List<String> rslist = new ArrayList<String>();

	public static void getConnection() {
		try {
			Class.forName(config.getUserdbdriver());
			conn = DriverManager.getConnection(config.getUserdburl(), config.getUserdbroot(), config.getUserdbpw());
		} catch (Exception e) {
			logger.error("错误故障为：" + e);
			e.printStackTrace();
		}
	}

	public static int getCustate() {
		return custate;
	}

	public static void setCustate(int custate) {
		userdao.custate = custate;
	}

	public static int getState() {
		return state;
	}

	public static void setState(int state) {
		userdao.state = state;
	}

	public static List<String> queryupdate() throws Exception {
		try {
			getdata.getconn();
			String usertoken = ntm.getAcceptKey(config.getCorpid(), config.getTxlkey()).trim();
			logger.info("正在读取服务器记录。。。");
			HashMap<String, user> nowuser = moduserdao.query();
			logger.info("共计读取服务器记录：" + nowuser.size());
			logger.info("正在读取本地记录。。。");
			localdao.getConnection();
			HashMap<String, user> olduser = localdao.query();
			logger.info("共计读取本地记录：" + olduser.size());
			logger.info("开始进行同步操作。。。");
			int unum = 0;
			if (nowuser.size() < 0) {
			} else {
				for (String nowalias : nowuser.keySet()) {
					if (state == 1) {
						break;
					}
					unum++;
					if (unum % 100 == 0) {
						localdao.conn.close();
						localdao.getConnection();
					}
					String name = nowuser.get(nowalias).getName();
					String ExtId = nowuser.get(nowalias).getExtId();
					String partylist = nowuser.get(nowalias).getPartylist();
					String password = nowuser.get(nowalias).getPassword();
					String mobile=nowuser.get(nowalias).getPhone();
					String gender=nowuser.get(nowalias).getGender();
					String position=nowuser.get(nowalias).getPosition();
					String alias = nowalias;
					String tel=nowuser.get(nowalias).getTel();
					Long pid = 0L;
					if (NewMailMod.getpartyid(partylist, usertoken) == 0) {
						String rsaddp = NewMailMod.addparty(partylist, usertoken);
						if (rsaddp.contains("ok")) {
							pid = NewMailMod.getpartyid(partylist, usertoken);
							System.out.println("添加部门：" + partylist + "成功");
						} else {
							System.out.println("处理添加部门:" + partylist + "失败,故障原因为" + rsaddp + "\n");
						}
					} else {
						pid = NewMailMod.getpartyid(partylist, usertoken);
					}
					JSONObject newuser = new JSONObject();
					HashMap<String, String> user = localdao.queryById(ExtId);
					if (user.size() < 1) {
						newuser.element("userid", alias);
						newuser.element("department", "[" + pid + "]");
						newuser.element("name", name);
						newuser.element("extid", ExtId);
						newuser.element("mobile", mobile);
						newuser.element("password", password);
						newuser.element("gender", gender);
						newuser.element("position", position);
						newuser.element("tel", tel);
						newuser.element("cpwd_login", 1);
						logger.info(newuser.toString());
						String resulttemp = du.adduser(usertoken, newuser);
						if (resulttemp.contains("ok")) {
							result = "1";
							newuser.element("party", partylist);
							localdao.insertuser(newuser);
							logger.info(unum + ".添加账号：" + ExtId + "成功");
							rslist.add(result);
						} else {
							if (resulttemp.contains("60102")) {
								newuser.element("party", partylist);
								localdao.insertuser(newuser);
							}
							rslist.add("处理记录:" + ExtId + "失败,故障原因为" + resulttemp + "\n");
							logger.error("处理记录:" + ExtId + "失败,故障原因为" + resulttemp + "\n");
						}
					} else {
						if (partylist.equals(user.get("partylist"))) {
							logger.info(unum + ".账号：" + ExtId + "无变化");
							result = "1";
							rslist.add(result);
						} else {
							String rsaddp = NewMailMod.addparty(partylist, usertoken);
							if (rsaddp.contains("ok")) {
								System.out.println("添加部门：" + partylist + "成功");
							} else {
								System.out.println("处理添加部门:" + partylist + "失败,故障原因为" + rsaddp + "\n");
							}
							newuser.element("userid", alias);
							newuser.element("extid", ExtId);
							newuser.element("department", "[" + pid + "]");
							newuser.element("gender", gender);
							newuser.element("position", position);
							newuser.element("tel", tel);
							String resulttemp = du.moduser(usertoken, newuser);
							newuser.element("party", partylist);
							localdao.updateuser(newuser);
							newuser.clear();
							if (resulttemp.contains("ok")) {
								result = "1";
								logger.info(unum + ".更新成员：" + ExtId + "成功");
								rslist.add(result);
							} else {
								rslist.add(resulttemp);
							}
						}
					}
					;
				}
			}

		} catch (SQLException e) {
			logger.error("错误故障为：" + e);
		}
		return rslist;
	}

	public static List<String> cuupdate(JTextArea jt, JProgressBar jpb) throws Exception {
		try {
			String usertoken = ntm.getAcceptKey(config.getCorpid(), config.getTxlkey()).trim();
			logger.info("正在读取服务器记录。。。");
			jt.append("正在读取服务器记录。。。" + "\n");
			HashMap<String, user> nowuser = moduserdao.query();
			int sum = nowuser.size();
			logger.info("共计读取服务器记录：" + sum);
			jt.append("共计读取服务器记录：" + sum + "\n");
			logger.info("正在读取本地记录。。。");
			jt.append("正在读取本地记录。。。" + "\n");
			localdao.getConnection();
			HashMap<String, user> olduser = localdao.query();
			logger.info("共计读取本地记录：" + olduser.size());
			jt.append("共计读取本地记录：" + olduser.size() + "\n");
			userdao.getConnection();
			logger.info("开始进行同步操作。。。");
			jt.append("开始进行同步操作。。。" + "\n");
			int unum = 0;
			if (nowuser.size() < 0) {
			} else {
				for (String nowalias : nowuser.keySet()) {
					unum++;
					if (unum % 100 == 0) {
						localdao.conn.close();
						localdao.getConnection();
					}
					String name = nowuser.get(nowalias).getName();
					String ExtId = nowuser.get(nowalias).getExtId();
					String partylist = nowuser.get(nowalias).getPartylist();
					String password = nowuser.get(nowalias).getPassword();
					String alias = nowalias;
					String status = nowuser.get(nowalias).getStatus();
					Long pid = 0L;
					if (NewMailMod.getpartyid(partylist, usertoken) == 0) {
						String rsaddp = NewMailMod.addparty(partylist, usertoken);
						if (rsaddp.contains("created")) {
							pid = NewMailMod.getpartyid(partylist, usertoken);
						} else {
							logger.error("处理添加部门:" + partylist + "失败,故障原因为" + rsaddp + "\n");
						}
					} else {
						pid = NewMailMod.getpartyid(partylist, usertoken);
					}
					JSONObject newuser = new JSONObject();
					HashMap<String, String> user = localdao.queryById(ExtId);
					if (user.size() < 1) {
						if (status.equals("0")) {
							newuser.element("enable", 0);
						} else {
							newuser.element("enable", 1);
						}
						newuser.element("userid", alias);
						newuser.element("department", "[" + pid + "]");
						newuser.element("name", name);
						newuser.element("extid", ExtId);
						newuser.element("password", password);
						newuser.element("cpwd_login", 1);
						String resulttemp = du.adduser(usertoken, newuser);
						if (resulttemp.contains("created")) {
							result = "1";
							newuser.element("status", status);
							newuser.element("party", partylist);
							localdao.insertuser(newuser);
							logger.info(unum + "添加账号：" + ExtId + "成功"+ "\n");
							jt.append(unum + "添加账号：" + ExtId + "成功"+ "\n");
							rslist.add(result);
						} else {
							if (resulttemp.contains("60102")) {
								newuser.element("status", status);
								newuser.element("party", partylist);
								localdao.insertuser(newuser);
							}
							usertoken = ntm.getAcceptKey(config.getCorpid(), config.getTxlkey()).trim();
							rslist.add("处理记录:" + ExtId + "失败,故障原因为" + resulttemp + "\n");
							logger.error("处理记录:" + ExtId + "失败,故障原因为" + resulttemp + "\n");
							jt.append(unum + "处理记录:" + ExtId + "失败,故障原因为" + resulttemp + "\n");
						}
					} else {
						if (partylist.equals(user.get("partylist"))) {
							logger.info(unum + "成员：" + ExtId + "无变化"+ "\n");
							jt.append(unum + "成员：" + ExtId + "无变化"+ "\n");
							result = "1";
							rslist.add(result);
						} else {
							newuser.element("userid", alias);
							newuser.element("department", "[" + pid + "]");
							String resulttemp = du.moduser(usertoken, newuser);
							newuser.element("status", status);
							newuser.element("party", partylist);
							localdao.updateuser(newuser);
							newuser.clear();
							if (resulttemp.contains("updated")) {
								result = "1";
								logger.info(unum + "更新成员：" + ExtId + "成功"+ "\n");
								jt.append(unum + "更新成员：" + ExtId + "成功"+ "\n");
								rslist.add(result);
							} else {
								rslist.add(resulttemp);
							}
						}
					}
					user.clear();
				}
				localdao.conn.close();
			}
			st.close();
			userdao.conn.close();
		} catch (SQLException e) {
			userdao.conn.close();
			localdao.conn.close();
			logger.error("错误故障为：" + e);
		}
		return rslist;
	}
}
