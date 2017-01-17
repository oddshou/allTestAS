package com.oddshou.testall.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by win7 on 2016/12/3.
 */

public class UninstallDialogFragment extends DialogFragment {

    static UninstallDialogFragment newInstance(int num) {
        UninstallDialogFragment f = new UninstallDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.lay)


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
