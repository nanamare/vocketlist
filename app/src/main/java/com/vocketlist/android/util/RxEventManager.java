package com.vocketlist.android.util;

import rx.subjects.PublishSubject;

/**
 * description nanamare.tistory.com/8
 * Created by kinamare on 2017-05-12.
 */

public class RxEventManager {

	private static RxEventManager mInstance;

	private PublishSubject<Object> mData;

	private RxEventManager(){
		mData = PublishSubject.create();
	}

	public static RxEventManager getInstance(){
		if(mInstance == null) {
			mInstance = new RxEventManager();
		}
		return mInstance;
	}

	public void sendData(Object data){
		mData.onNext(data);
	}

	public rx.Observable<Object> getObjectObservable(){
		return mData;
	}

}
