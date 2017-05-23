$(document).ready(
		function() {
			navigator.geolocation.getCurrentPosition(success,error, options);
			var options = {
				enableHighAccuracy : true,
				timeout : 5000,
				maximumAge : 0
			};
			$("#reason").hide();
			$("#actualAmount").keyup(
					function() {
						if (parseInt($('#actualAmount').val()) < parseInt($('#dueAmount').val())) {
							$("#reason").show();
						} else {
							$("#reason").hide();
						}
					});
			function error(error) {
				switch(error.code) {
		        case error.PERMISSION_DENIED:
		        	$('#location').val('PERMISSION_DENIED');
		            break;
		        case error.POSITION_UNAVAILABLE:
		        	$('#location').val('POSITION_UNAVAILABLE');
		            break;
		        case error.TIMEOUT:
		        	$('#location').val('TIMEOUT');
		            break;
		        case error.UNKNOWN_ERROR:
		        	$('#location').val('UNKNOWN_ERROR');
		            break;
		        default:
		        	$('#location').val('IGNORED');
	            	break;
				}
			}
			function success(pos) {
				var crd = pos.coords;
				$('#location').val(crd.latitude + ', ' + crd.longitude);
			}
		});