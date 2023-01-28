package start.tf.com.timefriends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import start.tf.com.timefriends.activities.AlarmClockFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager mFm;

    /**
     * Tab未选中文字颜色
     */
    private int mUnSelectColor;

    /**
     * Tab选中时文字颜色
     */
    private int mSelectColor;

    /**
     * 滑动菜单视图
     */
    private ViewPager mViewPager;

    /**
     * Tab页面集合
     */
    private List<Fragment> mFragmentList;

    /**
     * 当前Tab的Index
     */
    private int mCurrentIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//            }
//        });
        mFm = getSupportFragmentManager();

        // 设置Tab页面集合
        mFragmentList = new ArrayList<>();
        // 展示闹钟的Fragment
        AlarmClockFragment mAlarmClockFragment = new AlarmClockFragment();
        mFragmentList.add(mAlarmClockFragment);

        // 设置ViewPager
        mViewPager = (ViewPager) findViewById(R.id.fragment_container);
        mViewPager.setAdapter(new MyFragmentPagerAdapter(mFm));
        // 设置一边加载的page数
        mViewPager.setOffscreenPageLimit(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ViewPager适配器
     */
    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

    }
}
