package com.example.disign.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.disign.R;
import com.example.disign.databinding.FragmentHomeBinding;
import com.example.disign.service.SessionService;

public class HomeFragment extends Fragment   {

    private FragmentHomeBinding binding;
    private HomeContent homeContent;
    private SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("CheckResult")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.newEvent.setOnClickListener((event)->{
            Navigation.findNavController(root).navigate(R.id.nav_slideshow);
        });
        swipeRefreshLayout= root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            this.refresh();
        });
        this.homeContent=this.refresh();
        FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.homeContent.getId(),this.homeContent);
        fragmentTransaction.commit();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.refresh();
    }

    public HomeContent refresh(){
        View.OnScrollChangeListener scrollChangeListener=(a,b,c,d,e)->{
            boolean isAtTop = !ViewCompat.canScrollVertically(this.homeContent.scrollView, -1);
            swipeRefreshLayout.setEnabled(b==0 && isAtTop && a.getId()==this.homeContent.scrollView.getId());

        };
        HomeContent newHomeContent=new HomeContent();

        SessionService.getEventFromSource().subscribe((e)->{
            if(this.homeContent !=null){
                if(this.homeContent.getId()!=0){
                    FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(this.homeContent.getId(),newHomeContent);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.commit();
                }
            }
            this.homeContent=newHomeContent;
            this.homeContent.setScrollChangeListener(scrollChangeListener);

                swipeRefreshLayout.setRefreshing(false);
        });


        return newHomeContent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}





