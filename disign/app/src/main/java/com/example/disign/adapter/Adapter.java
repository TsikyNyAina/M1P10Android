package com.example.disign.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class Adapter<T extends Fragment> extends FragmentStateAdapter {
    private List<T> fragments;

    public Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<T> fragments) {
        super(fragmentManager, lifecycle);
        this.fragments = fragments;
    }
    @NonNull
    @Override
    public T createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
