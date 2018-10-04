package com.evola.driving.util
{
	import com.evola.driving.controls.EDTAlert;
	
	import mx.controls.Alert;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.http.mxml.HTTPService;

	public class EvolaHttpService extends HTTPService
	{

		public var faultFunction:Function;

		private var _restUrl:String;

		public function EvolaHttpService(rootURL:String=null, destination:String=null)
		{
			super(rootURL, destination);

			method="GET";
			resultFormat="e4x";

			addEventListener(FaultEvent.FAULT, onFault);
		}

		public function set restUrl(value:String):void
		{

			_restUrl=value;
		}

		protected function onFault(event:FaultEvent):void
		{

			if (faultFunction == null)
				EDTAlert.show("Greška :(. Pokušaj ponovo ili osveži stranicu u svom pregledaču.", event.toString());
			else
				faultFunction(event);
		}

		override public function send(params:Object=null):AsyncToken
		{

			if (_restUrl)
				url=Settings.SERVER_URL + "/rest/" + _restUrl;

//			if (params == null)
//				params={AuthenticationToken: Settings.userToken};
//			else
//				params.AuthenticationToken=Settings.userToken;

			return super.send(params);
		}

	}
}
