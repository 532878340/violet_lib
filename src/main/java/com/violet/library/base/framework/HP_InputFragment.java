package com.violet.library.base.framework;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by perley on 15/12/22.
 */
public abstract class HP_InputFragment extends HP_Fragment {

    private final List<TextView> mInputViews = new ArrayList<>();
    private final List<CheckBox> mCheckViews = new ArrayList<>();

    private boolean mAllNotEmpty;

    private InputWatcher mInputWatcher;

    protected final void watchEditText(TextView editText) {
        if (mInputWatcher == null) {
            mInputWatcher = new InputWatcher();
        }
        mInputViews.add(editText);
        editText.addTextChangedListener(mInputWatcher);
    }

    protected final void watchCheckBox(CheckBox check) {
        if (mInputWatcher == null) {
            mInputWatcher = new InputWatcher();
        }
        mCheckViews.add(check);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mInputWatcher.afterTextChanged(null);
            }
        });
    }

    /**
     * 输入框空白状态发生变化时回调
     * @param allNotEmpty
     */
    protected void onInputEmptyChanged(boolean allNotEmpty) {

    }


    class InputWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean allNotEmpty = true;
            for (int i = 0; i < mInputViews.size(); i++) {
                if(TextUtils.isEmpty(mInputViews.get(i).getText())){
                    allNotEmpty = false;
                    break;
                }
            }

            if (allNotEmpty) {
                for (Checkable check : mCheckViews) {
                    if (!check.isChecked()) {
                        allNotEmpty = false;
                        break;
                    }
                }
            }

            if (mAllNotEmpty != allNotEmpty) {
                onInputEmptyChanged(allNotEmpty);
                mAllNotEmpty = allNotEmpty;
            }

        }
    }

}
