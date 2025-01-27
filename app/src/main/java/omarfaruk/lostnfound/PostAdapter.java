package omarfaruk.lostnfound;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    private List<Post> posts;
    private Context context;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_post_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.itemNameView.setText(post.getItemName());
        holder.categoryView.setText(post.getCategory());
        holder.dateView.setText(post.getType().equals("found") ? "Found on: " + post.getDate() : "Lost on: " + post.getDate());

        if (post.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(post.getImage(), 0, post.getImage().length);
            holder.imageView.setImageBitmap(bitmap);
        } else {
            holder.imageView.setImageResource(R.drawable.noimage);
        }
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView itemNameView;
        public TextView categoryView;
        public TextView dateView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_show_item_image);
            itemNameView = itemView.findViewById(R.id.tv_show_item_name);
            categoryView = itemView.findViewById(R.id.tv_show_category);
            dateView = itemView.findViewById(R.id.tv_show_date_found);
        }
    }
}