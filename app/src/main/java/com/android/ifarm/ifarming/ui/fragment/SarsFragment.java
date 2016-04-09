package com.android.ifarm.ifarming.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.android.ifarm.ifarming.R;
import com.android.ifarm.ifarming.app.AppConfig;
import com.android.ifarm.ifarming.ui.db.DicFarm;
import com.android.ifarm.ifarming.ui.db.DicSars;
import com.android.ifarm.ifarming.ui.event.AddFarmEvent;
import com.android.ifarm.ifarming.ui.event.FarmEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SarsFragment extends BaseFragment {

    public static SarsFragment newFragment() {
        SarsFragment fragment = new SarsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sars, container, false);
        bindView(this, view);
        return view;
    }

    ArrayAdapter adapterFrom, adapterType, adapterPz;
    String sFrom, sType, sPz;
    List<DicFarm> farms;
    ArrayList<String> mData;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //        farms = new Select().from(DicFarm.class).where("DicUid = ?", AppConfig.getUserId()).execute();//根据用户id搜索
        farms = new Select().from(DicFarm.class).execute();//搜索全部
        mData = new ArrayList<>();
        for (DicFarm farm : farms) {
            mData.add(farm.dicName);
        }
        adapterFrom = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mData);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFrom.setAdapter(adapterFrom);
        mFrom.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sFrom = adapterFrom.getItem(arg2).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        adapterType = ArrayAdapter.createFromResource(getActivity(), R.array.type, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(adapterType);
        mType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sType = adapterType.getItem(arg2).toString();
                if (adapterType.getItem(arg2).toString().equals("牛")) {
                    adapterPz = ArrayAdapter.createFromResource(getActivity(), R.array.niu, android.R.layout.simple_spinner_item);
                } else if (adapterType.getItem(arg2).toString().equals("羊")) {
                    adapterPz = ArrayAdapter.createFromResource(getActivity(), R.array.yang, android.R.layout.simple_spinner_item);
                } else if (adapterType.getItem(arg2).toString().equals("猪")) {
                    adapterPz = ArrayAdapter.createFromResource(getActivity(), R.array.zhu, android.R.layout.simple_spinner_item);
                } else if (adapterType.getItem(arg2).toString().equals("鸡")) {
                    adapterPz = ArrayAdapter.createFromResource(getActivity(), R.array.ji, android.R.layout.simple_spinner_item);
                }
                adapterPz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mPz.setAdapter(adapterPz);
                mPz.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        sPz = adapterPz.getItem(arg2).toString();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Bind(R.id.from)
    AppCompatSpinner mFrom;
    @Bind(R.id.type)
    AppCompatSpinner mType;
    @Bind(R.id.pinzhong)
    AppCompatSpinner mPz;
    @Bind(R.id.num)
    TextView mNum;
    @Bind(R.id.time)
    TextView mTime;

    @OnClick(R.id.read)
    void onRead() {

    }

    @OnClick(R.id.photo)
    void onPhoto() {

    }

    @OnClick(R.id.save)
    void onSave() {
        if (farms.size() == 0) {
            Snackbar.make(mFrom, "暂时还没有添加养殖场信息！", Snackbar.LENGTH_SHORT).setAction("现在去添加", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postEvent(new AddFarmEvent());
                }
            }).show();
            return;
        }
        DicSars sars = new DicSars(sFrom, sType, sPz, "", System.currentTimeMillis(), "", AppConfig.getUserId());
        sars.save();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
        Bundle args = getArguments();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterEventBus();
    }

    @Subscribe
    public void onEvent(FarmEvent event) {
        super.onEvent(event);
        //        farms = new Select().from(DicFarm.class).where("DicUid = ?", AppConfig.getUserId()).execute();//根据用户id搜索
        farms = new Select().from(DicFarm.class).execute();//搜索全部
        mData.clear();
        for (DicFarm farm : farms) {
            mData.add(farm.dicName);
        }
        adapterFrom.notifyDataSetChanged();
    }
}
