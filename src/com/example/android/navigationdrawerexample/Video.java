package com.example.android.navigationdrawerexample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
    public static List<videoObject_xvideo> xvid_page(String url) {
        Document info = Jsoup.parse(videoObject.parseURLtoHTML(url));
        Elements elem = info.select("div[class = thumbInside]");
        List<videoObject_xvideo> kList = new ArrayList<videoObject_xvideo>();
        String vid, title, img;
        Pattern pattern = Pattern.compile("/profiles/");
        Matcher matcher;
        for (Element link : elem) {
            vid = link.children().select("a").attr("href");
            matcher = pattern.matcher(vid);
            if (!vid.equals("") && !matcher.find()) {
                title = link.children().select("p").text();
                img = link.children().select("img").attr("src");
                kList.add(new videoObject_xvideo(title, "http://www.xvideos.com" + vid, img));
            }
        }
        return kList;
    }

    //sort = relevance, uploaddate, or rating (rating is default)
    //dur = 1-3min, 3-10min, 10min_more, or allduration (allduration is default)
    //date = today, week, month, or all (all is default)
    public static String xvid_search(String search, String sort, String dur, String date) {
        if (sort.isEmpty()) {
            sort = "rating";
        }
        if (date.isEmpty()) {
            date = "all";
        }
        if (dur.isEmpty()) {
            dur = "allduration";
        }
        search = search.replace(" ", "+");
        String base = "http://www.xvideos.com/?k=";
        return base + search + "&sort=" + sort + "&durf" + dur + "&datef=" + date;
    }

    public static String xvid_nPage(String old_url, int pagenum) {
        String new_url = "http://www.xvideos.com/new/1";
        if(old_url.contains("com/new/")){
            new_url = "http://www.xvideos.com/new/" + (pagenum + 1);
        }
        else if(old_url.contains("com/?k=")){
            new_url = old_url.replaceAll("&p=\\d+", "&p=" + (pagenum+1));
        }
        return new_url;
    }

    public static String xvid_pPage(String old_url, int pagenum) {
        String new_url = "http://www.xvideos.com/";
        if(pagenum > 0 && old_url.contains("com/new/")){
            new_url = "http://www.xvideos.com/new/" + (pagenum - 1);
        }
        else if(pagenum > 0 && old_url.contains("com/?k=")){
            new_url = old_url.replaceAll("&p=\\d+", "&p=" + (pagenum-1));
        }
        return new_url;
    }

    public static List<videoObject_xvideo> xnxx_page(String url) {
        Document info = Jsoup.parse(videoObject.parseURLtoHTML(url));
        Elements elem = info.select("li");
        List<videoObject_xvideo> kList = new ArrayList<videoObject_xvideo>();
        String vid, title, img;
        for (Element link : elem){
            vid = link.children().select("a").attr("href");
            img = link.children().select("img").attr("src");
            if (!vid.equals("") && !img.equals("")) {
                title = link.text();
                kList.add(new videoObject_xvideo(title, vid, img));
            }
        }
        return kList;
    }

    //sort = relevance, uploaddate, or rating (rating is default)
    //dur = 1-3min, 3-10min, 10min_more, or allduration (allduration is default)
    //date = today, week, month, or all (all is default)
    public static String xnxx_search(String search, String sort, String dur, String date){
        if (sort.isEmpty()) {
            sort = "rating";
        }
        if (date.isEmpty()) {
            date = "all";
        }
        if (dur.isEmpty()) {
            dur = "allduration";
        }
        search = search.replace(" ", "+");
        String base = "http://www.xnxx.com/?k=";
        return base + search + "&sort=" + sort + "&durf" + dur + "&datef=" + date;
    }

    public static String xnxx_nPage(String old_url, int pagenum) {
        String new_url = "http://www.xnxx.com/new/1";
        if(old_url.contains("com/new/")){
            new_url = "http://www.xnxx.com/new/" + (pagenum + 1);
        }
        else if(old_url.contains("com/?k=")){
            new_url = old_url.replaceAll("&p=\\d+", "&p=" + (pagenum+1));
        }
        return new_url;
    }

    public static String xnxx_pPage(String old_url, int pagenum) {
        String new_url = "http://www.xnxx.com/";
        if(pagenum > 0 && old_url.contains("com/new/")){
            new_url = "http://www.xnxx.com/new/" + (pagenum - 1);
        }
        else if(pagenum > 0 && old_url.contains("com/?k=")){
            new_url = old_url.replaceAll("&p=\\d+", "&p=" + (pagenum-1));
        }
        return new_url;
    }


    public static List<videoObject_redtube> redtube_page(String url) {
        Document info = Jsoup.parse(videoObject.parseURLtoHTML(url));
        Elements elem = info.select("div[class = video]");
        List<videoObject_redtube> kList = new ArrayList<videoObject_redtube>();
        String vid, title, img;
        System.out.println("Function ACCESSED redtube");
        for (Element link : elem){
            System.out.println("INSIDE FOR LOOP");
            vid = link.children().select("a").attr("href");
            if (!vid.equals("")) {
                title = link.children().select("a").attr("title");
                img = link.children().select("img").attr("src");
                kList.add(new videoObject_redtube(title, "http://www.redtube.com" + vid, img));
                System.out.println("ADDED");
            }
        }
        return kList;
    }

    //sort = "new", "top", "mostviewed", or "" ("" is default for most relevant)
    public static String redtube_search(String search, String sort){
        String base = "http://www.redtube.com/";
        search = search.replace(" ", "+");
        return base + sort + "?search=" + search;
    }

    public static String redtube_nPage(String old_url, int pagenum) {
        String new_url = "http://www.redtube.com?page=1";
        if(old_url.contains("?page")){
            new_url = "http://www.redtube.com/?page=" + (pagenum + 1);
        }
        else if (old_url.contains("&page=")){
            new_url = old_url.replaceAll("&page=\\d+", "&page=" + (pagenum+1));
        }
        return new_url;
    }

    public static String redtube_pPage(String old_url, int pagenum) {
        String new_url = "http://www.redtube.com/";
        if(pagenum > 0 && old_url.contains("com/new/")){
            new_url = "http://www.redtube.com/?page=" + (pagenum - 1);
        }
        else if(pagenum > 0 && old_url.contains("com/?k=")){
            new_url = old_url.replaceAll("&page=\\d+", "&page=" + (pagenum-1));
        }
        return new_url;
    }

}


