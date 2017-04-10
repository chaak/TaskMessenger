package taskmessenger.karlowitczak.com.taskmessenger;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ChatMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";


    // TODO: Rename and change types of parameters
    private String mSectionNumber;


    public ChatMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber Parameter 1.
     * @return A new instance of fragment ChatMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatMenuFragment newInstance(int sectionNumber) {
        ChatMenuFragment fragment = new ChatMenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionNumber = getArguments().getString(ARG_SECTION_NUMBER);

        }
    }
    private ListView chatListView;
    private String[] names;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_menu, container, false);


    //    chatListView = (ListView)view.findViewById(R.id.listView2);

     //   names = getResources().getStringArray(R.array.myDataList);

//        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
//                this.getActivity(),
//                android.R.layout.simple_list_item_1,
//                names
//        );
//        chatListView.setAdapter(listViewAdapter);

        return view;
    }
}
