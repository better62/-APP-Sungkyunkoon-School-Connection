//CustomAdapter.java

package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.googleloginexample.R;

import java.util.ArrayList;

import models.Modify;

public class ModifyAdapter extends RecyclerView.Adapter<ModifyAdapter.modifyviewholder> {
    private ArrayList<Modify> arrayList;

    public ModifyAdapter(ArrayList<Modify> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public modifyviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_info, parent, false);
        modifyviewholder holder = new modifyviewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull modifyviewholder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_major.setText(arrayList.get(position).getMajor());
        holder.tv_spec1.setText(arrayList.get(position).getSpec1());
        holder.tv_spec2.setText(arrayList.get(position).getSpec2());
        holder.tv_spec3.setText(arrayList.get(position).getSpec3());
        holder.tv_phone.setText(arrayList.get(position).getPhone());
        holder.tv_account.setText(arrayList.get(position).getAccount());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class modifyviewholder extends RecyclerView.ViewHolder {

        private ImageView iv_profile;
        private TextView tv_name;
        private TextView tv_major;
        private TextView tv_spec1;
        private TextView tv_spec2;
        private TextView tv_spec3;
        private TextView tv_phone;
        private TextView tv_account;

        public modifyviewholder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.profile);
            this.tv_name = itemView.findViewById(R.id.name);
            this.tv_major = itemView.findViewById(R.id.major);
            this.tv_spec1 = itemView.findViewById(R.id.spec1);
            this.tv_spec2 = itemView.findViewById(R.id.spec2);
            this.tv_spec3 = itemView.findViewById(R.id.spec3);
            this.tv_phone = itemView.findViewById(R.id.phone);
            this.tv_account = itemView.findViewById(R.id.account);
        }
    }
}
