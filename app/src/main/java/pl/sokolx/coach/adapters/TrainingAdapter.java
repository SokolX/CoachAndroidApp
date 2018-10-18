package pl.sokolx.coach.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pl.sokolx.coach.R;
import pl.sokolx.coach.models.TrainingModel;
import pl.sokolx.coach.utils.DateUtils;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.StepViewHolder> {

    private List<TrainingModel> stepList;
    private Context context;

    public TrainingAdapter(Context context, List<TrainingModel> stepList) {
        this.stepList = stepList;
        this.context = context;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_training, parent, false);
        StepViewHolder viewHolder = new StepViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StepViewHolder holder, int position) {

        TrainingModel step = stepList.get(position);

        holder.textViewNumberTraining.setText(String.format(context.getResources().getString(R.string.training_nr), step.getStep_id()));
        holder.textViewNumberStep.setText(
                String.format(context.getResources().getString(R.string.date_from_to),
                        DateUtils.parseDateToStringWithSeconds(step.getTimeStart()),
                                DateUtils.parseDateToStringWithSeconds(step.getTimeStop())));
        holder.textViewDateOfCounterStepEver.setText(String.format(context.getResources().getString(R.string.count_steps_out_of_steps), step.getStepCounter()));//+ step.stepCounterEver
        holder.textViewPercent.setText("" + ((step.getStepCounter() * 100)/10000)+ "%");
        String durationTraining = DateUtils.parseMilisecondToHMS((step.getTimeStop() - step.getTimeStart()));
        holder.textViewDuration.setText(String.format(context.getResources().getString(R.string.duration_time_string), durationTraining));
        holder.progressBarTraining.setMax(10000);
        holder.progressBarTraining.setProgress(step.getStepCounter());
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNumberStep, textViewDateOfCounterStepEver, textViewDuration, textViewNumberTraining, textViewPercent;
        ProgressBar progressBarTraining;
        public StepViewHolder(View itemView) {
            super(itemView);
            textViewPercent = (TextView) itemView.findViewById(R.id.textViewPercent);
            textViewNumberTraining = (TextView) itemView.findViewById(R.id.textViewNumberTraining);
            textViewNumberStep = (TextView) itemView.findViewById(R.id.textViewNumberStep);
            textViewDateOfCounterStepEver = (TextView) itemView.findViewById(R.id.textViewDateOfCounterStepEver);
            textViewDuration = (TextView) itemView.findViewById(R.id.textViewDuration);
            progressBarTraining = (ProgressBar) itemView.findViewById(R.id.progressBarOneTrening);//findViewById();
        }
    }

}
