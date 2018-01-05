package com.marmot.intrepid.naturalhealer.control;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Herb;
import com.marmot.intrepid.naturalhealer.model.Item;
import com.marmot.intrepid.naturalhealer.model.OtherIngredients;
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.model.Shop;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GameService game;
    private Player player;
    private Shop shop;

    private OnFragmentInteractionListener mListener;

    public ShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
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
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        game = GameService.getInstance();

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("SHOP");
        }

        // Here we will can create click listners etc for all the gui elements on the fragment.
        // For eg: Button btn1= (Button) view.findViewById(R.id.frag1_btn1);
        // btn1.setOnclickListener(...

        final ListView list = (ListView) view.findViewById(R.id.listCategories);

        //list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categories));

        final Button herbs = (Button) view.findViewById(R.id.buttonHerbs);
        final Button recipes = (Button) view.findViewById(R.id.buttonRecipies);
        final Button other = (Button) view.findViewById(R.id.buttonOtherIngredients);

        player = game.getPlayer();
        shop = game.getShop();

        final GridView itemList = (GridView) view.findViewById(R.id.inventory);

        TextView capacity = (TextView) view.findViewById(R.id.capacity);
        TextView money = (TextView) view.findViewById(R.id.money);

        final ArrayList<Herb> herbList = shop.getHerbs();
        ArrayList<Recipe> recipeList = shop.getRecipes();
        ArrayList<OtherIngredients> otherList = shop.getOtherIngredients();

        final String[] herbPics = new String[herbList.size()];
        final String[] recipePics = new String[herbList.size()];
        final String[] otherPics = new String[herbList.size()];

        for (int i = 0; i < herbList.size(); i++) {
            herbPics[i] = herbList.get(i).getPicName();
        }

        for (int i = 0; i < recipeList.size(); i++) {
            recipePics[i] = recipeList.get(i).getPicName();
        }

        for (int i = 0; i < otherList.size(); i++) {
            otherPics[i] = otherList.get(i).getPicName();
        }

        //Taille de référence
        BitmapDrawable bd=(BitmapDrawable) this.getResources().getDrawable(R.mipmap.ic_water);
        float density = getContext().getResources().getDisplayMetrics().density;

        int imageWidth = bd.getBitmap().getWidth();

        itemList.setColumnWidth((int) (imageWidth * 0.4));

        GridAdapter adapter = new GridAdapter(this.getContext(), new String[]{}, herbPics);
        itemList.setAdapter(adapter);

        herbs.setPressed(true);

        herbs.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    recipes.setPressed(false);
                    other.setPressed(false);
                    herbs.setPressed(true);

                    for (int i = 0; i < herbPics.length; i++) {
                        System.out.println(herbPics[i]);
                    }
                    GridAdapter adapter = new GridAdapter(getActivity(), new String[]{}, herbPics);
                    itemList.setAdapter(adapter);
                }
                return true;//Return true, so there will be no onClick-event
            }
        });

        recipes.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    herbs.setPressed(false);
                    other.setPressed(false);
                    recipes.setPressed(true);

                    for (int i = 0; i < recipePics.length; i++) {
                        System.out.println(recipePics[i]);
                    }
                    GridAdapter adapter = new GridAdapter(getActivity(), new String[]{}, recipePics);
                    itemList.setAdapter(adapter);
                }
                return true;//Return true, so there will be no onClick-event
            }
        });

        other.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    herbs.setPressed(false);
                    recipes.setPressed(false);
                    other.setPressed(true);

                    for (int i = 0; i < otherPics.length; i++) {
                        System.out.println(otherPics[i]);
                    }
                    GridAdapter adapter = new GridAdapter(getActivity(), new String[]{}, otherPics);
                    itemList.setAdapter(adapter);
                }
                return true;//Return true, so there will be no onClick-event
            }
        });

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                Object obj = itemList.getAdapter().getItem(position);
                String value = obj.toString();

                Intent intentList = new Intent(getActivity(), ItemInfoActivity.class);
                intentList.putExtra("item", value);
                intentList.putExtra("shop", 1);
                startActivity(intentList);
            }
        });

        /*
        itemList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "TEST : " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */

        itemList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View arg1,
                                           int position, long arg3) {

                Toast.makeText(getContext(), "TEST : " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String s) {
        if (mListener != null) {
            mListener.onFragmentInteraction(s);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String s);
    }
}
