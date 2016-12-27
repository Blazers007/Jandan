// IOfflineDownloadInterface.aidl
package com.blazers.jandan;

// Declare any non-default types here with import statements

interface IOfflineDownloadInterface {
	void startDownloadNews(int fromPage, int pageSize);
	void startDownloadPicture(String currentType, int fromPage, int pageSize);
	void startDownloadJokes(int fromPage, int pageSize);
}
