package com.example.musicfun.banner;

import static com.example.musicfun.activity.MusicbannerService.COPA_RESULT;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicfun.R;
import com.example.musicfun.activity.MainActivity;
import com.example.musicfun.activity.MusicbannerService;
import com.example.musicfun.databinding.FragmentLyricsBinding;
import com.example.musicfun.datatype.Lyrics;
import com.example.musicfun.datatype.RelativeSizeColorSpan;
import com.example.musicfun.viewmodel.MainActivityViewModel;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.util.ArrayList;
import java.util.List;

public class LyricsFragment extends Fragment {

    private FragmentLyricsBinding binding;
    private MusicbannerService service;
    protected @Nullable ExoPlayer player;
    private BroadcastReceiver broadcastReceiver;
    private MainActivityViewModel viewModel;

    private StyledPlayerControlView controlView;
    private TextView tv_title;
    private TextView tv_artist;
    private ImageView back;
    private ImageView coverView;

    // views and variables for the canvas
    private List<Lyrics> lyricsList;
    private int currentLine = -1;   // current singing row , should be highlighted.
    private TextView tv_lyrics;
//    private ScrollView scrollView;
    private boolean isVisible;

//    private int sumChar = 0;
    private final int POLL_INTERVAL_MS_PLAYING = 1000;
    private final int POLL_INTERVAL_MS_PAUSED = 3000;
    int spanColorHighlight = Color.parseColor("#311B92");
    private RelativeSizeColorSpan highlight = new RelativeSizeColorSpan (1.3f, spanColorHighlight);
    private Spannable spannableText;
    private ScrollingMovementMethod scrolltext = new ScrollingMovementMethod();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentLyricsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isVisible = true;
        controlView = binding.playerView;
        controlView.setShowTimeoutMs(0);
        service = ((MainActivity)getActivity()).getService();
        player = service.player;
        controlView.setPlayer(player);

//      Initialize the views
        tv_title = getView().findViewById(R.id.styled_player_song_name);
        tv_artist = getView().findViewById(R.id.styled_player_artist);
        coverView = getView().findViewById(R.id.imageView2);
        Bundle arguments = getArguments();
        String title = arguments.getString("title");
        tv_title.setText(title);
        String artist = arguments.getString("artist");
        tv_artist.setText(artist);
        //todo: arguments songid
        String id = player.getCurrentMediaItem().mediaMetadata.description.toString();
        String coverUrl = "http://10.0.2.2:3000/images/" + id + ".jpg";
        Picasso.get().load(coverUrl).into(coverView);
        back = binding.scaleDownToBanner;
        back.setOnClickListener(backToPreviousFragment);

//        update title and artist
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String title = intent.getStringExtra("title");
                String artist = intent.getStringExtra("artist");
                tv_title.setText(title);
                tv_artist.setText(artist);
                String coverUrl = intent.getStringExtra("coverUrl");
                changeCover(coverUrl);
            }
        };

//        lyrics relevant
        tv_lyrics = binding.lyrics;
//        scrollView = binding.svLyrics;
        tv_lyrics.setMovementMethod(scrolltext);
        updateLyricsFile();
    }

    private View.OnClickListener backToPreviousFragment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Hide the layout. The fragment is not paused!!!!
            FrameLayout frameLayout = getActivity().findViewById(R.id.fl_lyrics);
            frameLayout.setVisibility(View.GONE);
            isVisible =false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver((broadcastReceiver), new IntentFilter(COPA_RESULT));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    private void updateLyricsFile(){
        lyricsList = new ArrayList<>();
        viewModel.fetchLyrics(player.getCurrentMediaItem().mediaMetadata.description.toString());
        viewModel.getM_songLrc().observe(getViewLifecycleOwner(), new Observer<ArrayList<Lyrics>>() {
            @Override
            public void onChanged(ArrayList<Lyrics> lyrics) {
                if(lyrics.size() != 0){
                    lyricsList = lyrics;
//                    lrcInterface.setLrc(lyrics);
                    String allText = "";
                    for (Lyrics l : lyrics){
                        allText = allText + l.getLyrics() + "\n";
                    }
                    System.out.println( "textview height = " + tv_lyrics.getHeight());
                    tv_lyrics.setText(allText, TextView.BufferType.SPANNABLE);
                    spannableText = (Spannable) tv_lyrics.getText();
                    getCurrentPlayerPosition();
                }
            }
        });
    }

    // checks the current player position every 500ms
    private void getCurrentPlayerPosition() {
        // if there is no need to scroll, scrollAmount will be <=0
        if (player.isPlaying() && isVisible) {
            long time = player.getCurrentPosition();
            int newLine = findLine(time);
            // update UI if line index has been changed
            if (newLine != currentLine){
                // remove all existing span (old span)
                spannableText.removeSpan(highlight);
                // set span for the new line
                int[] startEnd = currentStartPoint(newLine);
                spannableText.setSpan(highlight, startEnd[0], startEnd[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                showFiveLines(newLine);
                currentLine = newLine;
            }
            controlView.postDelayed(this::getCurrentPlayerPosition, POLL_INTERVAL_MS_PLAYING);
        }
        else if (isVisible){
            long time = player.getCurrentPosition();
            int newLine = findLine(time);
            showFiveLines(newLine);
            int[] startEnd = currentStartPoint(newLine);
            spannableText.setSpan(highlight, startEnd[0], startEnd[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            currentLine = newLine;
            controlView.postDelayed(this::getCurrentPlayerPosition, POLL_INTERVAL_MS_PAUSED);
        }
        else{
            System.out.println("view invisible");
        }
    }

    // find the start and end point of the current line. This helps to highlight the lyrics.
    private int[] currentStartPoint(int line){
        int start = 0;
        int[] result = new int[2];
        if (line == 0){
            result[0] = 0;
            result[1] = lyricsList.get(0).getLength() + 1;
            return result;
        }
        for (int i = 0; i < line; i++){
            start = start + lyricsList.get(i).getLength() + 1;
        }
        result[0] = start;
        result[1] = start + lyricsList.get(line).getLength();
        return result;
    }

    // Move the textview (including the hidden lines) so that the current line is always in the middle of the screen
    private void showFiveLines(int i){
        int middleOfTextviewHeight = tv_lyrics.getHeight() / 2;
        tv_lyrics.scrollTo(0, -middleOfTextviewHeight + tv_lyrics.getLayout().getLineTop(i));
    }





    // returns the index of the current line of lyrics
    private int findLine(long currentTime){
        int i = 0;
        while(i < lyricsList.size() - 1){
            Lyrics first = lyricsList.get(i);
            Lyrics second = lyricsList.get(i + 1);
            if (currentTime >= first.getStartTime() && currentTime < second.getStartTime()){
                break;
            }
            i++;
        }
        return i;
    }

    public void changeCover(String url){
        Picasso.get().load(url).into(coverView);
    }
}
