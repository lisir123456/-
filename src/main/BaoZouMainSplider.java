package main;
import java.sql.Connection;
import java.sql.SQLException;

import pageprossor.BaoZouPageProsser;
import pipeline.UserDefineDatabasePipiline;
import us.codecraft.webmagic.Spider;

public class BaoZouMainSplider{

    public static void main(String[] args) {
    	//Spider.create(new BaoZouPageProsser()).addUrl("http://baozoumanhua.com/text").addPipeline(new UserDefineFilePipeline("E:\\baozou.txt")).thread(5).run();
    	UserDefineDatabasePipiline userDefineDatabasePipiline = new UserDefineDatabasePipiline();
        Spider.create(new BaoZouPageProsser()).addUrl("http://baozoumanhua.com/text").addPipeline(userDefineDatabasePipiline).thread(5).run();
        Connection connection = userDefineDatabasePipiline.connection;
        if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
    }
}