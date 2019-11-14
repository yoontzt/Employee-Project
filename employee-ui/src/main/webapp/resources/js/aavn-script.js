$(document)
		.ready(
				function() {

					// Init "focus" event for text input fields when "click" on
					// label
					$(document).on(
							"click",
							".aavn-input-text-label",
							function() {
								$(this).parent().find('.aavn-input-text')
										.trigger('focus')
							});

					// Init "focus" event for number input fields when "click"
					// on label
					$(document).on(
							"click",
							".aavn-input-number-label",
							function() {
								$(this).parent().find('input').first().trigger(
										'focus')
							})

					// Check content to set "active" style for label of text
					// input fields
					$(".aavn-input-text").each(
							function() {
								if ($(this).val() == '') {
									$(this).parent().find("label").removeClass(
											'active');
								}
							});

					// Set "active" style for label of text input fields
					$(document).on(
							"focusin",
							".aavn-input-text",
							function() {
								$(this).parent().find("label").addClass(
										'active active-label-color');
							});

					// Remove "active" style for label of text input fields
					$(document).on(
							"focusout",
							".aavn-input-text",
							function() {
								$(this).parent().find("label").removeClass(
										'active-label-color');
								if ($(this).val() == '') {
									$(this).parent().find("label").removeClass(
											'active');
								}
							});

					// Check content to set "active" style for label of number
					// input fields
					$(".aavn-input-number")
							.each(
									function(index, item) {
										if ($(item).find("input").last().val() == '') {
											$(item).parent().find("label")
													.removeClass('active');
										}

										var $numberInputs = $(item).find(
												"input");

										// Set "active" style for label of
										// number input fields
										$numberInputs
												.first()
												.on(
														"focusin",
														function() {
															$numberInputs
																	.first()
																	.parent()
																	.parent()
																	.find(
																			"label")
																	.addClass(
																			'active active-label-color');
														});

										// Remove "active" style for label of
										// number input fields
										$numberInputs
												.first()
												.on(
														"focusout",
														function() {
															$numberInputs
																	.first()
																	.parent()
																	.parent()
																	.find(
																			"label")
																	.removeClass(
																			'active-label-color');
															if ($numberInputs
																	.last()
																	.val() == '') {
																$numberInputs
																		.last()
																		.parent()
																		.parent()
																		.find(
																				"label")
																		.removeClass(
																				'active');
															}
														});
									});

				})

// Method to show back drop
function showBackDrop(className) {
	$(".".concat(className)).addClass("active");
}

// Method to hide back drop
function hideBackDrop(className) {
	$(".".concat(className)).removeClass("active");
}
