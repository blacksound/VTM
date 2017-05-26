VTMAbstractData{
	var <name;
	var <manager;
	var parameters;
	var oscInterface;
	var path;
	var declaration;

	classvar viewClassSymbol = \VTMAbstractDataView;

	*managerClass{
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg name, declaration, manager;
		^super.new.initAbstractData(name, declaration, manager);
	}

	*newFromDeclaration{arg declaration, manager;
		var dec = declaration.deepCopy;
		^this.new(dec.removeAt(\name), dec, manager);
	}

	initAbstractData{arg name_, declaration_, manager_;
		name = name_;
		manager = manager_;
		declaration = VTMDeclaration.newFrom(declaration_ ? []);
		this.prInitParameters;
	}

	prInitParameters{
		var tempAttr;
		this.class.parameterDescriptions.keysValuesDo({arg key, val;
			//check if parameter is defined in parameter values
			if(declaration.includesKey(key), {
				var checkType;
				var checkValue;
				var tempVal = VTMValue.makeFromProperties(val);
				//is type strict? true by default
				checkType = val[\strictType] ? true;
				if(checkType, {
					if(tempVal.isValidType(declaration[key]).not, {
						Error("Parameter value '%' must be of type '%'".format(key, tempVal.type)).throw;
					});
				});
				//check if value is e.g. within described range.
				checkValue = val[\strictValid] ? false;
				if(checkValue, {
					if(tempVal.isValidValue(declaration[key]).not, {
						Error("Parameter value '%' is invalid".format(key)).throw;
					});
				});
			}, {
				var optional;
				//if not check if it is optional, true by default
				optional = val[\optional] ? true;
				if(optional.not, {
					Error("Parameters is missing non-optional value '%'".format(key)).throw;
				});
			});

		});
		parameters = VTMParameterManager.newFrom(declaration);
	}

	disable{
		this.disableOSC;
	}

	enable{
		this.enableOSC;
	}

	free{
		this.disableOSC;
		this.releaseDependants;
		parameters = nil;
		manager = nil;
	}

	*parameterKeys{
		^this.parameterDescriptions.keys;
	}

	*parameterDescriptions{
		^VTMOrderedIdentityDictionary[
			\name -> (type: \string, optional: true),
			\path -> (type: \string, optional: true)
	   	]; 
	}

	parameters{
		^parameters.as(VTMParameters);
	}

	description{
		var result = VTMOrderedIdentityDictionary[
			\parameters -> this.class.parameterDescriptions,
		];
		^result;
	}

	declaration{
		this.subclassResponsibility(thisMethod);
	}

	makeView{arg parent, bounds, definition, settings;
		var viewClass = this.class.viewClassSymbol.asClass;
		//override class if defined in settings.
		if(settings.notNil, {
			if(settings.includesKey(\viewClass), {
				viewClass = settings[\viewClass];
			});
		});
		^viewClass.new(parent, bounds, definition, settings, this);
	}

	fullPath{
		^(this.path ++ this.leadingSeparator ++ this.name).asSymbol;
	}

	path{
		if(manager.isNil, {
			^parameters.at(\path);
		}, {
			^manager.fullPath;
		});
	}

	hasDerivedPath{
		^manager.notNil;
	}

	get{arg key;
		^parameters.at(key);
	}

	leadingSeparator{ ^'/'; }

	enableOSC{
		//make OSC interface if not already created
		if(oscInterface.isNil, {
			//oscInterface = VTMOSCInterface.new(this);//TEMP uncommented
		});
		//oscInterface.enable; //TEMP uncommented
	}

	disableOSC{
		if(oscInterface.notNil, { oscInterface.free;});
		oscInterface = nil;
	}

	oscEnabled{
		^if(oscInterface.notNil, {
			oscInterface.enabled;
		}, {
			^nil;
		});
	}
}
