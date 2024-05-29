package pdf.reader.pdfviewer.listener;

import pdf.reader.pdfviewer.model.PdfFileModel;
import java.util.List;

public interface PdfDataGetListener {
    void onDatagetListener(List<PdfFileModel> list);

    void onMergeOpeation();

    void onPdfPickerOperation(PdfFileModel pdfFileModel, int i);
}
