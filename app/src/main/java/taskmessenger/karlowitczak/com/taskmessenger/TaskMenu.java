package taskmessenger.karlowitczak.com.taskmessenger;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;
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


public class TaskMenu extends Fragment implements View.OnClickListener {

    private FloatingActionButton addTaskButton;
    private Button acceptButton, okButton, removeTaskButton, shareFacebookButton;
    private static final String TASKS = "tasks";
    private ImageButton closeButton;
    private EditText taskEditText, descriptionEditText;
    private TextView taskTitle, taskDescription;
    public DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child(TASKS);
    public DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference().getRoot().child(TASKS);
    public DatabaseReference usersRef;
    private static final String ARG_SECTION_NUMBER = "section_number";
    public ArrayList<String> listOfTasks = new ArrayList<>();
    public ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private Spinner assignPersonSpinner, taskStatusSpinner;
    private SearchView searchView;
    private String titleForDatabase;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    public TaskMenu() {
        // Required empty public constructor
    }

    public static TaskMenu newInstance(int sectionNumber) {
        TaskMenu fragment = new TaskMenu();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_menu, container, false);

        listView = (ListView) view.findViewById(R.id.listView);
        addTaskButton = (FloatingActionButton) view.findViewById(R.id.taskButton);
        addTaskButton.setOnClickListener(this);


        searchView = (SearchView)view.findViewById(R.id.searchTask);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<String> templist = new ArrayList<String>();

                for (String temp : listOfTasks) {
                    if (temp.toLowerCase().contains(s.toLowerCase())) {
                        templist.add(temp);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TaskMenu.this.getActivity(), android.R.layout.simple_list_item_1, templist);
                listView.setAdapter(adapter);
                return true;
            }
        });

        arrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, listOfTasks);
        listView.setAdapter(arrayAdapter);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    set.add(((DataSnapshot) iterator.next()).getKey());
                }

                listOfTasks.clear();
                listOfTasks.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTaskInfo(l);
            }
        });

        return view;
    }

    public void showFacebookShareDialog(){

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                    .putString("og:type", "books.book")
                    .putString("og:title", taskTitle.getText().toString())
                    .putString("og:description", taskDescription.getText().toString())
                    .putString("books:isbn", "0-553-57340-3")
                    .build();
            ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                    .setActionType("books.reads")
                    .putObject("book", object)
                    .build();
            ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                    .setPreviewPropertyName("book")
                    .setAction(action)
                    .build();
            shareDialog.show(content);
        }
    }

    public void showTaskInfo(final long id) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(TaskMenu.this.getActivity());
        View mView = getActivity().getLayoutInflater().inflate(R.layout.show_task_info_dialog, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        taskTitle = (TextView) mView.findViewById(R.id.taskTitle);
        taskDescription = (TextView) mView.findViewById(R.id.taskDescription);

        taskStatusSpinner = (Spinner) mView.findViewById(R.id.taskStatusSpinner);
        assignPersonSpinner = (Spinner) mView.findViewById(R.id.assignPersonSpinner);

        closeButton = (ImageButton) mView.findViewById(R.id.closeButton);
        okButton = (Button) mView.findViewById(R.id.okButton);
        removeTaskButton = (Button) mView.findViewById(R.id.removeButton);
        shareFacebookButton = (Button)mView.findViewById(R.id.shareButton);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        root = database.getReferenceFromUrl("https://taskmessenger-8cd8d.firebaseio.com/");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator currentTask = dataSnapshot.child("tasks").child(listOfTasks.get((int) id)).getChildren().iterator();
                ArrayList<String> listTest = new ArrayList<>();

                while (currentTask.hasNext()) {
                    listTest.add(((DataSnapshot) currentTask.next()).getValue().toString());
                }

                taskDescription.setText(listTest.get(1));
                taskTitle.setText(listTest.get(3));

                titleForDatabase = "Title: " + listTest.get(3);

                final ArrayList<String> usersList = new ArrayList<>();
                for (DataSnapshot areaSnapshot : dataSnapshot.child("users").getChildren()) {
                    String email = areaSnapshot.child("Email").getValue(String.class);
                    usersList.add(email);
                }
                ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, usersList);
                userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                assignPersonSpinner.setAdapter(userAdapter);

                ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.taskStatus, android.R.layout.simple_spinner_item);
                statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                taskStatusSpinner.setAdapter(statusAdapter);

                String startStatus = listTest.get(2);
                ArrayAdapter adapter = (ArrayAdapter) taskStatusSpinner.getAdapter();
                int position = adapter.getPosition(startStatus);
                taskStatusSpinner.setSelection(position);

                String startPerson = listTest.get(0);
                ArrayAdapter userSpinnerAdapter = (ArrayAdapter) assignPersonSpinner.getAdapter();
                position = userSpinnerAdapter.getPosition(startPerson);
                assignPersonSpinner.setSelection(position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference taskRoot = root.child("tasks").child(titleForDatabase.substring(7));
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("Status", taskStatusSpinner.getSelectedItem().toString());
                map2.put("Assigned person", assignPersonSpinner.getSelectedItem().toString());
                taskRoot.updateChildren(map2);
                dialog.dismiss();
            }
        });

        removeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskRef.child(titleForDatabase.substring(7)).removeValue();
                arrayAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        shareFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFacebookShareDialog();
            }
        });

        dialog.show();
    }

    public void createAddTaskDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(TaskMenu.this.getActivity());
        View mView = getActivity().getLayoutInflater().inflate(R.layout.activity_popup, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        taskEditText = (EditText) mView.findViewById(R.id.taskTitle);
        descriptionEditText = (EditText) mView.findViewById(R.id.taskDescription);
        taskStatusSpinner = (Spinner) mView.findViewById(R.id.taskStatusSpinner);
        assignPersonSpinner = (Spinner) mView.findViewById(R.id.assignPersonSpinner);
        closeButton = (ImageButton) mView.findViewById(R.id.closeButton);
        acceptButton = (Button) mView.findViewById(R.id.acceptButton);

        ArrayAdapter<CharSequence> taskAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.taskStatus, android.R.layout.simple_spinner_item);
        taskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskStatusSpinner.setAdapter(taskAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReferenceFromUrl("https://taskmessenger-8cd8d.firebaseio.com/users/");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> usersList = new ArrayList<>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String email = areaSnapshot.child("Email").getValue(String.class);
                    usersList.add(email);
                }

                ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, usersList);
                userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                assignPersonSpinner.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(taskEditText.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Please, enter the title", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(descriptionEditText.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Please, enter the description", Toast.LENGTH_LONG).show();
                } else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(taskEditText.getText().toString(), " ");
                    taskRef.updateChildren(map);

                    DatabaseReference taskRoot = taskRef.child(taskEditText.getText().toString());
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("Title", taskEditText.getText().toString());
                    map2.put("Description", descriptionEditText.getText().toString());
                    map2.put("Status", taskStatusSpinner.getSelectedItem().toString());
                    map2.put("Assigned person", assignPersonSpinner.getSelectedItem().toString());
                    taskRoot.updateChildren(map2);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view == addTaskButton) {
            createAddTaskDialog();
        }
    }

}