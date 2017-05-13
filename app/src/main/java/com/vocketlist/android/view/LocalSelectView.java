package com.vocketlist.android.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.vocketlist.android.R;
import com.vocketlist.android.api.address.AddressModel;
import com.vocketlist.android.api.address.AddressServiceManager;
import com.vocketlist.android.api.address.FirstAddress;
import com.vocketlist.android.api.user.FavoritListModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by kinamare on 2017-04-29.
 * @author kinamare
 * 지역 선택 뷰
 */

public class LocalSelectView extends LinearLayout {

	private static final String TAG = LocalSelectView.class.getSimpleName();
	private static final int BASEVALUE = 0;

	@BindView(R.id.local_spinner) AppCompatSpinner local_spinner;
	@BindView(R.id.local_detail_spinner) AppCompatSpinner local_detail_spinner;

	private List<AddressModel.SecondAddress> getSecondAddress;
	private List<FirstAddress> sFirstAddress;
	private int localDetailId;

	public LocalSelectView(Context context) {
		this(context, null);
	}

	public LocalSelectView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LocalSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		View v = LayoutInflater.from(context).inflate(R.layout.local_select_view, this, true);
		ButterKnife.bind(this,v);

		initFirstSpinner();
		initSecondSpinner(BASEVALUE);
	}


	private void initFirstSpinner() {

		sFirstAddress = AddressServiceManager.getFirstAddressList();
		String[] firstArray = new String[sFirstAddress.size()];

		for (int i =0; i < sFirstAddress.size(); i++){
			firstArray[i] = sFirstAddress.get(i).mName;
		}

		ArrayAdapter localAdapter = new ArrayAdapter(
				getApplicationContext(), R.layout.custom_spinner_item,firstArray);

		localAdapter.setDropDownViewResource(
				R.layout.custom_spinner_dropdown_item);

		local_spinner.setAdapter(localAdapter);
		local_spinner.setPrompt("지역 설정");

		local_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
				initSecondSpinner(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});


	}

	private void initSecondSpinner(int position) {

		getSecondAddress = AddressServiceManager.getSecondAddress(sFirstAddress.get(position).mName);
		String[] secondArray = new String[getSecondAddress.size()];

		for (int i =0; i < getSecondAddress.size(); i++){
			secondArray[i] = getSecondAddress.get(i).mAddressName;
		}

		ArrayAdapter localAdapter = new ArrayAdapter(
				getApplicationContext(), R.layout.custom_spinner_item, secondArray);

		localAdapter.setDropDownViewResource(
				R.layout.custom_spinner_dropdown_item);

		local_detail_spinner.setAdapter(localAdapter);
		local_detail_spinner.setPrompt("지역 세부 설정");

		local_detail_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				localDetailId = getSecondAddress.get(i).mId;
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});


	}

	public int getLocalDetailId(){
		return localDetailId;
	}

	public void setInitValue(FavoritListModel.Region mAddress){

		int firstPosition = 0;
		int secondPosition = 0;
		List<Integer> array = new ArrayList<>();

		array.add(mAddress.mFirstAddressId);
		array.add(mAddress.mSecondAddressId);

		for(int i = 0; i< sFirstAddress.size(); i++){
			if(array.get(0)==sFirstAddress.get(i).mId){
				firstPosition = i;
			}
		}

		for(int i = 0; i< getSecondAddress.size(); i++){
			if(array.get(1)==getSecondAddress.get(i).mId){
				secondPosition = i;
			}
		}

		local_spinner.setSelection(firstPosition);
		local_detail_spinner.setSelection(secondPosition);
	}

}
