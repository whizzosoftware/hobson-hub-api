/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.task.condition.ConditionClassType;
import com.whizzosoftware.hobson.api.task.condition.ConditionEvaluationContext;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClass;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClassProvider;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class TaskHelperTest {
    @Test
    public void testGetTriggerCondition() {
        PluginContext pctx = PluginContext.createLocal("plugin1");
        MockTaskConditionClassProvider ccp = new MockTaskConditionClassProvider();
        PropertyContainerClassContext pccc1 = PropertyContainerClassContext.create(pctx, "cc1");
        PropertyContainerClassContext pccc2 = PropertyContainerClassContext.create(pctx, "cc2");
        ccp.addConditionClass(new MockTaskConditionClass(pccc1, "CC1", "Foo", ConditionClassType.trigger, null));
        ccp.addConditionClass(new MockTaskConditionClass(pccc2, "CC2", "Bar", ConditionClassType.evaluator, null));

        List<PropertyContainer> conditions = new ArrayList<>();
        conditions.add(new PropertyContainer(pccc1, null));
        conditions.add(new PropertyContainer(pccc2, null));

        assertEquals("cc1", TaskHelper.getTriggerCondition(ccp, conditions).getContainerClassContext().getContainerClassId());
        assertNull(TaskHelper.getTriggerCondition(ccp, Collections.singletonList(new PropertyContainer(pccc2, null))));
    }

    private static class MockTaskConditionClassProvider implements TaskConditionClassProvider {
        private Map<PropertyContainerClassContext,TaskConditionClass> cclasses = new HashMap<>();

        public void addConditionClass(TaskConditionClass cc) {
            cclasses.put(cc.getContext(), cc);
        }

        @Override
        public TaskConditionClass getConditionClass(PropertyContainerClassContext ctx) {
            return cclasses.get(ctx);
        }
    }

    private static class MockTaskConditionClass extends TaskConditionClass {
        private ConditionClassType type;
        private List<TypedProperty> properties;

        public MockTaskConditionClass(PluginContext context, String id, String name, String descriptionTemplate, ConditionClassType type, List<TypedProperty> properties) {
            super(context, id, name, descriptionTemplate);
            this.type = type;
            this.properties = properties;
        }

        public MockTaskConditionClass(PropertyContainerClassContext context, String name, String descriptionTemplate, ConditionClassType type, List<TypedProperty> properties) {
            super(context, name, descriptionTemplate);
            this.type = type;
            this.properties = properties;
        }

        @Override
        public ConditionClassType getConditionClassType() {
            return type;
        }

        @Override
        public boolean evaluate(ConditionEvaluationContext context, PropertyContainer values) {
            return false;
        }

        @Override
        protected List<TypedProperty> createProperties() {
            return properties;
        }
    }
}
