package com.bunny.groovy.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.bunny.groovy.R;

import java.lang.reflect.Method;

import butterknife.Bind;

/**
 * 负责启动一个fragment
 */
public class FragmentContainerActivity extends BaseActivity {

    Fragment fragment = null;

    private Bundle values;
    /**
     * 添加到activity的fragment的tag
     */
    public static final String FRAGMENT_TAG = "FRAGMENT_CONTAINER";
    /**
     * 需要显示的标题
     */
    public static final String FRAGMENT_TITLE = "fragment_title";
    /**
     * fragment传递的参数（bundle）名称
     */
    public static final String FRAGMENT_BUNDLE_ARGS_KEY = "args";
    /**
     * acticity的启动模式
     */
    public static final String lunch_mode = "lunch_mode";
    //用于页面统计
    private String screenName;
    private int overrideTheme;

    /**
     * 启动一个界面
     *
     * @param activity
     * @param clazz
     * @param args
     */
    public static void launch(Context activity, Class<? extends Fragment> clazz, Bundle args) {
        //Attempt to invoke virtual method 'java.lang.String android.content.Context.getPackageName()' on a null object reference
        //判断activity是否空，避免该错误
        if (activity == null) return;

        Intent intent = new Intent(activity, FragmentContainerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("className", clazz.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (args != null) {
            intent.putExtra(FRAGMENT_BUNDLE_ARGS_KEY, args);
            initLunchMode(intent, args);
        }
        activity.startActivity(intent);
    }

    public static void launchForResult(Fragment fragment, Class<? extends Fragment> clazz, Bundle args, int requestCode) {
        if (fragment.getActivity() == null)
            return;
        Activity activity = fragment.getActivity();
        Intent intent = new Intent(activity, FragmentContainerActivity.class);
        intent.putExtra("className", clazz.getName());
        if (args != null) {
            intent.putExtra(FRAGMENT_BUNDLE_ARGS_KEY, args);
            initLunchMode(intent, args);
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void launchForResult(Activity from, Class<? extends Fragment> clazz, Bundle args, int requestCode) {
        //Attempt to invoke virtual method 'java.lang.String android.content.Context.getPackageName()' on a null object reference
        //判断activity是否空，避免该错误
        if (from == null) return;

        Intent intent = new Intent(from, FragmentContainerActivity.class);
        intent.putExtra("className", clazz.getName());
        if (args != null) {
            intent.putExtra(FRAGMENT_BUNDLE_ARGS_KEY, args);
            initLunchMode(intent, args);
        }

        from.startActivityForResult(intent, requestCode);
    }

    private static void initLunchMode(Intent intent, Bundle args) {
        int anInt = args.getInt(lunch_mode, -3);
        if (anInt != -3) {
            intent.addFlags(anInt);
        }
    }

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.toolbar_title)
    TextView toolBarTitle;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void onCreate(Bundle savedInstanceState) {

        String className = getIntent().getStringExtra("className");
        if (TextUtils.isEmpty(className)) {
            finish();
            return;
        }

        values = getIntent().getBundleExtra(FRAGMENT_BUNDLE_ARGS_KEY);

        if (savedInstanceState == null) {
            try {
                Class clazz = Class.forName(className);
                fragment = (Fragment) clazz.newInstance();
                // 设置参数给Fragment
                if (values != null) {
                    try {
                        Method method = clazz.getMethod("setArguments", Bundle.class);
                        method.invoke(fragment, values);
                    } catch (Exception e) {
                    }
                }
                // 重写Activity的主题
                try {
                    Method method = clazz.getMethod("setTheme");
                    if (method != null)
                        overrideTheme = Integer.parseInt(method.invoke(fragment).toString());
                } catch (Exception e) {
                }

            } catch (Exception e) {
                e.printStackTrace();
                finish();
                return;
            }
        }

        super.onCreate(savedInstanceState);

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment, FRAGMENT_TAG).commit();
        }

    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    @Override
    public void initView() {
        super.initView();
        String title = values.getString(FRAGMENT_TITLE);
        if (values == null || TextUtils.isEmpty(title)) {
            toolBarTitle.setText(getString(R.string.app_name));
        } else {
            toolBarTitle.setText(title.toUpperCase());
        }
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_fragment_container_layout;
    }

    @Override
    public boolean enableSlideClose() {
        return false;
    }

    public Fragment getFragment() {
        return fragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void finish() {
        if (fragment != null) {
            fragment.onDestroy();
            fragment = null;
        }
        super.finish();
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
        } catch (Exception e) {
        }

    }
}
