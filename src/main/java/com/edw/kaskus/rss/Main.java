package com.edw.kaskus.rss;

import com.edw.kaskus.rss.bean.Posts;
import com.edw.kaskus.rss.service.PostsService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

/**
 *
 * @author edwin < edwinkun at gmail dot com >
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        crawl("53b85e00a1cb17dc228b4580");
    }

    public static void crawl(String threadId) {

        Document doc = Jsoup.parse(readURL("http://www.kaskus.co.id/thread/"+threadId+"/-/"));
        Element threadTitle = doc.select("span.current").first();
        LOGGER.debug(threadTitle.text());

        Element numberOfPage = doc.select("li.page-count").first();
        int pageNumber = Integer.parseInt(numberOfPage.text().split(" ")[3]);

        PostsService postsService = new PostsService();
        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm"); // 06-07-2014 03:20 
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy"); // 06-07-2014 03:20 
        
        String today = simpleDateFormat.format(new Date());
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String yesterday = simpleDateFormat.format(calendar.getTime());
        
        for (int i = 1; i <= pageNumber; i++) {
            Document page = Jsoup.parse(readURL("http://www.kaskus.co.id/thread/"+threadId+"/-/" + i));

            Elements post = page.select("section.hfeed");
            for (Element element : post) {
                
                Posts posts = new Posts();
                
                String post_id = element.parent().parent().attr("id");
                posts.setPostId(post_id);
                
                Element trs = element.select("a.nickname").first();
                posts.setPostAuthor(trs.text());
                
                Element time = element.select("time").first();
                try {
                    String postTime = time.text();
                    if(!postTime.contains("Yesterday") && !postTime.contains("Today"))
                        posts.setPostDate(simpleDateTimeFormat.parse(postTime));
                    if(postTime.contains("Yesterday")) {
                        postTime = postTime.replace("Yesterday", "");
                        LOGGER.debug("Yesterday");
                        LOGGER.debug(postTime.trim());
                        posts.setPostDate(simpleDateTimeFormat.parse(yesterday+" "+postTime.trim()));
                    }
                    if(postTime.contains("Today")) {
                        postTime = postTime.replace("Today", "");
                        LOGGER.debug("Today");
                        LOGGER.debug(postTime.trim());
                        posts.setPostDate(simpleDateTimeFormat.parse(today+" "+postTime.trim()));
                    }
                } catch (Exception e) {
                    LOGGER.error(e);
                    posts.setPostDate(null);
                }

                Element content = element.select("div.entry").first();
                content.select("br").append("\\n");
                content.select("p").prepend("\\n\\n");
                String stringContent = content.html().replaceAll("\\\\n", "\n");
                posts.setPostContent(Jsoup.clean(stringContent, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false)));
                
                posts.setThreadId(threadId);
                
                postsService.insert(posts);
            }
        }
    }

    public static String readURL(String url) {

        String fileContents = "";
        String currentLine = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            fileContents = reader.readLine();
            while (currentLine != null) {
                currentLine = reader.readLine();
                fileContents += "\n" + currentLine;
            }
            reader.close();
            reader = null;
        } catch (IOException e) {
            LOGGER.error(e, e);
        }
        return fileContents;
    }

}
