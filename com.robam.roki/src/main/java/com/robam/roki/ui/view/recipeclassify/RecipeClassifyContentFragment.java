//package com.robam.roki.ui.view.recipeclassify;
//
//import android.os.Bundle;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.robam.common.pojos.Tag;
//import com.robam.roki.R;
//import com.robam.roki.ui.page.device.oven.CookBookTag;
//import com.robam.roki.ui.view.networkoptimization.GridChildView;
//
//import java.util.ArrayList;
//
//
//public class RecipeClassifyContentFragment extends Fragment {
//    ArrayList<Tag> tags;
//    GridChildView gridview;
//
//    public static Fragment instance(ArrayList<Tag> tags) {
//        RecipeClassifyContentFragment fragment = new RecipeClassifyContentFragment();
//        if (tags != null) {
//            Bundle bundle = new Bundle();
//            bundle.putParcelableArrayList("tags", tags);
//            fragment.setArguments(bundle);
//        }
//        return fragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.classify_pager_item, null);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        gridview = view.findViewById(R.id.gridView);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            tags = bundle.getParcelableArrayList("tags");
//        }
//        if (tags != null && tags.size() > 0) {
//            RecipeClassiflyGridViewAdapter recipeClassiflyGridViewAdapter = new RecipeClassiflyGridViewAdapter(getContext(), tags);
//            gridview.setAdapter(recipeClassiflyGridViewAdapter);
//        }
//    }
//
//
//}
