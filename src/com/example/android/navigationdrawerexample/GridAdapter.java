package com.example.android.navigationdrawerexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GoodComp on 6/27/2014.
 */
public class GridAdapter extends BaseAdapter {
    protected final List<Item> items = new ArrayList<Item>();
    protected LayoutInflater inflater;
    protected Context mContext;
    protected ImageLoader imageLoader;
    protected String vsourceURL = "";

    public GridAdapter(Context context , ImageLoader mimageLoader) {
        mContext = context;
        imageLoader = mimageLoader;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if(v == null) {
            v = inflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }



        picture = (ImageView)v.getTag(R.id.picture);
        name = (TextView)v.getTag(R.id.text);
        final Item item = (Item)getItem(i);
        imageLoader.displayImage(item.sourceURL,picture);
        assert name != null;
        if (!item.name.equals(""))
            name.setText(item.name);
        else
            name.setVisibility(View.GONE);
        vsourceURL = item.videoSourceURL;
        return v;
    }

    public class Item {
        final String name;
        final String sourceURL;
        final String videoSourceURL;

        Item(String name, String sourceURL, String videoSourceURL) {
            this.name = name;
            this.sourceURL = sourceURL;
            this.videoSourceURL = videoSourceURL;
        }
    }
}

class GridAdapter_xvideos extends GridAdapter {

    public GridAdapter_xvideos(Context context, List<videoObject_xvideo> videoList, ImageLoader mimageLoader) {
        super(context, mimageLoader);
        int i = 0;
        for (videoObject_xvideo videoObject_variable : videoList) {
            items.add(new Item(videoObject_variable.title, videoObject_variable.picture, videoObject_variable.vid_pg_url ));
            i++;
        }
    }
}

class GridAdapter_redtube extends GridAdapter {

    public GridAdapter_redtube(Context context, List<videoObject_redtube> videoList, ImageLoader mimageLoader) {
        super(context, mimageLoader);
        for (videoObject_redtube videoObject_variable : videoList) {
            items.add(new Item(videoObject_variable.title, videoObject_variable.picture, videoObject_variable.vid_pg_url ));
        }
    }
}

class GridAdapter_pornhub extends GridAdapter {

    public GridAdapter_pornhub(Context context, List<videoObject_pornhub> videoList, ImageLoader mimageLoader) {
        super(context, mimageLoader);
        for (videoObject_pornhub videoObject_variable : videoList) {
            items.add(new Item(videoObject_variable.title, videoObject_variable.picture, videoObject_variable.vid_pg_url ));
        }
    }
}

class GridAdapter_xhamster extends GridAdapter {

    public GridAdapter_xhamster(Context context, List<videoObject_xhamster> videoList, ImageLoader mimageLoader) {
        super(context, mimageLoader);
        for (videoObject_xhamster videoObject_variable : videoList) {
            items.add(new Item(videoObject_variable.title, videoObject_variable.picture, videoObject_variable.vid_pg_url ));
        }
    }
}

class GridAdapter_porn extends GridAdapter {

    public GridAdapter_porn(Context context, List<videoObject_porn> videoList, ImageLoader mimageLoader) {
        super(context, mimageLoader);
        for (videoObject_porn videoObject_variable : videoList) {
            items.add(new Item(videoObject_variable.title, videoObject_variable.picture, videoObject_variable.vid_pg_url ));
        }
    }
}