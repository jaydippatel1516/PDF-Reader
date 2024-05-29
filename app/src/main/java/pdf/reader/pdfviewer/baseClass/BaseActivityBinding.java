package pdf.reader.pdfviewer.baseClass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import pdf.reader.pdfviewer.utility.BetterActivityResult;

public abstract class BaseActivityBinding extends AppCompatActivity implements View.OnClickListener {
    protected final BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);
    public Context context;

   
    public abstract void initMethods();

   
    public abstract void setBinding();

   
    public abstract void setOnClicks();

   
    public abstract void setToolbar();

   
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.context = this;
        setBinding();
        setToolbar();
        setOnClicks();
        initMethods();
    }
}
