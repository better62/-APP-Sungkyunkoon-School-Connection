package com.example.googleloginexample.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.googleloginexample.mento_list.MentoSelectActivity;
import com.example.googleloginexample.mento_list.Mento_info;
import com.example.googleloginexample.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MentoAdapter extends RecyclerView.Adapter<MentoAdapter.MentoViewHolder> {

    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Mento_info> arrayList;
    private Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ImageButton fvrt_btn;
    DatabaseReference favoriteref;
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    public MentoAdapter(ArrayList<Mento_info> arrayList,Context context) {
        this.arrayList = arrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public MentoAdapter.MentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //실제 리스트가 어댑터와 연결된 다음에, 여기서 최초로 뷰홀더 생성
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mento,parent,false);
        MentoViewHolder holder=new MentoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MentoAdapter.MentoViewHolder holder, int position) {
    //실제적으로 매칭 시작


        holder.tv_id.setText(arrayList.get(position).getId());
        holder.tv_info.setText(arrayList.get(position).getInfo());

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference rootRef = firebaseStorage.getReference();
        StorageReference imgRef = rootRef.child(arrayList.get(position).getProfile());
        imgRef = rootRef.child("profile/" + arrayList.get(position).getProfile());
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.iv_img);
            }
        });

    }

    @Override
    public int getItemCount() {
        //삼항 연산자
        return (arrayList !=null?arrayList.size():0);
    }

    public class MentoViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img;
        TextView tv_id;
        TextView tv_info;



        public MentoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_img=itemView.findViewById(R.id.iv_img);
            this.tv_id=itemView.findViewById(R.id.tv_id);
            this.tv_info=itemView.findViewById(R.id.tv_info);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int current_position=getAdapterPosition();
                    if(current_position!=RecyclerView.NO_POSITION){
                        Mento_info mentoInfo=arrayList.get(current_position);
                        Intent intent=new Intent(context, MentoSelectActivity.class);
                        intent.putExtra("mentoInfo", mentoInfo);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
