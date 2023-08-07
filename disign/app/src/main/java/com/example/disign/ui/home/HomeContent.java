package com.example.disign.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.disign.R;
import com.example.disign.component.Event.CardEvent;
import com.example.disign.databinding.FragmentHomeContentBinding;
import com.example.disign.model.Event;
import com.example.disign.service.SessionService;

import java.util.List;
import java.util.stream.Collectors;


public class HomeContent extends Fragment {

    private FragmentHomeContentBinding  binding;
    private View.OnScrollChangeListener scrollChangeListener;
    public ScrollView scrollView;

    public HomeContent() {
    }

    public void setScrollChangeListener(View.OnScrollChangeListener scrollChangeListener) {
        this.scrollChangeListener = scrollChangeListener;
    }

    public View.OnScrollChangeListener getScrollChangeListener() {
        return scrollChangeListener;
    }

    public static HomeContent newInstance( ) {
        return  new HomeContent();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeContentBinding.inflate(inflater, container, false);
        View root=binding.getRoot();

        LinearLayout linearLayout=root.findViewById(R.id.verticalCardContainer);
        scrollView=binding.homeScrollView;

        List<Event> eventList= SessionService.getEvent();
        List <CardEvent> cardEventList=eventList.stream().map(x -> new CardEvent(x)).collect(Collectors.toList());
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        for(CardEvent cardEvent:cardEventList){
            fragmentTransaction.add(linearLayout.getId(),cardEvent);

        }
        fragmentTransaction.commit();


        scrollView.setOnScrollChangeListener(this.getScrollChangeListener());
        return root;


    }

}