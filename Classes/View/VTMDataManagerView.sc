VTMDataManagerView : VTMView {
	var itemsView;
	var showItems = false;
	var showItemsButton;
	var showNumItemsLabel;

	showItems_{| bool |
		showItems = bool;
		{
			itemsView.visible = showItems;
		}.defer;
	}

	rebuildItemsView{
		itemsView.children.do(_.remove);
		itemsView.layout_(
			VLayout(
				*model.items.collect({| item |
					[
						item.makeView,
						\align,
						\topLeft
					];
				}).add(nil)
			).spacing_(2).margins_(2)
		);
		showNumItemsLabel.string_(model.numItems);
	}

	prMakeChildViews{
		labelView = this.prMakeLabelView();
		itemsView = this.prMakeItemsView();
		showItemsButton = Button()
		.states_([
			["[+]", Color.black, Color.white.alpha_(0.1)],
			["[—]", Color.black, Color.white.alpha_(0.1)]
		])
		.value_(showItems.asInteger)
		.action_({| butt | this.showItems_(butt.value.booleanValue); })
		.font_(this.font)
		.background_(labelView.background)
		.fixedSize_(Size(15,15))
		.canFocus_(false);
		showNumItemsLabel = StaticText()
		.string_(model.numItems)
		.font_(this.font.italic_(true))
		.fixedSize_(Size(15,15));
		this.rebuildItemsView();
	}

	prMakeLayout{
		^VLayout(
			View().layout_(
				HLayout(
					[showItemsButton, \align: \left],
					[labelView, \align: \left],
					nil,
					[showNumItemsLabel, \align: \right]
				).spacing_(0).margins_(1),
				nil
			)
			.maxHeight_(15)
			.background_(labelView.background),
			itemsView.visible_(showItems)
		);
	}


	prMakeItemsView{
		var result;
		result = View()
		.background_(Color.yellow.alpha_(0.1));
		^result;
	}

	//pull style update
	update{| theChanged, whatChanged, whoChangedIt, toValue |
		"Dependant update: % % % %".format(
			theChanged, whatChanged, whoChangedIt, toValue
		).vtmdebug(3, thisMethod);

		//only update the view if the valueObj changed
		if(theChanged === model, {
			switch(whatChanged,
				\items, { { this.rebuildItemsView; }.defer; },
				\freed, { { this.remove; }.defer; }
			);
			{this.refresh;}.defer;
		});
	}

}
