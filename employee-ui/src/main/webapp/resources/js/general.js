function hideIcons() {
	$(".ui-datepicker-trigger, .ui-selectonemenu-trigger").css("display",
			"none");
}

function hideCertificateHeader() {
	$(".certificateHeader").css("display", "none");
}
function reload() {
	/* $(".ui-datatable").load("employee-list.xhtml .ui-datatable"); */
	$("document").load("employee-list.xhtml document");

}

function goToMainPage() {
	setTimeout("location.href = '/employee-tool/employee-list.xhtml';", 2000);
}

function goToAddPage() {
	window.location.href = "/employee-tool/views/add-employee-form.xhtml";
}

function goToMian(){
	window.location.href = "/employee-tool/employee-list.xhtml";
}

function goToUpdatePage() {
	window.location.href = "/employee-tool/views/view-employee.xhtml";
}

window.onload = function() {
	document.getElementById('addEmployeeForm:firstname').focus();
}
