package main;
import java.util.ArrayList;
import java.util.List;

import model.BaoZouItems;
import pipeline.UserDefineFilePipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class TestFirstSplider implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override  
    public void process(Page page) {  
        Html html = page.getHtml();
		page.addTargetRequests(html.css("div.pager-content").links().all());  
        Selectable xpath = html.xpath("//div[@class='article article-text']");
        List<String> all = xpath.all();
        List<BaoZouItems> baozouNews = new ArrayList<BaoZouItems>();
        for (String string : all) {
        	BaoZouItems news = new BaoZouItems(); 
        	Html partHtml = new Html(string);
        	String author = partHtml.xpath("//a[@class='article-author-name']/text()").toString();  
        	if(author == null) {
        		author = partHtml.xpath("//span[@class='article-author-name']/text()").toString();  
        	}
        	news.setAuthor(author);  
            Selectable content = partHtml.xpath("//div[@class='article article-text']/@data-text");
            news.setContent(content.toString());  
            news.setTime(html.xpath("//span[@class='article-date']/text()").toString());  
            if(news.getTime() != null) {
            	baozouNews.add(news);
            }
        }
        if(baozouNews.size() == 0) {
        	page.setSkip(true);
        }else {
        	page.putField("news", baozouNews);
        }
    }  
  
    @Override  
    public Site getSite() {  
        return site;  
    }  
    public static void main(String[] args) {
        Spider.create(new TestFirstSplider()).addUrl("http://baozoumanhua.com/text").addPipeline(new UserDefineFilePipeline("E:\\baozou.txt")).thread(5).run();
    }
}