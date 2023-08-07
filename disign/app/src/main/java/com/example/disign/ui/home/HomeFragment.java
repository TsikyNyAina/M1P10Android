package com.example.disign.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.disign.R;
import com.example.disign.component.Event.CardEvent;
import com.example.disign.databinding.FragmentHomeBinding;
import com.example.disign.model.Event;
import com.example.disign.service.ApiManager;
import com.example.disign.service.SessionService;
import com.example.disign.util.CallBackToObservableConverter;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import retrofit2.Call;

public class HomeFragment extends Fragment implements View.OnTouchListener {

    private FragmentHomeBinding binding;

    @SuppressLint("CheckResult")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel =
          //      new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.newEvent.setOnClickListener((event)->{
            Navigation.findNavController(root).navigate(R.id.nav_slideshow);
        });






        LinearLayout l=binding.verticalCardContainer;
        List<Event> eventList= SessionService.getEvent();
        List <CardEvent> cardEventList=eventList.stream().map(x -> new CardEvent(x)).collect(Collectors.toList());
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        for(CardEvent cardEvent:cardEventList){
            fragmentTransaction.add(l.getId(),cardEvent);

        }
        fragmentTransaction.commit();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showToast("Screen Pushed");
                return true; // Return true to consume the touch event
            default:
                return false;
        }
    }
    private void showToast(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}





