package com.example.android.jitsbankingtime.ui;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.databinding.FragmentIngredientsBinding;


public class FragmentIngredientsList extends Fragment {
    private FragmentIngredientsBinding ingredientsBinding;

    /**
     *  Mandatory empty constructor
     */
    public FragmentIngredientsList() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the fragment ingredientslist layout
        ingredientsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients,
                container, false);
        View rootView = ingredientsBinding.getRoot();

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
