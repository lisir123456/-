package demo;
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
        BaozouNews news = new BaozouNews();  
        Selectable xpath = html.xpath("//a[@class='article-author-name']/text()");
        //多个获取值，只保存了一个
        news.setAuthor(xpath.toString());  
        Selectable xpath2 = html.xpath("//div[@class='article article-text']/@data-text");
        news.setContent(xpath2.toString());  
        news.setTime(html.xpath("//span[@class='article-date']/text()").toString());  
        if(news.getTime() == null) {
        	page.setSkip(true);
        }
        page.putField("news", news);  
    }  
  
    @Override  
    public Site getSite() {  
        return site;  
    }  

    public static void main(String[] args) {
        Spider.create(new TestFirstSplider()).addUrl("http://baozoumanhua.com/text").thread(5).run();
    }
}