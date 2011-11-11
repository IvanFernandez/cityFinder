/**
 * Plugin: jquery.zGoogleMap
 * 
 * Version: 1.0.1
 * (c) Copyright 2010, Zazar Ltd
 * 
 * Description: jQuery plugin for displaying maps via Google Maps API v3
 * 
 * History:
 * 1.0.1 - Support for multiple map instances
 * 
 **/

(function($){
       
	$.fn.GoogleMap = function(addresses, titles, details, options) {
		
		// Set pluign defaults
		var defaults = {  
			type: 0,
			width: '600px',
			height: '400px',
			zoom: 10,
			clickable: true,
			tooltip: true,
			tipsuffix: ' (click for more)'
		};  
		var options = $.extend(defaults, options); 

		// Select map type
		var maptype = getGoogleMapID(options.type);
		
		// Set Map defaults
		var apiDefaults = {
			mapTypeId: maptype,
			zoom: options.zoom
		}
		
		// Functions
		return this.each(function(i, e) {
			var $e = $(e);
	
			// Initialise Map variables
			var apiCenter = new google.maps.LatLng(0,0);
			var apiMap = new google.maps.Map($e.get(0), $.extend(apiDefaults, { center: apiCenter }));

			// Add map class to user div
			if (!$e.hasClass('mapGoogle')) {
				$e.addClass('mapGoogle');
				$e.css('width', options.width);
				$e.css('height', options.height);
			}

			// Loop through addresses adding markers
			if (addresses) {
				if (addresses.length > 0) {
					var i = 0;
					while (i < addresses.length) {
						
						// Plot location
						getGeoCode(apiCenter, apiMap, i++);
					}
				}
			}
		});
		
		// Geocode address and plot markers	
		function getGeoCode(apiCenter, apiMap, index) {

			var apiGeocoder = new google.maps.Geocoder();

			// Check for valid address array
			if (addresses && index >= 0) {
				
				// Get Lat/Long values from address
				apiGeocoder.geocode({ address: addresses[index] }, function(results, status) {
			
					if (status == google.maps.GeocoderStatus.OK && results.length) {
				
						if (status != google.maps.GeocoderStatus.ZERO_RESULTS) {
								
							// Center in map on location
							apiMap.setCenter(results[0].geometry.location);
								
							// Create tooltip text
							var title = '';
							if (options.tooltip) title = titles[index] + options.tipsuffix;

							// Add marker to map
							var apiMarker = new google.maps.Marker({
								position: results[0].geometry.location,
								map: apiMap,
								title: title
							});
					
							// Create info window
							var apiInfoWindow = new google.maps.InfoWindow({
								content: details[index]
							});
					
							// Create 'click' event and attach info window to marker
							if (options.clickable)
								google.maps.event.addListener(apiMarker, 'click', function() {
									apiInfoWindow.open(apiMap, apiMarker);
								});
						}
					}
				});
			}
		}

		// Convert map type to Google Map ID
		function getGoogleMapID(map) {
			var mapid = google.maps.MapTypeId.ROADMAP;

			switch (map) {
				case 1:
					mapid = google.maps.MapTypeId.SATELLITE;
					break;
				case 2:
					mapid = google.maps.MapTypeId.HYBRID;
					break;
				case 3:
					mapid = google.maps.MapTypeId.TERRAIN;
					break;
			}

			return mapid;
        	}
	};
})(jQuery);