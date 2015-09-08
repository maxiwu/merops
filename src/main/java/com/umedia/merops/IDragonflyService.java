package com.umedia.merops;

import java.util.List;

public interface IDragonflyService {

	List<String> getDevices() throws DragonflyException;
	String readDeviceStatus(String deviceId) throws DragonflyException;
}