class videoObject {//title, preview image, and landing page (url to html)
    String title;
    String picture;
    String vid_pg_url;
    String vid_url;

    videoObject(String tit, String url, String pic){
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
            System.out.println(url);
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

    static void playVideo(String streamURL, Context mContext){
        if (!streamURL.equals("")) {
            Uri url_to_video = Uri.parse(streamURL);
            Intent intent = new Intent(Intent.ACTION_VIEW, url_to_video);
            intent.setDataAndType(url_to_video, "video/*");
            mContext.startActivity(intent);
        }
    }

}

class videoObject_xvideo extends videoObject{

    videoObject_xvideo(String tit, String url, String pic) {
        super(tit, url, pic);
    }

    String getVideoSourceURL(){
        //gets the line full of junk, turns into html doc for jsoup, and converts into the wanted vid url
        String line = Jsoup.parse(parseURLtoHTML(vid_pg_url)).select("embed").attr("flashvars");
        //makes the vid url into a proper url
        Pattern pattern = Pattern.compile("(?<=flv_url=).+(?=&url_bigthumb=)");
        Matcher matcher = pattern.matcher(line);
        vid_url = "";
        if (matcher.find()) {
            vid_url = matcher.group().replace("%3D", "=").replace("%26", "&").replace("%3F", "?")
                    .replace("%3B", ";").replace("%2F", "/").replace("%3A", ":");
        }
        return vid_url;
    }
    static String getVideoSourceURL(String vid_pg_url_2){
        //gets the line full of junk, turns into html doc for jsoup, and converts into the wanted vid url
        String line = Jsoup.parse(parseURLtoHTML(vid_pg_url_2)).select("embed").attr("flashvars");
        //makes the vid url into a proper url
        Pattern pattern = Pattern.compile("(?<=flv_url=).+(?=&url_bigthumb=)");
        Matcher matcher = pattern.matcher(line);
        String vid_url_2 = "";
        if (matcher.find()) {
            vid_url_2 = matcher.group().replace("%3D", "=").replace("%26", "&").replace("%3F", "?")
                    .replace("%3B", ";").replace("%2F", "/").replace("%3A", ":");
        }
        return vid_url_2;
    }

}

class videoObject_redtube extends videoObject {

    videoObject_redtube(String tit, String url, String pic) {
        super(tit, url, pic);
    }

    String getVideoSourceURL(){
        //gets the line full of junk, turns into html doc for jsoup, and converts into the wanted vid url
        String line = Jsoup.parse(parseURLtoHTML(vid_pg_url)).select("div[id = redtube_flv_player]").select("script").html();
        //makes the vid url into a proper url
        Pattern pattern = Pattern.compile("(?<=&flv_h264_url=).+(?=&sitepath=)");
        Matcher matcher = pattern.matcher(line);
        vid_url = "";
        if (matcher.find()) {
            vid_url = matcher.group().replace("%3D", "=").replace("%26", "&").replace("%3F", "?")
                    .replace("%3B", ";").replace("%2F", "/").replace("%3A", ":");
        }
        return vid_url;
    }
    static String getVideoSourceURL(String vid_pg_url_2){
        //gets the line full of junk, turns into html doc for jsoup, and converts into the wanted vid url
        String line = Jsoup.parse(parseURLtoHTML(vid_pg_url_2)).select("div[id = redtube_flv_player]").select("script").html();
        //makes the vid url into a proper url
        Pattern pattern = Pattern.compile("(?<=&flv_h264_url=).+(?=&sitepath=)");
        Matcher matcher = pattern.matcher(line);
        String vid_url_2 = "";
        if (matcher.find()) {
            vid_url_2 = matcher.group().replace("%3D", "=").replace("%26", "&").replace("%3F", "?")
                    .replace("%3B", ";").replace("%2F", "/").replace("%3A", ":");
        }
        return vid_url_2;
    }
}
