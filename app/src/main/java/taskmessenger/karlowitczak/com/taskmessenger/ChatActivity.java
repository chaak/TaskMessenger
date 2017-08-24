package taskmessenger.karlowitczak.com.taskmessenger;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ImageButton sendButton;
    private EditText messageInput;
    private TextView roomToolbarTitle;
    private String userName, roomName, key, chatUserName, chatMessage, messageStoredText;
    private DatabaseReference root;
    private ScrollView messageScrollView;
    private ImageButton backButton;
    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setupStatusBar();

        sendButton = (ImageButton) findViewById(R.id.sendButton);
        messageInput = (EditText) findViewById(R.id.messageInput);
        roomToolbarTitle = (TextView) findViewById(R.id.room_name);
        messageScrollView = (ScrollView) findViewById(R.id.scrollView);
        messageInput.setText(UserPreferences.getMessageInput(this));
        roomName = getIntent().getExtras().get("roomName").toString();
        userName = getIntent().getExtras().get("userName").toString();
        roomToolbarTitle.setText(roomName);

        root = FirebaseDatabase.getInstance().getReference().child("chat_rooms").child(roomName);

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
                messageInput.setText("");
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    private void appendChatConversation(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            TextView newMessage = new TextView(this);
            TextView senderName = new TextView(this);

            LinearLayout layout = (LinearLayout) findViewById(R.id.messagesWindow);
            Drawable messagePatch = getResources().getDrawable(R.drawable.torn_paper_patch);

            newMessage.setBackground(messagePatch);
            newMessage.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            chatMessage = (String) ((DataSnapshot) iterator.next()).getValue();
            chatUserName = (String) ((DataSnapshot) iterator.next()).getValue();
            if (chatUserName.equals(userName)) {
                if (messagePatch != null) {
                    String yourColorPreference = PreferenceManager.getDefaultSharedPreferences(this).getString("PREF_YOUR_MESSAGE_COLOR", "1");
                    switch (yourColorPreference) {
                        case "1":
                            senderName.setTextColor(Color.rgb(112, 128, 246));
                            newMessage.getBackground().setColorFilter(Color.rgb(112, 128, 246), PorterDuff.Mode.SRC_IN);
                            break;
                        case "0":
                            senderName.setTextColor(Color.rgb(255, 0, 0));
                            newMessage.getBackground().setColorFilter(Color.rgb(255, 0, 0), PorterDuff.Mode.SRC_IN);
                            break;
                        case "-1":
                            senderName.setTextColor(Color.rgb(137, 0, 210));
                            newMessage.getBackground().setColorFilter(Color.rgb(137, 0, 210), PorterDuff.Mode.SRC_IN);
                            break;
                        default:
                            senderName.setTextColor(Color.rgb(112, 128, 246));
                            newMessage.getBackground().setColorFilter(Color.rgb(112, 128, 246), PorterDuff.Mode.SRC_IN);
                    }
                }
                params.gravity = Gravity.RIGHT;
            } else {
                if (messagePatch != null) {
                    String yourColorPreference = PreferenceManager.getDefaultSharedPreferences(this).getString("PREF_FRIEND_MESSAGE_COLOR", "-1");
                    switch (yourColorPreference) {
                        case "1":
                            senderName.setTextColor(Color.rgb(112, 128, 246));
                            newMessage.getBackground().setColorFilter(Color.rgb(112, 128, 246), PorterDuff.Mode.SRC_IN);
                            break;
                        case "0":
                            senderName.setTextColor(Color.rgb(255, 0, 0));
                            newMessage.getBackground().setColorFilter(Color.rgb(255, 0, 0), PorterDuff.Mode.SRC_IN);
                            break;
                        case "-1":
                            senderName.setTextColor(Color.rgb(137, 0, 210));
                            newMessage.getBackground().setColorFilter(Color.rgb(137, 0, 210), PorterDuff.Mode.SRC_IN);
                            break;
                        default:
                            senderName.setTextColor(Color.rgb(112, 128, 246));
                            newMessage.getBackground().setColorFilter(Color.rgb(112, 128, 246), PorterDuff.Mode.SRC_IN);
                    }
                }
                params.gravity = Gravity.LEFT;
            }
            newMessage.setLayoutParams(params);
            senderName.setLayoutParams(params);
            senderName.setText(chatUserName);
            newMessage.setText(chatMessage);
            layout.addView(senderName);
            layout.addView(newMessage);
            messageScrollView.post(new Runnable() {

                @Override
                public void run() {
                    messageScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    public void setupStatusBar() {
        backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UserPreferences.saveMessageInput(ChatActivity.this, messageInput.getText().toString());
                        UserPreferences.saveRoomName(ChatActivity.this, roomName);
                        finish();
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserPreferences.saveMessageInput(ChatActivity.this, messageInput.getText().toString());
        UserPreferences.saveRoomName(ChatActivity.this, roomName);
    }
}
