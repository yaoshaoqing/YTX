package ytx.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ytx.app.main.MainActivity;
import ytx.app.R;

/**
 * Created by vi爱 on 2018/1/10.
 */

public class PersonalFragment extends BaseFragment{
    protected View view;
    protected MainActivity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.personal_view,container,false);
        return this.view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //首页
        activity = (MainActivity) this.getActivity();
        //index();
    }

    protected void init(){

    }

    @Override
    protected void lazyLoad() {

    }
}

