package com.example.android.jitsbankingtime.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.jitsbankingtime.R;
import com.example.android.jitsbankingtime.databinding.StepRowItemBinding;
import com.example.android.jitsbankingtime.model.Step;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepsViewHolder> {
    /**
     * The interface that receives onClick messages.
     */
    public interface StepAdapterOnClickHandler {
        void onClick (Step step);
    }

    private List<Step> stepsList = new ArrayList<>();
    private final StepsListAdapter.StepAdapterOnClickHandler clickHandler;


    public StepsListAdapter(StepAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public StepsListAdapter.StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        StepRowItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.step_row_item, parent, false);
        return new StepsListAdapter.StepsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsListAdapter.StepsViewHolder holder, int position) {
        Step currentStep = stepsList.get(position);
        Timber.d("currentStep id: %d is: %s", currentStep.getId(), currentStep.getShortDescription());

        holder.bind(currentStep);

    }

    @Override
    public int getItemCount() {
        if (stepsList !=null) {
            return stepsList.size();
        }
        //Error case
        Timber.e("Step list does not exist, or is empty");
        return 0;
    }

    public void setStepsList (List<Step> stepsListForAdapter) {
        Timber.d("inside setStepsList");
        //TODO - check if the following addAll is better
        //stepsList.addAll(stepsListForAdapter);
        //TODO - test this - update MainActivity to remove the clearList call from there
        //stepsList.clear();
        stepsList = stepsListForAdapter;
        notifyDataSetChanged();
        Timber.d("we have %d steps", stepsList.size());
    }

    /* Clean all elements of the recycler */
    public void clearList() {
        Timber.d("clearing the list");
        stepsList.clear();
        notifyDataSetChanged();
    }


    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // This field is used for data binding
        private StepRowItemBinding binding;

        public StepsViewHolder(@NonNull StepRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(this);
        }


        /**
         * This method takes a Step object and uses that recipe to display
         * the appropriate information within a list item.
         **/
        private void bind(Step step) {
            if (step == null)
            {
                Timber.e("Step is null :(");
                return;
            }

            binding.tvStepId.setText(String.valueOf(step.getId()));
            binding.tvStepShortDescription.setText(step.getShortDescription());
            //TODO show video or thumbnail
        }

        /**
         * This gets called by the child views during a click.
         * @param v The View that was clicked
         **/
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Step currentStep = stepsList.get(adapterPosition);
            clickHandler.onClick(currentStep);

        }
    }
}
