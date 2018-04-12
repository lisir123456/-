package pipeline;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.BaoZouItems;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class UserDefineDatabasePipiline implements Pipeline {
	private Logger logger = LoggerFactory.getLogger(getClass());
	public Connection connection = null; 
	private Connection getConn() {
		Connection conn = null;
		InputStream in = ClassLoader.getSystemResourceAsStream("database.properties");
		Properties p = new Properties();
		try {
			p.load(in);
			Class.forName(p.getProperty("driver")); // 加载对应驱动
			conn = (Connection) DriverManager.getConnection(p.getProperty("jdbcUrl"), p.getProperty("user"),
					p.getProperty("password"));
		} catch (IOException e) {
			logger.warn("参数配置加载失败", e);
		} catch (ClassNotFoundException e) {
			logger.warn("驱动加载失败", e);
		} catch (SQLException e) {
			logger.warn("数据库连接失败", e);
		}
		return conn;
	}

	public int insert(Connection conn, BaoZouItems news) {
		int i = 0;
		String sql = "insert into baozou (author,time,content) values(?,?,?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, news.getAuthor());
			pstmt.setString(2, news.getTime());
			pstmt.setString(3, news.getContent());
			i = pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.warn("数据插入失败", e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					logger.warn("PreparedStatement关闭失败", e);
				}
			}
		}
		return i;
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		List<BaoZouItems> news = resultItems.get("news");
		connection = this.getConn();
		if(connection != null) {
			for (BaoZouItems baozouNews : news) {
	        	this.insert(connection, baozouNews);
			}
		}
		
	}
}
