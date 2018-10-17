VTMValueProperties : VTMOrderedIdentityDictionary {
	*newFrom{| what |
		if(what.notNil, {
			if(what.isEmpty, {
				^super.newFrom(what);
			}, {
				if(what.every(_.isKindOf(Association)), {
					var d;
					what.do({| item |
						d = d.addAll([item.key, item.value]);
					});
					^super.newFrom(d);
				}, {
					^super.newFrom(what);
				});
			});
		}, {
			^this.new;
		});
	}
}
