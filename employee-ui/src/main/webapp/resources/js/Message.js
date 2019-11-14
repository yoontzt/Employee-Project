
var ui = {}

ui.showErrorMessage = function (message) {
	$.toast({
		text : message,
		heading : 'Error',
		icon : 'error',
		showHideTransition : 'slide',
		allowToastClose : true,
		hideAfter : 2500,
		position : 'top-center',
		textAlign : 'left',
		loader : false,
		loaderBg : 'white',
		bgColor : '#dc3545',
	});
}

ui.showSuccessMessage = function (message) {
	$.toast({
		text : message,
		heading : 'Success',
		icon : 'info',
		showHideTransition : 'slide',
		allowToastClose : true,
		hideAfter : 2000,
		position : 'top-center',
		textAlign : 'left',
		loader : false,
		loaderBg : 'white',
		bgColor : '#4CAF50',
	});
}