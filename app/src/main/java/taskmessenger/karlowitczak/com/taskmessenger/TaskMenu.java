package taskmessenger.karlowitczak.com.taskmessenger;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TaskMenu extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String mSectionNumber;

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
        if (getArguments() != null) {
            mSectionNumber = getArguments().getString(ARG_SECTION_NUMBER);

        }
    }

    private ListView taskListView;
    private String[] names;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        taskListView = (ListView)view.findViewById(R.id.listView2);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                createDialog(position);
            }
        });

        names = getResources().getStringArray(R.array.myDataList);

        TaskListAdapter taskAdapter = new TaskListAdapter();
        taskListView.setAdapter(taskAdapter);

        return view;
    }

    public void createDialog(int position){
        //task title bundle
        Bundle title = new Bundle();
        title.putString("NAME", names[position]);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(TaskMenu.this.getActivity());
        View mView = getActivity().getLayoutInflater().inflate(R.layout.activity_popup, null);
        mBuilder.setView(mView);

        final AlertDialog dialog = mBuilder.create();


        TextView taskTitle = (TextView) mView.findViewById(R.id.taskTitle);
        Button closeButton = (Button) mView.findViewById(R.id.closeButton);

        taskTitle.setText(title.get("NAME").toString());

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private Popup popUpWindow;
    private Button close;


    class TaskListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getActivity().getLayoutInflater().inflate(R.layout.tasks_listview,null);

            TextView name = (TextView) convertView.findViewById(R.id.textView_name);
            TextView description = (TextView) convertView.findViewById(R.id.textView_description);

            name.setText(names[position]);
            description.setText(TaskStatus.TO_DO.name());
            return convertView;
        }
    }
}
