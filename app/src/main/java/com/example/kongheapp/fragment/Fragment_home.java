package com.example.kongheapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;
import com.example.kongheapp.activity.TianJiaActivity;

public class Fragment_home extends Fragment {
    Button dianjishoucang;
    Bundle bundle;
    int zt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(preferences.getStringSet("tjid",null)!=null) {
            getFragmentManager().beginTransaction().replace(R.id.pcontent, new Fragment_shoucang()).commit();
        }
         zt = ((MainActivity)getActivity()).getZt();

        System.out.println(zt);
        dianjishoucang =view.findViewById(R.id.djsc);
        dianjishoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TianJiaActivity.class);
                intent.putExtra("zt",zt);
                startActivity(intent);
            }
        });

        return view;
    }

    public void setZt(int zt) {
        this.zt = zt;
    }

    public int getZt() {
        return zt;
    }
}
