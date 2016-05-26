package com.test.boatstagram;

import java.util.ArrayList;

public interface OnBoatListListener {

    public void onSuccess(ArrayList<BoatStagramItem> boatStagramItemArrayList);

    public void onError();

}
