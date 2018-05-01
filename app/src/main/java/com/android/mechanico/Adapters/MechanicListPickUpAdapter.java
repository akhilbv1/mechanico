package com.android.mechanico.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mechanico.FirebaseTablesPojo.MechanicRegistrationFirebaseTable;
import com.android.mechanico.R;

import java.util.List;

/**
 * Created by anuaki on 4/28/2018.
 */

public class MechanicListPickUpAdapter extends RecyclerView.Adapter<MechanicListPickUpAdapter.ViewHolder> {

    private List<MechanicRegistrationFirebaseTable> mechList;

    private onMechPickUpItemClickListener listener;


    public  interface onMechPickUpItemClickListener{
        void onClickMechPickUpItem(MechanicRegistrationFirebaseTable mech);
    }

    public MechanicListPickUpAdapter(List<MechanicRegistrationFirebaseTable> mechList,onMechPickUpItemClickListener listener){
        this.mechList = mechList;
        this.listener = listener;
    }

    public void refreshList(List<MechanicRegistrationFirebaseTable> mechList,onMechPickUpItemClickListener listener){
        this.mechList = mechList;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public MechanicListPickUpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_activity_mech_list_pickup,parent,false);
        return new MechanicListPickUpAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(MechanicListPickUpAdapter.ViewHolder holder, int position) {
      holder.updateUi(position);
    }

    @Override
    public int getItemCount() {
        return mechList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvMechName,tvMechNum;
        private MechanicRegistrationFirebaseTable mechanic;
        public ViewHolder(View itemView) {
            super(itemView);
            tvMechName = itemView.findViewById(R.id.tvMechName);
            tvMechNum = itemView.findViewById(R.id.tvMechNumber);
            itemView.setOnClickListener(this);
        }

        private void updateUi(int position)
        {
            mechanic = mechList.get(position);

            tvMechName.setText("Mechanic Name:- "+mechanic.getUsername());
            tvMechNum.setText("Contact Number:- "+mechanic.getMobileNumber());


        }

        @Override
        public void onClick(View view) {
            listener.onClickMechPickUpItem(mechanic);
        }
    }
}
