package pdf.reader.pdfviewer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import pdf.reader.pdfviewer.R;
import pdf.reader.pdfviewer.databinding.RowPagePreviewBinding;
import pdf.reader.pdfviewer.listener.ItemClick;
import java.util.List;

public class PagePreviewAdapter extends RecyclerView.Adapter<PagePreviewAdapter.AllImageHolder> {
    List<Bitmap> bitmapList;
    private Context context;
    ItemClick itemClick;
    public int selectedItemPosition = 0;

    public PagePreviewAdapter(Context context2, List<Bitmap> list, ItemClick itemClick2) {
        this.context = context2;
        this.bitmapList = list;
        this.itemClick = itemClick2;
    }

    @Override 
    public AllImageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AllImageHolder(LayoutInflater.from(this.context).inflate(R.layout.row_page_preview, (ViewGroup) null));
    }

    public void onBindViewHolder(AllImageHolder allImageHolder, int i) {
        int i2 = i + 1;
        try {
            Glide.with(this.context).asBitmap().load(this.bitmapList.get(i)).into(allImageHolder.binding.categoryImage);
            TextView textView = allImageHolder.binding.pageNo;
            textView.setText(i2 + "");
            if (i == this.selectedItemPosition) {
                allImageHolder.binding.view.setStrokeColor(ContextCompat.getColor(this.context, R.color.colorPrimary));
                allImageHolder.binding.llPage.setBackground(ContextCompat.getDrawable(this.context, R.drawable.corner_text_selected));
            } else {
                allImageHolder.binding.view.setStrokeColor(ContextCompat.getColor(this.context, R.color.card_preview_stroke));
                allImageHolder.binding.llPage.setBackground(ContextCompat.getDrawable(this.context, R.drawable.corner_text));
            }
            Log.e("TAG", "onBindViewHolder: bitmap ---->  " + i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override 
    public int getItemCount() {
        List<Bitmap> list = this.bitmapList;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class AllImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowPagePreviewBinding binding;

        public void onClick(View view) {
        }

        public AllImageHolder(View view) {
            super(view);
            this.binding = (RowPagePreviewBinding) DataBindingUtil.bind(view);
            view.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    int i = selectedItemPosition;
                    selectedItemPosition = AllImageHolder.this.getAdapterPosition();
                    notifyItemChanged(i);
                    notifyItemChanged(selectedItemPosition);
                    itemClick.onClick(AllImageHolder.this.getAdapterPosition());
                }
            });
        }
    }
}
