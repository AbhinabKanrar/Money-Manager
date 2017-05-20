$(document).ready(function() {
	var navsidebar = document.getElementById("navsidebar");
	var navoverlay = document.getElementById("navoverlay");
	$("#nav-menu-button").click(function() {
		if (navsidebar.style.display === 'block') {
			navsidebar.style.display = 'none';
			navoverlay.style.display = "none";
		} else {
			navsidebar.style.display = 'block';
			navoverlay.style.display = "block";
		}
	});
	$(".nav-menu-button-close").click(function() {
		navsidebar.style.display = "none";
		navoverlay.style.display = "none";
	});
	$('#datatable').DataTable({
		paging : false
	});
	$('#mappingdatatable').DataTable({
		paging : false
	});
	$("#customer-collectorid-edit-button").click(function() {
	    $("#customer-collectorid-select").removeClass("w3-hide");
	    $("#customer-collectorid-text").removeClass("w3-show").addClass("w3-hide");
	});
});