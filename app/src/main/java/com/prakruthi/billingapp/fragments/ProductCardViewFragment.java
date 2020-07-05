package com.prakruthi.billingapp.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.prakruthi.billingapp.spotbilling.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductCardViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductCardViewFragment extends Fragment implements AddEditProductDialog.IAddEditProduct{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    private FloatingActionButton floatbtn_addproduct;

    List<ItemDataObject> results = new ArrayList<>();

    public ProductCardViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductCardViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductCardViewFragment newInstance(String param1, String param2) {
        ProductCardViewFragment fragment = new ProductCardViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_product_card_view, container, false);

        floatbtn_addproduct = (FloatingActionButton) view.findViewById(R.id.floatbtn_addproduct);

        floatbtn_addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddEditDialog();
            }
        });

        results.clear();

        mAdapter = new MyRecyclerViewAdapter(results);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_product_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        /*mAdapter = new MyRecyclerViewAdapter(getDataSet());*/
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListenerForEdit(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getActivity(), "Single Click on position :"+position ,
                        Toast.LENGTH_SHORT).show();

                openOptionMenu(view,position);
            }
        }));

        return view;
    }

    private void openOptionMenu(View view, final int position) {
        final PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.inflate(R.menu.productmenu_recyclerview);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.productmenu_edit:
                        //handle menu1 click
                        Toast.makeText(getContext(), "You selected the action : " + item.getTitle()+" position "+position, Toast.LENGTH_SHORT).show();
                        popup.dismiss();

                        openAddEditDialog();

                        break;
                    case R.id.productmenu_delete:
                        //handle menu2 click
                        Toast.makeText(getContext(), "You selected the action : " + item.getTitle()+" position "+position, Toast.LENGTH_SHORT).show();
                        popup.dismiss();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void openAddEditDialog() {

        FragmentManager fm = getFragmentManager();
        AddEditProductDialog dialogFragment = new AddEditProductDialog();
        //dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "Add/Edit Fragment");

        dialogFragment.setIProductAddEditDialogListener(new AddEditProductDialog.IProductAddEditDialogListener() {
            @Override
            public void onSavingProductDetailsDialog(ItemDataObject output) {

                //Insert/Update the details into item_master here
                Log.d("Product output : ",""+output);

                ItemDataObject itemDataObject = new ItemDataObject(
                        output.getmProductNameEnglish(),
                        output.getmProductNameKannada(),
                        output.getmProductMeasuringUnit(),
                        output.getmProductDiscountType(),
                        output.getmProductPrice(),
                        output.getmProductDiscountPrice()
                        );

                ContentValues contentValues_productdetails = new ContentValues();

                contentValues_productdetails.put("DESCRIPTION_ENGLISH",output.getmProductNameEnglish());




                results.add(itemDataObject);

                mAdapter.notifyDataSetChanged();
                mRecyclerView.setFocusable(true);

            }
        });

    }

    private ArrayList<DataObject> getDataSet() {

        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 20; index++) {
            DataObject obj = new DataObject("Item-" + (index+1),"100","25");
            results.add(index, obj);
        }
        return results;
    }

    @Override
    public void OnFinishDialogListener() {

    }

   /* @Override
    public void onPause() {
        super.onPause();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }*/

    public interface OnFragmentInteractionListener {
    }
}