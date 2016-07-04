+ NetAddr {
	*newFromIPString{arg str;
		var hostname, port;
		#hostname, port = str.split($:);
		port = port.asInteger;
		^this.new(hostname, port);
	}

	generateIPString{
		^"%:%".format(this.hostname, this.port);
	}
}
