package pdf.reader.pdfviewer.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;

import pdf.reader.pdfviewer.R;
import pdf.reader.pdfviewer.baseClass.BaseActivityBinding;
import pdf.reader.pdfviewer.baseClass.OnFragmentInteractionListener;
import pdf.reader.pdfviewer.databinding.ActivityMainBinding;
import pdf.reader.pdfviewer.fragment.AllPdfFragment;
import pdf.reader.pdfviewer.fragment.FavoriteFragment;
import pdf.reader.pdfviewer.listener.PdfDataGetListener;
import pdf.reader.pdfviewer.model.PdfFileModel;
import pdf.reader.pdfviewer.utility.AppConstants;
import pdf.reader.pdfviewer.utility.AppPref;
import pdf.reader.pdfviewer.utility.BetterActivityResult;
import pdf.reader.pdfviewer.utility.FileResolver;
import pdf.reader.pdfviewer.utility.PdfAsyncTask;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.protocol.HTTP;

import com.roughike.bottombar.OnTabSelectListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivityBinding implements OnFragmentInteractionListener, PdfDataGetListener {
    public static List<PdfFileModel> multiSelectList = null;
    public static List<PdfFileModel> pdfFileModelList = null;
    String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    Fragment activeFragment;
    public AllPdfFragment allPdfFragment;
    ActivityMainBinding binding;
    public FavoriteFragment favoriteFragment;
    FragmentManager fm;
    public List<PdfFileModel> isFavoriteList = new ArrayList();
    public boolean isSelectAll = false;
    AlertDialog mMyDialog;
    PdfAsyncTask pdfAsyncTask;
    boolean permissionNotify = false;
    String playStoreUrl = "";

    @Override 
    public void onFragmentInteraction(Uri uri) {
    }

    @Override 
    public void onMergeOpeation() {
    }

    @Override 
    public void onPdfPickerOperation(PdfFileModel pdfFileModel, int i) {
    }

   
    @Override 
    public void setBinding() {
        this.binding =  DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.isFavoriteList = AppPref.getFavArrayList(this.context);
        InitFragments();
        AppConstants.setStatusBarDefault(this);
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override 
            public void handleOnBackPressed() {
                if (activeFragment instanceof AllPdfFragment) {
                    if (allPdfFragment.isMultiSelect) {
                        binding.toolbarMain.title.setText("PDF Viewer");
                        setBottomBarVisible(!allPdfFragment.isMultiSelect);
                        allPdfFragment.pdfFileAdapter.visible = false;
                        MainActivity.multiSelectList.clear();
                        allPdfFragment.isMultiSelect = false;
                        allPdfFragment.setHasOptionsMenu(true);
                        allPdfFragment.pdfFileAdapter.notifyDataSetChanged();
                        return;
                    }
                    finish();
                } else if (!(activeFragment instanceof FavoriteFragment)) {
                    finish();
                } else if (favoriteFragment.isMultiSelect) {
                    binding.toolbarMain.title.setText("PDF Viewer");
                    setBottomBarVisible(!favoriteFragment.isMultiSelect);
                    favoriteFragment.favFileAdapter.visible = false;
                    MainActivity.multiSelectList.clear();
                    favoriteFragment.isMultiSelect = false;
                    favoriteFragment.setHasOptionsMenu(true);
                    favoriteFragment.favFileAdapter.notifyDataSetChanged();
                } else {
                    finish();
                }
            }
        });
    }

    public void setRemoveIcon() {
        Fragment fragment = this.activeFragment;
        if (fragment instanceof AllPdfFragment) {
            this.binding.Ivmerge.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.merger));
        } else if (fragment instanceof FavoriteFragment) {
            this.binding.Ivmerge.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.dialog_icon_remove));
        }
    }

   
    @Override
    public void setToolbar() {
        setSupportActionBar(this.binding.toolbarMain.toolBar);

    }

   
    @Override 
    public void setOnClicks() {
        this.binding.llDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (activeFragment instanceof AllPdfFragment) {
                    if (MainActivity.multiSelectList.size() > 0) {
                        allPdfFragment.deleteMultiFile();
                    } else {
                        Toast.makeText(context, "Please Select at least one file", 0).show();
                    }
                } else if (!(activeFragment instanceof FavoriteFragment)) {
                } else {
                    if (MainActivity.multiSelectList.size() > 0) {
                        favoriteFragment.deleteMultiFile();
                    } else {
                        Toast.makeText(context, "Please Select at least one file", 0).show();
                    }
                }
            }
        });
        this.binding.llMerge.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (activeFragment instanceof AllPdfFragment) {
                    if (MainActivity.multiSelectList.size() > 0) {
                        allPdfFragment.mergeMultiFile();
                    } else {
                        Toast.makeText(context, "Please Select at least one file", Toast.LENGTH_SHORT).show();
                    }
                } else if (!(activeFragment instanceof FavoriteFragment)) {
                } else {
                    if (MainActivity.multiSelectList.size() > 0) {
                        favoriteFragment.removeOnly();
                    } else {
                        Toast.makeText(context, "Please Select at least one file", 0).show();
                    }
                }
            }
        });
        this.binding.llShare.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (activeFragment instanceof AllPdfFragment) {
                    if (MainActivity.multiSelectList.size() > 0) {
                        allPdfFragment.shareMultiFile();
                    } else {
                        Toast.makeText(context, "Please Select at least one file", 0).show();
                    }
                } else {
                    if (MainActivity.multiSelectList.size() > 0) {
                        favoriteFragment.shareMultiFile();
                    } else {
                        Toast.makeText(context, "Please Select at least one file", 0).show();
                    }
                }
            }
        });
        this.binding.toolbarMain.imgSelectAll.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (activeFragment instanceof AllPdfFragment) {
                    isSelectAll = !isSelectAll;
                    allPdfFragment.addAll();
                } else if (activeFragment instanceof FavoriteFragment) {
                    isSelectAll = !isSelectAll;
                    favoriteFragment.addAll();
                }
                setRemoveIcon();
            }
        });
    }

   
    @Override 
    public void initMethods() {
        if (Build.VERSION.SDK_INT >= 30) {
            this.permissionNotify = AppConstants.checkStoragePermissionApi30(this.context);
        } else {
            this.permissionNotify = AppConstants.checkStoragePermissionApi19(this.context);
        }
        if (this.permissionNotify) {
            callAsynck();
        } else if (Build.VERSION.SDK_INT >= 30) {
            openDialogPermission();
        } else if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 1009);
        }
        pdfFileModelList = new ArrayList();
        multiSelectList = new ArrayList();
        setupTablayout();
        this.playStoreUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
    }

    private void openDialogPermission() {
        View inflate = getLayoutInflater().inflate(R.layout.dialog_permissions, (ViewGroup) null, false);
        Dialog dialog = new Dialog(this.context);
        dialog.setContentView(inflate);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();
        Glide.with((FragmentActivity) this).load(Integer.valueOf((int) R.raw.permission_switch)).into((ImageView) inflate.findViewById(R.id.imgPermission));
        ((CardView) inflate.findViewById(R.id.cardSave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showPermissionNotifyDialog();
            }
        });
        ((CardView) inflate.findViewById(R.id.cardCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
    }


    public void showPermissionNotifyDialog() {
        if (Build.VERSION.SDK_INT >= 30) {
            this.activityLauncher.launch(new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION", Uri.parse("package:pdf.reader.pdfviewer")), new BetterActivityResult.OnActivityResult<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (AppConstants.checkStoragePermissionApi30(context)) {
                        callAsynck();
                    } else {
                        openDialogPermission();
                    }
                }
            });
        } else if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 1009);
        }
    }


    public void onClick(View view) {
        if(view.getId()==R.id.cardDrawer){
            if (this.allPdfFragment.isMultiSelect ||  this.favoriteFragment.isMultiSelect) {
                Fragment fragment = this.activeFragment;
                if (fragment instanceof AllPdfFragment) {
                    this.binding.toolbarMain.title.setText("PDF Viewer");
                    setBottomBarVisible(!this.allPdfFragment.isMultiSelect);
                    this.allPdfFragment.pdfFileAdapter.visible = false;
                    multiSelectList.clear();
                    this.allPdfFragment.isMultiSelect = false;
                    this.allPdfFragment.setHasOptionsMenu(true);
                    this.allPdfFragment.pdfFileAdapter.notifyDataSetChanged();
                    return;
                } else if (fragment instanceof FavoriteFragment) {
                    this.binding.toolbarMain.title.setText("PDF Viewer");
                    setBottomBarVisible(!this.favoriteFragment.isMultiSelect);
                    this.favoriteFragment.favFileAdapter.visible = false;
                    multiSelectList.clear();
                    this.favoriteFragment.isMultiSelect = false;
                    this.favoriteFragment.setHasOptionsMenu(true);
                    this.favoriteFragment.favFileAdapter.notifyDataSetChanged();

                }
            }
        }else if(view.getId()==R.id.cardShare){
            shareapp();
        }
    }


    private void setupTablayout() {
        this.binding.bottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelected(int i) {
                if (i==R.id.menu_fav){
                    binding.merge.setText("Remove");
                    showFragments(favoriteFragment);
                }else  if (i==R.id.menu_pdf){
                    binding.merge.setText("Merge");
                    showFragments(allPdfFragment);
                }
                setTitleToolbar();
            }
        });
    }

    private void setTitleToolbar() {
        Fragment fragment = this.activeFragment;
        if (fragment instanceof FavoriteFragment) {
            this.binding.toolbarMain.title.setText("BookMark");
        } else {
            this.binding.toolbarMain.title.setText("PDF Reader");
        }
    }


    private void InitFragments() {
        this.allPdfFragment = new AllPdfFragment();
        this.favoriteFragment = new FavoriteFragment();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.fm = supportFragmentManager;
        supportFragmentManager.beginTransaction().add(R.id.llFrm, this.allPdfFragment, "1").show(this.allPdfFragment).commit();
        this.activeFragment = this.allPdfFragment;
        this.fm.beginTransaction().add(R.id.llFrm, this.favoriteFragment, "2").hide(this.favoriteFragment).commit();
    }

    public void showFragments(Fragment fragment) {
        if (!this.activeFragment.equals(fragment)) {
            this.fm.beginTransaction().show(fragment).hide(this.activeFragment).commit();
            this.activeFragment = fragment;
        }

        if (this.activeFragment instanceof FavoriteFragment) {
            this.favoriteFragment.sortingPref();
        }
    }

    public void requestPermission() {
        if (Build.VERSION.SDK_INT > 29) {
            callAsynck();
        } else if (hasPermissions(this, this.PERMISSIONS)) {
            callAsynck();
        } else if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(this.PERMISSIONS, 1009);
        }
    }

    private static boolean hasPermissions(Context context, String... strArr) {
        if (Build.VERSION.SDK_INT < 23 || context == null || strArr == null) {
            return true;
        }
        for (String str : strArr) {
            if (ActivityCompat.checkSelfPermission(context, str) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1009) {
            int length = strArr.length;
            int i2 = 0;
            char c = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                String str = strArr[i2];
                if (iArr[i2] == -1) {
                    if (!(Build.VERSION.SDK_INT >= 23 ? shouldShowRequestPermissionRationale(str) : false)) {
                        show_alert(1009);
                        c = 2;
                        break;
                    }
                    c = 1;
                }
                i2++;
            }
            if (c == 1) {
                ActivityCompat.requestPermissions(this, this.PERMISSIONS, 1009);
            } else if (c == 0) {
                callAsynck();
            }
        }
    }

    public void show_alert(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires permission. Please ensure that this is enabled in settings, then press the back button to continue");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                activityLauncher.launch(intent, new BetterActivityResult.OnActivityResult<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getData() != null) {
                            requestPermission();
                        }
                    }
                });
                mMyDialog.dismiss();
            }

            

        });
        this.mMyDialog = builder.show();
    }

    private void callAsynck() {
        this.binding.progressbar.setVisibility(View.VISIBLE);
        this.pdfAsyncTask = new PdfAsyncTask(getApplicationContext(), this, this.isFavoriteList);
    }

    @Override 
    public void onDatagetListener(List<PdfFileModel> list) {
        List<PdfFileModel> list2 = pdfFileModelList;
        if (list2 != null) {
            list2.addAll(list);
            Collections.sort(pdfFileModelList, PdfFileModel.Comparators.DATEDes);
            this.allPdfFragment.notifyAdapter(list);
            if (this.activeFragment instanceof AllPdfFragment) {
                this.allPdfFragment.sortingPref();
            }
            if (this.activeFragment instanceof FavoriteFragment) {
                this.favoriteFragment.sortingPref();
            }
            this.binding.progressbar.setVisibility(View.GONE);
        }
    }

    public void mergerPdfResult(String str) {
        FileResolver.insert(str, getApplicationContext());
        File file = new File(str);
        PdfFileModel pdfFileModel = new PdfFileModel(file.getPath(), file.getName(), file.lastModified(), file.length());
        if (this.activeFragment instanceof AllPdfFragment) {
            this.allPdfFragment.onAddPdf(pdfFileModel);
            multiSelectList.clear();
            setFixTitlebar();
        }
    }

    public void renameFileChange(PdfFileModel pdfFileModel, File file) {
        pdfFileModel.setFilepath(file.getPath());
        pdfFileModel.setFilename(file.getName());
        pdfFileModel.setFilesize(file.length());
        pdfFileModel.setLastmodified(file.lastModified());
        int indexOf = pdfFileModelList.indexOf(pdfFileModel);
        if (indexOf != -1) {
            pdfFileModelList.set(indexOf, pdfFileModel);
        }
        int indexOf3 = this.favoriteFragment.pdfFavList.indexOf(pdfFileModel);
        if (indexOf3 != -1) {
            this.favoriteFragment.pdfFavList.set(this.favoriteFragment.pdfFavList.indexOf(pdfFileModel), pdfFileModel);
        }
        ((MainActivity) this.context).allPdfFragment.sortingPref();
        ((MainActivity) this.context).favoriteFragment.sortingPref();
    }

    public void deleteFiles(PdfFileModel pdfFileModel) {
        pdfFileModelList.remove(pdfFileModel);
        if (this.activeFragment instanceof AllPdfFragment) {
            this.allPdfFragment.notifyAdapter(pdfFileModelList);
        }
    }

    public void shareapp() {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType(HTTP.PLAIN_TEXT_TYPE);
            intent.putExtra("android.intent.extra.SUBJECT", "PDF Reader & Viewer, PDF Tools");
            intent.putExtra("android.intent.extra.TEXT", "PDF Reader & Viewer, PDF Tools\nFast, user-friendly, feature-packed for easy PDF management.\n- Easily view and read PDF files with a user-friendly interface.\n- Contains PDF tools - Merge, Split, Delete Page, Extract Page, Lock PDF, Unlock PDF and Rotate Page.\n- Contains feature to convert images to PDF, PDF to image file.\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception unused) {
        }
    }
    boolean doubleBackToExitPressedOnce = false;


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void setBottomBarVisible(boolean z) {
        if (z) {
            this.binding.bottomBar.setVisibility(View.GONE);
            this.binding.toolbarMain.imgSelectAll.setVisibility(View.VISIBLE);
            this.binding.llBottomMulti.setVisibility(View.VISIBLE);
            return;
        }
        this.binding.bottomBar.setVisibility(View.VISIBLE);
        this.binding.toolbarMain.imgSelectAll.setVisibility(View.GONE);
        this.binding.llBottomMulti.setVisibility(View.GONE);
    }

    public void setToolbarTitle(boolean z) {
        if (z) {
            TextView textView = this.binding.toolbarMain.title;
            textView.setText(multiSelectList.size() + " Selected");
            this.binding.toolbarMain.drawerIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.back));
        } else if (!this.isSelectAll) {
            TextView textView2 = this.binding.toolbarMain.title;
            textView2.setText(multiSelectList.size() + " Selected");
            this.binding.toolbarMain.drawerIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.back));
        } else {
            this.binding.toolbarMain.title.setText("PDF Viewer");
        }
    }

    public void setFixTitlebar() {
        this.binding.toolbarMain.title.setText("PDF Viewer");
        this.binding.toolbarMain.imgSelectAll.setVisibility(View.GONE);
        this.binding.bottomBar.setVisibility(View.VISIBLE);
        this.binding.llBottomMulti.setVisibility(View.GONE);
        Fragment fragment = this.activeFragment;
        if (fragment instanceof FavoriteFragment) {
            this.favoriteFragment.favFileAdapter.visible = false;
            this.favoriteFragment.setHasOptionsMenu(true);
            this.favoriteFragment.favFileAdapter.notifyDataSetChanged();
        } else if (fragment instanceof AllPdfFragment) {
            this.allPdfFragment.pdfFileAdapter.visible = false;
            this.allPdfFragment.setHasOptionsMenu(true);
            this.allPdfFragment.pdfFileAdapter.notifyDataSetChanged();
        }
    }
}
