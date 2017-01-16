package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.device.proxy.HobsonDeviceProxy;
import io.netty.util.concurrent.Future;

import java.util.Map;

public class MockRegisterFailureDeviceManager extends MockDeviceManager {
    @Override
    public Future publishDevice(final HobsonDeviceProxy proxy, final Map<String, Object> config, Runnable runnable) {
        return eventLoop.submit(new Runnable() {
            @Override
            public void run() {
                throw new HobsonRuntimeException("Something went wrong");
            }
        });
    }
}
