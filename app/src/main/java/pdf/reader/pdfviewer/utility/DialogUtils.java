package pdf.reader.pdfviewer.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import pdf.reader.pdfviewer.R;
import pdf.reader.pdfviewer.databinding.DialogSaveBinding;

import pdf.reader.pdfviewer.listener.ProgressListener;

public class DialogUtils {
    Activity context;
    public AlertDialog dialog;
    public AlertDialog dialogExit;
    boolean isCheck = false;
    boolean ispassshow;
    TextView pagecount;
    public String passwordtext = "";
    public RoundCornerProgressBar progressBar;
    ProgressListener progressListener;
    public TextView progressText;
    SaveListenere saveListenere;
    int totalFile = 0;

    public interface SaveListenere {
        void onSaveClickListener(String str, String str2, boolean z);
    }


    public DialogUtils(Activity activity, boolean z, SaveListenere saveListenere2) {
        this.context = activity;
        this.saveListenere = saveListenere2;
        this.ispassshow = z;
    }

    public DialogUtils(Activity activity, ProgressListener progressListener2, int i) {
        this.context = activity;
        this.progressListener = progressListener2;
        this.totalFile = i;
    }


    public void onSaveDialog() {
        AlertDialog alertDialog = this.dialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.dialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        final DialogSaveBinding dialogSaveBinding = (DialogSaveBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.dialog_save, null, false);
        builder.setView(dialogSaveBinding.getRoot());
        if (this.ispassshow) {
            dialogSaveBinding.layout.setVisibility(View.GONE);
        } else {
            dialogSaveBinding.layout.setVisibility(View.GONE);
        }
        AlertDialog create = builder.create();
        this.dialog = create;
        create.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        this.dialog.show();
        dialogSaveBinding.logo.cardlogo.setCardBackgroundColor(ContextCompat.getColor(this.context, R.color.bg_color));
        dialogSaveBinding.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    DialogUtils.this.isCheck = true;
                    dialogSaveBinding.passwordlayout.setVisibility(View.VISIBLE);
                    return;
                }
                DialogUtils.this.passwordtext = "";
                DialogUtils.this.isCheck = false;
                dialogSaveBinding.passwordlayout.setVisibility(View.GONE);
            }
        });
        dialogSaveBinding.password.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                DialogUtils.this.passwordtext = charSequence.toString();
            }
        });
        dialogSaveBinding.bottom.cardSave.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                if (dialogSaveBinding.name.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DialogUtils.this.context, "File name can not be empty", 0).show();
                } else if (DialogUtils.this.ispassshow) {
                    DialogUtils.this.saveListenere.onSaveClickListener(dialogSaveBinding.name.getText().toString().trim(), DialogUtils.this.passwordtext, DialogUtils.this.isCheck);
                } else {
                    DialogUtils.this.saveListenere.onSaveClickListener(dialogSaveBinding.name.getText().toString().trim(), "", false);
                }
            }
        });
        dialogSaveBinding.bottom.cardCancel.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                DialogUtils.this.dialog.dismiss();
            }
        });
        this.dialog.getWindow().setBackgroundDrawableResource(17170445);
    }

    public void dismissProgressDialog() {
        AlertDialog alertDialog = this.dialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.dialog.dismiss();
        }
        AlertDialog alertDialog2 = this.dialogExit;
        if (alertDialog2 != null && alertDialog2.isShowing()) {
            this.dialogExit.dismiss();
        }
    }


}
