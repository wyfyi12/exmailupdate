package uitl;

import java.io.InputStream;
import java.net.HttpURLConnection;

import common.json.DoJson;
import net.sf.json.JSONObject;

public class newtxmail {
	HttpDeal hd=new HttpDeal();
	public String accesstoken = "";
	public String Authorization = "";
	public String getAcceptKey(String corpid,String corpsecret )
			throws Exception {
		String url = "https://api.exmail.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+corpsecret;
		JSONObject data=new JSONObject();
		HttpURLConnection conn=hd.creattconn(url);
		InputStream rsin=hd.getHttpResponse(conn, data);
		String rs=hd.getStringFromStream(rsin);
		JSONObject rsjob=DoJson.getJSONObjectfromString(rs);
		String wxtoken=rsjob.getString("access_token");
		return wxtoken;
	}
}
