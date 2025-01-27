package omarfaruk.lostnfound;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoundItemAdapter extends RecyclerView.Adapter<FoundItemAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public FoundItemAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCat, tvDate;
        ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_show_item_name);
            tvCat = itemView.findViewById(R.id.tv_show_category);
            tvDate = itemView.findViewById(R.id.tv_show_date_found);
            itemImage = itemView.findViewById(R.id.iv_show_item_image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_of_item_found, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {
            String name = mCursor.getString(mCursor.getColumnIndexOrThrow(DBhelper.col_item_name));
            String cat = mCursor.getString(mCursor.getColumnIndexOrThrow(DBhelper.col_cat));
            String date = mCursor.getString(mCursor.getColumnIndexOrThrow(DBhelper.col_date));
            String location = mCursor.getString(mCursor.getColumnIndexOrThrow(DBhelper.col_location));
            int post_id = mCursor.getInt(mCursor.getColumnIndexOrThrow(DBhelper.col_found_id));
            String description = mCursor.getString(mCursor.getColumnIndexOrThrow(DBhelper.col_description));
            byte[] imageBytes = mCursor.getBlob(mCursor.getColumnIndexOrThrow(DBhelper.col_image));

            holder.tvName.setText(name);
            holder.tvCat.setText(cat);
            holder.tvDate.setText(date);

            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                holder.itemImage.setImageBitmap(bitmap);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("item_name", name);
                intent.putExtra("item_description", description);
                intent.putExtra("item_category", cat);
                intent.putExtra("item_location", location);
                intent.putExtra("item_date", date);
                intent.putExtra("item_post_id", post_id);
                intent.putExtra("item_lost_id", 0);
                intent.putExtra("item_image", imageBytes);
                mContext.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }
}
