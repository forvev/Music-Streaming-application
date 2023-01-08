package com.example.musicfun.fragment.chat;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicfun.R;
import com.example.musicfun.adapter.MessageListAdapter;
import com.example.musicfun.databinding.FragmentChatBinding;
import com.example.musicfun.datatype.Message;
import com.example.musicfun.ui.friends.FriendsViewModel;
import com.example.musicfun.viewmodel.chat.ChatViewModel;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    //private ChatViewBinding binding;
    private FragmentChatBinding binding;
    private SharedPreferences sp;
    private String chatPartnerName;
    ChatViewModel chatViewModel;

    public EditText editText;
    public RelativeLayout chatView;

    private RecyclerView  mMessageRecycler;
    private MessageListAdapter messageListAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        Bundle data = getArguments();
        if (data != null){
            chatPartnerName = data.getString("Username");
        }
        //Log.d("disTest", chatPartnerName);
        //Log.d("disTest", "itworked");

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
        int state = sp.getInt("logged", 999);
        String token = sp.getString("token", "");
        //Log.d("disTest", sp.getString("token", ""));

        chatViewModel.init(token, chatPartnerName);
        mMessageRecycler = (RecyclerView) view.findViewById(R.id.recycler_gchat);
        chatViewModel.getMessages().observe(getViewLifecycleOwner(), new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(ArrayList<Message> messages) {
                messageListAdapter = new MessageListAdapter(getContext(),messages,chatPartnerName);
                mMessageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                mMessageRecycler.setAdapter(messageListAdapter);
            }
        });

        chatView = (RelativeLayout) view.findViewById(R.id.layout_gchat_chatbox);

        mMessageRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Log.d("myTest", "closeEdittext");
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
                //Log.d("disChat", newMessage);
                editText.setText("");
            }
        });



    }

    //TODO: start at bottom of chat, message view going up when clicking on EditText

    private void closeKeyboard(View view) {
        // this will give us the view which is currently focus in this layout
        // if nothing is currently focus then this will protect the app from crash
        if (view != null) {
            // assign the system service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
