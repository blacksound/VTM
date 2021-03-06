/*
JSON Schema

Six types: null, boolean, object, array, number, string



*/
VTMSchemaValue : VTMDictionaryValue {
	var <schema;//can be true, false, or dictionary
	var description;
	var required;

	isValidType{| val |
		var result = false;
		if(super.isValidType(val), {
			result = this.validate(val);
		});
		^result;
	}
	*type{ ^\schema; }
	*prDefaultValueForType{
		^[];
	}
	validate{| val |
		^true;//temp always validate to true
	}
}
