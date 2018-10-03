package in.sminfo.tool.mgmt.entity;

import org.springframework.data.annotation.Id;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Transient;

@Entry(base = "ou=users", objectClasses = { "person", "inetOrgPerson", "top" })
public class ldapUser {

	@Id
	@Attribute(name = "udiNumber")
	private String uidNumber;

	@DnAttribute(value = "uid")
	private String uid;

	@Attribute(name = "cn")
	private String cn;

	@Attribute(name = "sn")
	private String sn;

	@Attribute(name = "gidNumber")
	private String gidNumber;

	@Attribute(name = "givenName")
	private String givenName;

	@Transient
	private String homeDirectory;
	@Transient
	private String loginshell;

	public void ldpUser(String cn, String homeDirectory, String sn, String uid, String uidNumber, String loginshell,
			String givenName, String gidNumber) {
		this.cn = cn;
		this.sn = sn;
		this.gidNumber = gidNumber;
		this.givenName = givenName;
		this.uid = uid;
		this.uidNumber = uidNumber;
		this.homeDirectory = homeDirectory;
		this.loginshell = loginshell;
	}

	public ldapUser() {

	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getGidNumber() {
		return gidNumber;
	}

	public void setGidNumber(String gidNumber) {
		this.gidNumber = gidNumber;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getUidNumber() {
		return uidNumber;
	}

	public void setUidNumber(String uidNumber) {
		this.uidNumber = uidNumber;
	}

	public String getHomeDirectory() {
		return homeDirectory;
	}

	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

	public String getLoginshell() {
		return loginshell;
	}

	public void setLoginshell(String loginshell) {
		this.loginshell = loginshell;
	}

	@Override
	public String toString() {
		return "ldapUser{" + "cn='" + cn + '\'' + ", sn='" + sn + '\'' + ", uid='" + uid + '\'' + ", gidNumber='"
				+ gidNumber + '\'' + ", givenNumber='" + givenName + '\'' + ", uidNumber='" + uidNumber + '\'' +
				'}';
	}

}
