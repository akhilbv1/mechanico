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
 * Created by anuaki on 4/27/2018.
 */

public class MechanicListAdapter extends RecyclerView.Adapter<MechanicListAdapter.ViewHolder> {

    private List<MechanicRegistrationFirebaseTable> mechList;

    private onMechItemClickListener listener;


  public  interface onMechItemClickListener{
        void onClickMechItem(MechanicRegistrationFirebaseTable mech);
    }

    public MechanicListAdapter(List<MechanicRegistrationFirebaseTable> mechList,onMechItemClickListener listener){
        this.mechList = mechList;
        this.listener = listener;
    }

    public void refreshList(List<MechanicRegistrationFirebaseTable> mechList,onMechItemClickListener listener){
        this.mechList = mechList;
        this.listener = listener;
        notifyDataSetChanged();
    }


    @Override
    public MechanicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_mechanics_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MechanicListAdapter.ViewHolder holder, int position) {
        holder.updateUi(position);
    }

    @Override
    public int getItemCount() {
        return mechList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mechName,mechLocation,mechNumber,mechLicenseNumber;
        MechanicRegistrationFirebaseTable mechanic;

        private ViewHolder(View itemView) {
            super(itemView);
            mechName = itemView.findViewById(R.id.tvMechName);
          //  mechLocation = itemView.findViewById(R.id.tvMechLocation);
            mechNumber = itemView.findViewById(R.id.tvMechNumber);
            mechLicenseNumber = itemView.findViewById(R.id.tvMechLicenseNumber);
            itemView.setOnClickListener(this);
        }

        private void updateUi(int position){
            mechanic  = mechList.get(position);
            mechName.setText("Mechanic Name:- "+mechanic.getUsername());
            mechNumber.setText("Mechanic Number:- "+mechanic.getMobileNumber());
          //  mechLocation.setText("Location:- "+mechanic.getLocation());
            mechLicenseNumber.setText("License Number:- "+mechanic.getLicenseNumber());
        }

        @Override
        public void onClick(View view) {
            listener.onClickMechItem(mechanic);
        }
    }
}
