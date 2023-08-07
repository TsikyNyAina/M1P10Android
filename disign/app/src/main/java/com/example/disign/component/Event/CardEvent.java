package com.example.disign.component.Event;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.disign.R;
import com.example.disign.adapter.Adapter;
import com.example.disign.component.imageView.ImageViewer;
import com.example.disign.component.video.VideoFragment;
import com.example.disign.component.web.WebFragment;
import com.example.disign.model.Event;
import com.example.disign.model.Media;
import com.example.disign.util.FileTypeChecker;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardEvent extends Fragment {
    private Event event;

    public CardEvent(Event event) {
        setEvent(event);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public static CardEvent newInstance(Event event) {
        return new CardEvent(event);
    }
    private static final int BOTTOM_MARGIN_DP = 10; // Set your desired margin in dp


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View root=inflater.inflate(R.layout.fragment_card_event, container, false);
        TextView title=root.findViewById(R.id.cardTitle);
        TextView desc=root.findViewById(R.id.cardDesc);
        desc.setText(getEvent().getDescription());
        title.setText(getEvent().getTitre());



        List<Fragment> list=event.getMedia().stream().map((m->{
            if(m.getFileInfo()!=null){
                Uri uri=Uri.parse("https://m1p10androidnode.onrender.com/fileNotAttachment/"+m.getFileInfo().getFilename());
                if(FileTypeChecker.isImage(this.getContext(),uri)){
                    ImageViewer imageViewer=new ImageViewer(uri);
                    return imageViewer;
                }
                else if(FileTypeChecker.isVideo(this.getContext(),uri)){
                    return new VideoFragment(uri);
                }

            }
            else if (m.getWebUrl()!=null){
                return new WebFragment(m.getWebUrl());
            }
            return null;
        })).collect(Collectors.toList());
        if(list.size()==0){
            LinearLayout layout=root.findViewById(R.id.cardContainer);
            ViewGroup.LayoutParams layoutParams=layout.getLayoutParams();
            layoutParams.height= ViewGroup.LayoutParams.WRAP_CONTENT;
            layout.setLayoutParams(layoutParams);
        }

        Adapter adapter = new Adapter(getChildFragmentManager(),getLifecycle(), list);
        ViewPager2 viewPager= root.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);


        int bottomMarginPx = (int) (BOTTOM_MARGIN_DP * getResources().getDisplayMetrics().density);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) root.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, bottomMarginPx);
        root.setLayoutParams(layoutParams);








        return root;
    }

}