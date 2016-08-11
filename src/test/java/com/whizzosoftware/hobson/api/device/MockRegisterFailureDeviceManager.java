package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.device.proxy.DeviceProxy;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import io.netty.util.concurrent.Future;

import java.util.Map;

public class MockRegisterFailureDeviceManager extends MockDeviceManager {
    @Override
    public Future publishDevice(final PluginContext pctx, final DeviceProxy proxy, final Map<String,Object> config) {
        return eventLoop.submit(new Runnable() {
            @Override
            public void run() {
                throw new HobsonRuntimeException("Something went wrong");
            }
        });
    }
}
