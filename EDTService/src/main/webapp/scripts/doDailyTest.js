jQuery(function($) {

	handleSubmitButton($);
});

function handleSubmitButton($) {

	$('#submit').click(function(e) {

		e.preventDefault();

		var dto = {
			questions : [],
			startTime : $("#startTime").val(),
			testId : $("#testId").val()
		};

		var questionDivs = $("div [name='question']");

		for ( var i = 0; i < questionDivs.length; i++) {

			var questionDiv = questionDivs[i];

			var questionId = questionDiv.id;
			var questionLongId = questionId.substring('question'.length);
			var questionDTO = {
				id : questionLongId,
				answers : []
			};

			dto.questions.push(questionDTO);

			var answerDivs = $("#" + questionId + " input");

			for ( var j = 0; j < answerDivs.length; j++) {

				var answerInput = answerDivs[j];

				var type = answerInput.type;
				var inputId = answerInput.value;

				if (type == "radio") {

					if (answerInput.checked)
						questionDTO.answers.push(inputId);
				} else {

					if (answerInput.checked)
						questionDTO.answers.push(inputId);
				}
			}
		}

		var a = 2;
		
		doAjaxPostWithResponse("rest/learning/submitDailyTest", dto, callbackFunction);
		
	});
};


function callbackFunction(data){
	
	window.location.assign(baseUrl + "test-dana/rezultat/"+data.dateString+"/"+data.username+"/");
}