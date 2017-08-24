package taskmessenger.karlowitczak.com.taskmessenger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;
import java.util.Set;

public class ChatMenu extends Fragment implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button addRoomButton;
    private ImageButton closeRoomDialogButton;
    private SearchView searchView;
    private EditText newRoomName;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listOfRooms = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("chat_rooms");
    private String userName;
    private FloatingActionButton addNewRoomButton;

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

        listView = (ListView) view.findViewById(R.id.listView);
        searchView = (SearchView) view.findViewById(R.id.searchChat);
        addNewRoomButton = (FloatingActionButton) view.findViewById(R.id.fab);
        addNewRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoomDialog();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<String> templist = new ArrayList<String>();

                for (String temp : listOfRooms) {
                    if (temp.toLowerCase().contains(s.toLowerCase())) {
                        templist.add(temp);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChatMenu.this.getActivity(), android.R.layout.simple_list_item_1, templist);
                listView.setAdapter(adapter);
                return true;
            }
        });

        arrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, listOfRooms);
        listView.setAdapter(arrayAdapter);


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
                if (!Objects.equals(((TextView) view).getText().toString(), UserPreferences.getSavedMessageRoomName(getActivity()))) {
                    UserPreferences.saveMessageInput(getActivity(), "");
                }
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });
        return view;
    }

    private void retriveUserName() {
        userName = getActivity().getIntent().getExtras().get("userName").toString();
        if (userName == null) {
            getActivity().finish();
            startActivity(new Intent(this.getActivity(), LoginActivity.class));
        }
    }

    public void addNewRoom(Dialog dialog) {
        if (TextUtils.isEmpty(newRoomName.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Please, enter the chat room name", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(newRoomName.getText().toString(), " ");
            root.updateChildren(map);
            Toast.makeText(ChatMenu.this.getContext(), getString(R.string.newRoomAddedToast), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public void createRoomDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ChatMenu.this.getActivity());
        View mView = getActivity().getLayoutInflater().inflate(R.layout.new_chat_room_popup, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        newRoomName = (EditText) mView.findViewById(R.id.newRoomTextEdit);
        addRoomButton = (Button) mView.findViewById(R.id.addRoomButton);
        closeRoomDialogButton = (ImageButton) mView.findViewById(R.id.closeRoomDialog);
        closeRoomDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewRoom(dialog);

            }
        });


        dialog.show();
    }

    @Override
    public void onClick(View view) {
    }

}
