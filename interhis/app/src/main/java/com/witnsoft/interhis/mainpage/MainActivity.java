package com.witnsoft.interhis.mainpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.witnsoft.interhis.R;
import com.witnsoft.libinterhis.base.BaseActivity;
import com.witnsoft.libinterhis.db.HisDbManager;
import com.witnsoft.libinterhis.db.model.ChineseDetailModel;
import com.witnsoft.libinterhis.db.model.ChineseModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengchengpeng on 2017/5/12.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
<<<<<<< HEAD



=======
>>>>>>> 4ae13319cd384257b6c26a106582a8c4fe38a020
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

}
