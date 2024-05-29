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
import pdf.reader.pdfviewer.utility.AppPref;
import java.util.ArrayList;
import java.util.List;

public class PdfFileAdapter extends RecyclerView.Adapter {
    Context context;
    private List<PdfFileModel> isFavoriteList = new ArrayList();
    List<PdfFileModel> multiSelectList;
    OnLongItemClick onLongItemClick;
    OnRecyclerItemClick onRecyclerItemClick;
    PdfDataGetListener pdfDataGetListener;
    List<PdfFileModel> pdfFileModelList;
    List<PdfFileModel> temparraylist;
    public boolean visible = false;

    public PdfFileAdapter(Context context2, List<PdfFileModel> list, List<PdfFileModel> list2, List<PdfFileModel> list3, PdfDataGetListener pdfDataGetListener2, OnRecyclerItemClick onRecyclerItemClick2, OnLongItemClick onLongItemClick2) {
        this.context = context2;
        this.pdfFileModelList = list;
        this.temparraylist = list2;
        this.multiSelectList = list3;
        this.pdfDataGetListener = pdfDataGetListener2;
        this.onRecyclerItemClick = onRecyclerItemClick2;
        this.onLongItemClick = onLongItemClick2;
        this.isFavoriteList = AppPref.getFavArrayList(context2);
    }

    public void setIsFavoriteList(List<PdfFileModel> list) {
        this.isFavoriteList = list;
        notifyDataSetChanged();
    }

    public class MyView extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowPdffileDataBinding binding;

        public MyView(View view) {
            super(view);
            RowPdffileDataBinding rowPdffileDataBinding = (RowPdffileDataBinding) DataBindingUtil.bind(view);
            this.binding = rowPdffileDataBinding;
            rowPdffileDataBinding.FrmMenu.setOnClickListener(this);
            view.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    if (visible) {
                        if (multiSelectList.contains(pdfFileModelList.get(getAdapterPosition()))) {
                            pdfFileModelList.get(getAdapterPosition()).setSelected(false);
                            binding.check.setChecked(false);
                            binding.rootlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        } else {
                            pdfFileModelList.get(getAdapterPosition()).setSelected(true);
                            binding.check.setChecked(true);
                            binding.rootlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.card_selected));
                        }
                        onRecyclerItemClick.onClick(pdfFileModelList.get(getAdapterPosition()), 1, getAdapterPosition());
                        return;
                    }
                    pdfDataGetListener.onPdfPickerOperation(pdfFileModelList.get(getAdapterPosition()), AppConstants.PDF_VIEWER);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {

                public boolean onLongClick(View view) {
                    visible = true;
                    onLongItemClick.selectLongClick(getAdapterPosition());
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
        PdfFileModel pdfFileModel = this.pdfFileModelList.get(i);
        int indexOf = this.pdfFileModelList.indexOf(pdfFileModel);
        int i2 = 0;
        if (indexOf == -1) {
            ((MyView) viewHolder).binding.fav.setVisibility(View.GONE);
        } else if (this.pdfFileModelList.get(indexOf).isFav()) {
            ((MyView) viewHolder).binding.fav.setVisibility(View.VISIBLE);
        } else {
            ((MyView) viewHolder).binding.fav.setVisibility(View.GONE);
        }
        if (this.multiSelectList.contains(this.pdfFileModelList.get(i))) {
            MyView myView = (MyView) viewHolder;
            myView.binding.check.setChecked(true);
            myView.binding.rootlayout.setBackgroundColor(ContextCompat.getColor(this.context, R.color.card_selected));
        } else {
            MyView myView2 = (MyView) viewHolder;
            myView2.binding.check.setChecked(false);
            myView2.binding.rootlayout.setBackgroundColor(ContextCompat.getColor(this.context, R.color.white));
        }
        MyView myView3 = (MyView) viewHolder;
        myView3.binding.FrmCheckbox.setVisibility(this.visible ? 0 : 8);
        FrameLayout frameLayout = myView3.binding.FrmMenu;
        if (this.visible) {
            i2 = 8;
        }
        frameLayout.setVisibility(i2);
        myView3.binding.title.setText(pdfFileModelList.get(i).getFilename());
        myView3.binding.date.setText(pdfFileModelList.get(i).getFormateData());
        myView3.binding.size.setText(pdfFileModelList.get(i).getFileSizeString());
        myView3.binding.executePendingBindings();
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
