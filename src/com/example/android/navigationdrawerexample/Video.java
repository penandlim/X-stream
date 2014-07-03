package com.example.android.navigationdrawerexample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

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

    private static int page_number = 1;
    private static String current_url;

    public static void reset_page(){
        page_number=1;
    }

    public static int getPage_number() {
        Log.e("current_url", current_url);
        return page_number;}

    public static String npPage(int NorP){
        Pattern pattern = Pattern.compile("(?<=\\.)\\S+(?=\\.)");
        Matcher match = pattern.matcher(current_url);
        String web = "";
        if (match.find())
            web = match.group();
        if (web.equals("xvideos")) return xvid_npPage(NorP);
        if (web.equals("xnxx")) return xnxx_npPage(NorP);
        if (web.equals("redtube")) return redtube_npPage(NorP);
        if (web.equals("xhamster")) return xhamster_npPage(NorP);
        if (web.equals("pornhub")) return pornhub_npPage(NorP);
        if (web.equals("porn")) return porn_npPage(NorP);
        else return current_url;
    }

    public static String search(String search, String[] sort){
        reset_page();
        Pattern pattern = Pattern.compile("(?<=\\.)\\S+(?=\\.)");
        Matcher match = pattern.matcher(current_url);
        String web = "";
        if (match.find())
            web = match.group();
        if (web.equals("xvideos")) return xvid_search(search, sort);//sort, duration, date
        if (web.equals("xnxx")) return xnxx_search(search, sort);//sort, duration, date
        if (web.equals("redtube")) return redtube_search(search, sort);//sort
        if (web.equals("xhamster")) return xhamster_search(search);// no sorting options
        if (web.equals("pornhub")) return pornhub_search(search, sort);//sort
        if (web.equals("porn")) return porn_search(search, sort);//sort, date
        if (web.equals("youporn")) return youporn_search(search, sort);//sort
        return current_url;
    }

    public static List<String> multiSearch(List<String> sites, String search, String[] sort){
        reset_page();
        List<String> url_list = new ArrayList<String>();
        for(String web : sites){
            if (web.equals("xvideos")) url_list.add(xvid_search(search, sort));//sort, duration, date
            if (web.equals("xnxx"))  url_list.add(xnxx_search(search, sort));//sort, duration, date
            if (web.equals("redtube"))  url_list.add(redtube_search(search, sort));//sort
            if (web.equals("xhamster"))  url_list.add(xhamster_search(search));// no sorting options
            if (web.equals("pornhub"))  url_list.add(pornhub_search(search, sort));//sort
            if (web.equals("porn"))  url_list.add(porn_search(search, sort));//sort, date
            if (web.equals("youporn"))  url_list.add(youporn_search(search, sort));//sort
        }
        return url_list;
    }

    public static List<videoObject_xvideo> xvid_page(String url) {
        current_url = url;
        Document info = Jsoup.parse(videoObject.parseURLtoHTML(url));
        Elements elem = info.select("div[class = thumbInside]");
        List<videoObject_xvideo> kList = new ArrayList<videoObject_xvideo>();
        String vid, title, img;
        Pattern pattern = Pattern.compile("/profiles/");
        Matcher matcher;
        for (Element link : elem){
            vid = link.children().select("a").attr("href");
            matcher = pattern.matcher(vid);
            if (!vid.equals("") && !matcher.find()) {
                title = link.children().select("p").text();
                img = link.children().select("img").attr("src");
                kList.add(new videoObject_xvideo(title, "http://www.xvideos.com" + vid, img));
            }
        }
        if (kList.size() % 2 == 1)
            kList.add(new videoObject_xvideo("", "",""));
        kList.add(new videoObject_xvideo("","prev","drawable://" + R.drawable.arrow_prev));
        kList.add(new videoObject_xvideo("","next","drawable://" + R.drawable.arrow_next));
        return kList;
    }

    //sort[0] = relevance, uploaddate, or rating (rating is default)
    //sort[1], dur = 1-3min, 3-10min, 10min_more, or allduration (allduration is default)
    //sort[2], date = today, week, month, or all (all is default)
    public static String xvid_search(String search, String[] sort) {
        if (sort[0].isEmpty()) sort[0] = "rating";
        if (sort[1].isEmpty()) sort[1] = "allduration";
        if (sort[2].isEmpty()) sort[2] = "all";
        search = search.replace(" ", "+");
        String base = "http://www.xvideos.com/?k=";
        current_url = base + search + "&sort=" + sort[0] + "&durf=" + sort[1] + "&datef=" + sort[2];
        return current_url;
    }

    //NorP is (-1) or (1), for gong to next or previous
    //starts at 0 not 1, so new var of page instead of page_number
    public static String xvid_npPage(int NorP) {
        int page = page_number -1;
        if(page==0 && NorP<0){
            NorP=0;
        }

        if(current_url.contains("com/?k=")){
            if (current_url.contains("&p="))
                current_url = current_url.replaceAll("&p=\\d+", "&p=" + (page+NorP));
            else
                current_url += "&p=" + (page+NorP);
        }
        else
        {
            current_url="http://www.xvideos.com/new/" + (page+NorP);
        }
        page_number += NorP;
        return current_url;
    }

    public static List<videoObject_xvideo> xnxx_page(String url) {
        current_url = url;
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
        if (kList.size() % 2 == 1)
            kList.add(new videoObject_xvideo("", "",""));
        kList.add(new videoObject_xvideo("","prev","drawable://" + R.drawable.arrow_prev));
        kList.add(new videoObject_xvideo("","next","drawable://" + R.drawable.arrow_next));
        return kList;
    }

    //sort = relevance, uploaddate, or rating (rating is default)
    //sort[1], dur = 1-3min, 3-10min, 10min_more, or allduration (allduration is default)
    //sort[2], date = today, week, month, or all (all is default)
    public static String xnxx_search(String search, String[] sort) {
        if (sort[0].isEmpty()) sort[0] = "rating";
        if (sort[1].isEmpty()) sort[1] = "allduration";
        if (sort[2].isEmpty()) sort[2] = "all";
        String base = "http://www.xnxx.com/?k=";
        search = search.replace(" ", "+");
        current_url = base + search + "&sort=" + sort[0] + "&durf=" + sort[1] + "&datef=" + sort[2];
        return current_url;
    }

    //NorP is (-1) or (1), for gong to next or previous
    //starts at 0 not 1, so new var of page instead of page_number
    public static String xnxx_npPage(int NorP) {
        int page = page_number -1;
        if(page==0 && NorP<0){
            NorP=0;
        }

        if(current_url.contains("com/?k=")){
            if (current_url.contains("&p="))
                current_url = current_url.replaceAll("&p=\\d+", "&p=" + (page+NorP));
            else
                current_url += "&p=" + (page+NorP);
        }
        else{
            if (0 == (page_number + NorP))
                current_url = "http://www.xnxx.com/";
            else
                current_url = "http://www.xnxx.com/home/" + (page + NorP);
        }
        page_number +=NorP;
        return current_url;
    }

    public static List<videoObject_redtube> redtube_page(String url) {
        current_url = url;
        Document info = Jsoup.parse(videoObject.parseURLtoHTML(url));
        Elements elem = info.select("div[class = video]");
        List<videoObject_redtube> kList = new ArrayList<videoObject_redtube>();
        String vid, title, img;
        for (Element link : elem){
            vid = link.children().select("a").attr("href");
            if (!vid.equals("")) {
                title = link.children().select("a").attr("title");
                img = link.children().select("img").attr("data-src");
                kList.add(new videoObject_redtube(title, "http://www.redtube.com" + vid, img));
            }
        }
        if (kList.size() % 2 == 1)
            kList.add(new videoObject_redtube("","",""));
        kList.add(new videoObject_redtube("","prev","drawable://" + R.drawable.arrow_prev));
        kList.add(new videoObject_redtube("","next","drawable://" + R.drawable.arrow_next));
        return kList;
    }

    //sort = "new", "top", "mostviewed", or "" ("" is default for most relevant)
    public static String redtube_search(String search, String[] sort) {
        String base = "http://www.redtube.com/";
        search = search.replace(" ", "+");
        current_url = base + sort[0] + "?search=" + search;
        return current_url;
    }

    //NorP is (-1) or (1), for going to next or previous
    public static String redtube_npPage(int NorP) {
        if(page_number == 1 && NorP<0){
            NorP=0;
        }

        if(current_url.contains("?page")){
            current_url = "http://www.redtube.com/?page=" + (page_number + NorP);
        }
        else if (current_url.contains("&page")){
            current_url = current_url.replaceAll("&page=\\d+", "&page=" + (page_number + NorP));
        }
        else if(current_url.contains("?search")){
            current_url += "&page=" + (page_number + NorP);
        }
        else{
            current_url += "?page=" + (page_number + NorP);
        }
        page_number +=NorP;
        return current_url;
    }

    public static List<videoObject_xhamster> xhamster_page(String url) {
        current_url = url;
        Document info = Jsoup.parse(videoObject.parseURLtoHTML(url));
        Elements elem = info.select("div[class = video]").select("a");
        List<videoObject_xhamster> kList = new ArrayList<videoObject_xhamster>();
        String vid, title, img;
        for (Element link : elem){
            vid = link.attr("href");
            if (!vid.equals("")) {
                title = link.children().select("u").attr("title");
                if (title.equals(""))
                    title = link.attr("title");
                img = link.children().select("img").attr("src");
                kList.add(new videoObject_xhamster(title, vid, img));
            }
        }
        if (kList.size() % 2 == 1)
            kList.add(new videoObject_xhamster("","",""));
        kList.add(new videoObject_xhamster("","prev","drawable://" + R.drawable.arrow_prev));
        kList.add(new videoObject_xhamster("","next","drawable://" + R.drawable.arrow_next));
        return kList;
    }

    public static String xhamster_search(String search){
        String base = "http://www.xhamster.com/search.php?";
        search = search.replace(" ", "+");
        current_url = base + "q=" + search;
        return current_url;
    }

    //NorP is (-1) or (1), for going to next or previous
    public static String xhamster_npPage(int NorP) {
        if(page_number == 1 && NorP<0){
            NorP=0;
        }

        if (current_url.contains("page")){
            current_url = current_url.replaceAll("page=\\d+", "page=" + (page_number + NorP));
        }
        else{
            current_url = "http://www.xhamster.com/new/" + (page_number + NorP) + ".html";
        }
        page_number +=NorP;
        return current_url;
    }

    public static List<videoObject_pornhub> pornhub_page(String url) {
        current_url = url;
        Document info = Jsoup.parse(videoObject.parseURLtoHTML(url));
        Elements elem = info.select("li[class=videoblock]");
        List<videoObject_pornhub> kList = new ArrayList<videoObject_pornhub>();
        String vid, title, img;
        for (Element link : elem){
            vid = link.children().select("a").attr("href");
            if (!vid.equals("")) {
                title = link.children().select("a").attr("title");
                img = link.children().select("img").attr("data-smallthumb");
                kList.add(new videoObject_pornhub(title, "http://www.pornhub.com" + vid, img));
            }
        }
        if (kList.size() % 2 == 1)
            kList.add(new videoObject_pornhub("","",""));
        kList.add(new videoObject_pornhub("","prev","drawable://" + R.drawable.arrow_prev));
        kList.add(new videoObject_pornhub("","next","drawable://" + R.drawable.arrow_next));
        return kList;
    }

    //lg(longest), tr(top rated), mv(most viewed), mr(most recent), or ""(default is most relevant)
    public static String pornhub_search(String search, String[] sort) {
        String base = "http://www.pornhub.com/video/search?search=";
        search = search.replace(" ", "+").toLowerCase();
        current_url = base + search + "&o=" + sort[0];
        return current_url;
    }

    //NorP is (-1) or (1), for going to next or previous
    public static String pornhub_npPage(int NorP) {
        if(page_number == 1 && NorP<0){
            NorP=0;
        }

        if (current_url.contains("page")){
            current_url = current_url.replaceAll("page=\\d+", "page=" + (page_number + NorP));
        }
        else if (!current_url.contains("com/")){
            current_url = "http://www.pornhub.com/video?page=" + (page_number + NorP);
        }
        else{
            current_url += "&page=" + (page_number + NorP);
        }
        page_number +=NorP;
        return current_url;
    }

    public static List<videoObject_porn> porn_page(String url) {
        current_url = url;
        Document info = Jsoup.parse(videoObject.parseURLtoHTML(url));
        Elements elem = info.select("ul[class=listThumbs]").select("li");
        List<videoObject_porn> kList = new ArrayList<videoObject_porn>();
        String vid, title, img;
        for (Element link : elem){
            vid = link.children().select("a").attr("href");
            if (!vid.equals("")) {
                title = link.children().select("a[class=title]").attr("title");
                img = link.children().select("img").attr("src");
                kList.add(new videoObject_porn(title, "http://www.porn.com" + vid, img));
            }
        }
        if (kList.size() % 2 == 1)
            kList.add(new videoObject_porn("","",""));
        kList.add(new videoObject_porn("","prev","drawable://" + R.drawable.arrow_prev));
        kList.add(new videoObject_porn("","next","drawable://" + R.drawable.arrow_next));
        return kList;
    }

    //sort[0] = d(date), v(views), f(popularity), r(rating), or ""(default is most relevant)
    //sort[1], date = 1(today), 7(week), 30(month), or ""(default is all time)
    public static String porn_search(String search, String[] sort) {
        String base = "http://www.porn.com/search?q=";
        search = search.replace(" ", "+");
        current_url = base + search + "&o=" + sort[0] + sort[1];
        return current_url;
    }

    //NorP is (-1) or (1), for going to next or previous
    public static String porn_npPage(int NorP) {
        if(page_number == 1 && NorP<0){
            NorP=0;
        }

        if (current_url.contains("p=")){
            current_url = current_url.replaceAll("p=\\d+", "p=" + (page_number + NorP));
        }
        else if (current_url.equals("http://www.porn.com/") || current_url.equals("http://www.porn.com")){
            current_url = "http://www.porn.com/videos?p=" + (page_number + NorP);
        }
        else{
            current_url += "&p=" + (page_number + NorP);
        }
        page_number +=NorP;
        return current_url;
    }

    public static List<videoObject_youporn> youporn_page(String url) {
        current_url = url;
        Document info = Jsoup.parse(videoObject.parseURLtoHTML(url));
        Elements elem = info.select("div[class=wrapping-video-box]");
        List<videoObject_youporn> kList = new ArrayList<videoObject_youporn>();
        String vid, title, img;
        for (Element link : elem) {
            vid = link.children().select("a").attr("href");
            if (!vid.equals("")) {
                title = link.children().select("img").attr("alt");
                img = link.children().select("img").attr("src");
                kList.add(new videoObject_youporn(title, "http://www.youporn.com/" + vid, img));
            }
        }
        return kList;
    }

    //"views", "rating", "duration", "date", or "relevance" / "" (relevance is defualt)
    public static String youporn_search(String search, String[] sort) {
        String base = "http://www.youporn.com/search/";
        search = search.replace(" ", "+");
        current_url = base + sort[0] + "/?query=" + search;
        return current_url;
    }

    //NorP is (-1) or (1), for going to next or previous
    public static String youporn_npPage(int NorP) {
        if (page_number == 1 && NorP < 0) {
            NorP = 0;
        }

        if (current_url.contains("page=")) {
            current_url = current_url.replaceAll("page=\\d+", "page=" + (page_number + NorP));
        } else if (current_url.contains("query=")) {
            current_url += "&page=" + (page_number + NorP);
        } else {
            current_url = "http://www.youporn.com/?page=" + (page_number + NorP);
        }
        page_number += NorP;
        return current_url;
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
            String useragent = "Mozilla/5.0 (Windows NT 6.1; WOW64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2071.0 Safari/537.36";
            con.setRequestProperty("User-Agent", useragent);
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

class videoObject_xhamster extends videoObject {

    videoObject_xhamster(String tit, String url, String pic) {
        super(tit, url, pic);
    }

    String getVideoSourceURL(){
        vid_url = Jsoup.parse(parseURLtoHTML(vid_pg_url)).select("video").attr("file");
        return vid_url;
    }

    static String getVideoSourceURL(String vid_pg_url_2){
        String vid_url_2="";
        vid_url_2 = Jsoup.parse(parseURLtoHTML(vid_pg_url_2)).select("video").attr("file");
        return vid_url_2;
    }
}

class videoObject_pornhub extends videoObject {

    videoObject_pornhub(String tit, String url, String pic) {
        super(tit, url, pic);
    }

    String getVideoSourceURL(){
        vid_url = "";
        String emb_url = Jsoup.parse(parseURLtoHTML(vid_pg_url)).select("textarea[onclick = this.select()]").text();
        Pattern pattern = Pattern.compile("(?<=src=\")\\S+(?=\")");
        Matcher matcher = pattern.matcher(emb_url);
        matcher.find();
        String line = Jsoup.parse(parseURLtoHTML(matcher.group())).select("script").html();
        Pattern pat = Pattern.compile("(?<=\\ssrc\\s\\s:\\s')\\S+(?=')");
        Matcher mat = pat.matcher(line);
        mat.find();
        vid_url = mat.group();
        return vid_url;
    }

    static String getVideoSourceURL(String vid_pg_url_2){
        String vid_url_2 = "";
        String emb_url = Jsoup.parse(parseURLtoHTML(vid_pg_url_2)).select("textarea[onclick = this.select()]").text();
        Pattern pattern = Pattern.compile("(?<=src=\")\\S+(?=\")");
        Matcher matcher = pattern.matcher(emb_url);
        matcher.find();
        String line = Jsoup.parse(parseURLtoHTML(matcher.group())).select("script").html();
        Pattern pat = Pattern.compile("(?<=\\ssrc\\s\\s:\\s')\\S+(?=')");
        Matcher mat = pat.matcher(line);
        mat.find();
        vid_url_2 = mat.group();
        return vid_url_2;
    }
}

class videoObject_porn extends videoObject {

    videoObject_porn(String tit, String url, String pic) {
        super(tit, url, pic);
    }

    String getVideoSourceURL() {
        vid_url = "";
        String line = Jsoup.parse(parseURLtoHTML(vid_pg_url)).select("script").html();
        Pattern pattern = Pattern.compile("(?<=file:\")\\S+(?=\")");
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        vid_url = matcher.group();
        return vid_url;
    }

    static String getVideoSourceURL(String vid_pg_url_2) {
        String vid_url_2 = "";
        String line = Jsoup.parse(parseURLtoHTML(vid_pg_url_2)).select("script").html();
        Pattern pattern = Pattern.compile("(?<=file:\")\\S+(?=\")");
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        vid_url_2 = matcher.group();
        return vid_url_2;
    }
}

class videoObject_youporn extends videoObject {

    videoObject_youporn(String tit, String url, String pic) {
        super(tit, url, pic);
    }

    String getVideoSourceURL() {
        vid_url = Jsoup.parse(parseURLtoHTML(vid_pg_url)).select("video").attr("src");
        return vid_url;
    }

    static String getVideoSourceURL(String vid_pg_url_2) {
        String vid_url_2 = Jsoup.parse(parseURLtoHTML(vid_pg_url_2)).select("video").attr("src");
        return vid_url_2;
    }
}
