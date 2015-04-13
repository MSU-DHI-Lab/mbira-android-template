package com.bielicki.brandon.mbira;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
* A simple {@link Fragment} subclass.
* to handle interaction events.
* Use the {@link MediaFragment#newInstance} factory method to
* create an instance of this fragment.
*/
// In this case, the fragment displays simple text based on the page
public class MediaFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    AppData project = AppData.get();
    static boolean isLocation = false;
    static int id = 0;


    private int mPage;

    public static MediaFragment newInstance(int page, boolean loc, int proj_id) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MediaFragment fragment = new MediaFragment();
        fragment.setArguments(args);
        isLocation = loc;
        id = proj_id;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getActivity().getApplicationContext()));
        return view;
    }

    // Image Adapter for GridView.
    private class ImageAdapter extends BaseAdapter {
        private List<Item> items = new ArrayList<Item>();
        private LayoutInflater inflater;

        public ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            if (isLocation)
            {
                String title = project.getLocationArrayList().get(id).name;

                for (Bitmap media : project.getLocationArrayList().get(id).media){
                    items.add(new Item(media));
                }
            }

            else {
                String title = project.getAreaArrayList().get(id).name;
                for (Bitmap media : project.getAreaArrayList().get(id).media){
                    items.add(new Item(media));
                }
            }
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).item_id;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup)
        {
            View v = view;
            ImageButton picture;
            TextView name;

            if(v == null)
            {
                v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
                v.setTag(R.id.text, v.findViewById(R.id.text));
            }

            picture = (ImageButton)v.getTag(R.id.picture);
            name = (TextView)v.getTag(R.id.text);

            Item item = (Item)getItem(position);

            picture.setImageBitmap(item.drawable);
//            name.setText(item.name);

            //Hiding the TextView in the gridview_item layout
            name.setVisibility(View.GONE);

            return v;
        }

        private class Item {
            final int item_id;
            final Bitmap drawable;

            Item(Bitmap drawable) {
                this.item_id = id;
                this.drawable = drawable;
            }
        }
    }
}


