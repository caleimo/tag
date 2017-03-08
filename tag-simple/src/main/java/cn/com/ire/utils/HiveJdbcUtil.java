package cn.com.ire.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcUtil {
	//private static final Logger log = Logger.getLogger(HiveJdbcUtil.class);
	private static String hivedriverName;
	private static String hivejdbcurl;
	private static String hivejdbcuser;
	private static String hivejdbcpasswd;

	static {
		hivedriverName = RayConfManager.getProperty("hivedriverName",
				String.class);
		hivejdbcurl = RayConfManager.getProperty("hivejdbcurl", String.class);
		hivejdbcuser = RayConfManager.getProperty("hivejdbcuser", String.class);
		hivejdbcpasswd = RayConfManager.getProperty("hivejdbcpasswd",
				String.class);
	}

	public static Connection getConnection() {

		Connection conn;
		try {
			Class.forName(hivedriverName).newInstance();
			conn = DriverManager.getConnection(hivejdbcurl, hivejdbcuser,
					hivejdbcpasswd);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void release(Connection conn, Statement st, ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (st != null)
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

}
