package com.marmot.intrepid.naturalhealer.control;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Rank;
import com.marmot.intrepid.naturalhealer.model.Villager;
import com.marmot.intrepid.naturalhealer.model.enumerations.QuestType;
import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestBookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestBookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GameService game;
    ArrayList<Villager> villagers = new ArrayList<Villager>();
    private ArrayList<String> mainQuestList = new ArrayList<String>();
    private ArrayList<String> dailyQuestList = new ArrayList<String>();
    private ArrayList<String> eventQuestList = new ArrayList<String>();

    private OnFragmentInteractionListener mListener;

    public QuestBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestBookFragment newInstance(String param1, String param2) {
        QuestBookFragment fragment = new QuestBookFragment();
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
        View view = inflater.inflate(R.layout.fragment_quest_book, container, false);

        game = GameService.getInstance();

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("QUEST BOOK");
        }

        // Here we will can create click listners etc for all the gui elements on the fragment.
        // For eg: Button btn1= (Button) view.findViewById(R.id.frag1_btn1);
        // btn1.setOnclickListener(...

        final ListView mainList = (ListView) view.findViewById(R.id.mainList);
        final ListView dailyList = (ListView) view.findViewById(R.id.dailyList);
        final ListView eventList = (ListView) view.findViewById(R.id.eventList);

        final Button mainButton = (Button) view.findViewById(R.id.main);
        final Button dailyButton = (Button) view.findViewById(R.id.daily);
        final Button eventButton = (Button) view.findViewById(R.id.event);

        villagers = game.getVillagers();

        for (int i = 0; i < villagers.size(); i++) {
            ArrayList<Quest> villagerQuests = villagers.get(i).getQuests();

            System.out.println(villagers.get(i).getName());

            for (int j = 0; j < villagerQuests.size(); j++) {
                if (villagerQuests.get(j).getType() == QuestType.MAIN) {
                    if (mainQuestList != null) {
                        boolean check = false;
                        int k=0;
                        while (!check && (k < mainQuestList.size())) {
                            if (mainQuestList.get(k) == villagerQuests.get(j).getName()) {
                                check = true;
                            }
                            k++;
                        }
                        if (!check) {
                            mainQuestList.add(villagerQuests.get(j).getName());
                        }
                    }
                    else {
                        mainQuestList.add(villagerQuests.get(j).getName());
                    }
                }
                else if (villagerQuests.get(j).getType() == QuestType.DAILY) {
                    if (dailyQuestList != null) {
                        boolean check = false;
                        int k=0;
                        while (!check && (k < dailyQuestList.size())) {
                            if (dailyQuestList.get(k) == villagerQuests.get(j).getName()) {
                                check = true;
                            }
                            k++;
                        }
                        if (!check) {
                            dailyQuestList.add(villagerQuests.get(j).getName());
                        }
                    } else {
                        dailyQuestList.add(villagerQuests.get(j).getName());
                    }
                }
                else if (villagerQuests.get(j).getType() == QuestType.EVENT) {
                    if (eventQuestList != null) {
                        boolean check = false;
                        int k=0;
                        while (!check && (k < eventQuestList.size())) {
                            if (eventQuestList.get(k) == villagerQuests.get(j).getName()) {
                                check = true;
                            }
                            k++;
                        }
                        if (!check) {
                            eventQuestList.add(villagerQuests.get(j).getName());
                        }
                    }
                    else {
                        eventQuestList.add(villagerQuests.get(j).getName());
                    }
                }
            }
        }

        mainButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    if (mainButton.isPressed()) {
                        mainButton.setPressed(false);
                        mainList.setAdapter(null);
                    } else {
                        dailyButton.setPressed(false);
                        eventButton.setPressed(false);
                        mainButton.setPressed(true);

                        dailyList.setAdapter(null);
                        eventList.setAdapter(null);
                        mainList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mainQuestList));
                    }
                }
                return true;//Return true, so there will be no onClick-event
            }
        });

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = mainList.getAdapter().getItem(position);
                String value = obj.toString();
                villagers = game.getVillagers();

                Intent intent = new Intent(getActivity(), QuestInfoActivity.class);
                for (int i = 0; i < villagers.size(); i++) {
                    ArrayList<Quest> villagerQuests = villagers.get(i).getQuests();
                    for (int j = 0; j < villagerQuests.size(); j++) {
                        if (value == villagerQuests.get(j).getName()) {
                            intent.putExtra("quest", villagerQuests.get(j).getName());
                        }
                    }
                }
                startActivity(intent);
            }
        });

        dailyButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    if (dailyButton.isPressed()) {
                        dailyButton.setPressed(false);
                        dailyList.setAdapter(null);
                    } else {
                        dailyButton.setPressed(true);
                        eventButton.setPressed(false);
                        mainButton.setPressed(false);

                        mainList.setAdapter(null);
                        eventList.setAdapter(null);
                        dailyList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dailyQuestList));
                    }
                }
                return true;//Return true, so there will be no onClick-event
            }
        });

        dailyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = dailyList.getAdapter().getItem(position);
                String value = obj.toString();
                villagers = game.getVillagers();

                Intent intent = new Intent(getActivity(), QuestInfoActivity.class);
                for (int i = 0; i < villagers.size(); i++) {
                    ArrayList<Quest> villagerQuests = villagers.get(i).getQuests();
                    for (int j = 0; j < villagerQuests.size(); j++) {
                        if (value == villagerQuests.get(j).getName()) {
                            intent.putExtra("quest", villagerQuests.get(j).getName());
                        }
                    }
                }
                startActivity(intent);
            }
        });

        eventButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    if (eventButton.isPressed()) {
                        eventButton.setPressed(false);
                        eventList.setAdapter(null);
                    } else {
                        dailyButton.setPressed(false);
                        eventButton.setPressed(true);
                        mainButton.setPressed(false);

                        mainList.setAdapter(null);
                        dailyList.setAdapter(null);
                        eventList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, eventQuestList));
                    }
                }
                return true;//Return true, so there will be no onClick-event
            }
        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = eventList.getAdapter().getItem(position);
                String value = obj.toString();
                villagers = game.getVillagers();

                Intent intent = new Intent(getActivity(), QuestInfoActivity.class);
                for (int i = 0; i < villagers.size(); i++) {
                    ArrayList<Quest> villagerQuests = villagers.get(i).getQuests();
                    for (int j = 0; j < villagerQuests.size(); j++) {
                        if (value == villagerQuests.get(j).getName()) {
                            intent.putExtra("quest", villagerQuests.get(j).getName());
                        }
                    }
                }
                startActivity(intent);
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
