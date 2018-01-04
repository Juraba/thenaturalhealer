package com.marmot.intrepid.naturalhealer.control;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Item;
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.service.GameService;

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

        GridView itemList = (GridView) view.findViewById(R.id.inventory);

        Button sort = (Button) view.findViewById(R.id.sort);
        Button brew = (Button) view.findViewById(R.id.brew);

        TextView capacity = (TextView) view.findViewById(R.id.capacity);
        TextView money = (TextView) view.findViewById(R.id.money);


        //View gridview = inflater.inflate(R.layout.gridview_layout, container, false);
        //TextView itemNumber = (TextView) gridview.findViewById(R.id.itemNumber);
        //ImageView itemIcon = (ImageView) gridview.findViewById(R.id.itemIcon);

        String[] pictures = new String[]{};
        String[] numbers = new  String[]{};

        int cpt = 0;
        for (Item key : inventory.keySet()) {
            pictures[cpt] = key.getPicName();
            numbers[cpt] = inventory.get(key).toString();
            cpt++;
        }

        // Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        for(int i = 0; i < inventory.size() ; i++){
            HashMap<String, String> tmp = new HashMap<String,String>();
            tmp.put("img", pictures[i]);
            tmp.put("number", numbers[i]);
            list.add(tmp);
        }

        // Keys used in Hashmap
        String[] from = { "img","number"};
        // Ids of views in listview_layout
        int[] to = {R.id.itemIcon, R.id.itemNumber};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        //SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.gridview_layout, from, to);
        GridAdapter adapter = new GridAdapter(getActivity().getApplicationContext(), numbers, pictures);
        // Setting an adapter containing images to the gridview
        itemList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /*
        Context context = playerPic.getContext();
        int id = context.getResources().getIdentifier(player.getPicName(), "mipmap", context.getPackageName());
        playerPic.setImageResource(id);

        inventory.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_gallery_item, itemIconList));
        */

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
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
