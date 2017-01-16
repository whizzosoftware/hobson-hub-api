package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.action.ActionClass;
import com.whizzosoftware.hobson.api.action.ActionClassProvider;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerSet;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class HobsonTaskTest {
    @Test
    public void testGetDependencies() {
        HobsonTask task = new HobsonTask();

        assertEquals(0, task.getDependencies(null).size());

        task.setConditions(Collections.singletonList(new PropertyContainer(PropertyContainerClassContext.create(HubContext.createLocal(), "pccc1"), null)));
        assertEquals(1, task.getDependencies(null).size());
        assertEquals("pccc1", task.getDependencies(null).iterator().next().getContainerClassId());

        task.setActionSet(new PropertyContainerSet("aid1"));
        assertEquals(1, task.getDependencies(null).size());

        Collection<PropertyContainerClassContext> deps = task.getDependencies(new ActionClassProvider() {
            @Override
            public ActionClass getActionClass(PropertyContainerClassContext ctx) {
                return null;
            }

            @Override
            public Collection<PropertyContainerClassContext> getActionSetClassContexts(String actionSetId) {
                return Collections.singletonList(PropertyContainerClassContext.create(HubContext.createLocal(), "pccc2"));
            }
        });

        assertEquals(2, deps.size());

        Iterator<PropertyContainerClassContext> it = deps.iterator();
        assertEquals("pccc1", it.next().getContainerClassId());
        assertEquals("pccc2", it.next().getContainerClassId());
    }
}
