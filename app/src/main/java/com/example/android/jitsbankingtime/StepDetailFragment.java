package com.example.android.jitsbankingtime;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android.jitsbankingtime.databinding.FragmentStepDetailBinding;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import timber.log.Timber;

import static android.view.View.GONE;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.APP_NAME;

public class StepDetailFragment extends Fragment {
    private FragmentStepDetailBinding binding;
    private Step step;
    private int currentStepId;
    private Recipe recipe;

    //TODO private ExoPlayer mediaPlayer;
    private SimpleExoPlayer mediaPlayer;
    private PlaybackStateListener playbackStateListener;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private boolean containsVideo = false;
    private String videoUrl;
    private String thumbnailUrl;

    //required empty constructor
    public StepDetailFragment() {

    }

    /* 	OnCreateView() is where a fragment inflates its UI, hooks up any data sources it needs
    and returns the created view to the host activity.
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflate the fragment layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);

        populateRecipeAndStepDetails();


        //display the step id
        binding.textViewStepId.setText("Step " + String.valueOf(currentStepId) + "  of " + String.valueOf(recipe.getSteps().size() - 1));
        //get a reference to the textview in the fragment layout
        TextView stepDescriptionTextView = binding.textViewStepDescription;
        stepDescriptionTextView.setText(step.getDescription());

        //handler for previous button click
        onPreviousButtonClick();

        //handler for next button click
        onNextButtonClick();

        //playback state listeners
        playbackStateListener = new PlaybackStateListener();

        return binding.getRoot();
    }

    private void onNextButtonClick() {

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                if (currentStepId < recipe.getSteps().size() - 1) {
                    currentStepId++;
                    stepDetailFragment.currentStepId = currentStepId;
                    stepDetailFragment.step = recipe.getSteps().get(currentStepId);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_container, stepDetailFragment)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Can't go further. Nothing after this step!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onPreviousButtonClick() {
        binding.buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                if (currentStepId > 0) {
                    currentStepId--;
                    stepDetailFragment.currentStepId = currentStepId;
                    stepDetailFragment.step = recipe.getSteps().get(currentStepId);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_container, stepDetailFragment)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Can't go further. Nothing before this step!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateRecipeAndStepDetails() {
        if (recipe == null) {
            recipe = ((StepDetailActivity) Objects.requireNonNull(getActivity())).getRecipe();
        }
        if (step == null) {
            step = ((StepDetailActivity) Objects.requireNonNull(getActivity())).getStep();
            currentStepId = step.getId();

        }
        videoUrl = step.getVideoURL();
        thumbnailUrl = step.getThumbnailURL();

        //whether steps was null or not
        //do...?
        findIfWeHaveAVideo();

    }

    private void findIfWeHaveAVideo() {
        Timber.d("checking findIfWeHaveAVideo");
        if (videoUrl.trim().equals("") || videoUrl.isEmpty()) {
            //if (videoUrl != null && videoUrl.isEmpty()) {
            Timber.d("videoUrl isEmpty");
            //check if thumbailUrl has an "mp4"
            if (!(thumbnailUrl.isEmpty())) {
                Timber.d("thumbnailUrl is NOT empty");
                if (thumbnailUrl.contains("mp4")) {
                    //this can be used instead of videoUrl
                    Timber.d("thumbnailUrl contains mp4");
                    containsVideo = true;
                    videoUrl = thumbnailUrl;
                } else {
                    //its a normal thumbail :)
                    Timber.d("Its a normal thumbnail");
                    containsVideo = false;
                    binding.playerView.setVisibility(GONE);

                    //display it using picasso
                    Picasso.get()
                            .load(thumbnailUrl)
                            .error(R.drawable.recipe_placeholder_icon)
                            .into(binding.imageViewStepDefault);
                }
            } else {
                //thumbnail is also empty
                containsVideo = false;
                binding.playerView.setVisibility(GONE);

                //display default image using Picasso
                //TODO
                binding.imageViewStepDefault.setImageResource(R.drawable.recipe_placeholder_icon);
            }
        } else {
            containsVideo = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mediaPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    private void initializePlayer() {
        if (containsVideo) {
            if (mediaPlayer == null) {
                // Create an instance of the ExoPlayer.
                DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(this.getContext());
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                //TODO mediaPlayer = SimpleExoPlayer.Builder(this.getContext()).build();
                mediaPlayer = ExoPlayerFactory.newSimpleInstance(
                        (RenderersFactory) defaultRenderersFactory, trackSelector, loadControl);

                //mediaPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(),
                //        new DefaultTrackSelector());
                //mediaPlayer = SimpleExoPlayer.Builder(this).build;

                binding.playerView.setPlayer(mediaPlayer);

                //TODO
                //mediaPlayer.setPlayWhenReady();
            }

            // Set the ExoPlayer.EventListener to this fragment.
            mediaPlayer.addListener(playbackStateListener);

            // Prepare the MediaSource.
            Uri mediaUri = Uri.parse(videoUrl);
            String userAgent = Util.getUserAgent(this.getContext(), APP_NAME);

            //MediaSource mediaSource = new ExtractorMediaSource.Factory(
            //        new DefaultDataSourceFactory(this.getContext(), userAgent))
            //        .createMediaSource(mediaUri);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(mediaUri);

            mediaPlayer.prepare(mediaSource);
            mediaPlayer.setPlayWhenReady(true);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        //TODO mNotificationManager.cancelAll();
        if (mediaPlayer != null) {
            playbackPosition = mediaPlayer.getCurrentPosition();
            currentWindow = mediaPlayer.getCurrentWindowIndex();
            playWhenReady = mediaPlayer.getPlayWhenReady();
            //TODO mediaPlayer.stop();

            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        /*
        binding.playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

         */
    }

    private class PlaybackStateListener implements Player.EventListener {
        //Player.EventListener methods
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        /**
         * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
         * PlayBackState to keep in sync, and post the media notification.
         *
         * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
         * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
         *                      STATE_BUFFERING, or STATE_ENDED.
         */
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }

    }
}
