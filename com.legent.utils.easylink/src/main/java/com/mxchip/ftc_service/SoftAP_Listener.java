/**
 * 
 */
package com.mxchip.ftc_service;

/**
 * @author Perry
 *
 */
public interface SoftAP_Listener {

	void onSoftAPconfigOK(int status_code);

	void onSoftAPconfigFail(int status_code);

	void onDeviceRegisterOK();

	void onDeviceRegisterFail();

	void onAPConnectOK();

	void onAPConnectFail();

	void onBindOK(String json);

	void onBindFail();
	
}
