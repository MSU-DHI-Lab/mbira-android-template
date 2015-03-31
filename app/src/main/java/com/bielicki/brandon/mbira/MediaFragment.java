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

    private int mPage;

    public static MediaFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MediaFragment fragment = new MediaFragment();
        fragment.setArguments(args);
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

            /*for(int x = 0; x < project.getExplorationArrayList().get; x++) {
                String title = project.getExhibitArrayList().get(x).name;
                Bitmap img = project.getExhibitArrayList().get(x).exhibitImage;
                int id = project.getExhibitArrayList().get(x).id;
                items.add(new Item(x,id,title,img));
            }*/

            // Place Holder Images.
            items.add(new Item("Image 1", R.drawable.placeholder));
            items.add(new Item("Image 2", R.drawable.placeholder));

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
            return items.get(position).drawable;
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

            picture.setImageResource(item.drawable);
            name.setText(item.name);


            return v;
        }

        private class Item {
//            final int pos;
//            final int id;
            final String name;
            final int drawable;

            Item(String name, int drawable) {
//                this.pos = pos;
//                this.id = id;
                this.name = name;
                this.drawable = drawable;
            }
        }
    }
}


