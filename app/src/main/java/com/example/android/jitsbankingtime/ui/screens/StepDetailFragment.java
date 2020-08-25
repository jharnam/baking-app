package com.example.android.jitsbankingtime.ui.screens;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
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

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.databinding.FragmentStepDetailBinding;
import com.example.android.jitsbankingtime.model.Recipe;
import com.example.android.jitsbankingtime.model.Step;
import com.google.android.exoplayer2.C;
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
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import timber.log.Timber;

import static android.view.View.GONE;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.APP_NAME;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.SAVE_CURRENT_STEP_ID;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.SAVE_CURRENT_WINDOW;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.SAVE_PLAYBACK_POSITION;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.SAVE_PLAY_WHEN_READY;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.SAVE_RECIPE;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.SAVE_STEP;

public class StepDetailFragment extends Fragment {
    private FragmentStepDetailBinding binding;
    private Step step;
    private int currentStepId;
    private Recipe recipe;

    //TODO private ExoPlayer mediaPlayer;
    private SimpleExoPlayer mediaPlayer;
    private PlaybackStateListener playbackStateListener;

    // Tag for a MediaSessionCompat
    private static final String TAG = StepDetailFragment.class.getSimpleName();
    private static MediaSessionCompat mMediaSession;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private boolean containsVideo = false;
    private String videoUrl;
    private String thumbnailUrl;
    private PlaybackStateCompat.Builder mStateBuilder;

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

        populateRecipeAndStepDetails(savedInstanceState);

        //init Media Session for enabling external clients  to be able to interact with the ExoPlayer
        initializeMediaSession();


        //display the step id
        binding.textViewStepId.setText("Step " + String.valueOf(currentStepId) + "  of " + String.valueOf(recipe.getSteps().size() - 1));
        //get a reference to the textviewDescription in the fragment layout
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

    private void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this.getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    private void onNextButtonClick() {

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("JKM: next button was clicked, currentStepId is: %d", currentStepId);
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

    private void populateRecipeAndStepDetails(Bundle savedInstanceState) {
        //if the saved state exists, ....
        if (savedInstanceState != null) {
            //...retrieve the relevant data from there
            recipe = savedInstanceState.getParcelable(SAVE_RECIPE);
            step = savedInstanceState.getParcelable(SAVE_STEP);
            currentStepId = savedInstanceState.getInt(SAVE_CURRENT_STEP_ID);

            playbackPosition = savedInstanceState.getLong(SAVE_PLAYBACK_POSITION);
            currentWindow = savedInstanceState.getInt(SAVE_CURRENT_WINDOW);
            playWhenReady = savedInstanceState.getBoolean(SAVE_PLAY_WHEN_READY);

        } else {
            //...get it from the activity
            if (recipe == null) {
                recipe = ((StepDetailActivity) Objects.requireNonNull(getActivity())).getRecipe();
            }
            if (step == null) {
                step = ((StepDetailActivity) Objects.requireNonNull(getActivity())).getStep();
                currentStepId = step.getId();

            }
            // Clear the start position
            currentWindow = C.INDEX_UNSET;
            playbackPosition = C.TIME_UNSET;
            playWhenReady = true;

        }

        videoUrl = step.getVideoURL();
        thumbnailUrl = step.getThumbnailURL();

        //whether steps was null or not
        //do...?
        findIfWeHaveAVideo();

    }

    private void findIfWeHaveAVideo() {
        Timber.d("checking findIfWeHaveAVideo, stepid: %d", currentStepId);
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
        Timber.d("initializePlayer, step id: %d", currentStepId);
        if (containsVideo) {
            Timber.d("containsVideo: url is: %s", videoUrl);
            if (mediaPlayer == null) {
                Timber.d("mediaPlayer is null");
                // Create an instance of the ExoPlayer.
                DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(this.getContext());
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mediaPlayer = ExoPlayerFactory.newSimpleInstance(
                        (RenderersFactory) defaultRenderersFactory, trackSelector, loadControl);

                mediaPlayer.addAnalyticsListener(new EventLogger(null));

                binding.playerView.setPlayer(mediaPlayer);

                //TODO
            }

            //JKM
            mediaPlayer.setPlayWhenReady(playWhenReady);


            // Set the ExoPlayer.EventListener to this fragment.
            mediaPlayer.addListener(playbackStateListener);

            // Prepare the MediaSource.
            Uri mediaUri = Uri.parse(videoUrl);
            String userAgent = Util.getUserAgent(this.getContext(), APP_NAME);

            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(mediaUri);

            //if we had previously save the player position, we should maintain that
            boolean seekToStoredPosition = (currentWindow != C.INDEX_UNSET);
            if (seekToStoredPosition) {
                Timber.d("there was a previously stored player position");
                mediaPlayer.seekTo(currentWindow, playbackPosition);
            }


            mediaPlayer.prepare(mediaSource, !seekToStoredPosition, false);
            //JKM mediaPlayer.setPlayWhenReady(true);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mediaPlayer != null) {
            retrieveCurrentPlayerPosition();
            mediaPlayer.removeListener(playbackStateListener);

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

    /**
     * Retrieves and saves in the fragment the current state of the player
     */
    private void retrieveCurrentPlayerPosition() {
        if (mediaPlayer != null) {
            playbackPosition = mediaPlayer.getCurrentPosition();
            currentWindow = mediaPlayer.getCurrentWindowIndex();
            playWhenReady = mediaPlayer.getPlayWhenReady();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);


        //Store in the bundle what we will need
        outState.putParcelable(SAVE_STEP, step);
        outState.putInt(SAVE_CURRENT_STEP_ID, currentStepId);

        outState.putParcelable(SAVE_RECIPE, recipe);

        //Store in the fragment, the exoplayer positions
        retrieveCurrentPlayerPosition();

        //update the bundle with the latest exoplayer positions
        outState.putLong(SAVE_PLAYBACK_POSITION, playbackPosition);
        outState.putInt(SAVE_CURRENT_WINDOW, currentWindow);
        outState.putBoolean(SAVE_PLAY_WHEN_READY, playWhenReady);

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
            Timber.d("JKM inside onPlayerStateChanged, playWhenReady is %b, playbackState is: %d", playWhenReady, playbackState);
            if ((playbackState == Player.STATE_READY) && playWhenReady) {
                mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        mediaPlayer.getCurrentPosition(), 1f);
            } else if ((playbackState == Player.STATE_READY)) {
                mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        mediaPlayer.getCurrentPosition(), 1f);
            }
            mMediaSession.setPlaybackState(mStateBuilder.build());
            //showNotification(mStateBuilder.build());

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

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mediaPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mediaPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onRewind() {
            //mediaPlayer.seekTo((mediaPlayer.getCurrentPosition() - REWIND_FAST_FORWARD_INCREMENT), 0);
        }
    }
}
