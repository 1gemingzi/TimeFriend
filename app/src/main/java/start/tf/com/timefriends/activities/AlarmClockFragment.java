package start.tf.com.timefriends.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import start.tf.com.timefriends.R;
import start.tf.com.timefriends.adapter.AlarmClockAdapter;
import start.tf.com.timefriends.bean.AlarmClock;
import start.tf.com.timefriends.constant.Constants;
import start.tf.com.timefriends.database.AlarmClockOperate;
import start.tf.com.timefriends.listener.OnItemClickListener;
import start.tf.com.timefriends.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

public class AlarmClockFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AlarmClockFragment";

    /**
     * 新建闹钟的requestCode
     */
    private static final int REQUEST_ALARM_CLOCK_NEW = 1;

    /**
     * 修改闹钟的requestCode
     */
    private static final int REQUEST_ALARM_CLOCK_EDIT = 2;

    /**
     * 闹钟列表
     */
    private RecyclerView mRecyclerView;

    /**
     * 保存闹钟信息的list
     */
    private List<AlarmClock> mAlarmClockList;

    /**
     * 保存闹钟信息的adapter
     */
    private AlarmClockAdapter mAdapter;

    /**
     * 操作栏编辑按钮
     */
    private ImageView mEditAction;

    /**
     * 操作栏编辑完成按钮
     */
    private ImageView mAcceptAction;

    /**
     * List内容为空时的视图
     */
    private LinearLayout mEmptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "enter onCreate");
//        OttoAppConfig.getInstance().register(this);
        mAlarmClockList = new ArrayList<>();
        mAdapter = new AlarmClockAdapter(getActivity(), mAlarmClockList);
        // 注册Loader
//        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "enter onCreateView");
        View view = inflater.inflate(R.layout.fm_alarm_clock, container, false);
        view.setBackgroundColor(R.color.blue_green);

        mEmptyView = (LinearLayout) view
                .findViewById(R.id.alarm_clock_empty);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_view);
        mRecyclerView.setHasFixedSize(true);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.setLayoutManager(new ErrorCatchLinearLayoutManager(getActivity(),
//                LinearLayoutManager.VERTICAL, false));
        //设置Item增加、移除动画
//        mRecyclerView.setItemAnimator(new ScaleInLeftAnimator(new OvershootInterpolator(1f)));
        mRecyclerView.getItemAnimator().setAddDuration(300);
        mRecyclerView.getItemAnimator().setRemoveDuration(300);
        mRecyclerView.getItemAnimator().setMoveDuration(300);
        mRecyclerView.getItemAnimator().setChangeDuration(300);
        mRecyclerView.setAdapter(mAdapter);

//        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        // 监听闹铃item点击事件Listener
//        AdapterView.OnItemClickListener onItemClickListener = new OnItemClickListenerImpl();
//        mAdapter.setOnItemClickListener(onItemClickListener);

        // 操作栏新建按钮
        ImageView newAction = (ImageView) view.findViewById(R.id.action_new);
        newAction.setOnClickListener(this);

        // 编辑闹钟
        mEditAction = (ImageView) view.findViewById(R.id.action_edit);
        mEditAction.setOnClickListener(this);

        // 完成按钮
        mAcceptAction = (ImageView) view.findViewById(R.id.action_accept);
        mAcceptAction.setOnClickListener(this);

        updateList();
        return view;
    }


    private void updateList() {
        Log.i(TAG, "enter updateList");
        mAlarmClockList.clear();

        List<AlarmClock> list = AlarmClockOperate.getInstance().loadAlarmClocks();
        for (AlarmClock alarmClock : list) {
            mAlarmClockList.add(alarmClock);

            // 当闹钟为开时刷新开启闹钟
            if (alarmClock.isOnOff()) {
                MyUtil.startAlarmClock(getActivity(), alarmClock);
            }
        }

        checkIsEmpty(list);

        mAdapter.notifyDataSetChanged();
    }

    private SensorManager mSensorManager;
    private SensorEventListener mSensorEventListener;
    private AlarmClock mDeletedAlarmClock;
    private void checkIsEmpty(List<AlarmClock> list) {
        if (list.size() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);

            if (mSensorManager != null) {
                mSensorManager.unregisterListener(mSensorEventListener);
            }
        }
    }



    class OnItemClickListenerImpl implements OnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            // 不响应重复点击
            if (MyUtil.isFastDoubleClick()) {
                return;
            }
            AlarmClock alarmClock = mAlarmClockList.get(position);
            Intent intent = new Intent(getActivity(),
                    AlarmClockEditActivity.class);
            intent.putExtra(Constants.ALARM_CLOCK, alarmClock);
            // 开启编辑闹钟界面
            startActivityForResult(intent, REQUEST_ALARM_CLOCK_EDIT);
            // 启动移动进入效果动画
//            getActivity().overridePendingTransition(R.anim.move_in_bottom,
//                    0);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            // 显示删除，完成按钮，隐藏修改按钮
//            displayDeleteAccept();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_new:
                // 不响应重复点击
                if (MyUtil.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(getActivity(),
                        AlarmClockNewActivity.class);
                // 开启新建闹钟界面
                startActivityForResult(intent, REQUEST_ALARM_CLOCK_NEW);
//                // 启动渐变放大效果动画
//                getActivity().overridePendingTransition(R.anim.zoomin, 0);
                break;
            case R.id.action_edit:
                // 当列表内容为空时禁止响应编辑事件
                if (mAlarmClockList.size() == 0) {
                    return;
                }
                // 显示删除，完成按钮，隐藏修改按钮
//                displayDeleteAccept();
                break;
            case R.id.action_accept:
                // 隐藏删除，完成按钮,显示修改按钮
//                hideDeleteAccept();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        AlarmClock ac = data.getParcelableExtra(Constants.ALARM_CLOCK);
        switch (requestCode) {
            // 新建闹钟
            case REQUEST_ALARM_CLOCK_NEW:
                // 插入新闹钟数据
//                TabAlarmClockOperate.getInstance(getActivity()).insert(ac);
                AlarmClockOperate.getInstance().saveAlarmClock(ac);
                addList(ac);

//                showAlarmExplain();
                break;
            // 修改闹钟
            case REQUEST_ALARM_CLOCK_EDIT:
                // 更新闹钟数据
//                TabAlarmClockOperate.getInstance(getActivity()).update(ac);
                AlarmClockOperate.getInstance().updateAlarmClock(ac);
                updateList();
                break;

        }
    }

    private void addList(AlarmClock ac) {
        mAlarmClockList.clear();
        int id = ac.getId();
        int count = 0;
        int position = 0;
        List<AlarmClock> list = AlarmClockOperate.getInstance().loadAlarmClocks();
        for (AlarmClock alarmClock : list) {
            mAlarmClockList.add(alarmClock);

            if (id == alarmClock.getId()) {
                position = count;
                if (alarmClock.isOnOff()) {
                    MyUtil.startAlarmClock(getActivity(), alarmClock);
                }
            }
            count++;
        }

        checkIsEmpty(list);
        mAdapter.notifyItemInserted(position);
        mRecyclerView.scrollToPosition(position);
    }
}
