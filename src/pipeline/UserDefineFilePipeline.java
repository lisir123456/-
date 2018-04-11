package pipeline;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.BaoZouItems;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class UserDefineFilePipeline implements Pipeline{
	private Logger logger = LoggerFactory.getLogger(getClass());
	File baozou = null;

    public UserDefineFilePipeline(String path) {
    	baozou = new File(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
    	List<BaoZouItems> news = resultItems.get("news"); 
        try {
        	FileWriter fw = new FileWriter(baozou,true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (BaoZouItems baozouNews : news) {
            	bw.write("作者：\t"+baozouNews.getAuthor()+"\r\n");
            	bw.write("时间：\t"+baozouNews.getTime()+"\r\n");
            	bw.write("内容：\t"+baozouNews.getContent()+"\r\n");
            	bw.write("\r\n");
            	bw.write("-----------------------------------------------------------"
            			+ "----------------------------------------------");
            	bw.write("\r\n");
			}
            bw.close(); 
            fw.close();
        } catch (IOException e) {
            logger.warn("write file error", e);
        }
    }
}
