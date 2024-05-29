package pdf.reader.pdfviewer.fragment;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import pdf.reader.pdfviewer.R;
import pdf.reader.pdfviewer.activity.MainActivity;
import pdf.reader.pdfviewer.activity.PdfViewer;
import pdf.reader.pdfviewer.adapter.FavFileAdapter;
import pdf.reader.pdfviewer.baseClass.BaseFragmentBinding;
import pdf.reader.pdfviewer.databinding.DialogDeleteBinding;
import pdf.reader.pdfviewer.databinding.DialogFileInfoBinding;
import pdf.reader.pdfviewer.databinding.DialogPasswordBinding;
import pdf.reader.pdfviewer.databinding.DialogSortBinding;
import pdf.reader.pdfviewer.databinding.FragmentFavFragmetBinding;
import pdf.reader.pdfviewer.listener.OnLongItemClick;
import pdf.reader.pdfviewer.listener.OnRecyclerItemClick;
import pdf.reader.pdfviewer.listener.PdfDataGetListener;
import pdf.reader.pdfviewer.model.PdfFileModel;
import pdf.reader.pdfviewer.utility.AppConstants;
import pdf.reader.pdfviewer.utility.AppPref;
import pdf.reader.pdfviewer.utility.BetterActivityResult;
import pdf.reader.pdfviewer.utility.FileResolver;
import pdf.reader.pdfviewer.utility.PdfFileUtils;
import pdf.reader.pdfviewer.utility.SimpleDividerItemDecoration;
import pdf.reader.pdfviewer.utility.Test;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class FavoriteFragment extends BaseFragmentBinding implements PdfDataGetListener {
    public static boolean filterFlag = false;
    public String OLD_SORT_TYPE = AppConstants.DATE_DESC;
    protected final BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);
    FragmentFavFragmetBinding binding;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetDialog bottomSheetDialogInfo;
    BottomSheetDialog dialogSort;
    DialogSortBinding dialogSortBinding;
    CompositeDisposable disposable = new CompositeDisposable();
    public FavFileAdapter favFileAdapter;
    List<PdfFileModel> favoriteList;
    String filterByText = "";
    public boolean isMultiSelect = false;
    public List<PdfFileModel> pdfFavList;
    List<PdfFileModel> tempList = new ArrayList();

    public void onClick(View view) {
    }

    @Override 
    public void onMergeOpeation() {
    }

   
    @Override 
    public void setOnClicks() {
    }

   
    @Override 
    public void setToolbar() {
    }


    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getArguments();
        setHasOptionsMenu(true);
    }

   
    @Override 
    public View getViewBinding() {
        return this.binding.getRoot();
    }

   
    @Override 
    public void setBinding(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this.binding = (FragmentFavFragmetBinding) DataBindingUtil.inflate(layoutInflater, R.layout.fragment_fav_fragmet, viewGroup, false);
    }

   
    @Override 
    public void initMethods() {
        this.favoriteList = new ArrayList();
        this.pdfFavList = new ArrayList();
        setAdapter();
        sortDialog();
        loadIsFavoriteList();
        listIsEmpty();
        sortingPref();
    }

    public void sortingPref() {
        this.pdfFavList = AppPref.getFavArrayList(getContext());
        if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.NAME_ASC)) {
            if (this.favFileAdapter != null) {
                Collections.sort(this.pdfFavList, PdfFileModel.Comparators.NAMEAes);
            }
            notifyAdapter(this.pdfFavList);
        } else if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.NAME_DESC)) {
            if (this.favFileAdapter != null) {
                Collections.sort(this.pdfFavList, PdfFileModel.Comparators.NAMEDes);
            }
            notifyAdapter(this.pdfFavList);
        } else if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.DATE_ASC)) {
            if (this.favFileAdapter != null) {
                Collections.sort(this.pdfFavList, PdfFileModel.Comparators.DATEAes);
            }
            notifyAdapter(this.pdfFavList);
        } else if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.DATE_DESC)) {
            if (this.favFileAdapter != null) {
                Collections.sort(this.pdfFavList, PdfFileModel.Comparators.DATEDes);
            }
            notifyAdapter(this.pdfFavList);
        } else if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.SIZE_ASC)) {
            if (this.favFileAdapter != null) {
                Collections.sort(this.pdfFavList, PdfFileModel.Comparators.SIZEAes);
            }
            notifyAdapter(this.pdfFavList);
        } else if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.SIZE_DESC)) {
            if (this.favFileAdapter != null) {
                Collections.sort(this.pdfFavList, PdfFileModel.Comparators.SIZEDes);
            }
            notifyAdapter(this.pdfFavList);
        }
        listIsEmpty();
    }

    private void setAdapter() {
        if (AppPref.getRecentFileList(getContext()) == null) {
            AppPref.saveRecentFileList(((MainActivity) this.context).allPdfFragment.recentFileList, getContext());
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        this.binding.pdfview.setLayoutManager(linearLayoutManager);
        FragmentActivity activity = getActivity();
        List<PdfFileModel> list = this.pdfFavList;
        MainActivity mainActivity = (MainActivity) this.context;
        this.favFileAdapter = new FavFileAdapter(activity, list, this, MainActivity.multiSelectList, new OnRecyclerItemClick() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass1 */

            @Override // pdf.reader.pdfviewer.listener.OnRecyclerItemClick
            public void onClick(PdfFileModel pdfFileModel, int i, int i2) {
                if (i != 1) {
                    return;
                }
                if (FavoriteFragment.this.isMultiSelect) {
                    FavoriteFragment.this.multiSelectedList(pdfFileModel);
                } else if (i == 1) {
                    FavoriteFragment.this.pdfOperationDialog(pdfFileModel, i2);
                }
            }
        }, new OnLongItemClick() {
            @Override
            public void selectLongClick(int i) {
                isMultiSelect = true;
                multiSelectedList(pdfFavList.get(i));
                setHasOptionsMenu(false);
                favFileAdapter.visible = true;
                ((MainActivity) context).setBottomBarVisible(isMultiSelect);
                ((MainActivity) context).setToolbarTitle(isMultiSelect);
                favFileAdapter.notifyDataSetChanged();
            }
        });
        this.binding.pdfview.addItemDecoration(new SimpleDividerItemDecoration(this.context));
        this.binding.pdfview.setAdapter(this.favFileAdapter);
        listIsEmpty();
    }



    @Override 
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_selection) {
            this.isMultiSelect = true;
            setHasOptionsMenu(false);
            this.favFileAdapter.visible = true;
            ((MainActivity) this.context).setBottomBarVisible(this.isMultiSelect);
            ((MainActivity) this.context).setRemoveIcon();
            ((MainActivity) this.context).setToolbarTitle(this.isMultiSelect);
            this.favFileAdapter.notifyDataSetChanged();
        } else if (itemId == R.id.action_sort) {
            this.dialogSort.show();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void multiSelectedList(PdfFileModel pdfFileModel) {
        if (this.isMultiSelect) {
            MainActivity mainActivity = (MainActivity) this.context;
            if (MainActivity.multiSelectList.contains(pdfFileModel)) {
                MainActivity mainActivity2 = (MainActivity) this.context;
                MainActivity.multiSelectList.remove(pdfFileModel);
            } else {
                MainActivity mainActivity3 = (MainActivity) this.context;
                MainActivity.multiSelectList.add(pdfFileModel);
            }
            ((MainActivity) this.context).setToolbarTitle(this.isMultiSelect);
        }
    }

    private void loadIsFavoriteList() {
        this.disposable.add(Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    ((MainActivity) context).isFavoriteList = AppPref.getFavArrayList(getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        }));
    }


    @Override 
    public void onResume() {
        Log.e("TAG", "onResume: ");
        super.onResume();
    }

    @Override 
    public void onDatagetListener(List<PdfFileModel> list) {
        Log.e("TAG", "onDatagetListener: ");
    }

    @Override 
    public void onPdfPickerOperation(PdfFileModel pdfFileModel, int i) {
        if (i == AppConstants.PDF_VIEWER) {
            pdfviewerIntent(pdfFileModel);
        } else {
            pdfOperationDialog(pdfFileModel, 0);
        }
    }

    private void pdfviewerIntent(PdfFileModel pdfFileModel) {
        Intent intent = new Intent(getActivity(), PdfViewer.class);
        intent.putExtra(AppConstants.PDF_FILE_MODEL, pdfFileModel);
        this.activityLauncher.launch(intent, new BetterActivityResult.OnActivityResult<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                int indexOf;
                Intent data = o.getData();
                if (data != null) {
                    PdfFileModel pdfFileModel = (PdfFileModel) data.getParcelableExtra("pdfModel");
                    if (!pdfFileModel.isFav() && (indexOf = pdfFavList.indexOf(pdfFileModel)) != -1) {
                        pdfFavList.remove(indexOf);
                        favFileAdapter.notifyItemRemoved(indexOf);
                    }
                }
            }
        });
    }

    public void pdfOperationDialog(final PdfFileModel pdfFileModel, int i) {
        this.bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.bottomSheetDialogTheme);
        this.bottomSheetDialog.setContentView(LayoutInflater.from(getContext()).inflate(R.layout.dialog_share, (CardView) getViewBinding().findViewById(R.id.card_main)));
        ((TextView) this.bottomSheetDialog.findViewById(R.id.title)).setText(pdfFileModel.getFilename());
        ((TextView) this.bottomSheetDialog.findViewById(R.id.date)).setText(Test.getDate(pdfFileModel.getLastmodified()));
        ((TextView) this.bottomSheetDialog.findViewById(R.id.size)).setText(PdfFileUtils.getFileSize(pdfFileModel.getFilesize()) + " ,");
        ((TextView) this.bottomSheetDialog.findViewById(R.id.bookmark)).setText("UnMark");
        this.bottomSheetDialog.findViewById(R.id.delete).setVisibility(View.GONE);
        this.bottomSheetDialog.findViewById(R.id.openFile).setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass2 */

            public void onClick(View view) {
                Intent intent = new Intent(FavoriteFragment.this.getActivity(), PdfViewer.class);
                intent.putExtra(AppConstants.PDF_FILE_MODEL, pdfFileModel);
                FavoriteFragment.this.startActivity(intent);
                FavoriteFragment.this.bottomSheetDialog.dismiss();
            }
        });
        this.bottomSheetDialog.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                setInfoDialog(pdfFileModel);
            }
        });
        this.bottomSheetDialog.findViewById(R.id.rename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                passwordOrRenameDialog(pdfFileModel);
            }
        });
        this.bottomSheetDialog.findViewById(R.id.favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                int indexOf2 = MainActivity.pdfFileModelList.indexOf(pdfFileModel);
                MainActivity.pdfFileModelList.get(indexOf2).setFav(false);
                pdfFileModel.setFav(false);
                if (filterFlag) {
                    List<PdfFileModel> list = tempList;
                    list.remove(list.indexOf(pdfFileModel));
                    List<PdfFileModel> list2 = pdfFavList;
                    list2.remove(list2.indexOf(pdfFileModel));
                    favFileAdapter.notifyItemRemoved(i);
                } else {
                    pdfFavList.remove(i);
                    favFileAdapter.notifyItemRemoved(i);
                }
                AppPref.saveFavArrayList(pdfFavList, getContext());
                listIsEmpty();
            }
        });
        bottomSheetDialog.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                shareFile(pdfFileModel.getFilepath());
            }
        });
        this.bottomSheetDialog.show();
    }


    private void setInfoDialog(PdfFileModel pdfFileModel) {
        DialogFileInfoBinding dialogFileInfoBinding = (DialogFileInfoBinding) DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_file_info, null, false);
        dialogFileInfoBinding.path.setText(pdfFileModel.getFilepath());
        dialogFileInfoBinding.size.setText(pdfFileModel.getFileSizeString());
        dialogFileInfoBinding.name.setText(pdfFileModel.getFilename());
        dialogFileInfoBinding.lastmodified.setText(pdfFileModel.getFormateData());
        BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(this.context, R.style.bottomSheetDialogTheme);
        this.bottomSheetDialogInfo = bottomSheetDialog2;
        bottomSheetDialog2.setContentView(dialogFileInfoBinding.getRoot());
        this.bottomSheetDialogInfo.show();
        dialogFileInfoBinding.close.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass3 */

            public void onClick(View view) {
                FavoriteFragment.this.bottomSheetDialogInfo.dismiss();
            }
        });
    }

    private void deleteDialog(final PdfFileModel pdfFileModel, final int i) {
        DialogDeleteBinding inflate = DialogDeleteBinding.inflate(LayoutInflater.from(this.context));
        final Dialog dialog = new Dialog(this.context, R.style.dialogTheme);
        dialog.setContentView(inflate.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        inflate.bottom.save.setText("Yes, Delete!");
        inflate.logo.cardlogo.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.card_logo));
        inflate.logo.img.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bottom_delete));
        inflate.bottom.cardCancel.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass4 */

            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        inflate.bottom.cardSave.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass5 */

            public void onClick(View view) {
                if (FavoriteFragment.this.isMultiSelect) {
                    int i = 0;
                    while (true) {
                        MainActivity mainActivity = (MainActivity) FavoriteFragment.this.context;
                        if (i >= MainActivity.multiSelectList.size()) {
                            break;
                        }
                        MainActivity mainActivity2 = (MainActivity) FavoriteFragment.this.context;
                        PdfFileModel pdfFileModel = MainActivity.multiSelectList.get(i);
                        File file = new File(pdfFileModel.getFilepath());
                        if (file.exists()) {
                            MainActivity mainActivity3 = (MainActivity) FavoriteFragment.this.context;
                            int indexOf = MainActivity.pdfFileModelList.indexOf(pdfFileModel);
                            MainActivity mainActivity4 = (MainActivity) FavoriteFragment.this.context;
                            MainActivity.pdfFileModelList.set(indexOf, pdfFileModel);
                            int indexOf3 = ((MainActivity) FavoriteFragment.this.context).favoriteFragment.pdfFavList.indexOf(pdfFileModel);
                            if (indexOf3 != -1) {
                                ((MainActivity) FavoriteFragment.this.context).favoriteFragment.pdfFavList.remove(indexOf3);
                            }
                            AppPref.saveFavArrayList(((MainActivity) FavoriteFragment.this.context).isFavoriteList, FavoriteFragment.this.getContext());
                            pdfFileModel.setFav(false);
                            FileResolver.delete(pdfFileModel.getFilepath(), FavoriteFragment.this.getActivity());
                            FavoriteFragment.this.deleteFile(pdfFileModel);
                            ((MainActivity) FavoriteFragment.this.getActivity()).deleteFiles(pdfFileModel);
                            AppConstants.refreshFiles(FavoriteFragment.this.context, file);
                            FavoriteFragment.this.favFileAdapter.getList().remove(pdfFileModel);
                            FavoriteFragment.this.favoriteList.remove(pdfFileModel);
                            FavoriteFragment.this.favFileAdapter.notifyItemRemoved(indexOf);
                            FavoriteFragment.this.listIsEmpty();
                        }
                        i++;
                    }
                    FavoriteFragment.this.isMultiSelect = false;
                    MainActivity mainActivity5 = (MainActivity) FavoriteFragment.this.context;
                    MainActivity.multiSelectList.clear();
                    ((MainActivity) FavoriteFragment.this.context).setFixTitlebar();
                } else {
                    File file2 = new File(pdfFileModel.getFilepath());
                    if (file2.exists()) {
                        FavoriteFragment.this.favFileAdapter.getList().remove(pdfFileModel);
                        FavoriteFragment.this.favoriteList.remove(pdfFileModel);
                        pdfFileModel.setFav(false);
                        FavoriteFragment.this.favFileAdapter.notifyItemRemoved(i);
                        AppConstants.refreshFiles(FavoriteFragment.this.context, file2);
                        FavoriteFragment.this.listIsEmpty();
                    }
                }
                dialog.dismiss();
            }
        });
    }

    private void deleteDialogOnlyRemove(final PdfFileModel pdfFileModel, final int i) {
        DialogDeleteBinding inflate = DialogDeleteBinding.inflate(LayoutInflater.from(this.context));
        final Dialog dialog = new Dialog(this.context, R.style.dialogTheme);
        dialog.setContentView(inflate.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        inflate.msgRemove.setVisibility(View.VISIBLE);
        inflate.msg.setVisibility(View.GONE);
        inflate.bottom.save.setText("Yes, Remove!");
        inflate.dtitle.setText("Remove");
        inflate.logo.cardlogo.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.card_logo));
        inflate.logo.img.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dialog_icon_remove));
        inflate.bottom.cardCancel.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass6 */

            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        inflate.bottom.cardSave.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass7 */

            public void onClick(View view) {
                if (FavoriteFragment.this.isMultiSelect) {
                    int i = 0;
                    while (true) {
                        MainActivity mainActivity = (MainActivity) FavoriteFragment.this.context;
                        if (i >= MainActivity.multiSelectList.size()) {
                            break;
                        }
                        MainActivity mainActivity2 = (MainActivity) FavoriteFragment.this.context;
                        PdfFileModel pdfFileModel = MainActivity.multiSelectList.get(i);
                        File file = new File(pdfFileModel.getFilepath());
                        if (file.exists()) {
                            pdfFileModel.setFav(false);
                            MainActivity mainActivity3 = (MainActivity) FavoriteFragment.this.context;
                            int indexOf = MainActivity.pdfFileModelList.indexOf(pdfFileModel);
                            MainActivity mainActivity4 = (MainActivity) FavoriteFragment.this.context;
                            MainActivity.pdfFileModelList.set(indexOf, pdfFileModel);
                            ((MainActivity) FavoriteFragment.this.context).isFavoriteList.remove(pdfFileModel);
                            AppPref.saveFavArrayList(((MainActivity) FavoriteFragment.this.context).isFavoriteList, FavoriteFragment.this.getContext());
                            FavoriteFragment.this.favFileAdapter.getList().remove(pdfFileModel);
                            FavoriteFragment.this.favoriteList.remove(pdfFileModel);
                            FavoriteFragment.this.pdfFavList.remove(pdfFileModel);
                            FavoriteFragment.this.favFileAdapter.notifyItemRemoved(i);
                            AppConstants.refreshFiles(FavoriteFragment.this.context, file);
                            FavoriteFragment.this.listIsEmpty();
                        }
                        i++;
                    }
                    FavoriteFragment.this.isMultiSelect = false;
                    MainActivity mainActivity5 = (MainActivity) FavoriteFragment.this.context;
                    MainActivity.multiSelectList.clear();
                    ((MainActivity) FavoriteFragment.this.context).setFixTitlebar();
                } else {
                    File file2 = new File(pdfFileModel.getFilepath());
                    if (file2.exists()) {
                        FavoriteFragment.this.favFileAdapter.getList().remove(pdfFileModel);
                        FavoriteFragment.this.favoriteList.remove(pdfFileModel);
                        FavoriteFragment.this.favFileAdapter.notifyItemRemoved(i);
                        AppConstants.refreshFiles(FavoriteFragment.this.context, file2);
                        FavoriteFragment.this.listIsEmpty();
                    }
                }
                dialog.dismiss();
            }
        });
    }

    private void passwordOrRenameDialog(final PdfFileModel pdfFileModel) {
        final DialogPasswordBinding inflate = DialogPasswordBinding.inflate(LayoutInflater.from(this.context));
        final Dialog dialog = new Dialog(this.context, R.style.dialogTheme);
        dialog.setContentView(inflate.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        inflate.bottom.cardCancel.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass8 */

            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        inflate.renamelayout.setVisibility(View.VISIBLE);
        inflate.passwordlayout.setVisibility(View.GONE);
        inflate.rename.setText(FilenameUtils.removeExtension(pdfFileModel.getFilename()));
        inflate.bottom.cardSave.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass9 */

            public void onClick(View view) {
                if (!inflate.rename.getText().toString().trim().isEmpty()) {
                    File file = new File(pdfFileModel.getFilepath());
                    File file2 = new File(file.getParent() + "/" + inflate.rename.getText().toString() + ".pdf");
                    if (!file2.exists()) {
                        file.renameTo(file2);
                        AppConstants.refreshFiles(FavoriteFragment.this.getActivity(), file2);
                        FavoriteFragment.this.renamePdfFile(pdfFileModel, file2);
                        ((MainActivity) FavoriteFragment.this.getActivity()).renameFileChange(pdfFileModel, file2);
                        dialog.dismiss();
                        return;
                    }
                    Toast.makeText(FavoriteFragment.this.getActivity(), "This type of file name pdf already available", 0).show();
                    return;
                }
                Toast.makeText(FavoriteFragment.this.getActivity(), "File Name not be empty", 0).show();
            }
        });
    }

    private void renamePdfFile(PdfFileModel pdfFileModel, File file) {
        MainActivity mainActivity = (MainActivity) this.context;
        List<PdfFileModel> list = MainActivity.pdfFileModelList;
        MainActivity mainActivity2 = (MainActivity) this.context;
        PdfFileModel pdfFileModel2 = list.get(MainActivity.pdfFileModelList.indexOf(pdfFileModel));
        pdfFileModel2.setFilepath(file.getPath());
        pdfFileModel2.setFilename(file.getName());
        pdfFileModel2.setFilesize(file.length());
        pdfFileModel2.setLastmodified(file.lastModified());
        int indexOf2 = this.pdfFavList.indexOf(pdfFileModel);
        if (indexOf2 != -1) {
            ((MainActivity) this.context).favoriteFragment.pdfFavList.set(indexOf2, pdfFileModel2);
        }
        AppPref.saveFavArrayList(((MainActivity) this.context).favoriteFragment.pdfFavList, getContext());
        ((MainActivity) this.context).allPdfFragment.sortingPref();
        ((MainActivity) this.context).favoriteFragment.sortingPref();
        if (filterFlag && !filterByAll(pdfFileModel2)) {
            this.favFileAdapter.getList().remove(pdfFileModel);
        }
        this.favFileAdapter.notifyDataSetChanged();
    }

   
    private void deleteFile(PdfFileModel pdfFileModel) {
        if (filterFlag) {
            this.favFileAdapter.getList().remove(pdfFileModel);
        }
        MainActivity mainActivity = (MainActivity) this.context;
        MainActivity.pdfFileModelList.remove(pdfFileModel);
        this.favFileAdapter.notifyDataSetChanged();
    }

    public void shareFile(String str) {
        if (this.isMultiSelect) {
            Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
            intent.setType("*/*");
            ArrayList<Uri> arrayList = new ArrayList<>();
            int i = 0;
            while (true) {
                MainActivity mainActivity = (MainActivity) this.context;
                if (i < MainActivity.multiSelectList.size()) {
                    MainActivity mainActivity2 = (MainActivity) this.context;
                    arrayList.add(FileProvider.getUriForFile(getContext(), "pdf.reader.pdfviewer.easyphotopicker.fileprovider", new File(MainActivity.multiSelectList.get(i).getFilepath())));
                    i++;
                } else {
                    intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
                    try {
                        startActivity(Intent.createChooser(intent, "Share File "));
                        return;
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Not abel to read file", 0).show();
                        Log.e("exception", "shareFile: " + e.getMessage());
                        return;
                    }
                }
            }
        } else {
            File file = new File(str);
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType("*/*");
            intent2.addFlags(1073741824);
            intent2.addFlags(1);
            intent2.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getActivity(), "pdf.reader.pdfviewer.easyphotopicker.fileprovider", file));
            try {
                startActivity(Intent.createChooser(intent2, "Share File "));
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(getActivity(), "Not abel to read file", 0).show();
            }
        }
    }

    @Override 
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.all_pdf_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconified(false);
        ((ImageView) searchView.findViewById(R.id.search_close_btn)).setColorFilter(-1, PorterDuff.Mode.SRC_ATOP);
        searchView.setSearchableInfo(((SearchManager) getActivity().getSystemService("search")).getSearchableInfo(getActivity().getComponentName()));
        search(searchView);
    }

    private void search(SearchView searchView) {
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setHint("Search");
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.white));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.white));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass10 */

            @Override 
            public boolean onClose() {
                return false;
            }
        });
        if (searchView != null) {
            try {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass11 */

                    @Override 
                    public boolean onQueryTextSubmit(String str) {
                        return false;
                    }

                    @Override 
                    public boolean onQueryTextChange(String str) {
                        FavoriteFragment.this.filter(str);
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void filter(String str) {
        if (!str.isEmpty()) {
            filterFlag = true;
            this.filterByText = str;
            this.tempList.clear();
            for (PdfFileModel pdfFileModel : this.pdfFavList) {
                if (filterByAll(pdfFileModel)) {
                    this.tempList.add(pdfFileModel);
                }
            }
            notifyAdapter(this.tempList);
            if (this.tempList.size() > 0) {
                this.binding.centerlayout.setVisibility(View.GONE);
            } else {
                this.binding.centerlayout.setVisibility(View.VISIBLE);
            }
        } else {
            filterFlag = false;
            this.filterByText = "";
            notifyAdapter(this.pdfFavList);
        }
    }

    private boolean filterByAll(PdfFileModel pdfFileModel) {
        return pdfFileModel.getFilename().toLowerCase().contains(this.filterByText.toLowerCase());
    }

    public void notifyAdapter(List<PdfFileModel> list) {
        this.favFileAdapter.setData(list);
        this.favFileAdapter.notifyDataSetChanged();
        listIsEmpty();
    }

    public void listIsEmpty() {
        if (this.pdfFavList.size() > 0) {
            this.binding.centerlayout.setVisibility(View.GONE);
        } else {
            this.binding.centerlayout.setVisibility(View.VISIBLE);
        }
    }

    @Override 
    public void onHiddenChanged(boolean z) {
        if (!z) {
            this.favFileAdapter.notifyDataSetChanged();
        }
        super.onHiddenChanged(z);
    }

    public void addAll() {
        if (((MainActivity) this.context).isSelectAll) {
            MainActivity mainActivity = (MainActivity) this.context;
            MainActivity.multiSelectList.clear();
            MainActivity mainActivity2 = (MainActivity) this.context;
            MainActivity.multiSelectList.addAll(this.pdfFavList);
            int i = 0;
            while (true) {
                MainActivity mainActivity3 = (MainActivity) this.context;
                if (i >= MainActivity.multiSelectList.size()) {
                    break;
                }
                MainActivity mainActivity4 = (MainActivity) this.context;
                MainActivity.multiSelectList.get(i).setSelected(true);
                i++;
            }
            ((MainActivity) this.context).setToolbarTitle(this.isMultiSelect);
        } else {
            MainActivity mainActivity5 = (MainActivity) this.context;
            MainActivity.multiSelectList.clear();
            ((MainActivity) this.context).setToolbarTitle(this.isMultiSelect);
        }
        this.favFileAdapter.notifyDataSetChanged();
    }

    public void deleteMultiFile() {
        deleteDialog(null, 0);
    }

    public void shareMultiFile() {
        shareFile("");
    }

    public void removeOnly() {
        deleteDialogOnlyRemove(null, 0);
    }

    private void sortDialog() {
        this.dialogSortBinding = (DialogSortBinding) DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_sort, null, false);
        BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(getContext(), R.style.bottomSheetDialogTheme);
        this.dialogSort = bottomSheetDialog2;
        bottomSheetDialog2.setContentView(this.dialogSortBinding.getRoot());
        sortIconVisible(this.dialogSortBinding.sort4);
        if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.NAME_ASC)) {
            sortIconVisible(this.dialogSortBinding.sort1);
        }
        if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.NAME_DESC)) {
            sortIconVisible(this.dialogSortBinding.sort2);
        }
        if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.DATE_ASC)) {
            sortIconVisible(this.dialogSortBinding.sort3);
        }
        if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.DATE_DESC)) {
            sortIconVisible(this.dialogSortBinding.sort4);
        }
        if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.SIZE_ASC)) {
            sortIconVisible(this.dialogSortBinding.sort5);
        }
        if (AppPref.getSortTypeFAV(getContext()).equalsIgnoreCase(AppConstants.SIZE_DESC)) {
            sortIconVisible(this.dialogSortBinding.sort6);
        }
        this.dialogSortBinding.llNameAsc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass12 */

            public void onClick(View view) {
                FavoriteFragment.this.OLD_SORT_TYPE = AppConstants.NAME_ASC;
                FavoriteFragment favoriteFragment = FavoriteFragment.this;
                favoriteFragment.sortIconVisible(favoriteFragment.dialogSortBinding.sort1);
            }
        });
        this.dialogSortBinding.llNameDesc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass13 */

            public void onClick(View view) {
                FavoriteFragment.this.OLD_SORT_TYPE = AppConstants.NAME_DESC;
                FavoriteFragment favoriteFragment = FavoriteFragment.this;
                favoriteFragment.sortIconVisible(favoriteFragment.dialogSortBinding.sort2);
            }
        });
        this.dialogSortBinding.llDateAsc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass14 */

            public void onClick(View view) {
                FavoriteFragment.this.OLD_SORT_TYPE = AppConstants.DATE_ASC;
                FavoriteFragment favoriteFragment = FavoriteFragment.this;
                favoriteFragment.sortIconVisible(favoriteFragment.dialogSortBinding.sort3);
            }
        });
        this.dialogSortBinding.llDateDesc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass15 */

            public void onClick(View view) {
                FavoriteFragment.this.OLD_SORT_TYPE = AppConstants.DATE_DESC;
                FavoriteFragment favoriteFragment = FavoriteFragment.this;
                favoriteFragment.sortIconVisible(favoriteFragment.dialogSortBinding.sort4);
            }
        });
        this.dialogSortBinding.llSizeAsc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass16 */

            public void onClick(View view) {
                FavoriteFragment.this.OLD_SORT_TYPE = AppConstants.SIZE_ASC;
                FavoriteFragment favoriteFragment = FavoriteFragment.this;
                favoriteFragment.sortIconVisible(favoriteFragment.dialogSortBinding.sort5);
            }
        });
        this.dialogSortBinding.llSizeDesc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass17 */

            public void onClick(View view) {
                FavoriteFragment.this.OLD_SORT_TYPE = AppConstants.SIZE_DESC;
                FavoriteFragment favoriteFragment = FavoriteFragment.this;
                favoriteFragment.sortIconVisible(favoriteFragment.dialogSortBinding.sort6);
            }
        });
        this.dialogSortBinding.bottom.cardSave.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.FavoriteFragment.AnonymousClass18 */

            public void onClick(View view) {
                if (FavoriteFragment.this.OLD_SORT_TYPE.equalsIgnoreCase(AppConstants.DATE_ASC)) {
                    AppPref.setSortTypeFAV(FavoriteFragment.this.getContext(), AppConstants.DATE_ASC);
                    if (FavoriteFragment.this.favFileAdapter != null && FavoriteFragment.this.pdfFavList.size() > 0) {
                        Collections.sort(FavoriteFragment.this.pdfFavList, PdfFileModel.Comparators.DATEAes);
                        FavoriteFragment favoriteFragment = FavoriteFragment.this;
                        favoriteFragment.notifyAdapter(favoriteFragment.pdfFavList);
                    }
                } else if (FavoriteFragment.this.OLD_SORT_TYPE.equalsIgnoreCase(AppConstants.DATE_DESC)) {
                    AppPref.setSortTypeFAV(FavoriteFragment.this.getContext(), AppConstants.DATE_DESC);
                    if (FavoriteFragment.this.favFileAdapter != null && FavoriteFragment.this.pdfFavList.size() > 0) {
                        Collections.sort(FavoriteFragment.this.pdfFavList, PdfFileModel.Comparators.DATEDes);
                        FavoriteFragment favoriteFragment2 = FavoriteFragment.this;
                        favoriteFragment2.notifyAdapter(favoriteFragment2.pdfFavList);
                    }
                } else if (FavoriteFragment.this.OLD_SORT_TYPE.equalsIgnoreCase(AppConstants.NAME_ASC)) {
                    AppPref.setSortTypeFAV(FavoriteFragment.this.getContext(), AppConstants.NAME_ASC);
                    if (FavoriteFragment.this.favFileAdapter != null && FavoriteFragment.this.pdfFavList.size() > 0) {
                        Collections.sort(FavoriteFragment.this.pdfFavList, PdfFileModel.Comparators.NAMEAes);
                        FavoriteFragment favoriteFragment3 = FavoriteFragment.this;
                        favoriteFragment3.notifyAdapter(favoriteFragment3.pdfFavList);
                    }
                } else if (FavoriteFragment.this.OLD_SORT_TYPE.equalsIgnoreCase(AppConstants.NAME_DESC)) {
                    AppPref.setSortTypeFAV(FavoriteFragment.this.getContext(), AppConstants.NAME_DESC);
                    if (FavoriteFragment.this.favFileAdapter != null && FavoriteFragment.this.pdfFavList.size() > 0) {
                        Collections.sort(FavoriteFragment.this.pdfFavList, PdfFileModel.Comparators.NAMEDes);
                        FavoriteFragment favoriteFragment4 = FavoriteFragment.this;
                        favoriteFragment4.notifyAdapter(favoriteFragment4.pdfFavList);
                    }
                } else if (FavoriteFragment.this.OLD_SORT_TYPE.equalsIgnoreCase(AppConstants.SIZE_ASC)) {
                    AppPref.setSortTypeFAV(FavoriteFragment.this.getContext(), AppConstants.SIZE_ASC);
                    if (FavoriteFragment.this.favFileAdapter != null && FavoriteFragment.this.pdfFavList.size() > 0) {
                        Collections.sort(FavoriteFragment.this.pdfFavList, PdfFileModel.Comparators.SIZEAes);
                        FavoriteFragment favoriteFragment5 = FavoriteFragment.this;
                        favoriteFragment5.notifyAdapter(favoriteFragment5.pdfFavList);
                    }
                } else {
                    AppPref.setSortTypeFAV(FavoriteFragment.this.getContext(), AppConstants.SIZE_DESC);
                    if (FavoriteFragment.this.favFileAdapter != null && FavoriteFragment.this.pdfFavList.size() > 0) {
                        Collections.sort(FavoriteFragment.this.pdfFavList, PdfFileModel.Comparators.SIZEDes);
                        FavoriteFragment favoriteFragment6 = FavoriteFragment.this;
                        favoriteFragment6.notifyAdapter(favoriteFragment6.pdfFavList);
                    }
                }
                FavoriteFragment.this.dialogSort.dismiss();
            }
        });
        this.dialogSortBinding.bottom.cardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.DATE_ASC)) {
                    if (favFileAdapter != null && pdfFavList.size() > 0) {
                        sortIconVisible(dialogSortBinding.sort3);
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.DATEAes);
                        favFileAdapter.notifyDataSetChanged();
                    }
                } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.DATE_DESC)) {
                    if (favFileAdapter != null && pdfFavList.size() > 0) {
                        sortIconVisible(dialogSortBinding.sort4);
                        Collections.sort(pdfFavList, PdfFileModel.Comparators.DATEDes);
                        favFileAdapter.notifyDataSetChanged();
                    }
                } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.NAME_ASC)) {
                    if (favFileAdapter != null && pdfFavList.size() > 0) {
                        sortIconVisible(dialogSortBinding.sort1);
                        Collections.sort(pdfFavList, PdfFileModel.Comparators.NAMEAes);
                        favFileAdapter.notifyDataSetChanged();
                    }
                } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.NAME_DESC)) {
                    if (favFileAdapter != null && pdfFavList.size() > 0) {
                        sortIconVisible(dialogSortBinding.sort2);
                        Collections.sort(pdfFavList, PdfFileModel.Comparators.NAMEDes);
                        favFileAdapter.notifyDataSetChanged();
                    }
                } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.SIZE_ASC)) {
                    if (favFileAdapter != null && pdfFavList.size() > 0) {
                        sortIconVisible(dialogSortBinding.sort5);
                        Collections.sort(pdfFavList, PdfFileModel.Comparators.SIZEAes);
                        favFileAdapter.notifyDataSetChanged();
                    }
                } else if (favFileAdapter != null && pdfFavList.size() > 0) {
                    sortIconVisible(dialogSortBinding.sort6);
                    Collections.sort(pdfFavList, PdfFileModel.Comparators.SIZEDes);
                    favFileAdapter.notifyDataSetChanged();
                }
                dialogSort.dismiss();
            }
        });
        listIsEmpty();
    }

    private void sortIconVisible(ImageView imageView) {
        this.dialogSortBinding.sort1.setVisibility(View.GONE);
        this.dialogSortBinding.sort2.setVisibility(View.GONE);
        this.dialogSortBinding.sort3.setVisibility(View.GONE);
        this.dialogSortBinding.sort4.setVisibility(View.GONE);
        this.dialogSortBinding.sort5.setVisibility(View.GONE);
        this.dialogSortBinding.sort6.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }
}
