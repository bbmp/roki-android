package com.mxchip.ftc_service;

import java.net.Socket;

public interface FTC_Listener {
	void onFTCfinished(Socket s, String jsonString);

	void isSmallMTU(int MTU);
}
