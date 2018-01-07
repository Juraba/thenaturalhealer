package com.marmot.intrepid.naturalhealer.control;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.marmot.intrepid.naturalhealer.R;
import com.marmot.intrepid.naturalhealer.model.Villager;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VillagersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VillagersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VillagersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GameService game;
    private ArrayList<Villager> villagers;

    private OnFragmentInteractionListener mListener;

    public VillagersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VillagersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VillagersFragment newInstance(String param1, String param2) {
        VillagersFragment fragment = new VillagersFragment();
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

        View view = inflater.inflate(R.layout.fragment_villagers, container, false);

        game = GameService.getInstance();

        final ListView villagersList = (ListView) view.findViewById(R.id.villagers);

        villagers = game.getVillagers();

        if (villagers != null) {
            ArrayList<String> villagersName = new ArrayList<String>();
            for (int i = 0; i < villagers.size(); i++) {
                villagersName.add(villagers.get(i).getName());
            }

            villagersList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, villagersName));

            villagersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object obj = villagersList.getAdapter().getItem(position);
                    String value = obj.toString();

                    Intent intent = new Intent(getActivity(), VillagerQuestsActivity.class);
                    intent.putExtra("villager", value);
                    startActivity(intent);
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

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("==== On Start ====");
        MainActivity.quickLoad();
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("==== On Stop ====");
        MainActivity.quickSave();
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
