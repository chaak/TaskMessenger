package taskmessenger.karlowitczak.com.taskmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatMenu extends Fragment implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button addChatRoomButton;
    private Button logoutButton;
    private EditText roomName;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listOfRooms = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private String userName;

    private static final String ARG_SECTION_NUMBER = "section_number";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatMenu() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChatMenu newInstance(int sectionNumber) {
        ChatMenu fragment = new ChatMenu();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
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
        View view = inflater.inflate(R.layout.fragment_chat_menu, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        retriveUserName();

        if (firebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
            startActivity(new Intent(this.getActivity(), LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        addChatRoomButton = (Button) view.findViewById(R.id.addRoomButton);
        logoutButton = (Button) view.findViewById(R.id.logoutButton);
        roomName = (EditText) view.findViewById(R.id.roomNameEditText);
        listView = (ListView) view.findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, listOfRooms);
        listView.setAdapter(arrayAdapter);

        addChatRoomButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    set.add(((DataSnapshot) iterator.next()).getKey());
                }
                listOfRooms.clear();
                listOfRooms.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("roomName", ((TextView) view).getText().toString());
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void retriveUserName() {
        userName = getActivity().getIntent().getExtras().get("userName").toString();
        if (userName == null) {
            getActivity().finish();
            startActivity(new Intent(this.getActivity(), LoginActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == logoutButton) {
            firebaseAuth.signOut();
            getActivity().finish();
            startActivity(new Intent(this.getActivity(), LoginActivity.class));
        }

        if (view == addChatRoomButton) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(roomName.getText().toString(), " ");
            root.updateChildren(map);
        }
    }

}
