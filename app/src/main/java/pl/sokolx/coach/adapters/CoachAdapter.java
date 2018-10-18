package pl.sokolx.coach.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import pl.sokolx.coach.R;
import pl.sokolx.coach.models.CoachModel;

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.CoachViewHolder> {

    private List<CoachModel> coachModelList;

    public CoachAdapter(List<CoachModel> coachList) {
        this.coachModelList = coachList;
    }

    @Override
    public CoachViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CoachViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_coach_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(final CoachViewHolder holder, final int position) {

        final CoachModel coach = coachModelList.get(position);
        holder.textViewCoachName.setText(coach.getCoachName());

    }

    @Override
    public int getItemCount() {
        return coachModelList.size();
    }

    class CoachViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCoachName;

        public CoachViewHolder(View itemView) {
            super(itemView);

            textViewCoachName = (TextView) itemView.findViewById(R.id.textViewCoachName);

        }
    }

}
