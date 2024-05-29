package pdf.reader.pdfviewer.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import pdf.reader.pdfviewer.R;
import pdf.reader.pdfviewer.adapter.PagePreviewAdapter;
import pdf.reader.pdfviewer.baseClass.BaseActivityBinding;
import pdf.reader.pdfviewer.databinding.ActivityPdfViewerBinding;
import pdf.reader.pdfviewer.databinding.DialogFileInfoBinding;
import pdf.reader.pdfviewer.databinding.DialogGotoPageBinding;
import pdf.reader.pdfviewer.databinding.DialogPasswordBinding;
import pdf.reader.pdfviewer.databinding.DialogPdfViewBinding;
import pdf.reader.pdfviewer.listener.ItemClick;
import pdf.reader.pdfviewer.model.PdfFileModel;
import pdf.reader.pdfviewer.utility.AppConstants;
import pdf.reader.pdfviewer.utility.AppPref;
import pdf.reader.pdfviewer.utility.PdfFileUtils;
import pdf.reader.pdfviewer.utility.Test;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.itextpdf.text.Annotation;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class PdfViewer extends BaseActivityBinding implements View.OnTouchListener {
    private int SCROLL_TIME_DELAY = 2000;
    int SPACE_SCROLLBAR_WIDTH = 2;
    ActivityPdfViewerBinding binding;
    List<Bitmap> bitmapThumbnailList = new ArrayList();
    BottomSheetDialog bottomSheetDialog;
    BottomSheetDialog bottomSheetDialogInfo;
    byte[] buffer;
    Dialog dialogGoto;
    DialogGotoPageBinding dialogGotoPageBinding;
    DialogPdfViewBinding dialogPdfViewBinding;
    CompositeDisposable disposable = new CompositeDisposable();
    int favPos = 0;
    List<PdfFileModel> favlist = new ArrayList();
    private final Handler handler = new Handler();
    String imageText = "";
    boolean isAutoScrolling = false;
    boolean isItemClick = false;
    boolean isMinus = false;
    boolean isOutsidePdf = false;
    private boolean isPageScrollEnabled = false;
    private boolean isStatusBarVisible = false;
    boolean isUpDirection = true;
    boolean nightMode = false;
    int page = 0;
    private Integer pageNumber = 0;
    PagePreviewAdapter pagePreviewAdapter;
    List<String> pageWiseDetail;
    PdfFileModel pdfFileModel;
    boolean pdfViewStatus = false;
    PdfiumCore pdfiumCore;
    private LinearLayout previewLayout;
    int printNo = 5;
    Uri printUri;
    List<PdfFileModel> recentList = new ArrayList();
    Runnable scrollRunnable;
    private int scrollSpeed = 10;
    private int scrollY = 0;
    Animation slideDownAnimation;
    Animation slideInAnimation;
    Animation slideOutAnimation;
    Animation slideUpAnimation;
    private float startX;
    private float startY;

   
    @Override 
    public void initMethods() {
    }

    static  int access$1012(PdfViewer pdfViewer, int i) {
        int i2 = pdfViewer.scrollY + i;
        pdfViewer.scrollY = i2;
        return i2;
    }

    static  int access$1020(PdfViewer pdfViewer, int i) {
        int i2 = pdfViewer.scrollY - i;
        pdfViewer.scrollY = i2;
        return i2;
    }

   
    @Override 
    public void setBinding() {
        this.binding =  DataBindingUtil.setContentView(this, R.layout.activity_pdf_viewer);
        this.pageWiseDetail = new ArrayList();
        this.favPos = getIntent().getIntExtra("positionFav", 0);
        load();
        AppConstants.setStatusBarWhite(this);
        this.binding.getRoot();
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override 
            public void handleOnBackPressed() {
                Intent intent = new Intent();
                intent.putExtra("pdfModel", pdfFileModel);
                setResult(-1, intent);
                finish();
            }
        });
    }

   
    private void extractDataFromPDF() {
        this.binding.progressbar.setVisibility(View.VISIBLE);
        this.disposable.add(Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                pageWiseDetail.clear();
                extractPDF();
                return false;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                binding.progressbar.setVisibility(android.view.View.GONE);
                if (pageWiseDetail.isEmpty()) {
                    Toast.makeText(PdfViewer.this, "Scanned Pdf files are not supported!", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(PdfViewer.this, TextModeActivity.class).putExtra("pageWiseList", (Serializable) pageWiseDetail));
                }
            }
        }));
    }




    private void extractPDF() {
        String str = "";
        try {
            new File(this.pdfFileModel.getFilepath());
            PdfReader pdfReader = new PdfReader(this.pdfFileModel.getFilepath());
            int numberOfPages = pdfReader.getNumberOfPages();
            int i = 0;
            while (i < numberOfPages) {
                i++;
                str = str + PdfTextExtractor.getTextFromPage(pdfReader, i).trim() + IOUtils.LINE_SEPARATOR_UNIX;
                if (!PdfTextExtractor.getTextFromPage(pdfReader, i).trim().isEmpty()) {
                    this.pageWiseDetail.add(PdfTextExtractor.getTextFromPage(pdfReader, i).trim());
                }
            }
            pdfReader.close();
        } catch (Exception unused) {
        }
    }

   
    @Override 
    public void setToolbar() {
        setSupportActionBar(this.binding.toolBar);
        this.binding.title.setText(this.pdfFileModel.getFilename());
        this.binding.cardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("pdfModel", pdfFileModel);
                setResult(-1, intent);
                finish();
            }
        });
        this.slideInAnimation = AnimationUtils.loadAnimation(this.context, R.anim.slide_in);
        this.slideOutAnimation = AnimationUtils.loadAnimation(this.context, R.anim.slide_out);
        this.slideDownAnimation = AnimationUtils.loadAnimation(this.context, R.anim.slide_down);
        this.slideUpAnimation = AnimationUtils.loadAnimation(this.context, R.anim.slide_up);
        this.pdfiumCore = new PdfiumCore(this);
        this.dialogPdfViewBinding =  DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_pdf_view, null, false);
        bottomSheetDialog = new BottomSheetDialog(this, R.style.bottomSheetDialogTheme);
        bottomSheetDialog.setContentView(this.dialogPdfViewBinding.getRoot());
        gotoDialog();
    }
    private void setInfoDialog(PdfFileModel pdfFileModel2) {
        DialogFileInfoBinding dialogFileInfoBinding = (DialogFileInfoBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_file_info, null, false);
        dialogFileInfoBinding.path.setText(pdfFileModel.getFilepath());
        dialogFileInfoBinding.size.setText(pdfFileModel.getFileSizeString());
        dialogFileInfoBinding.name.setText(pdfFileModel.getFilename());
        dialogFileInfoBinding.lastmodified.setText(pdfFileModel.getFormateData());
        bottomSheetDialogInfo = new BottomSheetDialog(this.context, R.style.bottomSheetDialogTheme);
        bottomSheetDialogInfo.setContentView(dialogFileInfoBinding.getRoot());
        this.bottomSheetDialogInfo.show();
        dialogFileInfoBinding.close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                bottomSheetDialogInfo.dismiss();
            }
        });
    }

    private void gotoDialog() {
        this.dialogGotoPageBinding = DialogGotoPageBinding.inflate(LayoutInflater.from(this));
        dialogGoto = new Dialog(this, R.style.dialogTheme);
        dialogGoto.setContentView(this.dialogGotoPageBinding.getRoot());
        dialogGoto.setCancelable(false);
        dialogGoto.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogGoto.getWindow().setBackgroundDrawableResource(17170445);
        dialogGoto.getWindow().setLayout(-1, -2);
        dialogGotoPageBinding.logo.cardlogo.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_logo));
        dialogGotoPageBinding.logo.img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.goto_img));
    }

   
    private void toggleStatusBarVisibility() {
        if (isStatusBarVisible) {
            binding.toolBar.startAnimation(this.slideDownAnimation);
            binding.toolBar.setVisibility(View.VISIBLE);
            binding.rvPreview.startAnimation(this.slideInAnimation);
            binding.rvPreview.setVisibility(View.VISIBLE);
        } else {
            binding.toolBar.startAnimation(this.slideUpAnimation);
            binding.toolBar.setVisibility(View.GONE);
            binding.rvPreview.startAnimation(this.slideOutAnimation);
            binding.rvPreview.setVisibility(android.view.View.GONE);
        }
        isStatusBarVisible = !this.isStatusBarVisible;
    }

   
    @Override 
    public void setOnClicks() {
        this.recentList = AppPref.getRecentFileList(this);
        this.favlist = AppPref.getFavArrayList(this);
        this.binding.cardRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateFile();
            }
        });
        this.binding.cardNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nightMode = !nightMode;
                binding.pdfView.setNightMode(nightMode);
                binding.pdfView.loadPages();
                setNightMode(nightMode);
            }
        });
        this.binding.cardMore.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dialogPdfViewBinding.title.setText(pdfFileModel.getFilename());
                dialogPdfViewBinding.date.setText(Test.getDate(pdfFileModel.getLastmodified()));
                TextView textView = dialogPdfViewBinding.size;
                textView.setText(PdfFileUtils.getFileSize(pdfFileModel.getFilesize()) + " ,");
                if (pdfFileModel.isFav()) {
                    dialogPdfViewBinding.bookmark.setText("UnMark");
                } else {
                    dialogPdfViewBinding.bookmark.setText("BookMark");
                }
                if (isPageScrollEnabled) {
                    dialogPdfViewBinding.txtPageview.setText("Page Scroll View");
                } else {
                    dialogPdfViewBinding.txtPageview.setText("Page By Page View");
                }
                if (binding.pdfView.isSwipeVertical()) {
                    dialogPdfViewBinding.horizontal.setText(getString(R.string.horizontal));
                } else {
                    dialogPdfViewBinding.horizontal.setText(getString(R.string.vertical));
                }
                bottomSheetDialog.show();
            }
        });
        this.dialogPdfViewBinding.cardBookMark.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (pdfFileModel.isFav()) {
                    dialogPdfViewBinding.bookmark.setText("BookMark");
                } else {
                    dialogPdfViewBinding.bookmark.setText("UnMark");
                }
                pdfFileModel.setFav(!pdfFileModel.isFav());
                if (pdfFileModel.isFav()) {
                    favlist.add(pdfFileModel);
                    Toast.makeText(context, "Added to Bookmark", Toast.LENGTH_SHORT).show();
                } else {
                    favlist.remove(pdfFileModel);
                    Toast.makeText(context, "Removed from Bookmark", android.widget.Toast.LENGTH_SHORT).show();
                }
                int indexOf = MainActivity.pdfFileModelList.indexOf(pdfFileModel);
                if (indexOf != -1) {
                    MainActivity.pdfFileModelList.set(indexOf, pdfFileModel);
                }
                int indexOf2 = recentList.indexOf(pdfFileModel);
                if (indexOf2 != -1) {
                    recentList.set(indexOf2, pdfFileModel);
                }
                AppPref.saveRecentFileList(recentList, PdfViewer.this);
                AppPref.saveFavArrayList(favlist, PdfViewer.this);
                bottomSheetDialog.dismiss();
            }
        });
        this.dialogPdfViewBinding.cardPagebyPage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                viewPageByPagePdf();
                bottomSheetDialog.dismiss();
            }
        });
        this.dialogPdfViewBinding.cardGoto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dialogGoto.show();
                bottomSheetDialog.dismiss();
            }
        });
        this.dialogPdfViewBinding.cardDetail.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setInfoDialog(pdfFileModel);
                bottomSheetDialog.dismiss();
            }
        });
        this.dialogGotoPageBinding.bottom.cardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGoto.dismiss();
            }
        });
        this.dialogGotoPageBinding.bottom.cardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(dialogGotoPageBinding.edPageNo.getText().toString().trim())) {
                    binding.pdfView.jumpTo(Integer.parseInt(dialogGotoPageBinding.edPageNo.getText().toString().trim()) - 1);
                }
                dialogGotoPageBinding.edPageNo.setText("");
                dialogGoto.dismiss();
            }
        });
        this.dialogPdfViewBinding.cardShare.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                shareFile();
                bottomSheetDialog.dismiss();
            }
        });
        this.dialogPdfViewBinding.cardHorizontal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                binding.pdfView.fromFile(new File(pdfFileModel.getFilepath())).swipeHorizontal(!binding.pdfView.isSwipeVertical()).defaultPage(pageNumber.intValue()).onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int i, int pageCount) {
                        pagePreviewAdapter.selectedItemPosition = binding.pdfView.getCurrentPage();
                        binding.rvPreview.scrollToPosition(i);
                        pagePreviewAdapter.notifyDataSetChanged();
                        pageNumber = Integer.valueOf(i);
                    }
                }).enableAnnotationRendering(true).onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                }).nightMode(nightMode).scrollHandle(new DefaultScrollHandle(PdfViewer.this)).spacing(SPACE_SCROLLBAR_WIDTH).onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {

                    }
                }).onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {

                    }
                }).pageFitPolicy(FitPolicy.BOTH).load();
                bottomSheetDialog.dismiss();
            }
            
        });
        this.dialogPdfViewBinding.cardAutoScroll.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                binding.autoscrollView.setVisibility(View.VISIBLE);
                TextView textView = binding.scrollSpeedNo;
                textView.setText(printNo + "");
                bottomSheetDialog.dismiss();
            }
        });
        this.dialogPdfViewBinding.cardTextMode.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                extractDataFromPDF();
                bottomSheetDialog.dismiss();
            }
        });
        this.dialogPdfViewBinding.cardPrint.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                print();
                bottomSheetDialog.dismiss();
            }
        });
        this.binding.pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleStatusBarVisibility();

            }
        });
    }
    
    private void setNightMode(boolean z) {
        if (z) {
            binding.toolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_toolbar));
            binding.llMain.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_toolbar));
            binding.title.setTextColor(ContextCompat.getColor(this, R.color.white));
            AppConstants.setStatusBarBlack(this);
            binding.ivrotate.setColorFilter(ContextCompat.getColor(this.context, R.color.dark_icon), PorterDuff.Mode.SRC_IN);
            binding.ivnightMode.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.light_mode));
            binding.ivmenu.setColorFilter(ContextCompat.getColor(this.context, R.color.dark_icon), PorterDuff.Mode.SRC_IN);
            binding.back.setColorFilter(ContextCompat.getColor(this.context, R.color.dark_icon), PorterDuff.Mode.SRC_IN);
            return;
        }
        binding.toolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.llMain.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.title.setTextColor(ContextCompat.getColor(this, R.color.font1));
        AppConstants.setStatusBarWhite(this);
        binding.ivrotate.setColorFilter(ContextCompat.getColor(this.context, R.color.font1), PorterDuff.Mode.SRC_IN);
        binding.ivnightMode.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.dark_mode));
        binding.ivmenu.setColorFilter(ContextCompat.getColor(this.context, R.color.font1), PorterDuff.Mode.SRC_IN);
        binding.back.setColorFilter(ContextCompat.getColor(this.context, R.color.font1), PorterDuff.Mode.SRC_IN);
    }

    private void stopAutoScroll() {
        this.isAutoScrolling = false;
        this.handler.removeCallbacksAndMessages(null);
    }

    public void onClick(View view) {
        if(view.getId()==R.id.icScrollMinus){
            decreaseScrollSpeed();
            this.printNo--;
            this.isMinus = true;
            this.binding.scrollSpeedNo.setText(this.printNo + "");
            stopAutoScroll();
            if (this.printNo != 0) {
                startAutoScroll();
                return;
            }
        }else if(view.getId()==R.id.icScrollMinus){
            increaseScrollSpeed();
            this.printNo++;
            this.isMinus = false;
            this.binding.scrollSpeedNo.setText(this.printNo + "");
            stopAutoScroll();
            startAutoScroll();
        }else if(view.getId()==R.id.scrollCancel){
            this.binding.autoscrollView.setVisibility(View.GONE);

        }else if(view.getId()==R.id.scrollDown){
            this.isUpDirection = true;
            if (this.isAutoScrolling) {
                startAutoScroll();
                return;
            }
        }else if(view.getId()==R.id.scrollPlay){
            this.isMinus = false;
            if (!this.isAutoScrolling) {
                this.isAutoScrolling = true;
                startAutoScroll();
                ((ImageView) this.binding.autoscrollView1.findViewById(R.id.scrollivPlay)).setImageDrawable(ResourcesCompat.getDrawable(this.context.getResources(), R.drawable.ic_pause, null));
                return;
            }
            stopAutoScroll();
            this.isAutoScrolling = false;
            ((ImageView) this.binding.autoscrollView1.findViewById(R.id.scrollivPlay)).setImageDrawable(ResourcesCompat.getDrawable(this.context.getResources(), R.drawable.is_scroll_play, null));
        }else if(view.getId()==R.id.scrollUp){
            this.isUpDirection = false;
            if (this.isAutoScrolling) {
                startAutoScroll();
                return;
            }
        }

    }

    public void load() {
        this.binding.progressbar.setVisibility(android.view.View.VISIBLE);
        this.disposable.add(Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                String str;
                Exception e;
                String str2 = "";
                if ("android.intent.action.VIEW".equals(getIntent().getAction())) {
                    isOutsidePdf = true;
                    pdfFileModel = new PdfFileModel();
                    if (getIntent().getData() != null) {
                        Uri data = getIntent().getData();
                        if (data.getScheme().equals(Annotation.FILE)) {
                            pdfFileModel.setFilename(data.getLastPathSegment());
                            pdfFileModel.setFilepath(data.getPath());
                        } else {
                            pdfViewStatus = true;
                            try {
                                InputStream openInputStream = getContentResolver().openInputStream(data);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                byte[] bArr = new byte[16384];
                                while (true) {
                                    int read = openInputStream.read(bArr);
                                    if (read == -1) {
                                        break;
                                    }
                                    byteArrayOutputStream.write(bArr, 0, read);
                                }
                                byteArrayOutputStream.flush();
                                buffer = byteArrayOutputStream.toByteArray();
                                try {
                                    Cursor query = getContentResolver().query(data, null, null, null, null);
                                    if (query != null && query.moveToFirst()) {
                                        str = query.getString(query.getColumnIndex("_display_name"));
                                        try {
                                            Log.d("FILENAME1", str2 + str);
                                        } catch (Exception e2) {
                                            e = e2;
                                        }
                                        str2 = str;
                                    }
                                } catch (Exception e3) {
                                    str = str2;
                                    e = e3;
                                    e.printStackTrace();
                                    str2 = str;
                                    pdfFileModel.setFilename(str2);
                                    pdfFileModel.setFilepath(data.getPath());
                                    printUri = data;
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            showPDF(null);
                                        }
                                    });
                                    generateThumbnails(null);
                                    return false;
                                }
                                pdfFileModel.setFilename(str2);
                                pdfFileModel.setFilepath(data.getPath());
                            } catch (IOException e4) {
                                Toast.makeText(PdfViewer.this, e4.getMessage(), 0).show();
                            }
                        }
                        printUri = data;
                        runOnUiThread(new Runnable() {

                            public void run() {
                                showPDF(null);
                            }
                        });
                        generateThumbnails(null);
                    }
                } else {
                    isOutsidePdf = false;
                    if (getIntent() != null && getIntent().hasExtra(AppConstants.PDF_FILE_MODEL)) {
                        pdfFileModel = (PdfFileModel) getIntent().getParcelableExtra(AppConstants.PDF_FILE_MODEL);
                        printUri = Uri.fromFile(new File(pdfFileModel.getFilepath()));
                        runOnUiThread(new Runnable() {

                            public void run() {
                                showPDF(null);
                            }
                        });
                        generateThumbnails(null);
                    }
                }
                return false;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                binding.progressbar.setVisibility(View.GONE);

            }
        }));
    }

    private void showPDF(String str) {
        if (!this.pdfViewStatus) {
            PdfFileModel pdfFileModel2 = this.pdfFileModel;
            if (pdfFileModel2 != null && pdfFileModel2.getFilepath() != null && !TextUtils.isEmpty(this.pdfFileModel.getFilepath())) {
                this.binding.pdfView.fromFile(new File(this.pdfFileModel.getFilepath())).defaultPage(this.pageNumber.intValue()).onPageChange(new OnPageChangeListener() {

                    @Override 
                    public void onPageChanged(int i, int i2) {
                        if (pagePreviewAdapter != null) {
                            pagePreviewAdapter.selectedItemPosition = binding.pdfView.getCurrentPage();
                            binding.rvPreview.scrollToPosition(i);
                            pagePreviewAdapter.notifyDataSetChanged();
                        }
                    }
                }).enableAnnotationRendering(true).onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                }).scrollHandle(new DefaultScrollHandle(this)).spacing(this.SPACE_SCROLLBAR_WIDTH).nightMode(this.nightMode).onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages) {

                    }
                }).onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {

                    }
                }).onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable th) {
                        if (th.getMessage().contains("Password required or incorrect password")) {
                            getPdfPass();
                        }
                    }
                }).pageFitPolicy(FitPolicy.BOTH).password(str).load();
            }
        } else if (this.buffer != null) {
            this.binding.pdfView.fromBytes(this.buffer).defaultPage(this.pageNumber.intValue()).onPageChange(new OnPageChangeListener() {
                @Override
                public void onPageChanged(int page, int pageCount) {
                    PagePreviewAdapter pagePreviewAdapter2 = pagePreviewAdapter;
                    if (pagePreviewAdapter2 != null) {
                        pagePreviewAdapter2.selectedItemPosition = binding.pdfView.getCurrentPage();
                        binding.rvPreview.scrollToPosition(page);
                        pagePreviewAdapter.notifyDataSetChanged();
                    }
                }
            }).enableAnnotationRendering(true).onLoad(new OnLoadCompleteListener() {
                @Override 
                public void loadComplete(int i) {
                }
            }).scrollHandle(new DefaultScrollHandle(this)).spacing(this.SPACE_SCROLLBAR_WIDTH).onPageError(new OnPageErrorListener() {
                @Override
                public void onPageError(int page, Throwable t) {

                }
            }).onError(new OnErrorListener() {
                @Override
                public void onError(Throwable t) {
                    if (t.getMessage().contains("Password required or incorrect password")) {
                        Log.d("Error", "Password required or incorrect password");
                        getPdfPass();
                    }
                }
            }).nightMode(this.nightMode).pageFitPolicy(FitPolicy.BOTH).password(str).load();
        }
    }

    
    private void generateThumbnails(String str) {
        PdfDocument pdfDocument;
        PdfiumCore pdfiumCore2 = new PdfiumCore(this.context);
        if (str == null) {
            try {
                pdfDocument = pdfiumCore2.newDocument(ParcelFileDescriptor.open(new File(this.pdfFileModel.getFilepath()), 268435456));
            } catch (IOException unused) {
                return;
            }
        } else {
            try {
                pdfDocument = pdfiumCore2.newDocument(ParcelFileDescriptor.open(new File(this.pdfFileModel.getFilepath()), 268435456), str);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        int pageCount = pdfiumCore2.getPageCount(pdfDocument);
        for (int i = 0; i < pageCount; i++) {
            Bitmap createBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            pdfiumCore2.openPage(pdfDocument, i);
            pdfiumCore2.renderPageBitmap(pdfDocument, createBitmap, i, 0, 0, createBitmap.getWidth(), createBitmap.getHeight());
            this.bitmapThumbnailList.add(createBitmap);
        }
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(0);
        runOnUiThread(new Runnable() {
            /* class pdf.reader.pdfviewer.activity.PdfViewer.AnonymousClass17 */

            public void run() {
                binding.rvPreview.setLayoutManager(linearLayoutManager);
                PdfViewer pdfViewer = PdfViewer.this;
                PdfViewer pdfViewer2 = PdfViewer.this;
                pdfViewer.pagePreviewAdapter = new PagePreviewAdapter(pdfViewer2, pdfViewer2.bitmapThumbnailList, new ItemClick() {
                    @Override
                    public void onClick(int i) {
                        if (i > 0) {
                            page = i;
                            toggleStatusBarVisibility();
                            binding.pdfView.jumpTo(i);
                            binding.rvPreview.smoothScrollToPosition(i);
                        }
                    }
                });
                binding.rvPreview.setAdapter(pagePreviewAdapter);
            }
        });
    }

   
    private void getPdfPass() {
        final DialogPasswordBinding inflate = DialogPasswordBinding.inflate(LayoutInflater.from(this.context));
        final Dialog dialog = new Dialog(this.context, R.style.dialogTheme);
        dialog.setContentView(inflate.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        inflate.renamelayout.setVisibility(View.GONE);
        inflate.passwordlayout.setVisibility(View.VISIBLE);
        inflate.logo.img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.lock));
        inflate.logo.cardlogo.setCardBackgroundColor(ContextCompat.getColor(this, R.color.lock_card));
        inflate.bottom.cardCancel.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.activity.PdfViewer.AnonymousClass18 */

            public void onClick(View view) {
                dialog.dismiss();
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        inflate.bottom.cardSave.setOnClickListener(new View.OnClickListener() {
            /* class pdf.reader.pdfviewer.activity.PdfViewer.AnonymousClass19 */

            public void onClick(View view) {
                if (!inflate.password.getText().toString().isEmpty()) {
                    showPDF(inflate.password.getText().toString());
                    dialog.dismiss();
                    return;
                }
                Toast.makeText(getApplicationContext(), "Enter Password", 0).show();
            }
        });
    }

    @Override 
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void searchText(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (imageText.toLowerCase().contains(str.toLowerCase())) {
                    Toast.makeText(context, "Search -> " + str, 0).show();
                    return;
                }
                Toast.makeText(context, "No found", 0).show();
            }
        });
    }


    private void startAutoScroll() {
        Runnable r0 = new Runnable() {

            public void run() {
                try {
                    if (page <= binding.pdfView.getPageCount() || !isUpDirection) {
                        if (isMinus) {
                            PdfViewer pdfViewer = PdfViewer.this;
                            PdfViewer.access$1020(pdfViewer, pdfViewer.scrollSpeed);
                            if (scrollY <= 0) {
                                scrollY = 0;
                                isMinus = false;
                            }
                        } else {
                            PdfViewer pdfViewer2 = PdfViewer.this;
                            PdfViewer.access$1012(pdfViewer2, pdfViewer2.scrollSpeed);
                            int height = binding.scrollView.getChildAt(0).getHeight() - binding.scrollView.getHeight();
                            if (scrollY >= height) {
                                scrollY = height;
                                isMinus = true;
                            }
                        }
                        Log.e("TAG", "scrollY =>  " + scrollY);
                        PdfViewer pdfViewer3 = PdfViewer.this;
                        pdfViewer3.customSmoothScrollTo(pdfViewer3.binding.scrollView, scrollY);
                        PdfViewer pdfViewer4 = PdfViewer.this;
                        pdfViewer4.smoothScrollPDFView(pdfViewer4.page, scrollY);
                        if (isUpDirection) {
                            page++;
                        } else if (page > 0) {
                            page--;
                        }
                        handler.postDelayed(this, (long) (SCROLL_TIME_DELAY - scrollSpeed));
                        isAutoScrolling = true;
                    }
                } catch (Exception e) {
                    Log.e("TAG", "run: error " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        };
        this.scrollRunnable = r0;
        this.handler.postDelayed(r0, (long) (this.SCROLL_TIME_DELAY - this.scrollSpeed));
    }

    private void increaseScrollSpeed() {
        int i = this.scrollSpeed;
        if (i < 50) {
            this.scrollSpeed = i + 400;
        }
    }

    private void decreaseScrollSpeed() {
        int i = this.scrollSpeed;
        if (i > 1) {
            this.scrollSpeed = i - 400;
        }
    }

   
    private void customSmoothScrollTo(ScrollView scrollView, int i) {
        if (scrollView != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, i);
                }
            });
        }
    }

   
    private void smoothScrollPDFView(int i, int i2) {
        this.binding.pdfView.jumpTo(i, true);
    }

   
    @Override 
    public void onDestroy() {
        super.onDestroy();
    }

   
    private void viewPageByPagePdf() {
        boolean z = !this.isPageScrollEnabled;
        this.isPageScrollEnabled = z;
        if (z) {
            this.binding.scrollView.setOnTouchListener(this);
            this.binding.pdfView.setOnTouchListener(this);
            return;
        }
        this.binding.scrollView.setOnTouchListener(null);
    }

    private void rotateFile() {
        int i = getResources().getConfiguration().orientation;
        if (i == 2) {
            setRequestedOrientation(-1);
        } else if (i == 1) {
            setRequestedOrientation(0);
        }
    }

    @Override 
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (configuration.orientation == 2) {
            this.binding.pdfView.zoomTo(2.0f);
        } else {
            this.binding.pdfView.zoomTo(1.0f);
        }
    }

    private void print() {
        AppConstants.printPriview(this, this.printUri);
    }

    public void shareFile() {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.addFlags(1);
            intent.setType("*/*");
            if (this.isOutsidePdf) {
                intent.putExtra("android.intent.extra.STREAM", this.printUri);
            } else if (Build.VERSION.SDK_INT > 25) {
                intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this, "pdf.reader.pdfviewer.easyphotopicker.fileprovider", new File(this.pdfFileModel.getFilepath())));
            } else {
                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(this.pdfFileModel.getFilepath())));
            }
            startActivity(Intent.createChooser(intent, "Share Images...."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override 
    public void onBackPressed() {
        super.onBackPressed();
        if (!this.isOutsidePdf) {
            finish();
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.startX = motionEvent.getX();
            this.startY = motionEvent.getY();
            this.scrollY = this.binding.pdfView.getScrollY();
        } else if (action == 1) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            float f = x - this.startX;
            float f2 = y - this.startY;
            if (Math.abs(f) > Math.abs(f2)) {
                if (!this.binding.pdfView.isSwipeVertical()) {
                    if (f2 < 0.0f) {
                        this.binding.pdfView.jumpTo(this.binding.pdfView.getCurrentPage() + 1, true);
                    } else {
                        this.binding.pdfView.jumpTo(this.binding.pdfView.getCurrentPage() - 1, true);
                    }
                }
            } else if (f2 < 0.0f) {
                this.binding.pdfView.jumpTo(this.binding.pdfView.getCurrentPage() + 1, true);
            } else {
                this.binding.pdfView.jumpTo(this.binding.pdfView.getCurrentPage() - 1, true);
            }
            this.pagePreviewAdapter.selectedItemPosition = this.binding.pdfView.getCurrentPage();
            this.pagePreviewAdapter.notifyDataSetChanged();
        } else if (action == 261) {
            return false;
        }
        Log.e("TAG", "onTouch: " + motionEvent.getAction());
        return true;
    }
}
