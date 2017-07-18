package com.violet.library.app.windows.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.violet.library.R;
import com.violet.library.views.ProgressWheel;

/**
 * description：网络请求显示进度条(material)
 * author：JimG on 16/10/10 10:31
 * e-mail：info@zijinqianbao.com
 */

public class MaterialProgress extends Dialog{
    private ProgressWheel proWheel;
    public MaterialProgress(Context context) {
        this(context,R.style.netLoadingDialog);
    }

    public MaterialProgress(Context context, int theme) {
        super(context, theme);

        View view = View.inflate(context,R.layout.base_progress_dialog,null);
        proWheel = (ProgressWheel) view.findViewById(R.id.proWheel);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(view);
    }

    @Override
    public void show() {
        if(proWheel != null) proWheel.spin();
        super.show();
    }

    @Override
    public void dismiss() {
        if(proWheel != null) proWheel.stopSpinning();
        super.dismiss();
    }

    public ProgressWheel getProgressWheel(){
        return proWheel;
    }
}
