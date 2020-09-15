  
var stompClient = null;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#chat").show();
	} else {
		$("#chat").hide();
	}
	$("#chat").html("");
}

//소켓 연결
function connect() {
	var socket = new SockJS('/endpoint');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		// 입장에 대한 구독
		stompClient.subscribe('/chat/hello', function(msg) {
			showHello(JSON.parse(msg.body));
		});
		// 입장에 대한 메시지 전달
		stompClient.subscribe('/chat/detail', function(msg) {
			showDetail(JSON.parse(msg.body));
		});
		// 퇴장에 대한 구독
		stompClient.subscribe('/chat/bye', function(msg) {
			showBye(JSON.parse(msg.body));
		});
		sendHello();
	});
}

//소켓 연결 끊음
function disconnect() {
	if (stompClient != null) {
		sendBye();
		stompClient.disconnect();
	}
	setConnected(false);
	
	console.log("Disconnected");
}

function sendHello() {
	stompClient.send("/hello", {}, JSON.stringify({
		name	 : $('#name').val()
	}));
}

function sendDetail() {
	stompClient.send("/detail", {}, JSON.stringify({
		name	 : $('#name').val(),
		contents : $('#btn-input').val()
	}));
}

function sendBye() {
	stompClient.send("/bye", {}, JSON.stringify({
		name	 : $('#name').val()
	}));
}

function showDetail(message) {
	var html = "";
	if(message.name == $('#name').val()){
		html += '<li class="left clearfix">';
		html += '	<span class="chat-img pull-right" style ="float:right">>'
		html += '		<img src="http://placehold.it/50/FA6F57/fff" alt="User Avatar" class="img-circle" style="border-radius: 100%";>';
		html += '	</span>';
		html += '	<div class="chat-body clearfix" style ="float:right">';
		html += '		<div class="header">';
		html += '		<strong class="pull-right primary-font">' + message.name + '</strong>';
		html += '		<small class="text-muted">';
		html += '			<i class="fa fa-clock-o fa-fw"></i>' + new Date(message.send_date);
		html += '		</small>';
		html += '	</div>';
		html += '	<p>';
		html += 	message.contents;
		html += '	</p>';
		html += '	</div>';
		html += '</li>';
	} else {
		html += '<li class="left clearfix" >';
		html += '	<span class="chat-img pull-left" style ="float:left">>'
		html += '		<img src="http://placehold.it/50/55C1E7/fff" alt="User Avatar" class="img-circle" style="border-radius: 100%";>';
		html += '	</span>';
		html += '	<div class="chat-body clearfix" style ="float:left">';
		html += '		<div class="header">';
		html += '		<strong class="primary-font">' + message.name + '</strong>';
		html += '		<small class="pull-right text-muted" >';
		html += '			<i class="fa fa-clock-o fa-fw"></i>' + new Date(message.send_date);
		html += '		</small>';
		html += '	</div>';
		html += '	<p>';
		html += 	message.contents;
		html += '	</p>';
		html += '	</div>';
		html += '</li>';
	}
	
	
	$(".chat").append(html);
	$('.panel-body').scrollTop($(".chat")[0].scrollHeight);
    $('html, body').animate({scrollTop: '100000'}, 0);
}

function showHello(message) {
	var html = "";
	
	$.ajax({
		url : "/getChats",
		type : "GET",
		contentType: "application/json",
		success : function(data) {
			//alert(data[0].contents);
			for(var i =0; i<data.length; i++)
				{
				showDetail(data[i]);
				}
			
			 $('html, body').animate({scrollTop: '100000'}, 0);
			 /*showDetail();*/

		},
		error : function(error) {
			alert("실패");
		}
	});

	// showDetail(message);  -> For an 문으로 돌릴까?
	
	
/*	html += '<li class="left clearfix">';
	html += '	<div class="chat-body clearfix">';
	html += '	<div class="header">';
	html += '		<strong class="primary-font">' + message.name + '</strong>';
	html += '		<small class="pull-right text-muted">';
	html += '			<i class="fa fa-clock-o fa-fw"></i>' + new Date(message.sendDate);
	html += '		</small>';
	html += '	</div>';
	html += '	<p>';
	html += '	입장 하였습니다';
	html += '	</p>';
	html += '	</div>';
	html += '</li>';*/
	
	$(".chat").append(html);
	$('.panel-body').scrollTop($(".chat")[0].scrollHeight);
}

function showBye(message) {
	var html = "";
	
	html += '<li class="left clearfix">';
	html += '	<div class="chat-body clearfix">';
	html += '	<div class="header">';
	html += '		<strong class="primary-font">' + message.name + '</strong>';
	html += '		<small class="pull-right text-muted">';
	html += '			<i class="fa fa-clock-o fa-fw"></i>' + new Date(message.sendDate);
	html += '		</small>';
	html += '	</div>';
	html += '	<p>';
	html += '	퇴장 하였습니다';
	html += '	</p>';
	html += '	</div>';
	html += '</li>';
	
	$(".chat").append(html);
	$('.panel-body').scrollTop($(".chat")[0].scrollHeight);
}

$(document).ready(function(){

/*	$('html, body').scroll(function(){
alert("song");
	        })
*/
	
	connect();
	


	$("form").on('submit', function(e) {
		e.preventDefault();
	});

	$("#disconnect").click(function() {
		// 소켓 연결 끊음
		disconnect();
	});
	$("#btn-chat").click(function() {
		// 메시지 전달
		sendDetail();
		$('#btn-input').val('');
	});
});