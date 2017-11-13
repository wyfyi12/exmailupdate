package bean;

public class user {
	private String alias = "";
	private String password = "";
	private String name = "";
	private String ExtId = "";
	private String Gender = "";
	private String partylist = "";
	private String phone="";
	private String status="";
	private String position="";
	private String tel="";
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExtId() {
		return ExtId;
	}
	public void setExtId(String extId) {
		ExtId = extId;
	}
	public String getGender() {
		return Gender;
	}
	public void setGender(String gender) {
		Gender = gender;
	}
	public String getPartylist() {
		return partylist;
	}
	public void setPartylist(String partylist) {
		this.partylist = partylist;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public user(String alias, String password, String name, String extId, String gender, String partylist, String phone,
			String status, String position, String tel) {
		super();
		this.alias = alias;
		this.password = password;
		this.name = name;
		ExtId = extId;
		Gender = gender;
		this.partylist = partylist;
		this.phone = phone;
		this.status = status;
		this.position = position;
		this.tel = tel;
	}
	@Override
	public String toString() {
		return "user [alias=" + alias + ", password=" + password + ", name=" + name + ", ExtId=" + ExtId + ", Gender="
				+ Gender + ", partylist=" + partylist + ", phone=" + phone + ", status=" + status + ", position="
				+ position + ", tel=" + tel + "]";
	}

}
