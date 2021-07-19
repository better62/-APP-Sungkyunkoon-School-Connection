package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleloginexample.R;

import java.util.ArrayList;

import frag3.ItemActivity;
import models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Post> arrayList;
    private Context context;

    public PostAdapter(ArrayList<Post> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //뷰홀더를 만드는 메소드

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
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

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int current_position = getAdapterPosition(); //returns  the Adapter position of the item represented by this ViewHolder
                    if(current_position != RecyclerView.NO_POSITION) {
                        Post post = arrayList.get(current_position);
                        //arrayList = new ArrayList<>();
                        Intent intent = new Intent(context, ItemActivity.class);

                        intent.putExtra("post", post);
                        context.startActivity(intent);
                    }
               }
            });
        }


    }
}
