VTMSignalManager : VTMControlManager {
	name{ ^\signals; }
	*dataClass{ ^VTMSignal; }

	emit{arg key...args;
		items[key].valueAction_(*args);
	}
}
