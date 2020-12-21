package org.swampscottcurrents.serpentframework.gameplan;

import java.util.List;

import com.google.common.collect.ImmutableList;

import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

@Description(group = "4311 Swampscott Currents", name = "GamePlan", summary = "Facilitates the creation of custom autonomous robot routines at runtime.", version = "2.0.0.1")
public class App extends Plugin {
	/** Returns a list of all the widgets contained in this plugin. */
	@Override
	@SuppressWarnings("rawtypes")
	public List<ComponentType> getComponents() {
		return ImmutableList.of(WidgetType.forAnnotatedWidget(GamePlanWidget.class));
	}
}
