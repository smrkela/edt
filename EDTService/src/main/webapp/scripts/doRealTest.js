jQuery(function($) {

	handleSubmitButton($);
	
	loadTime = new Date();
	startTime();
});

var loadTime;

function startTime() {
    
	var now=new Date();
	var diff=new Date(now - loadTime);
    var h=diff.getHours() - 1;
    var m=diff.getMinutes();
    var s=diff.getSeconds();
    
    m = checkTime(m);
    s = checkTime(s);
    
    document.getElementById('clock').innerHTML = h+":"+m+":"+s;
    
    var t = setTimeout(function(){startTime()},500);
}

function checkTime(i) {
    if (i<10) {i = "0" + i};  // add zero in front of numbers < 10
    return i;
}

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
		
		doAjaxPostWithResponse("rest/learning/submitRealTest", dto, callbackFunction);
		
	});
};


function callbackFunction(data){
	
	window.location.assign(baseUrl + "testovi/rezultat/"+data.id+"/");
}