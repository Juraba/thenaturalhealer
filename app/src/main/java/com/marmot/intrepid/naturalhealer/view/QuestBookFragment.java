package com.marmot.intrepid.naturalhealer.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.marmot.intrepid.naturalhealer.R;


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
    String[] quests;

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
        View view= inflater.inflate(R.layout.fragment_quest_book, container, false);

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

                        quests = new String[]{"Main Quest 1", "Main Quest 2", "Main Quest 3", "Main Quest 4"};

                        dailyList.setAdapter(null);
                        eventList.setAdapter(null);
                        mainList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, quests));
                    }
                }
                return true;//Return true, so there will be no onClick-event
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

                        quests = new String[]{"Daily Quest 1", "Daily Quest 2", "Daily Quest 3", "Daily Quest 4", "Daily Quest 5", "Daily Quest 6", "Daily Quest 7", "Daily Quest 8", "Daily Quest 9", "Daily Quest 10"};

                        mainList.setAdapter(null);
                        eventList.setAdapter(null);
                        dailyList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, quests));
                    }
                }
                return true;//Return true, so there will be no onClick-event
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

                        quests = new String[]{"Event Quest 1", "Event Quest 2", "Event Quest 3", "Event Quest 4", "Event Quest 5", "Event Quest 6"};

                        mainList.setAdapter(null);
                        dailyList.setAdapter(null);
                        eventList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, quests));
                    }
                }
                return true;//Return true, so there will be no onClick-event
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
