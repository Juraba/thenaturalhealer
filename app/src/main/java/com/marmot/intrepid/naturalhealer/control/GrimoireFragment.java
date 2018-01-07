package com.marmot.intrepid.naturalhealer.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Grimoire;
import com.marmot.intrepid.naturalhealer.model.Herb;
import com.marmot.intrepid.naturalhealer.model.Item;
import com.marmot.intrepid.naturalhealer.model.OtherIngredients;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GrimoireFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GrimoireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GrimoireFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GameService game;
    private Grimoire grimoire = null;
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<String> herbList = new ArrayList<String>();
    private ArrayList<String> recipeList = new ArrayList<String>();
    private ArrayList<String> otherList = new ArrayList<String>();

    //HerbDAO herbs;

    private OnFragmentInteractionListener mListener;

    public GrimoireFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GrimoireFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GrimoireFragment newInstance(String param1, String param2) {
        GrimoireFragment fragment = new GrimoireFragment();
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
        View view = inflater.inflate(R.layout.fragment_grimoire, container, false);

        game = GameService.getInstance();

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("GRIMOIRE");
        }

        // Here we will can create click listners etc for all the gui elements on the fragment.
        // For eg: Button btn1= (Button) view.findViewById(R.id.frag1_btn1);
        // btn1.setOnclickListener(...

        grimoire = game.getGrimoire();

        if (grimoire != null) {
            for (int i = 0; i < grimoire.getHerbs().size(); i++) {
                Herb r = grimoire.getHerbs().get(i);
                boolean check = false;
                int j = 0;
                while (!check && (j < herbList.size())) {
                    if (r.getType().getEn() == herbList.get(j)) {
                        check = true;
                    }
                    j++;
                }
                if (check == false) {
                    herbList.add(r.getType().getEn());
                }
            }
            for (int i = 0; i < grimoire.getRecipes().size(); i++) {
                Recipe r = grimoire.getRecipes().get(i);
                for (int j = 0; j < r.getSymptoms().length; j++) {
                    boolean check = false;
                    int k=0;
                    while (!check && (k < recipeList.size())) {
                        if (r.getSymptoms()[j].getEn() == recipeList.get(k)) {
                            check = true;
                        }
                        k++;
                    }
                    if (check == false) {
                        recipeList.add(r.getSymptoms()[j].getEn());
                    }
                }
            }
            for (int i = 0; i < grimoire.getOtherIngredients().size(); i++) {
                otherList.add(grimoire.getOtherIngredients().get(i).getName());
            }

            final ListView list = (ListView) view.findViewById(R.id.listCategories);

            list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, herbList));

            final Button herbs = (Button) view.findViewById(R.id.buttonHerbs);
            final Button recipies = (Button) view.findViewById(R.id.buttonRecipies);
            final Button other = (Button) view.findViewById(R.id.buttonOtherIngredients);

            herbs.setPressed(true);

            herbs.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event){
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        recipies.setPressed(false);
                        other.setPressed(false);
                        herbs.setPressed(true);
                        list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, herbList));
                    }
                    return true;//Return true, so there will be no onClick-event
                }
            });

            recipies.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event){
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        herbs.setPressed(false);
                        other.setPressed(false);
                        recipies.setPressed(true);
                        list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, recipeList));
                    }
                    return true;//Return true, so there will be no onClick-event
                }
            });

            other.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event){
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        herbs.setPressed(false);
                        recipies.setPressed(false);
                        other.setPressed(true);
                        list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, otherList));
                    }
                    return true;//Return true, so there will be no onClick-event
                }
            });

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object obj = list.getAdapter().getItem(position);
                    String value = obj.toString();
                    grimoire = game.getGrimoire();

                    //Vérification du type d'item qui est inséré dans la liste
                    //Et création du render
                    String render = "";
                    for (int i = 0; i < grimoire.getHerbs().size(); i++) {
                        ArrayList<Herb> herbs = grimoire.getHerbs();
                        if (value.equals(herbs.get(i).getType().getEn())) {
                            render = "herb";
                        }
                    }
                    for (int i = 0; i < grimoire.getRecipes().size(); i++) {
                        ArrayList<Recipe> recipes = grimoire.getRecipes();
                        for (int j = 0; j < recipes.get(i).getSymptoms().length; j++) {
                            String str = recipes.get(i).getSymptoms()[j].getEn();
                            if (value.equals(str)) {
                                render = "recipe";
                            }
                        }
                    }
                    for (int i = 0; i < grimoire.getOtherIngredients().size(); i++) {
                        ArrayList<OtherIngredients> others = grimoire.getOtherIngredients();
                        if (value.equals(others.get(i).getName())) {
                            render = "other";
                        }
                    }

                    if (render == "herb") {
                        Intent intentList = new Intent(getActivity(), GrimoireItemsActivity.class);
                        intentList.putExtra("herb", value);
                        startActivity(intentList);
                    }
                    else if (render == "recipe") {
                        Intent intentList = new Intent(getActivity(), GrimoireItemsActivity.class);
                        intentList.putExtra("recipe", value);
                        startActivity(intentList);
                    }
                    else if (render == "other") {
                        Intent intentItemInfo = new Intent(getActivity(), ItemInfoActivity.class);
                        intentItemInfo.putExtra("other", value);
                        startActivity(intentItemInfo);
                    }
                }
            });
        }

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
