package HW3;

import java.util.Objects;

public class Client {
	private String username;
	private String password;
	private String academicStatus;
	private int years;

	public Client(String username, String password, String academicStatus, int years) {
		this.username = username;
		this.password = password;
		this.academicStatus = academicStatus;
		this.years = years;
	}

	public Client(String username) {
		this(username, "", "", 0);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getAcademicStatus() {
		return academicStatus;
	}

	public int getYears() {
		return years;
	}

	public void setUsername(String u) {
		username = u;
	}

	public void setPassword(String p) {
		password = p;
	}

	public void setAcademicStatus(String s) {
		academicStatus = s;
	}

	public void setYears(int y) {
		years = y;
	}

	public String toCsvRow() {
		return escape(username) + "," + escape(password) + "," + escape(academicStatus) + "," + years;
	}

	private static String escape(String s) {
		if (s == null)
			return "";
		if (s.contains(",") || s.contains("\"")) {
			return "\"" + s.replace("\"", "\"\"") + "\"";
		}
		return s;
	}

	@Override
	public String toString() {
		return "Client{" + "username='" + username + '\'' + ", academicStatus='" + academicStatus + '\'' + ", years="
				+ years + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Client))
			return false;
		Client c = (Client) o;
		return username != null && c.username != null && username.equalsIgnoreCase(c.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username == null ? "" : username.toLowerCase());
	}
}
