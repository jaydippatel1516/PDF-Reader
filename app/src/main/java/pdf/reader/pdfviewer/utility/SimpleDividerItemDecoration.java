package pdf.reader.pdfviewer.utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import pdf.reader.pdfviewer.R;

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private Drawable mDivider;

    public SimpleDividerItemDecoration(Context context2) {
        this.context = context2;
        this.mDivider = context2.getResources().getDrawable(R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        int dPToPixel = AppConstants.getDPToPixel(this.context, 69);
        int width = recyclerView.getWidth() - AppConstants.getDPToPixel(this.context, 0);
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            int bottom = childAt.getBottom() + ((RecyclerView.LayoutParams) childAt.getLayoutParams()).bottomMargin;
            this.mDivider.setBounds(dPToPixel, bottom, width, this.mDivider.getIntrinsicHeight() + bottom);
            this.mDivider.draw(canvas);
        }
    }
}
