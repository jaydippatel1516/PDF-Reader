package pdf.reader.pdfviewer.activity;

import android.app.SearchManager;
import android.graphics.PorterDuff;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import pdf.reader.pdfviewer.R;
import pdf.reader.pdfviewer.baseClass.BaseActivityBinding;
import pdf.reader.pdfviewer.databinding.ActivityTextModeBinding;
import pdf.reader.pdfviewer.utility.AppConstants;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class TextModeActivity extends BaseActivityBinding {
    ActivityTextModeBinding binding;
    int defaultTextSize = 14;
    boolean isSearch = false;
    int pageNo = 0;
    List<String> pageWiseList;
    EditText searchPlate;
    SearchView searchView;

   
    @Override
    public void setOnClicks() {
    }

   
    @Override
    public void setBinding() {
        this.binding = (ActivityTextModeBinding) DataBindingUtil.setContentView(this, R.layout.activity_text_mode);
        AppConstants.setStatusBarWhite(this);
    }

   
    @Override 
    public void setToolbar() {
        this.binding.toolbarTextMode.toolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        this.binding.toolbarTextMode.back.setColorFilter(ContextCompat.getColor(this.context, R.color.font1), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(this.binding.toolbarTextMode.toolBar);
        this.binding.toolbarTextMode.cardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();

            }
        });
        this.binding.toolbarTextMode.toolBar.setCollapseIcon(ContextCompat.getDrawable(this, R.drawable.black_arrow_back));
    }
    @Override 
    public void initMethods() {
        this.pageWiseList = new ArrayList();
        this.pageWiseList = getIntent().getStringArrayListExtra("pageWiseList");
        this.binding.pdfData.setText(this.pageWiseList.get(this.pageNo));
        this.binding.pdfData.setTextSize(2, (float) this.defaultTextSize);
        TextView textView = this.binding.currentPage;
        textView.setText((this.pageNo + 1) + "");
        TextView textView2 = this.binding.totalPage;
        textView2.setText(" / " + this.pageWiseList.size());
    }

    public void onClick(View view) {
        if(view.getId()==R.id.btnFontDecrement){
            fontDecrement();

        } else  if(view.getId()==R.id.btnFontIncrement){
            fontIncrement();
        } else  if(view.getId()==R.id.btnNext){
            nextPage();

        } else  if(view.getId()==R.id.btnPrev){
            prevPage();

        }

    }

    private void fontDecrement() {
        this.defaultTextSize--;
        this.binding.pdfData.setTextSize(2, (float) this.defaultTextSize);
        if (this.isSearch) {
            this.binding.pdfData.setText(highlightText(this.searchPlate.getText().toString(), this.pageWiseList.get(this.pageNo)));
        } else {
            this.binding.pdfData.setText(this.pageWiseList.get(this.pageNo));
        }
        this.binding.currentPage.setText((this.pageNo + 1) + "");
        this.binding.totalPage.setText(" / " + this.pageWiseList.size());
    }

    private void fontIncrement() {
        this.defaultTextSize++;
        this.binding.pdfData.setTextSize(2, (float) this.defaultTextSize);
        if (this.isSearch) {
            this.binding.pdfData.setText(highlightText(this.searchPlate.getText().toString(), this.pageWiseList.get(this.pageNo)));
        } else {
            this.binding.pdfData.setText(this.pageWiseList.get(this.pageNo));
        }
        this.binding.currentPage.setText((this.pageNo + 1) + "");
        this.binding.totalPage.setText(" / " + this.pageWiseList.size());
    }

    private void prevPage() {
        int i = this.pageNo - 1;
        this.pageNo = i;
        if (i < 0) {
            this.pageNo = this.pageWiseList.size() - 1;
        }
        if (this.isSearch) {
            this.binding.pdfData.setText(highlightText(this.searchPlate.getText().toString(), this.pageWiseList.get(this.pageNo)));
        } else {
            this.binding.pdfData.setText(this.pageWiseList.get(this.pageNo));
        }
        this.binding.currentPage.setText((this.pageNo + 1) + "");
        this.binding.totalPage.setText(" / " + this.pageWiseList.size());
    }

    private void nextPage() {
        int i = this.pageNo + 1;
        this.pageNo = i;
        if (i > this.pageWiseList.size() - 1) {
            this.pageNo = 0;
        }
        if (this.isSearch) {
            this.binding.pdfData.setText(highlightText(this.searchPlate.getText().toString(), this.pageWiseList.get(this.pageNo)));
        } else {
            this.binding.pdfData.setText(this.pageWiseList.get(this.pageNo));
        }
        binding.currentPage.setText((this.pageNo + 1) + "");
        binding.totalPage.setText(" / " + this.pageWiseList.size());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchview, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconified(false);
        searchPlate= (EditText) this.searchView.findViewById(R.id.search_src_text);
        searchPlate.setTextColor(ContextCompat.getColor(this, R.color.black));
        ((ImageView) this.searchView.findViewById(R.id.search_close_btn)).setColorFilter(ViewCompat.MEASURED_STATE_MASK, PorterDuff.Mode.SRC_ATOP);
        this.searchView.setSearchableInfo(((SearchManager) getSystemService("search")).getSearchableInfo(getComponentName()));
        search(this.searchView);
        return super.onCreateOptionsMenu(menu);
    }

    private void search(SearchView searchView2) {
        SearchView.SearchAutoComplete searchAutoComplete =  searchView2.findViewById(R.id.search_src_text);
        searchAutoComplete.setHint("Search");
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.font1));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.font1));
        searchView2.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override 
            public boolean onClose() {
                TextModeActivity.this.isSearch = false;
                return false;
            }
        });
        if (searchView2 != null) {
            try {
                searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override 
                    public boolean onQueryTextSubmit(String str) {
                        return false;
                    }

                    @Override 
                    public boolean onQueryTextChange(String str) {
                        str.isEmpty();
                        TextModeActivity.this.isSearch = true;
                        TextModeActivity.this.highlightSearchText(str);
                        Log.e("TAG", "onQueryTextChange: " + str);
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   
    private void highlightSearchText(String str) {
        for (int i = 0; i < this.pageWiseList.size(); i++) {
            this.binding.pdfData.setText(highlightText(str, this.pageWiseList.get(i)));
        }
    }

    public static CharSequence highlightText(String str, String str2) {
        String lowerCase;
        int indexOf;
        if (str == null || str.equalsIgnoreCase("") || (indexOf = (lowerCase = Normalizer.normalize(str2, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase()).indexOf(str)) < 0) {
            return str2;
        }
        SpannableString spannableString = new SpannableString(str2);
        while (indexOf >= 0) {
            int min = Math.min(indexOf, str2.length());
            int min2 = Math.min(indexOf + str.length(), str2.length());
            spannableString.setSpan(new BackgroundColorSpan((int) InputDeviceCompat.SOURCE_ANY), min, min2, 33);
            indexOf = lowerCase.indexOf(str, min2);
        }
        return spannableString;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        menuItem.getItemId();
        return super.onOptionsItemSelected(menuItem);
    }
}
