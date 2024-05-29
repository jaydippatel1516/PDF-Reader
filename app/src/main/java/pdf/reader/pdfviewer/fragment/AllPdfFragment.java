package pdf.reader.pdfviewer.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import pdf.reader.pdfviewer.R;
import pdf.reader.pdfviewer.activity.MainActivity;
import pdf.reader.pdfviewer.activity.PdfViewer;
import pdf.reader.pdfviewer.adapter.PdfFileAdapter;
import pdf.reader.pdfviewer.baseClass.BaseFragmentBinding;
import pdf.reader.pdfviewer.databinding.DialogDeleteBinding;
import pdf.reader.pdfviewer.databinding.DialogFileInfoBinding;
import pdf.reader.pdfviewer.databinding.DialogPasswordBinding;
import pdf.reader.pdfviewer.databinding.DialogSortBinding;
import pdf.reader.pdfviewer.databinding.FragmentAllPdfFragmetBinding;
import pdf.reader.pdfviewer.listener.OnLongItemClick;
import pdf.reader.pdfviewer.listener.OnRecyclerItemClick;
import pdf.reader.pdfviewer.listener.PdfDataGetListener;
import pdf.reader.pdfviewer.listener.ProgressListener;
import pdf.reader.pdfviewer.model.AllDirc;
import pdf.reader.pdfviewer.model.PdfFileModel;
import pdf.reader.pdfviewer.utility.AppConstants;
import pdf.reader.pdfviewer.utility.AppPref;
import pdf.reader.pdfviewer.utility.BetterActivityResult;
import pdf.reader.pdfviewer.utility.DialogUtils;
import pdf.reader.pdfviewer.utility.FileResolver;
import pdf.reader.pdfviewer.utility.FolderCreation;
import pdf.reader.pdfviewer.utility.PdfFileUtils;
import pdf.reader.pdfviewer.utility.SimpleDividerItemDecoration;
import pdf.reader.pdfviewer.utility.Test;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.cli.HelpFormatter;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class AllPdfFragment extends BaseFragmentBinding implements PdfDataGetListener {
    public static boolean filterFlag = false;
    public String OLD_SORT_TYPE = AppConstants.DATE_DESC;
    protected final BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);
    FragmentAllPdfFragmetBinding binding;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetDialog bottomSheetDialogInfo;
    creatingPDF creatingpdf;
    BottomSheetDialog dialogSort;
    DialogSortBinding dialogSortBinding;
    DialogUtils dialogUtils;
    CompositeDisposable disposable = new CompositeDisposable();
    PDDocument document;
    File f1;
    public ArrayList<String> filePathArrayList = new ArrayList<>();
    String filterByText = "";
    boolean isCanceled = false;
    public boolean isMultiSelect = false;
    File mergefilename;
    public PdfFileAdapter pdfFileAdapter;
    List<PdfFileModel> recentFileList;
    SearchView searchView;
    List<PdfFileModel> tempList;

    public void onClick(View view) {
    }

    @Override 
    public void onDatagetListener(List<PdfFileModel> list) {
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
        this.binding = (FragmentAllPdfFragmetBinding) DataBindingUtil.inflate(layoutInflater, R.layout.fragment_all_pdf_fragmet, viewGroup, false);
    }

   
    @Override 
    public void initMethods() {
        setAdapter();
        loadIsFavoriteList();
        this.recentFileList = new ArrayList();
        sortDialog();
    }

    public void sortingPref() {
        if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.NAME_ASC)) {
            if (this.pdfFileAdapter != null) {
                Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.NAMEAes);
            }
            notifyAdapter(MainActivity.pdfFileModelList);
        } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.NAME_DESC)) {
            if (this.pdfFileAdapter != null) {
                Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.NAMEDes);
            }
            notifyAdapter(MainActivity.pdfFileModelList);
        } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.DATE_ASC)) {
            if (this.pdfFileAdapter != null) {
                Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.DATEAes);
            }
            notifyAdapter(MainActivity.pdfFileModelList);
        } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.DATE_DESC)) {
            if (this.pdfFileAdapter != null) {
                Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.DATEDes);
            }
            notifyAdapter(MainActivity.pdfFileModelList);
        } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.SIZE_ASC)) {
            if (this.pdfFileAdapter != null) {
                Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.SIZEAes);
            }
            notifyAdapter(MainActivity.pdfFileModelList);
        } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.SIZE_DESC)) {
            if (this.pdfFileAdapter != null) {
                Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.SIZEDes);
            }
            notifyAdapter(MainActivity.pdfFileModelList);
        }
    }

    private void setAdapter() {
        this.tempList = new ArrayList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(1);
        this.binding.pdfview.setLayoutManager(linearLayoutManager);
        this.binding.pdfview.setNestedScrollingEnabled(true);
        FragmentActivity activity = getActivity();
        List<PdfFileModel> list = MainActivity.pdfFileModelList;
        List<PdfFileModel> list2 = this.tempList;
        this.pdfFileAdapter = new PdfFileAdapter(activity, list, list2, MainActivity.multiSelectList, this, new OnRecyclerItemClick() {

            @Override
            public void onClick(PdfFileModel pdfFileModel, int i, int i2) {
                if (AllPdfFragment.this.isMultiSelect) {
                    AllPdfFragment.this.multiSelectedList(pdfFileModel);
                } else if (i == 1 && !AllPdfFragment.this.isMultiSelect) {
                    AllPdfFragment.this.pdfOperationDialog(pdfFileModel, i2);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("onClick:  multiSelectedList ");
                sb.append(MainActivity.multiSelectList.size());
                Log.e("onclick", sb.toString());
            }
        }, new OnLongItemClick() {

            @Override
            public void selectLongClick(int i) {
                AllPdfFragment.this.isMultiSelect = true;
                AllPdfFragment.this.searchView.setVisibility(View.GONE);
                AllPdfFragment.this.setHasOptionsMenu(false);
                AllPdfFragment.this.pdfFileAdapter.visible = true;
                AllPdfFragment allPdfFragment = AllPdfFragment.this;
                allPdfFragment.multiSelectedList(MainActivity.pdfFileModelList.get(i));
                ((MainActivity) AllPdfFragment.this.context).setBottomBarVisible(AllPdfFragment.this.isMultiSelect);
                ((MainActivity) AllPdfFragment.this.context).setToolbarTitle(AllPdfFragment.this.isMultiSelect);
                AllPdfFragment.this.pdfFileAdapter.notifyDataSetChanged();
            }
        });
        this.binding.pdfview.addItemDecoration(new SimpleDividerItemDecoration(this.context));
        this.binding.pdfview.setAdapter(this.pdfFileAdapter);
    }

   
    private void multiSelectedList(PdfFileModel pdfFileModel) {
        if (this.isMultiSelect) {
            if (MainActivity.multiSelectList.contains(pdfFileModel)) {
                MainActivity.multiSelectList.remove(pdfFileModel);
            } else {
                MainActivity.multiSelectList.add(pdfFileModel);
            }
            ((MainActivity) this.context).setToolbarTitle(this.isMultiSelect);
        }
    }

    private void loadIsFavoriteList() {
        this.recentFileList = AppPref.getRecentFileList(getContext());
        if (AppPref.getRecentFileList(getContext()) == null) {
            AppPref.saveRecentFileList(this.recentFileList, getContext());
        }
    }

    @Override 
    public void onPdfPickerOperation(PdfFileModel pdfFileModel, int i) {
        if (i == AppConstants.PDF_VIEWER) {
            List<PdfFileModel> recentFileList2 = AppPref.getRecentFileList(getContext());
            this.recentFileList = recentFileList2;
            int indexOf = recentFileList2.indexOf(pdfFileModel);
            if (indexOf != -1) {
                this.recentFileList.remove(indexOf);
            }
            if (AppPref.getRecentFileList(getContext()) == null) {
                AppPref.saveRecentFileList(this.recentFileList, getContext());
            }
            this.recentFileList.add(pdfFileModel);
            Collections.sort(this.recentFileList, AllDirc.Comparators.DATE_MODIFIED_Des);
            AppPref.saveRecentFileList(this.recentFileList, getContext());
            pdfviewerIntent(pdfFileModel);
            return;
        }
        pdfOperationDialog(pdfFileModel, 0);
    }

    private void pdfviewerIntent(PdfFileModel pdfFileModel) {
        int indexOf = MainActivity.pdfFileModelList.indexOf(pdfFileModel);
        Intent intent = new Intent(getActivity(), PdfViewer.class);
        intent.putExtra(AppConstants.PDF_FILE_MODEL, pdfFileModel);
        if (indexOf != -1) {
            intent.putExtra("positionFav", indexOf);
        }
        this.activityLauncher.launch(intent, new BetterActivityResult.OnActivityResult<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                Intent data = o.getData();
                if (data != null) {
                    PdfFileModel pdfFileModel = (PdfFileModel) data.getParcelableExtra("pdfModel");
                    MainActivity.pdfFileModelList.set(MainActivity.pdfFileModelList.indexOf(pdfFileModel), pdfFileModel);
                    pdfFileAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void onAddPdf(PdfFileModel pdfFileModel) {
        MainActivity.pdfFileModelList.add(0, pdfFileModel);
        this.pdfFileAdapter.notifyDataSetChanged();
    }


    public void pdfOperationDialog(PdfFileModel pdfFileModel, int i) {
        this.bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.bottomSheetDialogTheme);
        this.bottomSheetDialog.setContentView(LayoutInflater.from(getContext()).inflate(R.layout.dialog_share, (CardView) getViewBinding().findViewById(R.id.card_main)));
        ((TextView) this.bottomSheetDialog.findViewById(R.id.title)).setText(pdfFileModel.getFilename());
        ((TextView) this.bottomSheetDialog.findViewById(R.id.date)).setText(Test.getDate(pdfFileModel.getLastmodified()));
        ((TextView) this.bottomSheetDialog.findViewById(R.id.size)).setText(PdfFileUtils.getFileSize(pdfFileModel.getFilesize()) + " ,");
        MainActivity mainActivity = (MainActivity) this.context;
        int indexOf = MainActivity.pdfFileModelList.indexOf(pdfFileModel);
        if (indexOf != -1) {
            MainActivity mainActivity2 = (MainActivity) this.context;
            if (MainActivity.pdfFileModelList.get(indexOf).isFav()) {
                ((TextView) this.bottomSheetDialog.findViewById(R.id.bookmark)).setText("UnMark");
            } else {
                ((TextView) this.bottomSheetDialog.findViewById(R.id.bookmark)).setText("BookMark");
            }
        }

        this.bottomSheetDialog.findViewById(R.id.rename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                passwordOrRenameDialog(pdfFileModel);
            }
        });
        this.bottomSheetDialog.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                setInfoDialog(pdfFileModel);
            }
        });
        this.bottomSheetDialog.findViewById(R.id.openFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (AppPref.getRecentFileList(getContext()) == null) {
                    AppPref.saveRecentFileList(recentFileList, getContext());
                }
                List<PdfFileModel> recentFileList2 = AppPref.getRecentFileList(getContext());
                recentFileList = recentFileList2;
                int indexOf = recentFileList2.indexOf(pdfFileModel);
                if (indexOf != -1) {
                    recentFileList.remove(indexOf);
                }
                recentFileList.add(pdfFileModel);
                Collections.sort(recentFileList, AllDirc.Comparators.DATE_MODIFIED_Des);
                AppPref.saveRecentFileList(recentFileList, getContext());
                pdfviewerIntent(pdfFileModel);
            }
        });
        this.bottomSheetDialog.findViewById(R.id.favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                MainActivity mainActivity = (MainActivity) context;
                int indexOf = MainActivity.pdfFileModelList.indexOf(pdfFileModel);
                int indexOf2 = recentFileList.indexOf(pdfFileModel);
                pdfFileModel.setFav(!pdfFileModel.isFav());
                MainActivity mainActivity2 = (MainActivity) context;
                MainActivity.pdfFileModelList.set(indexOf, pdfFileModel);
                if (filterFlag) {
                    List<PdfFileModel> list = tempList;
                    list.set(list.indexOf(pdfFileModel), pdfFileModel);
                    pdfFileAdapter.notifyItemChanged(indexOf);
                }
                pdfFileAdapter.setIsFavoriteList(AppPref.getFavArrayList(getContext()));
                if (pdfFileModel.isFav()) {
                    ((MainActivity) context).isFavoriteList.add(pdfFileModel);
                    ((MainActivity) context).favoriteFragment.pdfFavList.add(pdfFileModel);
                } else {
                    ((MainActivity) context).isFavoriteList.remove(pdfFileModel);
                    ((MainActivity) context).favoriteFragment.pdfFavList.remove(pdfFileModel);
                }
                if (indexOf2 != -1) {
                    recentFileList.set(indexOf2, pdfFileModel);
                }
                AppPref.saveFavArrayList(((MainActivity) context).favoriteFragment.pdfFavList, getContext());
                AppPref.saveRecentFileList(recentFileList, getContext());
                ((MainActivity) context).favoriteFragment.notifyAdapter(((MainActivity) context).favoriteFragment.pdfFavList);
            }
        });
        this.bottomSheetDialog.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                shareFile(pdfFileModel.getFilepath());
            }
        });
        this.bottomSheetDialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                deleteDialog(pdfFileModel, i);
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

            public void onClick(View view) {
                AllPdfFragment.this.bottomSheetDialogInfo.dismiss();
            }
        });
    }

    private void deleteDialog(final PdfFileModel pdfFileModel, int i) {
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
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass4 */

            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        inflate.bottom.cardSave.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass5 */

            public void onClick(View view) {
                if (AllPdfFragment.this.isMultiSelect) {
                    int i = 0;
                    while (true) {
                        MainActivity mainActivity = (MainActivity) AllPdfFragment.this.context;
                        if (i >= MainActivity.multiSelectList.size()) {
                            break;
                        }
                        MainActivity mainActivity2 = (MainActivity) AllPdfFragment.this.context;
                        PdfFileModel pdfFileModel = MainActivity.multiSelectList.get(i);
                        File file = new File(pdfFileModel.getFilepath());
                        if (file.exists() && file.delete()) {
                            int indexOf2 = ((MainActivity) AllPdfFragment.this.context).favoriteFragment.pdfFavList.indexOf(pdfFileModel);

                            if (indexOf2 != -1) {
                                ((MainActivity) AllPdfFragment.this.context).favoriteFragment.pdfFavList.remove(indexOf2);
                            }
                            FileResolver.delete(pdfFileModel.getFilepath(), AllPdfFragment.this.getActivity());
                            AllPdfFragment.this.deleteFile(pdfFileModel);
                            ((MainActivity) AllPdfFragment.this.getActivity()).deleteFiles(pdfFileModel);
                            AppConstants.refreshFiles(AllPdfFragment.this.getContext(), file);
                            AllPdfFragment.this.listIsEmpty();
                        }
                        i++;
                    }
                    AllPdfFragment.this.isMultiSelect = false;
                    MainActivity mainActivity3 = (MainActivity) AllPdfFragment.this.context;
                    MainActivity.multiSelectList.clear();
                    ((MainActivity) AllPdfFragment.this.context).setFixTitlebar();
                    AllPdfFragment.this.pdfFileAdapter.notifyDataSetChanged();
                    ((MainActivity) AllPdfFragment.this.context).favoriteFragment.favFileAdapter.notifyDataSetChanged();
                    AppPref.saveFavArrayList(((MainActivity) AllPdfFragment.this.context).favoriteFragment.pdfFavList, AllPdfFragment.this.getContext());
                } else {
                    File file2 = new File(pdfFileModel.getFilepath());
                    if (file2.exists() && file2.delete()) {

                        FileResolver.delete(pdfFileModel.getFilepath(), AllPdfFragment.this.getActivity());
                        AllPdfFragment.this.deleteFile(pdfFileModel);
                        int indexOf4 = ((MainActivity) AllPdfFragment.this.context).isFavoriteList.indexOf(pdfFileModel);
                        int indexOf5 = ((MainActivity) AllPdfFragment.this.context).favoriteFragment.pdfFavList.indexOf(pdfFileModel);
                        ((MainActivity) AllPdfFragment.this.getActivity()).deleteFiles(pdfFileModel);
                        if (indexOf4 != -1) {
                            ((MainActivity) AllPdfFragment.this.context).isFavoriteList.remove(indexOf4);
                        }
                        if (indexOf5 != -1) {
                            ((MainActivity) AllPdfFragment.this.context).favoriteFragment.pdfFavList.remove(indexOf5);
                        }
                        ((MainActivity) AllPdfFragment.this.context).favoriteFragment.favFileAdapter.notifyDataSetChanged();
                        AppPref.saveFavArrayList(((MainActivity) AllPdfFragment.this.context).isFavoriteList, AllPdfFragment.this.getContext());
                        AppConstants.refreshFiles(AllPdfFragment.this.getContext(), file2);
                        AllPdfFragment.this.listIsEmpty();
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
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass6 */

            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        inflate.renamelayout.setVisibility(View.VISIBLE);
        inflate.passwordlayout.setVisibility(View.GONE);
        inflate.rename.setText(FilenameUtils.removeExtension(pdfFileModel.getFilename()));
        inflate.logo.cardlogo.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.card_logo));
        inflate.logo.img.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bottom_rename));
        inflate.bottom.cardSave.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass7 */

            public void onClick(View view) {
                if (!inflate.rename.getText().toString().trim().isEmpty()) {
                    File file = new File(pdfFileModel.getFilepath());
                    File file2 = new File(file.getParent() + "/" + inflate.rename.getText().toString() + ".pdf");
                    if (!file2.exists()) {
                        file.renameTo(file2);
                        AppConstants.refreshFiles(AllPdfFragment.this.getActivity(), file2);
                        AllPdfFragment.this.renamePdfFile(pdfFileModel, file2);
                        dialog.dismiss();
                        return;
                    }
                    Toast.makeText(AllPdfFragment.this.getActivity(), "This type of file name pdf already available", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(AllPdfFragment.this.getActivity(), "File Name not be empty", 0).show();
            }
        });
    }

   
    private void renamePdfFile(PdfFileModel pdfFileModel, File file) {
        MainActivity mainActivity = (MainActivity) this.context;
        List<PdfFileModel> list = MainActivity.pdfFileModelList;
        MainActivity mainActivity2 = (MainActivity) this.context;
        PdfFileModel pdfFileModel2 = list.get(MainActivity.pdfFileModelList.indexOf(pdfFileModel));
        int indexOf2 = ((MainActivity) this.context).favoriteFragment.pdfFavList.indexOf(pdfFileModel2);
        pdfFileModel2.setFilepath(file.getPath());
        pdfFileModel2.setFilename(file.getName());
        pdfFileModel2.setFilesize(file.length());
        pdfFileModel2.setLastmodified(file.lastModified());
        MainActivity mainActivity3 = (MainActivity) this.context;
        List<PdfFileModel> list2 = MainActivity.pdfFileModelList;
        MainActivity mainActivity4 = (MainActivity) this.context;
        list2.set(MainActivity.pdfFileModelList.indexOf(pdfFileModel2), pdfFileModel2);
        if (indexOf2 != -1) {
            ((MainActivity) this.context).favoriteFragment.pdfFavList.set(indexOf2, pdfFileModel2);
        }
        AppPref.saveFavArrayList(((MainActivity) this.context).favoriteFragment.pdfFavList, getContext());
        ((MainActivity) this.context).allPdfFragment.sortingPref();
        ((MainActivity) this.context).favoriteFragment.sortingPref();
        if (filterFlag) {
            if (!filterByAll(pdfFileModel2)) {
                this.pdfFileAdapter.getList().remove(pdfFileModel);
            }
            filter(this.filterByText);
        }
        this.pdfFileAdapter.notifyDataSetChanged();
    }

   
    private void deleteFile(PdfFileModel pdfFileModel) {
        if (filterFlag) {
            this.pdfFileAdapter.getList().remove(pdfFileModel);
        }
        MainActivity mainActivity = (MainActivity) this.context;
        MainActivity.pdfFileModelList.remove(pdfFileModel);
        this.pdfFileAdapter.notifyDataSetChanged();
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
        SearchView searchView2 = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        this.searchView = searchView2;
        searchView2.setIconified(false);
        ((ImageView) this.searchView.findViewById(R.id.search_close_btn)).setColorFilter(-1, PorterDuff.Mode.SRC_ATOP);
        this.searchView.setSearchableInfo(((SearchManager) getActivity().getSystemService("search")).getSearchableInfo(getActivity().getComponentName()));
        search(this.searchView);
    }

    private void search(SearchView searchView2) {
        SearchView.SearchAutoComplete searchAutoComplete =  searchView2.findViewById(R.id.search_src_text);
        searchAutoComplete.setHint("Search");
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.white));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.white));
        searchView2.setOnCloseListener(new SearchView.OnCloseListener() {
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass8 */

            @Override 
            public boolean onClose() {
                return false;
            }
        });
        if (searchView2 != null) {
            try {
                searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass9 */

                    @Override 
                    public boolean onQueryTextSubmit(String str) {
                        return false;
                    }

                    @Override 
                    public boolean onQueryTextChange(String str) {
                        AllPdfFragment.this.filter(str);
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override 
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_selection) {
            this.isMultiSelect = true;
            this.searchView.setVisibility(View.GONE);
            setHasOptionsMenu(false);
            this.pdfFileAdapter.visible = true;
            ((MainActivity) this.context).setRemoveIcon();
            ((MainActivity) this.context).setBottomBarVisible(this.isMultiSelect);
            ((MainActivity) this.context).setToolbarTitle(this.isMultiSelect);
            if (filterFlag) {
                notifyAdapter(this.tempList);
            } else {
                MainActivity mainActivity = (MainActivity) this.context;
                notifyAdapter(MainActivity.pdfFileModelList);
            }
        } else if (itemId == R.id.action_sort) {
            this.dialogSort.show();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void sortDialog() {
        this.dialogSortBinding = (DialogSortBinding) DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_sort, null, false);
        BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(getContext(), R.style.bottomSheetDialogTheme);
        this.dialogSort = bottomSheetDialog2;
        bottomSheetDialog2.setContentView(this.dialogSortBinding.getRoot());
        sortIconVisible(this.dialogSortBinding.sort4);
        if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.NAME_ASC)) {
            sortIconVisible(this.dialogSortBinding.sort1);
        }
        if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.NAME_DESC)) {
            sortIconVisible(this.dialogSortBinding.sort2);
        }
        if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.DATE_ASC)) {
            sortIconVisible(this.dialogSortBinding.sort3);
        }
        if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.DATE_DESC)) {
            sortIconVisible(this.dialogSortBinding.sort4);
        }
        if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.SIZE_ASC)) {
            sortIconVisible(this.dialogSortBinding.sort5);
        }
        if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.SIZE_DESC)) {
            sortIconVisible(this.dialogSortBinding.sort6);
        }
        this.dialogSortBinding.llNameAsc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass10 */

            public void onClick(View view) {
                AllPdfFragment.this.OLD_SORT_TYPE = AppConstants.NAME_ASC;
                AllPdfFragment allPdfFragment = AllPdfFragment.this;
                allPdfFragment.sortIconVisible(allPdfFragment.dialogSortBinding.sort1);
            }
        });
        this.dialogSortBinding.llNameDesc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass11 */

            public void onClick(View view) {
                AllPdfFragment.this.OLD_SORT_TYPE = AppConstants.NAME_DESC;
                AllPdfFragment allPdfFragment = AllPdfFragment.this;
                allPdfFragment.sortIconVisible(allPdfFragment.dialogSortBinding.sort2);
            }
        });
        this.dialogSortBinding.llDateAsc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass12 */

            public void onClick(View view) {
                AllPdfFragment.this.OLD_SORT_TYPE = AppConstants.DATE_ASC;
                AllPdfFragment allPdfFragment = AllPdfFragment.this;
                allPdfFragment.sortIconVisible(allPdfFragment.dialogSortBinding.sort3);
            }
        });
        this.dialogSortBinding.llDateDesc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass13 */

            public void onClick(View view) {
                AllPdfFragment.this.OLD_SORT_TYPE = AppConstants.DATE_DESC;
                AllPdfFragment allPdfFragment = AllPdfFragment.this;
                allPdfFragment.sortIconVisible(allPdfFragment.dialogSortBinding.sort4);
            }
        });
        this.dialogSortBinding.llSizeAsc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass14 */

            public void onClick(View view) {
                AllPdfFragment.this.OLD_SORT_TYPE = AppConstants.SIZE_ASC;
                AllPdfFragment allPdfFragment = AllPdfFragment.this;
                allPdfFragment.sortIconVisible(allPdfFragment.dialogSortBinding.sort5);
            }
        });
        this.dialogSortBinding.llSizeDesc.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass15 */

            public void onClick(View view) {
                AllPdfFragment.this.OLD_SORT_TYPE = AppConstants.SIZE_DESC;
                AllPdfFragment allPdfFragment = AllPdfFragment.this;
                allPdfFragment.sortIconVisible(allPdfFragment.dialogSortBinding.sort6);
            }
        });
        this.dialogSortBinding.bottom.cardSave.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass16 */

            public void onClick(View view) {
                if (AllPdfFragment.this.OLD_SORT_TYPE.equalsIgnoreCase(AppConstants.DATE_ASC)) {
                    AppPref.setSortType(AllPdfFragment.this.getContext(), AppConstants.DATE_ASC);
                    if (AllPdfFragment.this.pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.DATEAes);
                        AllPdfFragment.this.notifyAdapter(MainActivity.pdfFileModelList);
                    }
                } else if (AllPdfFragment.this.OLD_SORT_TYPE.equalsIgnoreCase(AppConstants.DATE_DESC)) {
                    AppPref.setSortType(AllPdfFragment.this.getContext(), AppConstants.DATE_DESC);
                    if (AllPdfFragment.this.pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.DATEDes);
                        AllPdfFragment.this.notifyAdapter(MainActivity.pdfFileModelList);
                    }
                } else if (AllPdfFragment.this.OLD_SORT_TYPE.equalsIgnoreCase(AppConstants.NAME_ASC)) {
                    AppPref.setSortType(AllPdfFragment.this.getContext(), AppConstants.NAME_ASC);
                    if (AllPdfFragment.this.pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.NAMEAes);
                        AllPdfFragment.this.notifyAdapter(MainActivity.pdfFileModelList);
                    }
                } else if (AllPdfFragment.this.OLD_SORT_TYPE.equalsIgnoreCase(AppConstants.NAME_DESC)) {
                    AppPref.setSortType(AllPdfFragment.this.getContext(), AppConstants.NAME_DESC);
                    if (AllPdfFragment.this.pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.NAMEDes);
                        AllPdfFragment.this.notifyAdapter(MainActivity.pdfFileModelList);
                    }
                } else if (AllPdfFragment.this.OLD_SORT_TYPE.equalsIgnoreCase(AppConstants.SIZE_ASC)) {
                    AppPref.setSortType(AllPdfFragment.this.getContext(), AppConstants.SIZE_ASC);
                    if (AllPdfFragment.this.pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.SIZEAes);
                        AllPdfFragment.this.notifyAdapter(MainActivity.pdfFileModelList);
                    }
                } else {
                    AppPref.setSortType(AllPdfFragment.this.getContext(), AppConstants.SIZE_DESC);
                    if (AllPdfFragment.this.pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.SIZEDes);
                        AllPdfFragment.this.notifyAdapter(MainActivity.pdfFileModelList);
                    }
                }
                AllPdfFragment.this.dialogSort.dismiss();
            }
        });
        this.dialogSortBinding.bottom.cardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.DATE_ASC)) {
                    if (pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                        sortIconVisible(dialogSortBinding.sort3);
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.DATEAes);
                        pdfFileAdapter.notifyDataSetChanged();
                    }
                } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.DATE_DESC)) {
                    if (pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                        sortIconVisible(dialogSortBinding.sort4);
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.DATEDes);
                        pdfFileAdapter.notifyDataSetChanged();
                    }
                } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.NAME_ASC)) {
                    if (pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                        sortIconVisible(dialogSortBinding.sort1);
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.NAMEAes);
                        pdfFileAdapter.notifyDataSetChanged();
                    }
                } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.NAME_DESC)) {
                    if (pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                        sortIconVisible(dialogSortBinding.sort2);
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.NAMEDes);
                        pdfFileAdapter.notifyDataSetChanged();
                    }
                } else if (AppPref.getSortType(getContext()).equalsIgnoreCase(AppConstants.SIZE_ASC)) {
                    if (pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                        sortIconVisible(dialogSortBinding.sort5);
                        Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.SIZEAes);
                        pdfFileAdapter.notifyDataSetChanged();
                    }
                } else if (pdfFileAdapter != null && MainActivity.pdfFileModelList.size() > 0) {
                    sortIconVisible(dialogSortBinding.sort6);
                    Collections.sort(MainActivity.pdfFileModelList, PdfFileModel.Comparators.SIZEDes);
                    pdfFileAdapter.notifyDataSetChanged();
                }
                dialogSort.dismiss();
            }
        });
    }

    private void sortIconVisible(ImageView imageView) {
        dialogSortBinding.sort1.setVisibility(View.GONE);
        this.dialogSortBinding.sort2.setVisibility(View.GONE);
        this.dialogSortBinding.sort3.setVisibility(View.GONE);
        this.dialogSortBinding.sort4.setVisibility(View.GONE);
        this.dialogSortBinding.sort5.setVisibility(View.GONE);
        this.dialogSortBinding.sort6.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }

    public void filter(String str) {
        if (!str.isEmpty()) {
            filterFlag = true;
            this.filterByText = str;
            this.tempList.clear();
            MainActivity mainActivity = (MainActivity) this.context;
            for (PdfFileModel pdfFileModel : MainActivity.pdfFileModelList) {
                if (filterByAll(pdfFileModel)) {
                    this.tempList.add(pdfFileModel);
                    MainActivity mainActivity2 = (MainActivity) this.context;
                    MainActivity.pdfFileModelList.indexOf(pdfFileModel);
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
            MainActivity mainActivity3 = (MainActivity) this.context;
            notifyAdapter(MainActivity.pdfFileModelList);
        }
    }

    private boolean filterByAll(PdfFileModel pdfFileModel) {
        return pdfFileModel.getFilename().toLowerCase().contains(this.filterByText.toLowerCase());
    }

    public void notifyAdapter(List<PdfFileModel> list) {
        this.pdfFileAdapter.setData(list);
        this.pdfFileAdapter.notifyDataSetChanged();
        listIsEmpty();
    }

    public void listIsEmpty() {
        MainActivity mainActivity = (MainActivity) this.context;
        if (MainActivity.pdfFileModelList.size() > 0) {
            this.binding.centerlayout.setVisibility(View.GONE);
        } else {
            this.binding.centerlayout.setVisibility(View.VISIBLE);
        }
    }

    @Override 
    public void onHiddenChanged(boolean z) {
        if (!z) {
            this.pdfFileAdapter.notifyDataSetChanged();
        }
        super.onHiddenChanged(z);
    }

    public void deleteMultiFile() {
        deleteDialog(null, 0);
    }

    public void mergeMultiFile() {
        this.filePathArrayList.clear();
        MainActivity mainActivity = (MainActivity) this.context;
        if (MainActivity.multiSelectList.size() > 0) {
            FolderCreation.CreateDirecory();
            int i = 0;
            while (true) {
                MainActivity mainActivity2 = (MainActivity) this.context;
                if (i < MainActivity.multiSelectList.size()) {
                    try {
                        MainActivity mainActivity3 = (MainActivity) this.context;
                        this.document = PDDocument.load(new File(MainActivity.multiSelectList.get(i).getFilepath()));
                    } catch (IOException e) {
                        if (e.getMessage().equalsIgnoreCase(getString(R.string.EXCEPTION_MERGE_LOCK))) {
                            Toast.makeText(this.context, "Password Protected PDF Protected can not Merge", 0).show();
                            return;
                        }
                    }
                    ArrayList<String> arrayList = this.filePathArrayList;
                    MainActivity mainActivity4 = (MainActivity) this.context;
                    arrayList.add(MainActivity.multiSelectList.get(i).getFilepath());
                    i++;
                } else {
                    mergePdf();
                    return;
                }
            }
        } else {
            Toast.makeText(getActivity(), "Select at least one file", 0).show();
        }
    }

    public void mergePdf() {
        if (MainActivity.pdfFileModelList.size() > 1) {
            DialogUtils dialogUtils2 = new DialogUtils((Activity) getActivity(), true, (DialogUtils.SaveListenere) new DialogUtils.SaveListenere() {

                @Override 
                public void onSaveClickListener(final String str, final String str2, final boolean z) {
                    if (Build.VERSION.SDK_INT <= 29 || !FolderCreation.isFolderExists(AllPdfFragment.this.context, FolderCreation.FOLDER_MERGE, str)) {
                        if (new File(FolderCreation.PATH_MERGE() + "/" + str + ".pdf").exists()) {
                            Toast.makeText(AllPdfFragment.this.getActivity(), "File name already exists", 0).show();
                            return;
                        }
                        AllPdfFragment.this.dialogUtils.dismissProgressDialog();
                        AllPdfFragment allPdfFragment = AllPdfFragment.this;
                        allPdfFragment.mergefilename = new File(FolderCreation.PATH_MERGE() + "/" + str + ".pdf");
                        if (!AllPdfFragment.this.mergefilename.exists()) {
                            AllPdfFragment.this.dialogUtils.dismissProgressDialog();
                            AllPdfFragment.this.isCanceled = false;
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass17.AnonymousClass1 */

                                public void run() {
                                    AllPdfFragment.this.creatingpdf = new creatingPDF(str, str2, z);
                                }
                            }, 4000);
                        } else {
                            Toast.makeText(AllPdfFragment.this.getActivity(), "File name already exists", 1).show();
                        }
                        AllPdfFragment.this.dialogUtils = new DialogUtils(AllPdfFragment.this.getActivity(), new ProgressListener() {
                            /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.AnonymousClass17.AnonymousClass2 */

                            @Override // pdf.reader.pdfviewer.listener.ProgressListener
                            public void onProgrssListener() {
                            }

                            @Override // pdf.reader.pdfviewer.listener.ProgressListener
                            public void onProgressCancled() {
                                AllPdfFragment.this.isCanceled = true;
                                AllPdfFragment.this.disposable.dispose();
                            }
                        }, MainActivity.pdfFileModelList.size());
                        return;
                    }
                    Toast.makeText(AllPdfFragment.this.getActivity(), "File name already exists", 0).show();
                }
            });
            this.dialogUtils = dialogUtils2;
            dialogUtils2.onSaveDialog();
            return;
        }
        Toast.makeText(getContext(), "Please Select at least 2 Files", 0).show();
    }

    public class creatingPDF {
        String filename;
        boolean isPasswordEnbled;
        String password;

        public creatingPDF(String str, String str2, boolean z) {
            this.filename = str;
            this.password = str2;
            this.isPasswordEnbled = z;
            AllPdfFragment.this.isCanceled = false;
            AllPdfFragment.this.dialogUtils.progressBar.setMax((float) AllPdfFragment.this.filePathArrayList.size());
            AllPdfFragment.this.disposable.add(Observable.fromCallable(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    try {
                        if (AllPdfFragment.this.isCanceled) {
                            return false;
                        }
                        PDFMergerUtility pDFMergerUtility = new PDFMergerUtility();
                        pDFMergerUtility.setDestinationFileName(AllPdfFragment.this.mergefilename.getAbsolutePath());
                        AllPdfFragment.this.f1 = new File(AllPdfFragment.this.mergefilename.getAbsolutePath());
                        pDFMergerUtility.mergeDocuments(null);
                        Log.e("TAG", "creatingPDF: " + AllPdfFragment.this.dialogUtils.progressBar.getProgress() + HelpFormatter.DEFAULT_LONG_OPT_SEPARATOR + AllPdfFragment.this.dialogUtils.progressBar.getSecondaryProgress());
                        int i = 0;
                        while (true) {
                            if (i < AllPdfFragment.this.filePathArrayList.size()) {
                                int finalI = i;
                                AllPdfFragment.this.getActivity().runOnUiThread(new Runnable() {
                                    /* class pdf.reader.pdfviewer.fragment.AllPdfFragment.creatingPDF.AnonymousClass1 */

                                    public void run() {
                                        TextView textView = AllPdfFragment.this.dialogUtils.progressText;
                                        textView.setText((finalI + 1) + "/" + AllPdfFragment.this.filePathArrayList.size());
                                    }
                                });
                                if (AllPdfFragment.this.isCanceled) {
                                    AllPdfFragment.this.isCanceled = false;
                                    if (AllPdfFragment.this.f1.exists()) {
                                        AllPdfFragment.this.f1.delete();
                                    }
                                } else {
                                    try {
                                        pDFMergerUtility.addSource(AllPdfFragment.this.filePathArrayList.get(i));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    i++;
                                }
                            }
                        }

                    } catch (Exception e3) {
                        e3.printStackTrace();
                        Log.d("TAG", "creatingPDF: " + e3.getMessage());
                        return null;
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    if (((Serializable) o) == null) {
                        File file = new File(AllPdfFragment.this.mergefilename.getAbsolutePath());
                        if (file.exists()) {
                            file.delete();
                        }
                        Toast.makeText(AllPdfFragment.this.getContext(), "Error When Creating or passWord protected pdf File", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AllPdfFragment.this.dialogUtils.dismissProgressDialog();
                    ((MainActivity) AllPdfFragment.this.context).mergerPdfResult(AllPdfFragment.this.mergefilename.getAbsolutePath());
                }
            }));
        }

    }

    public void shareMultiFile() {
        shareFile("");
    }

    public void addAll() {
        if (((MainActivity) this.context).isSelectAll) {
            MainActivity mainActivity = (MainActivity) this.context;
            MainActivity.multiSelectList.clear();
            MainActivity mainActivity2 = (MainActivity) this.context;
            List<PdfFileModel> list = MainActivity.multiSelectList;
            MainActivity mainActivity3 = (MainActivity) this.context;
            list.addAll(MainActivity.pdfFileModelList);
            int i = 0;
            while (true) {
                MainActivity mainActivity4 = (MainActivity) this.context;
                if (i >= MainActivity.multiSelectList.size()) {
                    break;
                }
                MainActivity mainActivity5 = (MainActivity) this.context;
                MainActivity.multiSelectList.get(i).setSelected(true);
                i++;
            }
            ((MainActivity) this.context).setToolbarTitle(this.isMultiSelect);
        } else {
            MainActivity mainActivity6 = (MainActivity) this.context;
            MainActivity.multiSelectList.clear();
            ((MainActivity) this.context).setToolbarTitle(this.isMultiSelect);
        }
        this.pdfFileAdapter.notifyDataSetChanged();
    }
}
