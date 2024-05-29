package pdf.reader.pdfviewer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import pdf.reader.pdfviewer.R;
import pdf.reader.pdfviewer.databinding.RowPdffileDataBinding;
import pdf.reader.pdfviewer.listener.OnLongItemClick;
import pdf.reader.pdfviewer.listener.OnRecyclerItemClick;
import pdf.reader.pdfviewer.listener.PdfDataGetListener;
import pdf.reader.pdfviewer.model.PdfFileModel;
import pdf.reader.pdfviewer.utility.AppConstants;
import java.util.List;

public class FavFileAdapter extends RecyclerView.Adapter {
    Context context;
    List<PdfFileModel> favoriteList;
    List<PdfFileModel> multiSelectList;
    OnLongItemClick onLongItemClick;
    OnRecyclerItemClick onRecyclerItemClick;
    PdfDataGetListener pdfDataGetListener;
    List<PdfFileModel> pdfFileModelList;
    public boolean visible = false;

    public FavFileAdapter(Context context2, List<PdfFileModel> list, PdfDataGetListener pdfDataGetListener2, List<PdfFileModel> list2, OnRecyclerItemClick onRecyclerItemClick2, OnLongItemClick onLongItemClick2) {
        this.context = context2;
        this.pdfFileModelList = list;
        this.pdfDataGetListener = pdfDataGetListener2;
        this.onRecyclerItemClick = onRecyclerItemClick2;
        this.multiSelectList = list2;
        this.onLongItemClick = onLongItemClick2;
    }

    public class MyView extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowPdffileDataBinding binding;

        public MyView(View view) {
            super(view);
            RowPdffileDataBinding rowPdffileDataBinding = (RowPdffileDataBinding) DataBindingUtil.bind(view);
            this.binding = rowPdffileDataBinding;
            rowPdffileDataBinding.FrmMenu.setOnClickListener(this);
            this.binding.rootlayout1.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    if (visible) {
                        if (multiSelectList.contains(pdfFileModelList.get(MyView.this.getAdapterPosition()))) {
                            pdfFileModelList.get(MyView.this.getAdapterPosition()).setSelected(false);
                            MyView.this.binding.check.setChecked(false);
                            MyView.this.binding.rootlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        } else {
                            pdfFileModelList.get(MyView.this.getAdapterPosition()).setSelected(true);
                            MyView.this.binding.check.setChecked(true);
                            MyView.this.binding.rootlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.card_selected));
                        }
                        onRecyclerItemClick.onClick(pdfFileModelList.get(MyView.this.getAdapterPosition()), 1, MyView.this.getAdapterPosition());
                        return;
                    }
                    pdfDataGetListener.onPdfPickerOperation(pdfFileModelList.get(MyView.this.getAdapterPosition()), AppConstants.PDF_VIEWER);
                }
            });
            this.binding.rootlayout1.setOnLongClickListener(new View.OnLongClickListener() {

                public boolean onLongClick(View view) {
                    visible = true;
                    onLongItemClick.selectLongClick(MyView.this.getAdapterPosition());
                    return false;
                }
            });
        }

        public void onClick(View view) {
            if (view.getId() == R.id.Frm_menu) {
                onRecyclerItemClick.onClick(pdfFileModelList.get(getAdapterPosition()), 1, getAdapterPosition());
            }
        }
    }

    @Override 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyView(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_pdffile_data, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        MyView myView = (MyView) viewHolder;
        int i2 = 0;
        myView.binding.fav.setVisibility(View.VISIBLE);
        if (this.multiSelectList.contains(this.pdfFileModelList.get(i))) {
            myView.binding.check.setChecked(true);
            myView.binding.rootlayout.setBackgroundColor(ContextCompat.getColor(this.context, R.color.card_selected));
        } else {
            myView.binding.check.setChecked(false);
            myView.binding.rootlayout.setBackgroundColor(ContextCompat.getColor(this.context, R.color.white));
        }
        myView.binding.FrmCheckbox.setVisibility(this.visible ? 0 : 8);
        FrameLayout frameLayout = myView.binding.FrmMenu;
        if (this.visible) {
            i2 = 8;
        }
        frameLayout.setVisibility(i2);
        myView.binding.title.setText(pdfFileModelList.get(i).getFilename());
        myView.binding.date.setText(pdfFileModelList.get(i).getFormateData());
        myView.binding.size.setText(pdfFileModelList.get(i).getFileSizeString());
        myView.binding.executePendingBindings();
    }

    @Override 
    public int getItemCount() {
        return this.pdfFileModelList.size();
    }

    public List<PdfFileModel> getList() {
        return this.pdfFileModelList;
    }

    public void setData(List<PdfFileModel> list) {
        this.pdfFileModelList = list;
    }
}
