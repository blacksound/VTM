VTMContextView : VTMView {
	var <context;
	var labelView;
	var contentView;
	var headerView;
	var parameterManagerView;
	classvar <unitSize;
	classvar <defaultWidth;

	*initClass{
		unitSize = Size(150, 25);
		defaultWidth = unitSize.width;
	}

	*new{arg parent, bounds, definition, attributes, context;
		^super.new(parent, bounds, definition, attributes).initContextView(context);
	}

	initContextView{arg context_;
		context = context_;
		context.addDependant(this);

		labelView = StaticText().string_(context.name).font_(this.font);
		labelView.background_(Color.green);

		parameterManagerView = VTMContextParameterManagerView.new(this);

		contentView = View(this).layout_(
			VLayout(
				[parameterManagerView.background_(Color.yellow), align: \topLeft],
				// StaticText().string_("context content view").font_(this.font).background_(Color.cyan)
			).spacing_(0).margins_(0);
		);

		headerView = View(this).background_(Color.blue);
		headerView.layout_(
			HLayout(
				[labelView.fixedSize_(Size(150, 25)), align: \topLeft]
			).spacing_(0).margins_(0);
		);
		headerView.minWidth_(this.class.defaultWidth);
		headerView.maxHeight_(25);

		this.layout_(
			VLayout(
				headerView,
				contentView
			).spacing_(0).margins_(0);
		);
		this.minWidth_(this.class.defaultWidth);
		this.layout.spacing_(0).margins_(0);
		/*this.refreshLabel;*/
		this.refresh;
	}

	free{
		context.removeDependant(this);
		{this.remove;}.defer;
	}

	refreshLabel{
		{
			labelView.string_(context.name);
			labelView.toolTip = context.path;
		}.defer;
	}

	refreshContextView{arg what = \all;
		switch(what,
			\scenes, {
				this.refreshSceneList();
			},
			\modules, {
				this.refreshModulesList();
			},
			\hardware, {
				this.refreshHardwareList();
			},
			\network, {
				this.refreshNetworkList();
			},
			{
				this.refreshSceneList();
				this.refreshModulesList();
				this.refreshHardwareList();
				this.refreshNetworkList();
			}
		)
	}

	refreshSceneList{}
	refreshModulesList{}
	refreshHardwareList{}
	refreshNetworkList{}

	//pull style update
	update{arg theChanged, whatChanged, whoChangedIt, toValue;
		"Dependant update: % % % %".format(theChanged, whatChanged, whoChangedIt, toValue).postln;
		if(theChanged === context, {//only update the view if the parameter changed
			switch(whatChanged,
				//\enabled, { this.enabled_(context.enabled); },
				\path, { this.refreshLabel; },
				\name, { this.refreshLabel; },
				\freed, { this.free; }
			);
			{this.refresh;}.defer;
		}, {
			super.update(theChanged, whatChanged, whoChangedIt, toValue);
		});
	}
}
