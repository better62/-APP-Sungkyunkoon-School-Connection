package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleloginexample.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import models.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<Comment> arrayList;
    private EventListener<QuerySnapshot> context;

    public CommentAdapter(ArrayList<Comment> arrayList, EventListener<QuerySnapshot> context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_comment.setText(arrayList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return (null!=arrayList ? arrayList.size() : 0);
    }

   class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_comment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_comment = itemView.findViewById(R.id.tv_comment);
        }
    }
}