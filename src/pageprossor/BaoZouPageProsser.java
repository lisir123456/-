package pageprossor;

import java.util.ArrayList;
import java.util.List;

import model.BaoZouItems;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class BaoZouPageProsser implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override  
    public void process(Page page) {  
    	Html html = page.getHtml();
		page.addTargetRequests(html.css("div.pager-content").links().all());  
        //获取所有div块（每个div块有个一想要的信息）
		Selectable xpath = html.xpath("//div[@class='article article-text']");
        List<String> all = xpath.all();
        List<BaoZouItems> baozouNews = new ArrayList<BaoZouItems>();
        //遍历div块
        for (String string : all) {
        	BaoZouItems news = new BaoZouItems(); 
        	//转换为html
        	Html partHtml = new Html(string);
        	//解析数据
        	String author = partHtml.xpath("//a[@class='article-author-name']/text()").toString();  
        	//特殊处理，当用户为匿名时，所在元素为span而不是a
        	if(author == null) {
        		author = partHtml.xpath("//span[@class='article-author-name']/text()").toString();  
        	}
        	news.setAuthor(author);  
            Selectable content = partHtml.xpath("//div[@class='article article-text']/@data-text");
            news.setContent(content.toString());  
            news.setTime(html.xpath("//span[@class='article-date']/text()").toString());  
            //当时间为null，代表为垃圾数据
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
}
