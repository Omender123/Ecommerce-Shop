/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.example.newecommerce.view.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newecommerce.R;
import com.example.newecommerce.model.CenterRepository;
import com.example.newecommerce.util.Utils;
import com.example.newecommerce.view.activities.ECartHomeActivity;
import com.example.newecommerce.view.adapter.ShoppingListAdapter;
import com.example.newecommerce.view.customview.ItemTouchHelperAdapter;
import com.example.newecommerce.view.customview.OnStartDragListener;
import com.example.newecommerce.view.customview.SimpleItemTouchHelperCallback;
public class MyCartFragment extends Fragment implements OnStartDragListener {

    private static FrameLayout noItemDefault;
    private static RecyclerView recyclerView;
    private ItemTouchHelper mItemTouchHelper;
    public MyCartFragment() {
    }

     public static void updateMyCartFragment(boolean showList) {

        if (showList) {

            if (null != recyclerView && null != noItemDefault) {
                recyclerView.setVisibility(View.VISIBLE);

                noItemDefault.setVisibility(View.GONE);
            }
        } else {
            if (null != recyclerView && null != noItemDefault) {
                recyclerView.setVisibility(View.GONE);

                noItemDefault.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_product_list_fragment, container,
                false);

        view.findViewById(R.id.slide_down).setVisibility(View.VISIBLE);
        view.findViewById(R.id.slide_down).setOnTouchListener(
                new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        Utils.switchFragmentWithAnimation(R.id.frag_container,
                                new HomeFragment(),
                                ((ECartHomeActivity) (getContext())),
                                Utils.HOME_FRAGMENT, Utils.AnimationType.SLIDE_DOWN);

                        return false;
                    }
                });

        // Fill Recycler View

        noItemDefault = (FrameLayout) view.findViewById(R.id.default_nodata);

        recyclerView = (RecyclerView) view
                .findViewById(R.id.product_list_recycler_view);

        if (CenterRepository.getCenterRepository().getListOfProductsInShoppingList().size() != 0) {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    getActivity().getBaseContext());

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            ShoppingListAdapter shoppinListAdapter = new ShoppingListAdapter(
                    getActivity(), this);

            recyclerView.setAdapter(shoppinListAdapter);

            shoppinListAdapter
                    .SetOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {

                            Utils.switchFragmentWithAnimation(
                                    R.id.frag_container,
                                    new ProductDetailsFragment("", position,
                                            true),
                                    ((ECartHomeActivity) (getContext())), null,
                                    Utils.AnimationType.SLIDE_LEFT);

                        }
                    });

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(shoppinListAdapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(recyclerView);

        } else {

            updateMyCartFragment(false);

        }

        view.findViewById(R.id.start_shopping).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Utils.switchContent(R.id.frag_container,
                                Utils.HOME_FRAGMENT,
                                ((ECartHomeActivity) (getContext())),
                                Utils.AnimationType.SLIDE_UP);

                    }
                });

        // Handle Back press
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    Utils.switchContent(R.id.frag_container,
                            Utils.HOME_FRAGMENT,
                            ((ECartHomeActivity) (getContext())),
                            Utils.AnimationType.SLIDE_UP);

                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}