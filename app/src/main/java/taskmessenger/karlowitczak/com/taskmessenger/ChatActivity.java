package taskmessenger.karlowitczak.com.taskmessenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity{

    private Button sendButton;
    private EditText messageInput;
    private TextView chatConversation;
    private String userName, roomName, key, chatUserName, chatMessage;
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendButton = (Button)findViewById(R.id.sendButton);
        messageInput = (EditText)findViewById(R.id.messageInput);
        chatConversation = (TextView)findViewById(R.id.textView);

        roomName = getIntent().getExtras().get("roomName").toString();
        userName = getIntent().getExtras().get("userName").toString();
        setTitle(roomName);

        root = FirebaseDatabase.getInstance().getReference().child(roomName);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference messageRoot = root.child(key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", userName);
                map2.put("message", messageInput.getText().toString());
                messageRoot.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void appendChatConversation(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()){
            chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            chatUserName = (String) ((DataSnapshot)iterator.next()).getValue();
            chatConversation.append(chatUserName + ": " + chatMessage + "\n");
        }
    }
}
