package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleloginexample.R;

import java.util.ArrayList;

import models.Post2;

public class Post_Adapter extends RecyclerView.Adapter<Post_Adapter.PostViewHolder> {

    private ArrayList<Post2> arrayList;

    public Post_Adapter(ArrayList<Post2> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //뷰홀더를 만드는 메소드

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post2, parent, false);
        PostViewHolder holder = new PostViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.content.setText(arrayList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return (null!=arrayList ? arrayList.size() : 0);
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView content;

        public PostViewHolder(@NonNull View itemView) { //PostViewHolder 생성자
            super(itemView);
            this.title = itemView.findViewById(R.id.item_post_title);
            this.content = itemView.findViewById(R.id.item_post_content);
        }
    }
}
