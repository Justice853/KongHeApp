package com.example.kongheapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kongheapp.R;

public class Fragment_home extends Fragment {
    TextView tvwenzi ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        tvwenzi = view.findViewById(R.id.textView);
        tvwenzi.setText(R.string.info_app);

        return view;
    }
}
