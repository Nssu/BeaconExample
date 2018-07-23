package com.minewbeacon.blescan.demo;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.MinewBeacon;
import com.yuliwuli.blescan.demo.R;

import java.util.ArrayList;
import java.util.List;


public class BeaconListAdapter extends RecyclerView.Adapter<BeaconListAdapter.MyViewHolder> {

    private List<MinewBeacon> mMinewBeacons = new ArrayList<>();

    /**
     * 뷰 홀더를 생성하고, 뷰를 붙여주는 부분
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.main_item, null);
        return new MyViewHolder(view);
    }

    /**
     * 재활용 되는 뷰가 호출하여 실행되는 메소드
     * 뷰 홀더를 전달하고 어댑터는 position의 데이터를 결합
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setDataAndUi(mMinewBeacons.get(position));
    }

    /**
     * 데이터 개수 반환
     * @return
     */
    @Override
    public int getItemCount() {
        if (mMinewBeacons != null) {
            return mMinewBeacons.size();
        }
        return 0;
    }

    public void setData(List<MinewBeacon> minewBeacons) {
        this.mMinewBeacons = minewBeacons;

//        notifyItemRangeChanged(0,minewBeacons.size());
        notifyDataSetChanged();

    }

    /**
     * 비콘 리스트를 설정하는 곳
     * 사이즈 및 리스트 내용을 수정
     * @param newItems
     */
    public void setItems(List<MinewBeacon> newItems) {
//        validateItems(newItems);


        int startPosition = 0;
        int preSize = 0;
        if (this.mMinewBeacons != null) {
            preSize = this.mMinewBeacons.size();

        }
        if (preSize > 0) {
            this.mMinewBeacons.clear();
            notifyItemRangeRemoved(startPosition, preSize);
        }
        this.mMinewBeacons.addAll(newItems);
        notifyItemRangeChanged(startPosition, newItems.size());
    }

    /**
     * ViewHolder
     * http://uroa.tistory.com/6
     * 뷰 여러개를 관리하기 위한 객체, findViewById의 비용을 줄이기 위함
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mDevice_mac;
        private MinewBeacon mMinewBeacon;
        private final TextView mDevice_temphumi;
        private final TextView mDevice_name;
        private final TextView mDevice_uuid;
        private final TextView mDevice_other;


        public MyViewHolder(View itemView) {
            super(itemView);
            mDevice_name = (TextView) itemView.findViewById(R.id.device_name);
            mDevice_uuid = (TextView) itemView.findViewById(R.id.device_uuid);
            mDevice_other = (TextView) itemView.findViewById(R.id.device_other);
            mDevice_temphumi = (TextView) itemView.findViewById(R.id.device_temphumi);
            mDevice_mac = (TextView) itemView.findViewById(R.id.device_mac);
        }

        /**
         * 각 데이터를 설정하여, TextView에 연동
         * Beacon 설정 예시가 들어가 있다.
         * @param minewBeacon
         */
        public void setDataAndUi(MinewBeacon minewBeacon) {
            mMinewBeacon = minewBeacon;
            mDevice_name.setText(mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue());
            mDevice_mac.setText(mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_MAC).getStringValue());
            mDevice_uuid.setText("UUID:" + mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_UUID).getStringValue());
            String battery = mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_BatteryLevel).getStringValue();
            int batt = Integer.parseInt(battery);
            if (batt > 100) { // 100보다 넘는 값 존재시, 100으로 설정
                batt = 100;
            }

            String format = String.format("Major:%s Minor:%s Rssi:%s Battery:%s",
                    mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue(),
                    mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue(),
                    mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getStringValue(),
                    batt + "");
            mDevice_other.setText(format);

            /**
             * 온도와 습도가 모두 0일때 Hex 0 값을 설정한다.
             */
            if (mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Humidity).getFloatValue() == 0 &&
                    mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Temperature).getFloatValue() == 0) {
                mDevice_temphumi.setVisibility(View.GONE);
            } else {
                mDevice_temphumi.setVisibility(View.VISIBLE);
                String temphumi = String.format("Temperature:%s ℃   Humidity:%s ",
                        mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Temperature).getFloatValue() + "",
                        mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Humidity).getFloatValue() + "");

                mDevice_temphumi.setText(temphumi + "%");
            }

        }
    }
}
