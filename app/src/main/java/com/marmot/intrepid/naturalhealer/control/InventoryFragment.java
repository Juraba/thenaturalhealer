package com.marmot.intrepid.naturalhealer.control;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Herb;
import com.marmot.intrepid.naturalhealer.model.Item;
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InventoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GameService game;
    private Player player;
    private HashMap<Item, Integer> inventory = new HashMap<Item, Integer>();

    private OnFragmentInteractionListener mListener;

    public InventoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventoryFragment newInstance(String param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        game = GameService.getInstance();

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("INVENTORY");
        }

        // Here we will can create click listners etc for all the gui elements on the fragment.
        // For eg: Button btn1= (Button) view.findViewById(R.id.frag1_btn1);
        // btn1.setOnclickListener(...

        player = game.getPlayer();
        inventory = game.getPlayer().getInventory();
        final ArrayList<Item> brewSelection = new ArrayList<>();

        final GridView itemList = (GridView) view.findViewById(R.id.inventory);

        Button explore = (Button) view.findViewById(R.id.explore);
        Button brew = (Button) view.findViewById(R.id.brew);

        TextView capacity = (TextView) view.findViewById(R.id.capacity);
        TextView money = (TextView) view.findViewById(R.id.money);

        capacity.setText(player.getInventory().size() + " item(s)");
        money.setText(game.getPlayer().getPurse() + "$");

        final String[] pictures = new String[inventory.size()];
        String[] numbers = new  String[inventory.size()];

        int cpt = 0;
        for (Map.Entry<Item, Integer> i : inventory.entrySet()) {
            Item key = i.getKey();
            int number = i.getValue();

            pictures[cpt] = key.getPicName();
            numbers[cpt] = Integer.toString(number);
            cpt++;
        }

        //Taille de référence
        BitmapDrawable bd=(BitmapDrawable) this.getResources().getDrawable(R.mipmap.ic_water);
        float density = getContext().getResources().getDisplayMetrics().density;

        int imageWidth = bd.getBitmap().getWidth();

        itemList.setColumnWidth((int) (imageWidth));

        final GridAdapter adapter = new GridAdapter(this.getContext(), numbers, pictures);
        itemList.setAdapter(adapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                Object obj = itemList.getAdapter().getItem(position);
                String value = obj.toString();

                String render = "";

                for (int i = 0; i < pictures.length; i++) {
                    if (pictures[i].equals(value)) {
                        render = pictures[i];
                    }
                }

                System.out.println("ITEM VALUE : " + render);

                Intent intentList = new Intent(getActivity(), ItemInfoActivity.class);
                intentList.putExtra("item", render);
                intentList.putExtra("inventory", 1);
                startActivity(intentList);
            }
        });

        itemList.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                        Item item = null;
                        for(HashMap.Entry<Item, Integer> entry : inventory.entrySet()){
                            if(entry.getKey().getClass() != Recipe.class){
                                String itemName = "ic_"+entry.getKey().getName();
                                if(itemName.equals(itemList.getItemAtPosition(position))){
                                    System.out.println("PASSAGE ADD SELECTION LIST");
                                    brewSelection.add(entry.getKey());
                                    itemList.setClickable(false);
                                }
                            }
                        }
                        //System.out.println("");
                        return true;
                    }
        });

        /**itemList.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                Item item = null;
                for(HashMap.Entry<Item, Integer> entry : inventory.entrySet()){
                    if(entry.getKey().getClass() != Recipe.class){
                        String itemName = "ic_"+entry.getKey().getName();
                        if(itemName.equals(itemList.getItemAtPosition(position))){
                            System.out.println("PASSAGE ADD SELECTION LIST");
                            brewSelection.add(entry.getKey());
                            itemList.setClickable(false);

                        }
                    }
                }
                adapter.notifyDataSetChanged();
                //System.out.println("");
                //return true;
            }

            public void onNothingSelected(AdapterView<?> parent){}
        });*/

        explore.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ArrayList<Item> items = game.getItems();
                    ArrayList<Herb> herbs = new ArrayList<Herb>();

                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getClass() == Herb.class) {
                            herbs.add((Herb) items.get(i));
                        }
                    }
                    HashMap<Item, Integer> reward = game.getPlayer().explore(herbs);

                    String gain = "";
                    for (Map.Entry<Item, Integer> i : reward.entrySet()) {
                        gain += "\n" + i.getValue() + " " + i.getKey().getName();
                    }

                    Toast.makeText(v.getContext(), "You earnd :" + gain, Toast.LENGTH_LONG);
                }
                return true;//Return true, so there will be no onClick-event
            }
        });

        brew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //VERSION 1
                Intent intent = new Intent(getActivity(), BrewActivity.class);
                startActivity(intent);

                //VERSION 2
                //Making list of recipes
                /**for(HashMap.Entry<Item, Integer> entry : inventory.entrySet()){
                 if(entry.getKey().getClass() == Recipe.class){
                 recipeList.add((Recipe) entry.getKey());
                 System.out.println("Recipe : "+entry.getKey().getName());
                 }
                 }*/
                /*
                System.out.println("=== PASSAGE ONCLICK BREW ===");
                Item item = null;
                ArrayList<Recipe> recipeList = new ArrayList<>();

                for(int i=0; i < game.getItems().size(); i++){
                    if(game.getItems().get(i).getClass() == Recipe.class){
                        recipeList.add((Recipe) game.getItems().get(i));
                        System.out.println("Recipe : "+game.getItems().get(i).getName());
                    }
                }
                //Are components ok ?
                boolean ok = true;
                HashMap<Integer, String> protocol, protocolSave;
                protocolSave = new HashMap<>();
                System.out.println("RecipeList : "+recipeList.size());
                for(int i=0;i < recipeList.size(); i++){
                    //Getting protocol
                    protocol = new HashMap<>();
                    String[] components = recipeList.get(i).getProtocol().split(", ");
                    for(int j=0; j < components.length; j++){
                        String[] component = components[j].split(" ");
                        System.out.println(components[j]);
                        System.out.println("QUANTITE : "+component[0]);
                        System.out.println("NAME ITEM : "+component[1]);
                        if(component.length == 2){
                            protocol.put(Integer.parseInt(component[0]), component[1]);
                            protocolSave.put(Integer.parseInt(component[0]), component[1]);
                        }
                        else if(component.length == 3){
                            protocol.put(Integer.parseInt(component[0]),component[1]+" "+component[2]);
                            protocolSave.put(Integer.parseInt(component[0]),component[1]+" "+component[2]);
                        }
                    }
                    System.out.println("Protocol : "+protocol.toString());
                    if(brewSelection.size() == protocol.size()){ //Number of selected items = number of items in protocol
                        int j=0;
                        for(HashMap.Entry<Integer, String> entry : protocol.entrySet()){
                            if(brewSelection.get(j).getName().equals(entry.getValue())){
                                for(HashMap.Entry<Item, Integer> entryInv : inventory.entrySet()){
                                    if(entry.getKey() > entryInv.getValue()){
                                        ok = false;
                                    }
                                }
                            }
                            j++;
                        }
                    }
                }

                if(ok){
                    int i=0;
                    for(HashMap.Entry<Integer, String> entry : protocolSave.entrySet()){
                        if(brewSelection.get(i).getName().equals(entry.getValue())){
                            for(HashMap.Entry<Item, Integer> entryInv : inventory.entrySet()){
                                if(entry.getKey() <= entryInv.getValue()){
                                    entryInv.setValue(entryInv.getValue()-entry.getKey());
                                }
                            }
                        }
                        i++;
                    }
                    MainActivity.quickSave();
                }
                */
                //System.out.println("");
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("==== On Start ====");
        MainActivity.quickLoad();
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
