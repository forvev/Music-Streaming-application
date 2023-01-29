package com.example.musicfun.fragment.chat;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicfun.R;
import com.example.musicfun.activity.MessageListActivity;
import com.example.musicfun.adapter.SharedPlaylist.MessageListAdapter;
import com.example.musicfun.databinding.FragmentChatBinding;
import com.example.musicfun.datatype.Message;
import com.example.musicfun.datatype.SocketIOClient;
import com.example.musicfun.viewmodel.chat.ChatViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import io.socket.emitter.Emitter;

/**
 * Displays a chat view where you can communicate with the chosen friend
 */
public class ChatFragment extends Fragment {


    ArrayList<String> BadWordsList = new ArrayList<>();

    //private ChatViewBinding binding;
    private FragmentChatBinding binding;
    private SharedPreferences sp;
    private String chatPartnerName;
    ChatViewModel chatViewModel;
    private Toolbar toolbar;


    public EditText editText;
    public RelativeLayout chatView;

    private RecyclerView  mMessageRecycler;
    private MessageListAdapter messageListAdapter;

    //Socket IO
    SocketIOClient socketIOClient = new SocketIOClient();
    private String room;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
        room = sp.getString("name","");

        Bundle data = getArguments();
        if (data != null){
            chatPartnerName = data.getString("Username");
        }
       //Connection to Socket
        connectToSocketIO();


        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.getBadWords(sp.getString("token", ""));
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int state = sp.getInt("logged", 999);
        String token = sp.getString("token", "");

        chatViewModel.getBadWordsList().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                if(!strings.isEmpty()){
                    BadWordsList = strings;
                }

            }
        });


        toolbar = binding.toolbarGchannel;
        toolbar.setTitle(chatPartnerName);
        ((MessageListActivity)getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((MessageListActivity)getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(((MessageListActivity) getActivity()).getBackPress());

        chatViewModel.init(token, chatPartnerName);
        mMessageRecycler = (RecyclerView) view.findViewById(R.id.recycler_gchat);
        chatViewModel.getMessages().observe(getViewLifecycleOwner(), new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(ArrayList<Message> messages) {
                messageListAdapter = new MessageListAdapter(getContext(),messages,chatPartnerName);
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                manager.setStackFromEnd(true);
                mMessageRecycler.setLayoutManager(manager);
                mMessageRecycler.setAdapter(messageListAdapter);
            }
        });

        chatView = (RelativeLayout) view.findViewById(R.id.layout_gchat_chatbox);

        mMessageRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                closeKeyboard(chatView);
                return false;
            }
        });


        editText = (EditText) view.findViewById(R.id.edit_gchat_message);

        Button button = (Button) view.findViewById(R.id.button_gchat_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newMessage = editText.getText().toString();
                newMessage = testThisString(newMessage);

                chatViewModel.sendMsg(token,chatPartnerName,newMessage, sp.getString("name",""));

                JSONObject mess = new JSONObject();
                try{
                    mess.put("username", chatPartnerName);
                    mess.put("msg", newMessage);
                }catch(JSONException e){
                    e.printStackTrace();
                }

                socketIOClient.mSocket.emit("sendMsg",  mess);
                editText.setText("");
            }
        });

    }

    private void closeKeyboard(View view) {
        // this will give us the view which is currently focus in this layout
        // if nothing is currently focus then this will protect the app from crash
        if (view != null) {
            // assign the system service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try{
            JSONObject answer = (JSONObject) args[0];
            Message msg = new Message(answer.getString("message"), "0", answer.getString("time"), chatPartnerName);
            chatViewModel.activeAdd(msg);
            }catch(JSONException e){
            e.printStackTrace();
            }
        }
    };

    private void connectToSocketIO() {
        JSONObject channelName = new JSONObject();
        socketIOClient.mSocket = socketIOClient.getSocket();
        socketIOClient.mSocket.on("new_msg",onNewMessage);
        socketIOClient.mSocket.connect();

        try{
            channelName.put("channel", room);
        }catch(JSONException e){
            e.printStackTrace();
        }
        socketIOClient.mSocket.emit("join", channelName);

    }

    private String testThisString(String toTest){
        String toTestMsg = toTest.toLowerCase();
        String[] parts = toTestMsg.split(" ");
        for(int i = 0; i < parts.length; i++){
            if(BadWordsList.contains(parts[i])){
                return  getString(R.string.bad_words);
            }
        }
        return toTest;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        socketIOClient.mSocket.emit("end");
        socketIOClient.mSocket.disconnect();
    }
}
