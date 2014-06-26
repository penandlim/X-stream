package com.example.android.navigationdrawerexample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Video {
    public static List xvid_page(String url) {
        Document info = Jsoup.parse(videoObject.parseURLtoHTML(url));
        Elements elem = info.select("div[class = thumbInside]");
        List<videoObject_xvideo> kList = new ArrayList<videoObject_xvideo>();
        String vid_url, title, img;
        int count = 0;
        for (Element link : elem){
            if (link.children().select("a").attr("href") != "") {
                title = link.children().select("p").text();
                vid_url = "http://www.xvideos.com" + link.children().select("a").attr("href");
                img = link.children().select("img").attr("src");
                kList.add(new videoObject_xvideo());
                kList.get(count).set(title, vid_url, img);
                count++;
            }
        }
        return kList;
    }
}

class videoObject {//title, preview image, and landing page (url to html)
    String title;
    String picture;
    String vid_pg_url;
    String vid_url;

    void set(String tit, String url, String pic){
        title = tit;
        picture = pic;
        vid_pg_url = url;
    }

    String getTitle(){
        return title;
    }

    String getVideoURL(){
        return vid_pg_url;
    }

    String getPreviewURL(){
        return picture;
    }

    static String parseURLtoHTML(String given_url){
        URL url;
        try {
            url = new URL(given_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("bad url given");
            return "";
        }
        try {
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                a.append(inputLine);
            in.close();
            return a.toString();
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("error retrieving website content");
            return "";
        }
    }

    void playVideo(){
        //for john to fill in
    }

}

class videoObject_xvideo extends videoObject{

    String getVideoSourceURL(){
        //gets the line full of junk, turns into html doc for jsoup, and converts into the wanted vid url
        String line = Jsoup.parse(parseURLtoHTML(vid_pg_url)).select("embed").attr("flashvars");
        //makes the vid url into a proper url
        Pattern pattern = Pattern.compile("(?<=flv_url=).+(?=&url_bigthumb=)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            vid_url = matcher.group().replace("%3D", "=").replace("%26", "&").replace("%3F", "?")
                    .replace("%3B", ";").replace("%2F", "/").replace("%3A", ":");
        }
        else{
            vid_url = "";
        }
        return vid_url;
    }

}
