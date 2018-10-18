package pl.sokolx.coach.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import pl.sokolx.coach.R;
import pl.sokolx.coach.models.BmiModel;
import pl.sokolx.coach.utils.DateUtils;

public class BmiAdapter extends RecyclerView.Adapter<BmiAdapter.BmiViewHolder> {

    private List<BmiModel> bmiList;
    private Context context;

    public BmiAdapter(List<BmiModel> bmiList) {
        this.bmiList = bmiList;
    }


    @Override
    public BmiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_bmi, parent, false);
        BmiViewHolder bmiViewHolder = new BmiViewHolder(view);
        context = parent.getContext();

        return bmiViewHolder;
    }

    @Override
    public void onBindViewHolder(final BmiViewHolder holder, int position) {

        BmiModel bmi = bmiList.get(position);

        holder.textViewIconWeight.setText(String.valueOf(bmi.getMeasure_id()));
        holder.textViewDateMeasureBmi.setText(DateUtils.parseDateToString(bmi.getDateBmiMeasure()));
        holder.textViewMeasureBmi.setText(String.format(context.getResources().getString(R.string.bmi_value), bmi.getBmi()));
        holder.textViewWeightBmi.setText(String.format(context.getResources().getString(R.string.weight_value), bmi.getWeight()));

        if(bmi.getBmi() < 18.50) {
            holder.imageViewStar.setColorFilter(Color.RED);
        } else if(bmi.getBmi() >=18.50 && bmi.getBmi() <=24.99) {
            holder.imageViewStar.setColorFilter(Color.GREEN);
        } else if(bmi.getBmi() >=25.00) {
            holder.imageViewStar.setColorFilter(Color.YELLOW);
        }
    }

    @Override
    public int getItemCount() {
        return bmiList.size();
    }

    class BmiViewHolder extends RecyclerView.ViewHolder {

        TextView textViewIconWeight, textViewDateMeasureBmi, textViewMeasureBmi, textViewWeightBmi;
        ImageView imageViewStar;

        public BmiViewHolder(View itemView) {
            super(itemView);

            textViewIconWeight = (TextView) itemView.findViewById(R.id.textViewIconWeight);
            textViewDateMeasureBmi = (TextView) itemView.findViewById(R.id.textViewDateMeasureBmi);
            textViewMeasureBmi = (TextView) itemView.findViewById(R.id.textViewMeasureBmi);
            textViewWeightBmi = (TextView) itemView.findViewById(R.id.textViewWeightBmi);
            imageViewStar = (ImageView) itemView.findViewById(R.id.imageViewStar);
        }
    }
}
