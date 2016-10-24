package com.whizzosoftware.hobson.api.action;

import com.whizzosoftware.hobson.api.action.job.AsyncJobHandle;
import com.whizzosoftware.hobson.api.action.job.JobInfo;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerSet;

import java.util.Collection;

public class MockActionManager implements ActionManager {
    @Override
    public void addJobStatusMessage(PluginContext ctx, String msgName, Object o) {

    }

    @Override
    public AsyncJobHandle executeAction(PropertyContainer action) {
        return null;
    }

    @Override
    public AsyncJobHandle executeActionSet(PropertyContainerSet actionSet) {
        return null;
    }

    @Override
    public ActionClass getActionClass(PropertyContainerClassContext ctx) {
        return null;
    }

    @Override
    public Collection<ActionClass> getActionClasses(PluginContext ctx) {
        return null;
    }

    @Override
    public Collection<ActionClass> getActionClasses(HubContext ctx, boolean applyConstraints) {
        return null;
    }

    @Override
    public JobInfo getJobInfo(HubContext ctx, String jobId) {
        return null;
    }

    @Override
    public boolean hasActionClass(PropertyContainerClassContext ctx) {
        return false;
    }

    @Override
    public void publishActionClass(ActionClass actionClass) {

    }

    @Override
    public void unpublishActionClasses(PluginContext ctx) {

    }
}
