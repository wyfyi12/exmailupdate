package uitl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import common.json.DoJson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class douser {
	public String rs="";
	public String moduser(String token,JSONObject userjob) throws Exception{
		String url = "https://api.exmail.qq.com/cgi-bin/user/update?access_token="+token;
		HashMap<String, String> headMap = new HashMap<String, String>();
		headMap.put("Charsert", "UTF-8");
		HttpURLConnection conn = getConnection(url);
		initConnection(conn, headMap);
		InputStream in = getHttpResponse(conn, userjob);
		rs = getStringFromStream(in);
		in.close();
		conn.disconnect();
		return rs;
	}
	
	public String adduser(String token,JSONObject userjob) throws Exception{
		String url = "https://api.exmail.qq.com/cgi-bin/user/create?access_token="+token;
		HashMap<String, String> headMap = new HashMap<String, String>();
		headMap.put("Charsert", "UTF-8");
		HttpURLConnection conn = getConnection(url);
		initConnection(conn, headMap);
		InputStream in = getHttpResponse(conn, userjob);
		rs = getStringFromStream(in);
		in.close();
		conn.disconnect();
		return rs;
	}
	
	public String checkuser(String token,JSONObject userjob) throws Exception{
		String url = "https://api.exmail.qq.com/cgi-bin/user/batchcheck?access_token="+token;
		HashMap<String, String> headMap = new HashMap<String, String>();
		headMap.put("Charsert", "UTF-8");
		HttpURLConnection conn = getConnection(url);
		initConnection(conn, headMap);
		InputStream in = getHttpResponse(conn, userjob);
		rs = getStringFromStream(in);
		in.close();
		conn.disconnect();
		return rs;
	}
	
	public String getuser(String token,String userid) throws Exception{
		String url = "https://api.exmail.qq.com/cgi-bin/user/get?access_token="+token+"&userid="+userid;
		HashMap<String, String> headMap = new HashMap<String, String>();
		headMap.put("Charsert", "UTF-8");
		HttpURLConnection conn = getConnection(url);
		initConnection(conn, headMap);
		InputStream in = conn.getInputStream();
		rs = getStringFromStream(in);
		in.close();
		conn.disconnect();
		return rs;
	}
	
	public String modalias(String token,String userid,String slave) throws Exception{
		String url = "https://api.exmail.qq.com/cgi-bin/user/swapalias?access_token="+token+"&alias="+userid+"&slavealias="+slave;
		HashMap<String, String> headMap = new HashMap<String, String>();
		headMap.put("Charsert", "UTF-8");
		HttpURLConnection conn = getConnection(url);
		initConnection(conn, headMap);
		InputStream in = conn.getInputStream();
		rs = getStringFromStream(in);
		in.close();
		conn.disconnect();
		return rs;
	}
	
	public String deluser(String token,String userid) throws Exception{
		String url = "https://api.exmail.qq.com/cgi-bin/user/delete?access_token="+token+"&userid="+userid;
		HashMap<String, String> headMap = new HashMap<String, String>();
		headMap.put("Charsert", "UTF-8");
		HttpURLConnection conn = getConnection(url);
		initConnection(conn, headMap);
		InputStream in = conn.getInputStream();
		rs = getStringFromStream(in);
		in.close();
		conn.disconnect();
		return rs;
	}
	
	public ArrayList<String> getalluser(String token,String department_id,String fetch_child) throws Exception{
		ArrayList<String> userlist=new ArrayList<String>();
		String url = "https://api.exmail.qq.com/cgi-bin/user/simplelist?access_token="+token+"&department_id="+department_id+"&fetch_child="+fetch_child;
		HashMap<String, String> headMap = new HashMap<String, String>();
		headMap.put("Charsert", "UTF-8");
		HttpURLConnection conn = getConnection(url);
		initConnection(conn, headMap);
		InputStream in = conn.getInputStream();
		rs = getStringFromStream(in);
		if(rs.contains("userlist")){
			JSONArray userlistja=DoJson.getJSONArrayfromJSONObject(rs, "userlist");
			for(int i=0;i<userlistja.size();i++){
				userlist.add(userlistja.getJSONObject(i).getString("userid"));
			}
		}
		in.close();
		conn.disconnect();
		return userlist;
	}
	public ArrayList<String> getalluserinfo(String token,String department_id,String fetch_child) throws Exception{
		ArrayList<String> userlist=new ArrayList<String>();
		String url = "https://api.exmail.qq.com/cgi-bin/user/list?access_token="+token+"&department_id="+department_id+"&fetch_child="+fetch_child;
		HashMap<String, String> headMap = new HashMap<String, String>();
		headMap.put("Charsert", "UTF-8");
		HttpURLConnection conn = getConnection(url);
		initConnection(conn, headMap);
		InputStream in = conn.getInputStream();
		rs = getStringFromStream(in);
		if(rs.contains("userlist")){
			JSONArray userlistja=DoJson.getJSONArrayfromJSONObject(rs, "userlist");
			for(int i=0;i<userlistja.size();i++){
				userlist.add(userlistja.getJSONObject(i).getString("userid"));
			}
		}
		in.close();
		conn.disconnect();
		return userlist;
	}
	
	private String getStringFromStream(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"UTF-8"));
		String temp;
		while ((temp = reader.readLine()) != null) {
			sb.append(temp);
		}
		in.close();
		return sb.toString();
	}
	
	private static HttpURLConnection getConnection(String url)
			throws IOException {
		URL u = new URL(url);
		HttpsURLConnection conn= (HttpsURLConnection) u.openConnection();
		conn.setHostnameVerifier(new CustomizedHostnameVerifier());
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		return conn;
	}
	
	private void initConnection(HttpURLConnection conn,
			Map<String, String> headMap) throws ProtocolException {
		Iterator<String> it = headMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = headMap.get(key);
			conn.setRequestProperty(key, value);
		}
	}
	
	public InputStream getHttpResponse(HttpURLConnection conn,
			JSONObject job) throws IOException {
		OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),
				"UTF-8");
		osw.write(job.toString());
		osw.flush();
		osw.close();
		return conn.getInputStream();
	}
	
}
