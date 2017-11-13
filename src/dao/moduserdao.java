package dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import bean.user;

public class moduserdao {
	private static Logger logger = Logger.getLogger(moduserdao.class);
	public static HashMap<String, user> query(){
		HashMap<String, user> umap=new HashMap<String, user>();
		try{
			userdao.getConnection();
			String sql = "select * from	usr_rsfw.v_qqjzg";
			Statement st = (Statement) userdao.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("XM");
                String password ="Sqc" +rs.getString("PASSWORD");
                String ExtId = rs.getString("ZGH").trim();
                String partylist = rs.getString("SZDWDM");
                String mobile =rs.getString("SJ");
                String gender=rs.getString("XBDM");
                String position=rs.getString("DZZW");
                String tel=rs.getString("BGLXDH");
                partylist="宿迁学院/教职工/"+queryparty(partylist);
                String alias=ExtId+"@sqc.edu.cn";
                user u = new user(alias, password, name, ExtId, gender, partylist, "1", mobile,position,tel);
                	umap.put(alias, u);
                	System.out.println("读取"+alias+",成功");
            }
            st.close();
            userdao.conn.close();
		}catch(Exception e){
			logger.error(e.getMessage());
			 e.printStackTrace();
		}
		return umap;
	}
	
	public static String queryparty(String dm){
		String party = null;
		try{
			userdao.getConnection();
			String sql = "select * from	usr_rsfw.v_qqdw where DM ='"+dm+"'";
			Statement st = (Statement) userdao.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String mc = rs.getString("MC");
                String ls = rs.getString("LS");
                if(ls!=null){
                	mc= querypartyls(ls,mc);
                }
                party=mc;
            }
            st.close();
            userdao.conn.close();
		}catch(Exception e){
			logger.error(e.getMessage());
			 e.printStackTrace();
		}
		return party;
	}
	
	public static String querypartyls(String ls,String party){
		String mc=null;
		try{
			String sql = "select * from	usr_rsfw.v_qqdw where DM ='"+ls+"'";
			Statement st = (Statement) userdao.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                mc = rs.getString("MC");
                String newls = rs.getString("LS");
                if(newls!=null){
                	querypartyls(newls,party+"/"+mc);
                }
            }
            st.close();
		}catch(Exception e){
			logger.error(e.getMessage());
			 e.printStackTrace();
		}
		return party+"/"+mc;
	}
	
	public static int querylocal(String userid) throws Exception{
		String sql = "select * from	local where alias ='"+userid+"'";
		Statement st = (Statement) userdao.conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        int no =0;
        while (rs.next()) {
        no = rs.getInt("no");
        }
        return no;
	}
}
